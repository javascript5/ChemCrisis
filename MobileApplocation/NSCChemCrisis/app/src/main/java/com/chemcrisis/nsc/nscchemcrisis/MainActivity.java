package com.chemcrisis.nsc.nscchemcrisis;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.chemcrisis.nsc.nscchemcrisis.Route.TaskLoadedCallback;
import com.chemcrisis.nsc.nscchemcrisis.Services.FirebaseDataReceiver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, TaskLoadedCallback {

    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final int INITIAL_REQUEST=1337;

    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildGoogleApiClient();
        mGoogleApiClient.connect();

        requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);

        FirebaseMessaging.getInstance().subscribeToTopic("NEWS");


        getFCMToken();

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

    private float getDistanceBetweenTwoPoints(double la1, double ln1, double la2, double ln2){
        Location loc1 = new Location("");
        loc1.setLatitude(la1);
        loc1.setLongitude(ln1);

        Location loc2 = new Location("");
        loc2.setLatitude(la2);
        loc2.setLongitude(ln2);

        float distanceInMeters = loc1.distanceTo(loc2);

        return distanceInMeters;

    }

    private void checkLocationToRedirect(final double currentLa, final double currentLong){
        databaseReference = database.getReference("factory/KMITL/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double lat = dataSnapshot.child("latitude").getValue(Double.class);
                double ln = dataSnapshot.child("longitude").getValue(Double.class);

                float distance = getDistanceBetweenTwoPoints(currentLa, currentLong, lat, ln);
                Log.i("LOCATION", distance + "");
                if(distance <= 10000){
                    Bundle bundle = new Bundle();
                    bundle.putString("currentLa", currentLa + "");
                    bundle.putString("currentLong", currentLong + "");
                    FindPathFragment fragobj = new FindPathFragment();
                    fragobj.setArguments(bundle);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_view, fragobj)
                            .addToBackStack(null)
                            .commit();
                }else{
                    //redirect to history Fragment
                    getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new SafezoneFragment())
                        .addToBackStack(null)
                        .commit();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10);
        mLocationRequest.setFastestInterval(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        checkLocationToRedirect(location.getLatitude(), location.getLongitude());
        mGoogleApiClient.disconnect();
        Log.d("LOCATION", "accuracy: " + location.getAccuracy() + " lat: " + location.getLatitude() + " lon: " + location.getLongitude());

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onTaskDone(Object... values) {
        if (FindPathFragment.currentPolyline != null)
            FindPathFragment.currentPolyline.remove();
        FindPathFragment.currentPolyline = FindPathFragment.mMap.addPolyline((PolylineOptions) values[0]);
    }
}
