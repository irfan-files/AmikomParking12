package com.eekrain.amikomparking;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScanActitivy extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    public static final String URL_SCAN = "https://amikom.rocketjaket.com/api/Parking/processParking";
    Context context;
    String plat, nim;
    SessionManager sessionManager;
    private ZXingScannerView scannerView;
    private TextView txt_Result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        nim = user.get(SessionManager.NIM);
        //init
        scannerView = findViewById(R.id.zxscan);
        txt_Result = findViewById(R.id.txt_Result);
        context = this;
        getIntentExtraData();
        //req permission
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(QRScanActitivy.this);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(QRScanActitivy.this, "Anda harus memberi izin penggunaan kamera!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();

    }

    @Override
    protected void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }

    private void getIntentExtraData() {
        if (getIntent().hasExtra("plat")) {
            plat = getIntent().getStringExtra("plat");
//            Toast.makeText(context, "Plat : " + plat , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void handleResult(Result rawResult) {
        txt_Result.setText(rawResult.getText());

        AndroidNetworking.post(URL_SCAN)
                .addBodyParameter("nim", nim)
                .addBodyParameter("plat", plat)
                .addBodyParameter("qrcode", rawResult.getText())
                .setTag(context)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean status = response.getBoolean("status");
                            String message = response.getString("message");

                            if (status) {
                                String mhs_foto = response.getString("mhs_foto");
                                String mhs_nama = response.getString("mhs_nama");
                                String plat = response.getString("plat");
                                String jenis = response.getString("jenis");
                                String merk = response.getString("merk");
                                String tipe = response.getString("tipe");
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                Intent sukses = new Intent(context, ConfirmActivity.class);
                                sukses.putExtra("mhs_foto", mhs_foto);
                                sukses.putExtra("mhs_nama", mhs_nama);
                                sukses.putExtra("plat", plat);
                                sukses.putExtra("jenis", jenis);
                                sukses.putExtra("merk", merk);
                                sukses.putExtra("tipe", tipe);
                                context.startActivity(sukses);
                                finish();
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                Intent gagal = new Intent(context, HomeActivity.class);
                                context.startActivity(gagal);
                                finish();
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
