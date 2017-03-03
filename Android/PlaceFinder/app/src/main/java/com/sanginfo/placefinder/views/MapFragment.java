package com.sanginfo.placefinder.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.sanginfo.placefinder.R;

/**
 * Created by Sang.24 on 3/3/2017.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private static final String TAG="MapFragment";
    private GoogleMap map;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=null;
        MapView mapView;
        try{
            view= inflater.inflate(R.layout.map_fragment_screen, null, false);
            mapView = (MapView) view.findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);
        }catch (Exception ex){
            Log.e(TAG,ex.toString());
        }
        return view;
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try{
            map=googleMap;
        }catch (Exception ex){
            Log.e(TAG,ex.toString());
        }

    }
}
