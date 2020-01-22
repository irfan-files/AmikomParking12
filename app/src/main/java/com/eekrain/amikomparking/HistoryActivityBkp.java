package com.eekrain.amikomparking;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryActivityBkp extends AppCompatActivity {
    public static final String URL_HISTORY = "https://amikom.rocketjaket.com/api/History/getDataListHistory";
    RecyclerView recyclerView;
    String nim;
    Context context;
    ArrayList<String> plat = new ArrayList<String>();
    ArrayList<String> date = new ArrayList<String>();
    ArrayList<Boolean> status = new ArrayList<Boolean>();
    int[] status_images = {R.drawable.ic_check, R.drawable.ic_close};
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_bkp);

        AndroidNetworking.initialize(getApplicationContext());
        getHistoryData(nim);

        recyclerView = findViewById(R.id.recyclerHistory);
        context = this;
        sessionManager = new SessionManager(context);
        HashMap<String, String> user = sessionManager.getUserDetail();
        nim = user.get(SessionManager.NIM);

        HistoryAdapter historyAdapter = new HistoryAdapter(this, plat, date, status, status_images);
        recyclerView.setAdapter(historyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void getHistoryData(String nim_ref) {
        AndroidNetworking.post(URL_HISTORY)
                .addBodyParameter("nim", nim_ref)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String plat_res = jsonObject.getString("plat");
                                String date_res = jsonObject.getString("date");
                                Boolean status_res = jsonObject.getBoolean("status");
                                Log.v("HistoryActivityBkp", "ASUUUUUSUUUUUUUUUUUUUUUUUU : " + plat_res);
                                Toast.makeText(context, "plat : " + plat_res, Toast.LENGTH_SHORT).show();
                                plat.add(plat_res);
                                date.add(date_res);
                                status.add(status_res);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
