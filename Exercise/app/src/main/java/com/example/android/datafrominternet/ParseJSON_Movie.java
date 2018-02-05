package com.example.android.datafrominternet;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ucla on 2018/2/5.
 */

public class ParseJSON_Movie extends Throwable {


    public static ArrayList<MovieData> parseMovieData (String data) throws JSONException{

        ArrayList<MovieData> movieDataArrayList = new ArrayList<MovieData>();

        for (int i = 0; i < 7; i++){

            JSONObject movieSearchResult = new JSONObject(data);
            JSONObject movies = (JSONObject) movieSearchResult.getJSONObject("data").getJSONArray("movies").get(i);

            String name = movies.getString("nm");
            String img = movies.getString("img");
            String cast = movies.getString("star");
            String remark = movies.getString("scm");

            MovieData dataObject = new MovieData(name,cast,img,remark);

            movieDataArrayList.add(dataObject);

            Log.d("test", name+" ");

        }


        return movieDataArrayList;

    }

}
