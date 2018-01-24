package com.example.giog.rxfluxapplication.core;

import android.content.Context;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by wgoris31 on 1/24/2018.
 */

public class WifiApi {

    public static String getWifiSSID(Context context){
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;
        wifiInfo = wifiManager.getConnectionInfo();

        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            return wifiInfo.getSSID();
        }

        return "No Connection";
    }

    public static void connect(Context context, String ssid, String pass){
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration conf = new WifiConfiguration();
        conf.hiddenSSID = true;
        conf.priority = 1000;

        conf.SSID = "\"" + ssid + "\"";
        conf.preSharedKey = "\""+pass+"\"";
        conf.status = WifiConfiguration.Status.ENABLED;
        conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);

        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);

        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

        conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        int res = wifiManager.addNetwork(conf);
        boolean es = wifiManager.saveConfiguration();
        wifiManager.disconnect();
        boolean bRet = wifiManager.enableNetwork(res, true);
        wifiManager.reconnect();
    }


}
