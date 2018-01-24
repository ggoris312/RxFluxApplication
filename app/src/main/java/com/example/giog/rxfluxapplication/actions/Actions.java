package com.example.giog.rxfluxapplication.actions;

import android.content.Context;

import com.hardsoftstudio.rxflux.action.RxAction;

/**
 * Created by wgoris31 on 1/24/2018.
 */

public interface Actions {

    String TURN_ON_WIFI = "turn_on_wifi";
    String GET_WIFI_SSID = "get_wifi_SSID";

    void turnOnWifi(String ssid, String pass);
    void getCurrentWifiSSID();

    boolean retry(RxAction action);
}
