package com.sanginfo.placefinder.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sanginfo.placefinder.R;
import com.sanginfo.placefinder.adapters.PlaceListAdapter;
import com.sanginfo.placesapi.Place;

import java.util.List;

import static com.google.android.gms.internal.zzt.TAG;

/**
 * Created by Sang.24 on 3/3/2017.
 */

public class ListFragment extends Fragment {
    private ListView placeList;
    private List<Place> places;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        try {
            view = inflater.inflate(R.layout.list_fragment_screen, null, false);
            placeList = (ListView) view.findViewById(R.id.place_list);
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        return view;
    }

    /**
     * Populates places from the given list
     * @param placeArrayList
     */
    public void populatePlaceList(List<Place> placeArrayList) {
        PlaceListAdapter placeListAdapter;
        try {
            places = placeArrayList;
            placeListAdapter = new PlaceListAdapter(getContext(), R.layout.place_list_item_layout, places);
            placeList.setAdapter(placeListAdapter);
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }
}
