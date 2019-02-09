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

import com.chemcrisis.nsc.nscchemcrisis.LoadingDialog.CustomLoadingDialog;
import com.chemcrisis.nsc.nscchemcrisis.Route.TaskLoadedCallback;
import com.chemcrisis.nsc.nscchemcrisis.Services.FirebaseDataReceiver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
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
import com.google.maps.android.heatmaps.WeightedLatLng;

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

    }

    @Override
    public void onBackPressed() {
        Log.i("BACKBUTTON", "DISABLE");
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
        try {

            final String content;
            if (FirebaseDataReceiver.getContent() != null){
                content = FirebaseDataReceiver.getContent();
                Log.i("CONTENT", content);
                FirebaseDataReceiver.setContent();
            } else{
                content = "TU";
            }

            Log.i("CONTENT", "accident/" + content +"/accidentPosition/");

            databaseReference = database.getReference("accident/" + content + "/accidentPosition/");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean isInPlume = false;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        double lat = ds.child("0").getValue(Double.class);
                        double lng = ds.child("1").getValue(Double.class);
                        double mass = ds.child("2").getValue(Double.class);

                        if (mass > 10){
                            float distance = getDistanceBetweenTwoPoints(currentLa, currentLong, lat, lng);
                            if (distance <= 50){
                                Log.i("DISTANCEX", distance + "");
                                isInPlume = true;
                            }
                        }

                        if (isInPlume){
                            Bundle bundle = new Bundle();
                            bundle.putString("currentLa", currentLa + "");
                            bundle.putString("currentLong", currentLong + "");
                            bundle.putString("factory", content);
                            AccidentInformationFragment fragobj = new AccidentInformationFragment();
                            fragobj.setArguments(bundle);

                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_view, fragobj)
                                    .addToBackStack(null)
                                    .commit();
                            break;
                        } else{
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_view, new AccidentHistoryFragment())
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10);
            mLocationRequest.setFastestInterval(10);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            checkLocationToRedirect(location.getLatitude(), location.getLongitude());
//        checkLocationToRedirect(13.727524, 100.765024);
            mGoogleApiClient.disconnect();
            Log.d("LOCATION", "accuracy: " + location.getAccuracy() + " lat: " + location.getLatitude() + " lon: " + location.getLongitude());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onTaskDone(Object... values) {
        try {
            if (FindPathFragment.currentPolyline != null)
                FindPathFragment.currentPolyline.remove();
            FindPathFragment.currentPolyline = FindPathFragment.mMap.addPolyline((PolylineOptions) values[0]);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
