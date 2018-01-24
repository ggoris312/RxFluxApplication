package com.example.giog.rxfluxapplication;

import android.app.Application;
import android.content.Context;

import com.example.giog.rxfluxapplication.actions.WifiActionCreator;
import com.hardsoftstudio.rxflux.RxFlux;
import com.hardsoftstudio.rxflux.util.LogLevel;

/**
 * Created by wgoris31 on 1/24/2018.
 */

public class RxFluxApplication extends Application {

    private RxFlux rxFlux;

    private WifiActionCreator wifiActionCreator;

    public static RxFluxApplication get(Context context){
        return ((RxFluxApplication) context.getApplicationContext());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RxFlux.LOG_LEVEL = BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE;
        rxFlux = RxFlux.init(this);
        wifiActionCreator =
                new WifiActionCreator(getApplicationContext(), rxFlux.getDispatcher(), rxFlux.getSubscriptionManager());
    }

    public RxFlux getRxFlux() {
        return rxFlux;
    }

    public WifiActionCreator getWifiActionCreator() {
        return wifiActionCreator;
    }


}
