package com.example.android.datafrominternet.Movie;

import android.accounts.NetworkErrorException;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ucla on 2018/2/5.
 */

public class ParseJSON_Movie extends Throwable {


    public static ArrayList<MovieData> parseMovieData (String data) throws JSONException, NetworkErrorException{

        ArrayList<MovieData> movieDataArrayList = new ArrayList<MovieData>();

        for (int i = 0; i < 7; i++){

            JSONObject movieSearchResult = new JSONObject(data);

            String status = movieSearchResult.getString("status");
            if (status.equals("500")) {
                throw new NetworkErrorException();
            }

            JSONObject movies = (JSONObject) movieSearchResult.getJSONObject("data").getJSONArray("movies").get(i);

            String name = movies.getString("nm");
            String img = movies.getString("img");
            String cast = movies.getString("star");
            String remark = movies.getString("scm");
            String director = movies.getString("dir");
            String kind = movies.getString("cat");
            String cinema = movies.getString("showInfo");
            String date = movies.getString("rt");


            MovieData dataObject = new MovieData(name,cast,img,remark,director,kind,cinema,date);
            Log.d("test",dataObject.getCast()+"cast is balabalabal");
            movieDataArrayList.add(dataObject);

            Log.d("test", name+" ");

        }


        return movieDataArrayList;

    }

}
