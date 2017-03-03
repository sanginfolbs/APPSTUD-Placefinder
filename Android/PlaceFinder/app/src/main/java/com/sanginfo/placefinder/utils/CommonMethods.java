package com.sanginfo.placefinder.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

/**
 * Created by Sang.24 on 3/3/2017.
 */

public class CommonMethods {

    /**
     * CHeck if network connectivity is present
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                if (ni.isConnected()) {
                    haveConnectedWifi = true;
                }
            }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (ni.isConnected()) {
                    haveConnectedMobile = true;
                }
            }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    /**
     * Check if location is enabled on the device.
     * @param context
     * @return
     */
    public static boolean locationServicesEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean net_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            net_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return gps_enabled || net_enabled;
    }

    /**
     * Get Google places API Key from the manifest.
     * @param context
     * @return
     */
    public static String getPlacesAPIKey(Context context){
        ApplicationInfo applicationInfo;
        Bundle bundle;
        String apiKey="";
        try{
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            bundle = applicationInfo.metaData;
            apiKey = bundle.getString("com.google.android.places.API_KEY");
        }
        catch(Exception ex){
            ex.toString();
        }
        return apiKey;
    }


}
