package com.example.android.datafrominternet.Movie;

import android.accounts.NetworkErrorException;
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

import com.example.android.datafrominternet.R;
import com.example.android.datafrominternet.utilities.NetWorkUtil_Movie;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ucla on 2018/2/4.
 */

public class Movie extends AppCompatActivity {

    private ImageView movie;

    private TextView name,who,director;

    private TextView kind,date,point;

    private TextView cinema;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        movie = (ImageView) findViewById(R.id.iv_movie);

        name = (TextView) findViewById(R.id.tv_name);
        who = (TextView) findViewById(R.id.tv_who);
        director = (TextView) findViewById(R.id.tv_director);
        kind = (TextView) findViewById(R.id.tv_kind);
        date = (TextView) findViewById(R.id.tv_date);
        point = (TextView)findViewById(R.id.tv_point);
        cinema = (TextView) findViewById(R.id.tv_cinema);

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
                Log.d("test","a2ondso2");
            try {

                parsedData = ParseJSON_Movie.parseMovieData(data);

                Log.d("test","asdfasdfINle");
                Log.d("test","Why Null");
                URL[] urls = new URL[7];
                for (int i = 0; i < 7; i++){
                    urls[i] = new URL(parsedData.get(i).getImg_url());
                }
                new ShowPostTask().execute(urls);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (NetworkErrorException e) {
                Toast.makeText(Movie.this,"网络错误 请稍后重试",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }

            director.setText(parsedData.get(0).getCast());
            kind.setText(parsedData.get(0).getRemark());
            name.setText(parsedData.get(0).getMovieName());

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
            movie.setImageBitmap(bitmap);
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
