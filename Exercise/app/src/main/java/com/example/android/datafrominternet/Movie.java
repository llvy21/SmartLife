package com.example.android.datafrominternet;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.datafrominternet.utilities.NetWorkUtil_Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ucla on 2018/2/4.
 */

public class Movie extends AppCompatActivity {

    private ImageView post_imageView;

    private TextView cast_tv;

    private TextView movieName_tv;

    private TextView remark_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        post_imageView = (ImageView) findViewById(R.id.iv_movie);

        cast_tv = (TextView) findViewById(R.id.tv_cast);
        remark_tv = (TextView) findViewById(R.id.tv_remark);
        movieName_tv = (TextView) findViewById(R.id.tv_name);

        makerMovieSeachQuery();


    }

    public class MovieQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String movieSearchResults = null;
            try {
                movieSearchResults = NetWorkUtil_Movie.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieSearchResults;
        }

        @Override
        protected void onPostExecute(String data) {
            ArrayList<MovieData> parsedData= new ArrayList<MovieData>();
            if(data != null && !data.equals(""))
                try {
                    parsedData = ParseJSON_Movie.parseMovieData(data);
                    if (!new JSONObject(data).getString("status").equals("0")){
                        Toast.makeText(Movie.this,"状态错误 请稍后重试",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    URL[] urls = new URL[7];
                    for (int i = 0; i < 7; i++){
                        urls[i] = new URL(parsedData.get(i).getImg_url());
                    }
                    new ShowPostTask().execute(urls);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            cast_tv.setText(parsedData.get(0).getCast());
            remark_tv.setText(parsedData.get(0).getRemark());
            movieName_tv.setText(parsedData.get(0).getMovieName());

            super.onPostExecute(data);
        }
    }

    public class ShowPostTask extends AsyncTask<URL, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(URL... params) {
            Bitmap bitmap = null;
            URL searchUrl = params[0];
            try {
                bitmap = getBitmap(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            post_imageView.setImageBitmap(bitmap);
            super.onPostExecute(bitmap);
        }

    }


    private static Bitmap getBitmap(URL img_url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) img_url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }

    private void makerMovieSeachQuery(){
        URL movieSearchUrl = NetWorkUtil_Movie.buildUrl();
        new MovieQueryTask().execute(movieSearchUrl);

//        new ShowPostTask().execute();
    }


}
