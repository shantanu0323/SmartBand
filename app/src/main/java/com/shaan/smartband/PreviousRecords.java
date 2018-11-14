package com.shaan.smartband;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.shaan.smartband.Models.HealthData;
import com.shaan.smartband.Utilities.DataLoader;

import java.util.ArrayList;
import java.util.List;

public class PreviousRecords extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<HealthData>> {

    private static final String TAG = "PreviousRecords";
    private BarChart chartBodyTemp, chartAtmosTemp, chartPulseRate, chartHeatIndex;
    private ProgressDialog progressDialog;
    private boolean alreadyLoaded = false;
    private static final int LOADER_ID = 2;
    private static final String REQUEST_URL = "https://api.thingspeak.com/channels/312725/feeds.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_records);

        findViews();
        initiateLoader();
    }

    private void drawBodyTempChart(List<BarEntry> bodyTemps) {

        BarDataSet set = new BarDataSet(bodyTemps, "Body Temperature");
        set.setColor(Color.rgb(230, 155, 20));
        BarData data = new BarData(set);

        chartBodyTemp.setData(data);
        chartBodyTemp.setFitBars(true); // make the x-axis fit exactly all bars
        chartBodyTemp.invalidate(); // refresh
        chartBodyTemp.animateXY(2000, 2000);
    }

    private void drawAtmosTempChart(List<BarEntry> atmosTemps) {

        BarDataSet set = new BarDataSet(atmosTemps, "Atmospheric Temperature");
        set.setColor(Color.rgb(10, 230, 20));
        BarData data = new BarData(set);

        chartAtmosTemp.setData(data);
        chartAtmosTemp.setFitBars(true); // make the x-axis fit exactly all bars
        chartAtmosTemp.invalidate(); // refresh
        chartAtmosTemp.animateXY(2000, 2000);
    }

    private void drawPulseRateChart(List<BarEntry> pulseRates) {

        BarDataSet set = new BarDataSet(pulseRates, "Pulse Rate");
        set.setColor(Color.rgb(235, 30, 30));
        BarData data = new BarData(set);

        chartPulseRate.setData(data);
        chartPulseRate.setFitBars(true); // make the x-axis fit exactly all bars
        chartPulseRate.invalidate(); // refresh
        chartPulseRate.animateXY(2000, 2000);
    }

    private void drawHeatIndexChart(List<BarEntry> heatIndices) {

        BarDataSet set = new BarDataSet(heatIndices, "Heat Index");
        set.setColor(Color.rgb(105, 20, 230));
        BarData data = new BarData(set);

        chartHeatIndex.setData(data);
        chartHeatIndex.setFitBars(true); // make the x-axis fit exactly all bars
        chartHeatIndex.invalidate(); // refresh
        chartHeatIndex.animateXY(2000, 2000);
    }


    private void findViews() {
        chartBodyTemp = findViewById(R.id.chartBodyTemp);
        chartBodyTemp.setBackgroundColor(Color.WHITE);
        chartAtmosTemp = findViewById(R.id.chartAtmosTemp);
        chartAtmosTemp.setBackgroundColor(Color.WHITE);
        chartPulseRate = findViewById(R.id.chartPulseRate);
        chartPulseRate.setBackgroundColor(Color.WHITE);
        chartHeatIndex = findViewById(R.id.chartHeatIndex);
        chartHeatIndex.setBackgroundColor(Color.WHITE);
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
        Snackbar.make(findViewById(R.id.chartBodyTemp), "Data Updated Successfully", Snackbar.LENGTH_SHORT).show();
        String message = "";
        List<BarEntry> bodyTemps = new ArrayList<>();
        List<BarEntry> atmosTemps = new ArrayList<>();
        List<BarEntry> pulseRates = new ArrayList<>();
        List<BarEntry> heatIndices = new ArrayList<>();
        for (int index = healthDataList.size() - 25; index < healthDataList.size(); index++) {
            HealthData healthData = healthDataList.get(index);
            message += healthData.display();
            bodyTemps.add(new BarEntry(index, Float.parseFloat(healthData.getBodyTemp())));
            atmosTemps.add(new BarEntry(index, Float.parseFloat(healthData.getAtmosTemp())));
            pulseRates.add(new BarEntry(index, Float.parseFloat(healthData.getPulseRate())));
            heatIndices.add(new BarEntry(index, Float.parseFloat(healthData.getHeatIndex())));
        }
        drawBodyTempChart(bodyTemps);
        drawAtmosTempChart(atmosTemps);
        drawPulseRateChart(pulseRates);
        drawHeatIndexChart(heatIndices);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<HealthData>> loader) {

    }
}
