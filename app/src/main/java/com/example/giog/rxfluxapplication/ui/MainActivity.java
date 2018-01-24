package com.example.giog.rxfluxapplication.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giog.rxfluxapplication.R;
import com.example.giog.rxfluxapplication.RxFluxApplication;
import com.example.giog.rxfluxapplication.actions.Actions;
import com.example.giog.rxfluxapplication.stores.WifiCredentialsStore;
import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.giog.rxfluxapplication.RxFluxApplication.get;

public class MainActivity extends AppCompatActivity implements RxViewDispatch {

    @BindView(R.id.turn_wifi_on_button)
    Button turnWifiOn;
    @BindView(R.id.current_ssid_textview)
    TextView currentSsid;

    private String name = "";
    private String pass = "";

    private WifiCredentialsStore wifiCredentialsStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        turnWifiOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jsonString = loadJSONFromAsset(MainActivity.this);
                try {
                    JSONObject obj = new JSONObject(jsonString);
                    name = obj.getString("SSID");
                    pass = obj.getString("password");
                    get(MainActivity.this).getWifiActionCreator().turnOnWifi(name, pass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getStoreId()) {
            case WifiCredentialsStore.ID:
                switch (change.getRxAction().getType()) {
                    case Actions.GET_WIFI_SSID:
                        currentSsid.setText(wifiCredentialsStore.getSSID());
                        break;
                }
                break;
        }
    }

    @Override
    public void onRxError(@NonNull RxError error) {
        Throwable throwable = error.getThrowable();
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRxViewRegistered() {

    }

    @Override
    public void onRxViewUnRegistered() {

    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToRegister() {
        wifiCredentialsStore = WifiCredentialsStore.get(get(this).getRxFlux().getDispatcher());
        return Arrays.asList(wifiCredentialsStore);
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToUnRegister() {
        return null;
    }

    private void refresh() {

        get(this).getWifiActionCreator().getCurrentWifiSSID();
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("traveltab.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

}
