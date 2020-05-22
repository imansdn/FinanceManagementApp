package com.imandroid.financemanagement.data.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkConnectionHelper {

    private static String TAG = "HttpConnectionHelper";

    public static URL buildUrl(String baseUrl) {
        Uri builtUri = Uri.parse(baseUrl)
                .buildUpon()
//                .appendQueryParameter(Constant.API_KEY_PARAM, Constant.API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
            Log.i(TAG, "buildUrl: "+url.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(String baseUrl) throws IOException {
        URL url = buildUrl(baseUrl);
        Log.i(TAG, "getResponseFromHttpUrl: openConnection");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Log.i(TAG, "getResponseFromHttpUrl: length"+in.toString().length());
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
            Log.i(TAG, "getResponseFromHttpUrl: urlConnection.disconnect()");

        }
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
