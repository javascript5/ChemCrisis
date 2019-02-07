package com.chemcrisis.nsc.nscchemcrisis;

import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chemcrisis.nsc.nscchemcrisis.LoadingDialog.CustomLoadingDialog;
import com.chemcrisis.nsc.nscchemcrisis.Services.GMapV2Direction;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FindPathFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    private Gradient gradient;
    private ArrayList<WeightedLatLng> data;
    private ArrayList<LatLng> list;
    private SupportMapFragment mapFragment;
    private CustomLoadingDialog customLoadingDialog;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//       return inflater.inflate(R.layout.find_path_fragment, container, false);

        View view = inflater.inflate(R.layout.find_path_fragment, null, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        return view;
    }

    private void addHeatMap(final GoogleMap mMap) {
        customLoadingDialog = new CustomLoadingDialog(getActivity());
        customLoadingDialog.showDialog();

        int[] colors = {
                Color.GREEN,    // green(0-50)
                Color.YELLOW,    // yellow(51-100)
                Color.rgb(255,165,0), //Orange(101-150)
                Color.RED,              //red(151-200)
                Color.rgb(153,50,204), //dark orchid(201-300)
                Color.rgb(165,42,42) //brown(301-500)
        };

        float[] startpoints = {
                0.1F, 0.2F, 0.3F, 0.4F, 0.5F, 1.0F
        };

        gradient = new Gradient(colors,startpoints);
        data = new ArrayList<WeightedLatLng>();



        // Create a heat map tile provider, passing it the latlngs of the police stations.
        databaseReference = database.getReference("accident/KMITL/accidentPosition/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float distance = 1000;
                double nearestLa = 0, nearestLng = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    double lat = ds.child("0").getValue(Double.class);
                    double lng = ds.child("1").getValue(Double.class);
                    double mass = ds.child("2").getValue(Double.class);

                    if (mass > 10){
                        data.add(new WeightedLatLng(new LatLng(lat, lng), mass));
                    }

                    else if (mass <= 0){
                        float result = getDistanceBetweenTwoPoints(13.733129, 100.779534, lat, lng);
                        if (result < distance){
                            distance = result;
                            nearestLa = lat;
                            nearestLng = lng;
                        }
                    }
                }

                plotRouteLine(new LatLng(13.733129, 100.779534), new LatLng(nearestLa, nearestLng), mMap);
                Log.i("REST", "DistanceInMeter" + distance);
                getDistanceApi(nearestLa, nearestLng);

                final LatLng ORIGIN = new LatLng(13.733129, 100.779534);
                Marker origin  = mMap.addMarker(new MarkerOptions()
                        .position(ORIGIN)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                final LatLng MELBOURNE = new LatLng(nearestLa, nearestLng);
                Marker melbourne = mMap.addMarker(new MarkerOptions()
                        .position(MELBOURNE)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                if (mProvider == null) {
                    mProvider = new HeatmapTileProvider.Builder().weightedData(data).gradient(gradient).build();

                    mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                    mProvider.setRadius(150);

                    mOverlay.clearTileCache();
                    customLoadingDialog.dismissDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    customLoadingDialog.dismissDialog();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng( 13.729972, 	100.778495);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 500, null);
        addHeatMap(mMap);
    }


    private void getDistanceApi(double la, double ln) {
        double originLa = 13.733129;
        double originLn = 100.779534;
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + originLa + "," + originLn + "&&destinations=" + la + "," + ln + "&key=AIzaSyAYPbRd9v0N1KJcoa8I6mrVwWeXTUBlha4";
        try {
            run(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void run(String url) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String myResponse = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            String destination, origin, distance;

                            JSONObject json = new JSONObject(myResponse);
                            destination = json.getJSONArray("destination_addresses").get(0) + "";
                            origin = json.getJSONArray("origin_addresses").get(0) + "";
                            Object json2 = json.getJSONArray("rows").get(0);
                            JSONObject jsonObj = new JSONObject(json2+"");
                            Object json3 = jsonObj.getJSONArray("elements").get(0);
                            JSONObject jsonObject2 = new JSONObject(json3+"");
                            Object json4 = jsonObject2.get("distance");
                            JSONObject jsonObject3 = new JSONObject(json4+"");
                            distance = jsonObject3.get("value") + "";

                            Log.i("REST", "Destination : " + destination);
                            Log.i("REST", "Origin : " + origin);
                            Log.i("REST", "Distance : " + distance + "m");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    private void plotRouteLine(LatLng origin, LatLng destination,GoogleMap mMap){
        GMapV2Direction md = new GMapV2Direction();

        Document doc = md.getDocument(origin, destination,
                GMapV2Direction.MODE_DRIVING);
        Log.i("Test", "MD : " + doc);
        ArrayList<LatLng> directionPoint = md.getDirection(doc);
        PolylineOptions rectLine = new PolylineOptions().width(3).color(
                Color.RED);

        for (int i = 0; i < directionPoint.size(); i++) {
            rectLine.add(directionPoint.get(i));
        }
        Polyline polylin = mMap.addPolyline(rectLine);

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
}
