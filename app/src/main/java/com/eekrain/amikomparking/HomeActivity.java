package com.eekrain.amikomparking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class HomeActivity extends AppCompatActivity {

    public static String URL_GETVEHICLE = "https://amikom.rocketjaket.com/api/vehicle/getListVehicleJSON";
    public static String TAG = "HomeActivity";
    public ArrayList<String> plat = new ArrayList<String>();
    public ArrayList<String> jenis = new ArrayList<String>();
    SessionManager sessionManager;
    RecyclerView recycler_vehicle;
    String nim, nama;
    Context context;
    Button logoutbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        logoutbtn = findViewById(R.id.logoutbtn);
        recycler_vehicle = findViewById(R.id.list_vehicle);

        HashMap<String, String> user = sessionManager.getUserDetail();
        nim = user.get(SessionManager.NIM);
        nama = user.get(SessionManager.NAME);

//        Toast.makeText(context, "nama : " + nama, Toast.LENGTH_SHORT).show();

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent tete = new Intent(HomeActivity.this, SplashActivity1.class);
                startActivity(tete);
                sessionManager.logout();
                finish();
            }
        });

        context = this;

        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.post(URL_GETVEHICLE)
                .addBodyParameter("nim", nim)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String plat_extract = jsonObject.getString("plat");
                                String jenis_extract = jsonObject.getString("jenis");
                                plat.add(plat_extract);
                                jenis.add(jenis_extract);

                                Integer xxx = plat.size();
                                Log.v(TAG, xxx.toString());


                                VehicleAdapter vehicleAdapter = new VehicleAdapter(context, plat, jenis);
                                recycler_vehicle.setAdapter(vehicleAdapter);
                                recycler_vehicle.setLayoutManager(new LinearLayoutManager(context));
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
