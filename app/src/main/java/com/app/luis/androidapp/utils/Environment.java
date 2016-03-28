package com.app.luis.androidapp.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by Luis on 05/03/2016.
 */
public class Environment {

    public static final String ACCEPT_HEADER = "application/vnd.androidapp.v1+json";
    public static final String CONTENT_TYPE = "application/json";
    public static final String FIREBASE_URL = "https://<proyecto>.firebaseio.com/";
    private static Environment instance;
    private final String SSID_LOCAL = "Cablemas02D35";
    private final String BASE_URL;

    private Context context;

    private Environment(Context context) {
        this.context = context;
        String BASE_IP = (isLocalEnviroment()) ? "http://192.168.0.18/android-api/public" : "http://54.219.134.90";
        BASE_URL = BASE_IP + "/api/";
    }

    public static Environment getInstance(Context context) {
        if (instance == null) {
            instance = new Environment(context);
        }
        return instance;
    }

    public String getBASE_URL() {
        return BASE_URL;
    }

    private boolean isLocalEnviroment() {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d("SSID", wifiInfo.getSSID());
        return (wifiInfo.getSSID().equals("\"" + SSID_LOCAL + "\""));
    }
}
