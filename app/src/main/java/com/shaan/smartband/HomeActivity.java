package com.shaan.smartband;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.shaan.smartband.Models.HealthData;
import com.shaan.smartband.Utilities.DataLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<HealthData>> {

    private static final String TAG = "HomeActivity";
    private static final int LOADER_ID = 1;
    private static final String REQUEST_URL = "https://api.thingspeak.com/channels/312725/feeds.json";
    private boolean alreadyLoaded = false;
    private BarChart chartBodyTemp;
    private Button bRefresh, bPrevRecords;
    private TextView tvBodyTemp, tvAtmosTemp, tvPulseRate, tvHeatIndex, tvLastUpdated;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViews();
        initiateLoader();

        bRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initiateLoader();
            }
        });

        bPrevRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PreviousRecords.class));
            }
        });
//        drawBodyTempChart();
    }


    private void findViews() {
        tvBodyTemp = findViewById(R.id.tvBodyTemp);
        tvAtmosTemp = findViewById(R.id.tvAtmosTemp);
        tvPulseRate = findViewById(R.id.tvPulseRate);
        tvHeatIndex = findViewById(R.id.tvHeatIndex);
        tvLastUpdated = findViewById(R.id.tvLastUpdated);
        bRefresh = findViewById(R.id.bRefresh);
        bPrevRecords = findViewById(R.id.bPrevRecords);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Hold On !!!");
        progressDialog.setMessage("Just a sec while I fetch the details...");
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
            progressDialog.show();
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
            Toast.makeText(getApplicationContext(), "SMART BAND : No internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @NonNull
    @Override
    public Loader<List<HealthData>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new DataLoader(getApplicationContext(), REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<HealthData>> loader, List<HealthData> healthDataList) {
        progressDialog.dismiss();
        Snackbar.make(findViewById(R.id.bRefresh), "Data Updated Successfully", Snackbar.LENGTH_SHORT).show();
        String message = "";
        for (HealthData healthData :
                healthDataList) {
            message += healthData.display();
        }
        ((TextView) findViewById(R.id.tvData)).setText(message);
        HealthData healthData = healthDataList.get(healthDataList.size() - 1);
        tvBodyTemp.setText(healthData.getBodyTemp() + " °C");
        tvAtmosTemp.setText(healthData.getAtmosTemp() + " °C");
        tvPulseRate.setText(healthData.getPulseRate() + " bpm");
        tvHeatIndex.setText(healthData.getHeatIndex());
        tvLastUpdated.setText("Last Updated on : " + getDateValue(healthData.getCreatedAt()));
    }

    private String getDateValue(String dateStr) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-ddhh:mm:ss")
                    .parse(dateStr.replace('T', ' ').replace('Z', ' '));
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy, hh:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("IST"));
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "getDateValue: ERROR PARSING DATE", e);
            return dateStr + " in GMT";
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<HealthData>> loader) {

    }
}


