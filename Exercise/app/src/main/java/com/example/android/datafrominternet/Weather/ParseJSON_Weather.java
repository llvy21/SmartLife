package com.example.android.datafrominternet.Weather;

import android.accounts.NetworkErrorException;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ucla on 2018/2/1.
 */

public class ParseJSON_Weather extends Throwable {

    public static HashMap<String,String> parseJson(String data)throws JSONException {

        HashMap<String,String> weatherdata = new HashMap<>();

        JSONObject weatherSearchResult = new JSONObject(data);

        JSONObject Weather = weatherSearchResult.getJSONArray("HeWeather6").getJSONObject(0);

        String status = Weather.getString("status");
        Log.d("test",status);
        weatherdata.put("status",status);
        if (status.equals("unknown city")){
            return weatherdata;
        }
        weatherdata.put("location",Weather.getJSONObject("basic").getString("location"));



        for (int i=0; i<3; i++){
            JSONObject daily_forecast = (JSONObject) Weather.getJSONArray("daily_forecast").get(i);
            weatherdata.put("tmp_max"+i, daily_forecast.getString("tmp_max"));
            weatherdata.put("tmp_min"+i, daily_forecast.getString("tmp_min"));
            weatherdata.put("cond_code_d"+i, daily_forecast.getString("cond_code_d"));
            weatherdata.put("cond_code_n"+i, daily_forecast.getString("cond_code_n"));
            weatherdata.put("date"+i, daily_forecast.getString("date"));
        }


        JSONObject now = Weather.getJSONObject("now");
        weatherdata.put("tmp",now.getString("tmp"));
        weatherdata.put("cond_code",now.getString("cond_code"));
        weatherdata.put("cond_txt",now.getString("cond_txt"));
        weatherdata.put("wind_dir",now.getString("wind_dir"));
        weatherdata.put("wind_sc",now.getString("wind_sc"));


        for (int i=0; i<8; i++){//0157
            JSONObject lifestyle = (JSONObject) Weather.getJSONArray("lifestyle").get(i);
            weatherdata.put("txt"+i,lifestyle.getString("txt"));
        }

        return weatherdata;
    }



}
