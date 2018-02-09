package com.example.android.datafrominternet.utilities;

import android.net.Uri;
import android.util.Log;
import android.widget.BaseAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtil_Weather {

    final static String[] BASE_URL = {"https://free-api.heweather.com/s6/weather?"
            ,"https://free-api.heweather.com/s6/air/now?"};

    final static String PARAM_CITY = "location";

    final static String KEY = "key";

    final static String MYKEY = "4a88f102af9249a4b62391d50c633549";

    public static URL buildUrl(String citySearchQuery,int i) {
        Uri builtUri = Uri.parse(BASE_URL[i]).buildUpon()
                .appendQueryParameter(PARAM_CITY,citySearchQuery)
                .appendQueryParameter(KEY,MYKEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.d("test",url.toString());
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

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
        }
    }
}