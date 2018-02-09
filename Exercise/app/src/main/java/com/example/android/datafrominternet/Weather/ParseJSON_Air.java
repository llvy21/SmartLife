package com.example.android.datafrominternet.Weather;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ucla on 2018/2/6.
 */

public class ParseJSON_Air extends Throwable {

    public static HashMap<String,String> parseJson(String data)throws JSONException {

        HashMap<String, String> airdata = new HashMap<>();

        JSONObject airSearchResult = new JSONObject(data)
                .getJSONArray("HeWeather6")
                .getJSONObject(0)
                .getJSONObject("air_now_city");

        airdata.put("aqi",airSearchResult.getString("aqi"));
        airdata.put("main",airSearchResult.getString("main"));
        airdata.put("quality",airSearchResult.getString("qlty"));

        return airdata;

    }
}
