package com.example.giog.rxfluxapplication.stores;

import com.example.giog.rxfluxapplication.actions.Actions;
import com.example.giog.rxfluxapplication.actions.Keys;
import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;

/**
 * Created by wgoris31 on 1/24/2018.
 */

public class WifiCredentialsStore extends RxStore implements WifiCredentialsStoreInterface  {

    public static final String ID = "WifiCredentialsStore";
    private static WifiCredentialsStore instance;
    private String SSID;

    public WifiCredentialsStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    public static synchronized WifiCredentialsStore get (Dispatcher dispatcher){
        if (instance == null) instance = new WifiCredentialsStore(dispatcher);
        return instance;
    }

    @Override
    public String getSSID() {
        return this.SSID;
    }

    @Override
    public void onRxAction(RxAction action) {
        switch (action.getType()) {
            case Actions.GET_WIFI_SSID:
                this.SSID = action.get(Keys.SSID);
                break;
            default: // IMPORTANT if we don't modify the store just ignore
                return;
        }
        postChange(new RxStoreChange(ID, action));
    }
}
