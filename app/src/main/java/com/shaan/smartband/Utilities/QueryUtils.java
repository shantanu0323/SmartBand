package com.shaan.smartband.Utilities;

import android.text.TextUtils;
import android.util.Log;

import com.shaan.smartband.Models.HealthData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

class QueryUtils {
    private static final String TAG = "QueryUtils";

    public static List<HealthData> fetchHealthData(String queryUrl) {
        URL url = createUrl(queryUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
            Log.i(TAG, "fetchHealthDataHealthData: JSON RESPONSE FETCHED : " + jsonResponse);
        } catch (IOException e) {
            Log.e(TAG, "Problem making the HTTP request.");
        }

        // Extract relevant fields from the JSON response and create a list of {@link HealthData}s
        List<HealthData> healthDataList = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link HealthData}s
        return healthDataList;
    }

    private static URL createUrl(String stringUrl) {
//        Log.i(TAG, "createUrl: CALLED");
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
//        Log.i(TAG, "makeHttpRequest: CALLED");
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "Problem retrieving the news JSON results.");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<HealthData> extractFeatureFromJson(String healthJson) {
//        Log.i(TAG, "extractFeatureFromJson: CALLED");
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(healthJson)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<HealthData> healthDataList = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(healthJson);
            // put JSON data into the objects

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list of news
        return healthDataList;
    }
}
