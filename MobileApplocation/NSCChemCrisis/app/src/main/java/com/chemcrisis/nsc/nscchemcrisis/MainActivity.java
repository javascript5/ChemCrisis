package com.chemcrisis.nsc.nscchemcrisis;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.chemcrisis.nsc.nscchemcrisis.Services.FirebaseDataReceiver;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("NEWS");

        getFCMToken();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_view, new AccidentHistoryFragment())
                .addToBackStack(null)
                .commit();

//        if (savedInstanceState == null) {
//            if (FirebaseDataReceiver.getContent() != null){
//                String content = FirebaseDataReceiver.getContent();
//                Log.i("CONTENTINMAIN", content);
//                if(content.equals("CHEM")){
//                    getSupportFragmentManager()
//                            .beginTransaction()
//                            .replace(R.id.main_view, new FindPathFragment())
//                            .addToBackStack(null)
//                            .commit();
//                }else{
//                    getSupportFragmentManager()
//                            .beginTransaction()
//                            .replace(R.id.main_view, new SafezoneFragment())
//                            .addToBackStack(null)
//                            .commit();
//                }
//                FirebaseDataReceiver.setContent();
//            }else{
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.main_view, new FindPathFragment())
//                        .addToBackStack(null)
//                        .commit();
//            }
//        }

    }

    @Override
    public void onBackPressed() {
        Log.i("BACKBUTTON", "DISABLE");
    }

    public void getFCMToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("GETTOKEN", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();

                        String msg = token;
                        Log.d("GETTOKEN", msg);
                    }
                });
    }
}
