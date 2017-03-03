package com.sanginfo.placefinder.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.sanginfo.placefinder.R;
import com.sanginfo.placefinder.views.MapFragment;

public class HomeScreen extends AppCompatActivity {
    private static final String TAG="HomeScreen";
    BottomNavigationView bottomNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        try{
            initializeActivityControl();
            openMapFragment();
        }catch (Exception ex){
            Log.e(TAG,ex.toString());
        }
    }
    private void initializeActivityControl(){
        try{
            bottomNavView=(BottomNavigationView)findViewById(R.id.bottom_navigation);
            bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_map:
                            openMapFragment();
                            break;
                        case R.id.action_list:

                            break;
                    }
                    return false;
                }
            });

        }catch (Exception ex){
            Log.e(TAG,ex.toString());
        }
    }

    private void openMapFragment(){
        try{
            MapFragment map=new MapFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.main_container,map,"map Fragment");
            transaction.commit();
        }catch (Exception ex){
            Log.e(TAG,ex.toString());
        }
    }
}
