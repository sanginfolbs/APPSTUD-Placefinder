package com.sanginfo.placefinder.ui;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.sanginfo.placefinder.R;
import com.sanginfo.placefinder.utils.CommonMethods;
import com.sanginfo.placefinder.views.ListFragment;
import com.sanginfo.placefinder.views.MapFragment;
import com.sanginfo.placesapi.NRPlaces;
import com.sanginfo.placesapi.Place;
import com.sanginfo.placesapi.PlaceType;
import com.sanginfo.placesapi.PlacesException;
import com.sanginfo.placesapi.PlacesListener;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

import static com.sanginfo.placefinder.utils.CommonMethods.getPlacesAPIKey;

public class HomeScreen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "HomeScreen";
    BottomNavigationView bottomNavView;
    private String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private Handler mHandler = new Handler();
    private GoogleApiClient googleApiClient;
    LocationListener locationListener;
    Location userLocation;
    MapFragment mapFragment;
    ListFragment listFragment;
    private CircularProgressBar progressbar;
    private TextView header;
    private AlertDialog myAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        try {
            initializeActivityControl();
            addFragments();
            openMapFragment();
            buildGoogleApiClient();
            if (!hasPermission()) { //Check if required permisions are present
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(permissions, 111);
                        }
                    }
                }, 1000);
            } else {
                if (CommonMethods.isNetworkAvailable(getBaseContext())) {
                    googleApiClient.connect();
                } else {
                    showNoInternetDialog();
                }

            }
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    private void initializeActivityControl() {
        try {
            progressbar = (CircularProgressBar) findViewById(R.id.progressbar);
            header = (TextView) findViewById(R.id.header_text);
            bottomNavView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
            bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_map:
                            openMapFragment();
                            break;
                        case R.id.action_list:
                            openListFragment();
                            break;
                    }
                    return false;
                }
            });

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    private void addFragments() {
        addListFragment();
        addMapFragment();
    }

    /**
     * Creates and adds map to FragmentManager
     */
    private void addMapFragment() {
        try {
            mapFragment = new MapFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.main_container, mapFragment, "map Fragment");
            transaction.commit();
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    /**
     * Creates and adds list to FragmentManager
     */
    private void addListFragment() {
        try {
            listFragment = new ListFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.main_container, listFragment, "List Fragment");
            transaction.commit();
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    /**
     * Bring map view on the top
     */
    private void openMapFragment() {
        try {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.hide(listFragment);
            transaction.show(mapFragment);
            transaction.commit();
            header.setText("Map");
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    /**
     * Bring list view on the top
     */
    private void openListFragment() {
        try {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.hide(mapFragment);
            transaction.show(listFragment);
            transaction.commit();
            header.setText("List");

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    /**
     * CHeck if user has granted the permissions
     * @return
     */
    public boolean hasPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String p : permissions) {
            if (PackageManager.PERMISSION_DENIED == checkSelfPermission(p)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean permissionGranted = true;
        try {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    permissionGranted = false;
                    break;
                }
            }
            if (!permissionGranted) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_text), Toast.LENGTH_LONG).show();
                progressbar.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Reuqets user location only once and stop the update after getting it.
     */
    private void requestLocation() {
        LocationRequest locationRequest;
        try {

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    try {
                        userLocation = location;
                        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, locationListener);
                        mapFragment.renderUserLocation(new LatLng(location.getLatitude(), location.getLongitude()));
                        requestPlaces();
                        progressbar.setVisibility(View.VISIBLE);

                    } catch (Exception ex) {

                    }
                }
            };
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000);//1 minutes
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);

        } catch (Exception ex) {
            ex.toString();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasPermission()) {
            if (CommonMethods.isNetworkAvailable(getBaseContext())) {
                googleApiClient.connect();
            } else {
                showNoInternetDialog();
            }
        }
    }

    /**
     * Build API client for FusedLocation API
     */
    private void buildGoogleApiClient() {
        GoogleApiClient.Builder builder;
        try {
            builder = new GoogleApiClient.Builder(this);
            builder.addConnectionCallbacks(this);
            builder.addOnConnectionFailedListener(this);
            builder.addApi(LocationServices.API);
            googleApiClient = builder.build();
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    ArrayList<Place> placeList;

    /**
     * Make call to Google Places Web service. Refer https://github.com/nomanr/Android-Google-Places-API
     */
    private void requestPlaces() {
        NRPlaces.Builder builder;
        PlacesListener placesListener;
        NRPlaces nrPlaces;
        try {
            placesListener = new PlacesListener() {
                @Override
                public void onPlacesFailure(final PlacesException e) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressbar.setVisibility(View.GONE);
                                Toast.makeText(HomeScreen.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (Exception ex) {
                        Log.e(TAG, ex.toString());
                    }
                }

                @Override
                public void onPlacesStart() {
                }

                @Override
                public void onPlacesSuccess(List<Place> places) {
                    try {
                        placeList = new ArrayList<Place>();
                        placeList.addAll(places);
                        mapFragment.removeMarkers();
                        mapFragment.populatePlaces(places);
                        listFragment.populatePlaceList(places);

                    } catch (Exception ex) {
                        Log.e(TAG, ex.toString());
                    }
                }

                @Override
                public void onPlacesFinished() {
                    try {
                        progressbar.setVisibility(View.GONE);
                    } catch (Exception ex) {

                    }

                }
            };
            builder = new NRPlaces.Builder();
            builder.listener(placesListener);
            builder.key(getPlacesAPIKey(getApplicationContext()));
            builder.latlng(userLocation.getLatitude(), userLocation.getLongitude());
            builder.radius(2000);
            builder.type(PlaceType.BAR);
            nrPlaces = builder.build();
            nrPlaces.execute();
        } catch (Exception ex) {
            ex.toString();
        }
    }

    /**
     * Show the dialog if theres no internet connectivity
     */
    private void showNoInternetDialog() {
        try {
            if (myAlertDialog != null && myAlertDialog.isShowing()) {
                return;
            }
            AlertDialog.Builder popupBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            popupBuilder.setTitle(getResources().getString(R.string.app_name));
            popupBuilder.setMessage(R.string.no_internet_window_message);
            popupBuilder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    finish();
                }
            });
            popupBuilder.setCancelable(false);
            myAlertDialog = popupBuilder.create();
            myAlertDialog.show();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }


}
