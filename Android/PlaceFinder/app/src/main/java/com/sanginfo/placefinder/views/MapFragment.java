package com.sanginfo.placefinder.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sanginfo.placefinder.R;
import com.sanginfo.placefinder.controllers.CommonMethods;
import com.sanginfo.placesapi.Place;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Sang.24 on 3/3/2017.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private static final String TAG = "MapFragment";
    private GoogleMap map;
    private ImageView userCentreImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        MapView mapView;
        try {
            view = inflater.inflate(R.layout.map_fragment_screen, null, false);
            mapView = (MapView) view.findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);
            userCentreImage = (ImageView) view.findViewById(R.id.user_centre_image);
            userCentreImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    centreUserLocation();
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            map = googleMap;


        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }

    }

    private Marker userPositionMarker;

    public void showUserLocation(final LatLng latlng) {
        try {
            if (userPositionMarker == null) {
                userPositionMarker = map.addMarker(new MarkerOptions().position(latlng).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.point_circle)).anchor(0.5f, 0.5f));
            } else {
                userPositionMarker.setPosition(latlng);
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(calculateZoomLevel(getScreenWidth())).build(); // googlemap.getCameraPosition().zoom
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }, 400);
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    ArrayList<Marker> markerList = new ArrayList<>();

    public void populatePlaces(final List<Place> places) {
        try {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    LayoutInflater inflater;
                    inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    for (Place place : places) {
                        addPlace(Uri.parse(place.getThumbnailURL(CommonMethods.getPlacesAPIKey(getActivity()))), inflater, new LatLng(place.getLatitude(), place.getLongitude()));
                    }
                }
            });

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    Handler mHandler = new Handler();

    private void addPlace(final Uri iconUrl, final LayoutInflater inflater, final LatLng latlng) {
        try {
            final View placesView;
            final CircularImageView imageView;
            placesView = inflater.inflate(R.layout.palce_image_layout, null);
            placesView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView = (CircularImageView) placesView.findViewById(R.id.profile_pic);
            Picasso.with(getActivity())
                    .load(iconUrl)
                    .into(new Target() {
                              @Override
                              public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                  imageView.setImageBitmap(bitmap);
                                  Marker placeMarker = map.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActivity(), placesView))));
                                  markerList.add(placeMarker);

                              }

                              @Override
                              public void onBitmapFailed(Drawable errorDrawable) {
                                  imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_local_bar));
                                  Marker placeMarker = map.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActivity(), placesView))));
                                  markerList.add(placeMarker);
                              }

                              @Override
                              public void onPrepareLoad(Drawable placeHolderDrawable) {

                              }
                          }
                    );

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    public void removeMarkers() {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (markerList != null) {
                        for (Marker marker : markerList) {
                            marker.remove();
                        }
                    }
                }
            });

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    public Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private int calculateZoomLevel(int screenWidth) {
        double equatorLength = 40075004; // in meters
        double widthInPixels = screenWidth;
        double metersPerPixel = equatorLength / 256;
        int zoomLevel = 1;
        while ((metersPerPixel * widthInPixels) > 20000) {
            metersPerPixel /= 2;
            ++zoomLevel;
        }
        return zoomLevel;
    }

    private int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private void centreUserLocation() {
        try {
            if (userPositionMarker != null) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(userPositionMarker.getPosition()).zoom(map.getCameraPosition().zoom).build(); // googlemap.getCameraPosition().zoom
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

        } catch (Exception ex) {

        }
    }

}
