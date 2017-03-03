package com.sanginfo.placefinder.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;
import com.sanginfo.placefinder.R;

import static com.google.android.gms.internal.zzt.TAG;

/**
 * Created by Sang.24 on 3/3/2017.
 */

public class ListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        MapView mapView;
        try {
            view = inflater.inflate(R.layout.list_fragment_screen, null, false);
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        return view;
    }
}
