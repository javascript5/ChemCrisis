package com.chemcrisis.nsc.nscchemcrisis;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chemcrisis.nsc.nscchemcrisis.LoadingDialog.CustomLoadingDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

import java.util.ArrayList;

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

    private void addHeatMap() {
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
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    double lat = ds.child("0").getValue(Double.class);
                    double lng = ds.child("1").getValue(Double.class);
                    double mass = ds.child("2").getValue(Double.class);
                    if (mass > 0){
                        data.add(new WeightedLatLng(new LatLng(lat, lng), mass));
                    }
                }
                if (mProvider == null) {
                    mProvider = new HeatmapTileProvider.Builder().weightedData(data).gradient(gradient).build();

                    mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                    mProvider.setRadius(100);

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
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 100, null);
        addHeatMap();
    }
}
