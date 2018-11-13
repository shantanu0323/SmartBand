package com.shaan.smartband.Utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.shaan.smartband.Models.HealthData;

import java.io.IOException;
import java.util.List;

public class DataLoader extends AsyncTaskLoader<List<HealthData>> {

    private static final String TAG = "DataLoader";
    private final String queryUrl;

    public DataLoader(@NonNull Context context, String queryUrl) {
        super(context);
        this.queryUrl = queryUrl;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public List<HealthData> loadInBackground() {
        if (queryUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of news.
        List<HealthData> data = null;
        data = QueryUtils.fetchHealthData(queryUrl);
        return data;
    }
}
