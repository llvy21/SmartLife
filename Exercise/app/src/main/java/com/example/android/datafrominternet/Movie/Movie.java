package com.example.android.datafrominternet.Movie;

import android.accounts.NetworkErrorException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

    private ImageView movie1, movie2, movie3, movie4, movie5, movie6, movie7;

    private TextView name4,who4,director4,name5,who5,director5,name6,who6,director6,name7,who7,director7;

    private TextView kind4,date4,point4, kind5,date5,point5, kind6,date6,point6, kind7,date7,point7;

    private TextView cinema4,cinema5,cinema6,cinema7;

    private TextView name3,who3,director3;

    private TextView kind3,date3,point3;

    private TextView cinema3;

    private TextView name1,who1,director1;

    private TextView kind1,date1,point1;

    private TextView cinema1;

    private TextView name2,who2,director2;

    private TextView kind2,date2,point2;

    private TextView cinema2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        movie1 = (ImageView) findViewById(R.id.iv_movie1);
        name1 = (TextView) findViewById(R.id.tv_name1);
        who1 = (TextView) findViewById(R.id.tv_who1);
        director1 = (TextView) findViewById(R.id.tv_director1);
        kind1 = (TextView) findViewById(R.id.tv_kind1);
        date1 = (TextView) findViewById(R.id.tv_date1);
        point1 = (TextView)findViewById(R.id.tv_point1);
        cinema1 = (TextView) findViewById(R.id.tv_cinema1);

        movie2 = (ImageView) findViewById(R.id.iv_movie2);
        name2 = (TextView) findViewById(R.id.tv_name2);
        who2 = (TextView) findViewById(R.id.tv_who2);
        director2 = (TextView) findViewById(R.id.tv_director2);
        kind2 = (TextView) findViewById(R.id.tv_kind2);
        date2 = (TextView) findViewById(R.id.tv_date2);
        point2 = (TextView)findViewById(R.id.tv_point2);
        cinema2 = (TextView) findViewById(R.id.tv_cinema2);

        movie3 = (ImageView) findViewById(R.id.iv_movie3);
        name3= (TextView) findViewById(R.id.tv_name3);
        who3 = (TextView) findViewById(R.id.tv_who3);
        director3 = (TextView) findViewById(R.id.tv_director3);
        kind3 = (TextView) findViewById(R.id.tv_kind3);
        date3 = (TextView) findViewById(R.id.tv_date3);
        point3 = (TextView)findViewById(R.id.tv_point3);
        cinema3 = (TextView) findViewById(R.id.tv_cinema3);

        movie4 = (ImageView) findViewById(R.id.iv_movie4);
        name4= (TextView) findViewById(R.id.tv_name4);
        who4 = (TextView) findViewById(R.id.tv_who4);
        director4 = (TextView) findViewById(R.id.tv_director4);
        kind4 = (TextView) findViewById(R.id.tv_kind4);
        date4 = (TextView) findViewById(R.id.tv_date4);
        point4 = (TextView)findViewById(R.id.tv_point4);
        cinema4 = (TextView) findViewById(R.id.tv_cinema4);

        movie5 = (ImageView) findViewById(R.id.iv_movie5);
        name5= (TextView) findViewById(R.id.tv_name5);
        who5 = (TextView) findViewById(R.id.tv_who5);
        director5 = (TextView) findViewById(R.id.tv_director5);
        kind5 = (TextView) findViewById(R.id.tv_kind5);
        date5 = (TextView) findViewById(R.id.tv_date5);
        point5 = (TextView)findViewById(R.id.tv_point5);
        cinema5 = (TextView) findViewById(R.id.tv_cinema5);

        movie6 = (ImageView) findViewById(R.id.iv_movie6);
        name6= (TextView) findViewById(R.id.tv_name6);
        who6 = (TextView) findViewById(R.id.tv_who6);
        director6 = (TextView) findViewById(R.id.tv_director6);
        kind6 = (TextView) findViewById(R.id.tv_kind6);
        date6 = (TextView) findViewById(R.id.tv_date6);
        point6 = (TextView)findViewById(R.id.tv_point6);
        cinema6 = (TextView) findViewById(R.id.tv_cinema6);

        movie7 = (ImageView) findViewById(R.id.iv_movie7);
        name7= (TextView) findViewById(R.id.tv_name7);
        who7 = (TextView) findViewById(R.id.tv_who7);
        director7 = (TextView) findViewById(R.id.tv_director7);
        kind7 = (TextView) findViewById(R.id.tv_kind7);
        date7 = (TextView) findViewById(R.id.tv_date7);
        point7 = (TextView)findViewById(R.id.tv_point7);
        cinema7 = (TextView) findViewById(R.id.tv_cinema7);

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

            director1.setText("导演： "+parsedData.get(0).getDirector());
            kind1.setText("类型： "+parsedData.get(0).getKind());
            name1.setText(parsedData.get(0).getMovieName());
            date1.setText(parsedData.get(0).getDate());
            point1.setText("主演： "+parsedData.get(0).getCast());
            cinema1.setText(parsedData.get(0).getCinema());
            who1.setText(parsedData.get(0).getRemark());


            director2.setText("导演： "+parsedData.get(1).getDirector());
            kind2.setText("类型： "+parsedData.get(1).getKind());
            name2.setText(parsedData.get(1).getMovieName());
            date2.setText(parsedData.get(1).getDate());
            point2.setText("主演： "+parsedData.get(1).getCast());
            cinema2.setText(parsedData.get(1).getCinema());
            who2.setText(parsedData.get(1).getRemark());


            director3.setText("导演： "+parsedData.get(2).getDirector());
            kind3.setText("类型： "+parsedData.get(2).getKind());
            name3.setText(parsedData.get(2).getMovieName());
            date3.setText(parsedData.get(2).getDate());
            point3.setText("主演： "+parsedData.get(2).getCast());
            cinema3.setText(parsedData.get(2).getCinema());
            who3.setText(parsedData.get(2).getRemark());


            director4.setText("导演： "+parsedData.get(3).getDirector());
            kind4.setText("类型： "+parsedData.get(3).getKind());
            name4.setText(parsedData.get(3).getMovieName());
            date4.setText(parsedData.get(3).getDate());
            point4.setText("主演： "+parsedData.get(3).getCast());
            cinema4.setText(parsedData.get(3).getCinema());
            who4.setText(parsedData.get(3).getRemark());

            director5.setText("导演： "+parsedData.get(4).getDirector());
            kind5.setText("类型： "+parsedData.get(4).getKind());
            name5.setText(parsedData.get(4).getMovieName());
            date5.setText(parsedData.get(4).getDate());
            point5.setText("主演： "+parsedData.get(4).getCast());
            cinema5.setText(parsedData.get(4).getCinema());
            who5.setText(parsedData.get(4).getRemark());

            director6.setText("导演： "+parsedData.get(5).getDirector());
            kind6.setText("类型： "+parsedData.get(5).getKind());
            name6.setText(parsedData.get(5).getMovieName());
            date6.setText(parsedData.get(5).getDate());
            point6.setText("主演： "+parsedData.get(5).getCast());
            cinema6.setText(parsedData.get(5).getCinema());
            who6.setText(parsedData.get(5).getRemark());

            director7.setText("导演： "+parsedData.get(6).getDirector());
            kind7.setText("类型： "+parsedData.get(6).getKind());
            name7.setText(parsedData.get(6).getMovieName());
            date7.setText(parsedData.get(6).getDate());
            point7.setText("主演： "+parsedData.get(6).getCast());
            cinema7.setText(parsedData.get(6).getCinema());
            who7.setText(parsedData.get(6).getRemark());

            super.onPostExecute(data);
        }
    }

    public class ShowPostTask extends AsyncTask<URL, Void, Bitmap[]> {

        @Override
        protected Bitmap[] doInBackground(URL... params) {
            Bitmap[] bitmaps = new Bitmap[7];
            try {
                for(int i=0; i<7; i++){
                    bitmaps[i] = getBitmap(params[i]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmaps;
        }

        @Override
        protected void onPostExecute(Bitmap[] bitmaps) {

            movie1.setImageBitmap(bitmaps[0]);
            movie2.setImageBitmap(bitmaps[1]);
            movie3.setImageBitmap(bitmaps[2]);
            movie4.setImageBitmap(bitmaps[3]);
            movie5.setImageBitmap(bitmaps[4]);
            movie6.setImageBitmap(bitmaps[5]);
            movie7.setImageBitmap(bitmaps[6]);

            super.onPostExecute(bitmaps);
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
    }


}
