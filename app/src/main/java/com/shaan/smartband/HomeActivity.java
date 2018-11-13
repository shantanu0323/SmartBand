package com.shaan.smartband;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.shaan.smartband.Models.HealthData;
import com.shaan.smartband.Utilities.DataLoader;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<HealthData>> {

    private static final int LOADER_ID = 1;
    private static final String REQUEST_URL = "https://api.thingspeak.com/channels/259686/feeds.json";
    private boolean alreadyLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initiateLoader();
    }

    private void initiateLoader() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getSupportLoaderManager();

            if (alreadyLoaded) {
                loaderManager.restartLoader(LOADER_ID, null, this);
            } else {
                loaderManager.initLoader(LOADER_ID, null, this);
                alreadyLoaded = true;
            }

        } else {
            // No Connection
            Toast.makeText(getApplicationContext(), "No internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @NonNull
    @Override
    public Loader<List<HealthData>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new DataLoader(getApplicationContext(), REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<HealthData>> loader, List<HealthData> healthDataList) {
        String message = "";
        for (HealthData healthData :
                healthDataList) {
            message += healthData.display();
        }
        ((TextView) findViewById(R.id.tvData)).setText(message);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<HealthData>> loader) {

    }
}


