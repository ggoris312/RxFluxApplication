package com.example.giog.rxfluxapplication.actions;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;

import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.action.RxActionCreator;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.util.SubscriptionManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.giog.rxfluxapplication.actions.Keys.SSID;
import static com.example.giog.rxfluxapplication.core.WifiApi.connect;
import static com.example.giog.rxfluxapplication.core.WifiApi.getWifiSSID;


/**
 * Created by wgoris31 on 1/24/2018.
 */

public class WifiActionCreator extends RxActionCreator implements Actions{

    private Context context;

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
                SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
                switch (state) {
                    case COMPLETED:
                        getCurrentWifiSSID();
                        break;
                    case DISCONNECTED:
                        getCurrentWifiSSID();

                }
            }
        }
    };



    public WifiActionCreator(Context context, Dispatcher dispatcher, SubscriptionManager manager) {
        super(dispatcher, manager);
        this.context = context;
        IntentFilter mIntentFilter = new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        this.context.registerReceiver(wifiReceiver, mIntentFilter);
    }

    @Override
    public void turnOnWifi(String ssid, String pass) {
        connect(context, ssid, pass);
    }

    @Override
    public void getCurrentWifiSSID() {
        final RxAction action = newRxAction(GET_WIFI_SSID);
        if (hasRxAction(action)) return;
        String ssid = getWifiSSID(context);
        Observable<String> o = Observable.just(ssid);
        addRxAction(action, o
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(currentSSID -> postRxAction(newRxAction(GET_WIFI_SSID, SSID, currentSSID))
                ,throwable -> postError(action, throwable)));
    }

    @Override
    public boolean retry(RxAction action) {
        if (hasRxAction(action)) return true;

        switch (action.getType()) {
            case GET_WIFI_SSID:
                getCurrentWifiSSID();
                return true;
        }
        return false;

    }
}
