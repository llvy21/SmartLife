package com.example.android.datafrominternet.Weather;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.android.datafrominternet.R;
import com.example.android.datafrominternet.utilities.NetWorkUtil_Background;
import com.example.android.datafrominternet.utilities.NetworkUtil_Weather;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class Weather extends AppCompatActivity {

    private TextView textView,now_temp,date1,date2,temp_future1,temp_future2,temp_future3;

    private TextView air_quanlity,tv_location,dirc;

    private ImageView mWeatherIcon,mBackGround,weather1,weather2;
    private int weather_code_int;
    private String weather_code_str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_weather);

        textView = (TextView) findViewById(R.id.testView);
        now_temp = (TextView) findViewById(R.id.tv_temperature);
        air_quanlity = (TextView) findViewById(R.id.air_quanlity);

        mWeatherIcon = (ImageView) findViewById(R.id.iv_icon_weather);
        mBackGround = (ImageView) findViewById(R.id.iv_background_weather);
        weather1 = (ImageView) findViewById(R.id.weather1);
        weather2 = (ImageView) findViewById(R.id.weather2);

        tv_location = (TextView) findViewById(R.id.tv_location_weather);
        temp_future1 = (TextView) findViewById(R.id.temp_future1);
        temp_future2 = (TextView) findViewById(R.id.temp_future2);
        dirc = (TextView) findViewById(R.id.dirc);
        date1 = (TextView) findViewById(R.id.date1);
        date2 = (TextView) findViewById(R.id.date2);



        android.support.v7.widget.Toolbar mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
        String location = preferences.getString("location","default");
        if (location.equals("default")){
            tv_location.setText("哈尔滨");
            makeWeatherSeachQuery("haerbin");
        }else {
            tv_location.setText(location);
            makeWeatherSeachQuery(location);
        }
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        makeBackgroundSeachQuery();
    }

    private void makeWeatherSeachQuery(String query){

        String cityQuery = query;
        URL weatherSearchUrl = NetworkUtil_Weather.buildUrl(cityQuery,0);
        URL airSearchUrl = NetworkUtil_Weather.buildUrl(cityQuery,1);
        new WeatherQueryTask().execute(weatherSearchUrl,airSearchUrl);
        new ShowIconTask().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_weather, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSubmitButtonEnabled(true);

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText editText = (EditText) searchView.findViewById(id);
        editText.setTextColor(getResources().getColor(R.color.colorWhite));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                makeWeatherSeachQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tv_location.setVisibility(View.GONE);

                return true;
            }

        });



        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                tv_location.setVisibility(View.VISIBLE);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public class WeatherQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... params) {
            String searchResults = null;
            try {
                searchResults = NetworkUtil_Weather.getResponseFromHttpUrl(params[0])
                        +"|"+NetworkUtil_Weather.getResponseFromHttpUrl(params[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String data) {
            HashMap<String,String> weatherdata = new HashMap<>();
            HashMap<String,String> airdata = new HashMap<>();
            if(data != null && !data.equals(""))
                try {
                    String[] searchResults=data.split("\\|");
                    Log.d("test",data+"0"+searchResults[0]);
                    weatherdata = ParseJSON_Weather.parseJson(searchResults[0]);
                    if (weatherdata.get("status").equals("unknown city")){
                        Toast.makeText(Weather.this,"请重新输入城市",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    airdata = ParseJSON_Air.parseJson(searchResults[1]);
                    SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putString("now",weatherdata.get("cond_code"));
                    editor.putString("location",weatherdata.get("location"));
                    editor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            air_quanlity.setText("        空气"+airdata.get("quality").toString()+"    PM10:"+airdata.get("aqi").toString()+"\n"+" "+"\n"+"    未来天气：");
            tv_location.setText(weatherdata.get("location"));
            tv_location.setVisibility(View.VISIBLE);
            now_temp.setText("     "+weatherdata.get("tmp")+"℃"+"    "+weatherdata.get("cond_txt"));
            textView.setText("            今日温度："+weatherdata.get("tmp_min"+0).toString()+"℃---"+weatherdata.get("tmp_max"+0).toString()+"℃"+"\n"+" ");
            date1.setText(weatherdata.get("date"+1).toString()) ;
            temp_future1.setText(weatherdata.get("tmp_min"+1).toString()+"℃---"+weatherdata.get("tmp_max"+1).toString()+"℃  "+weatherdata.get("cond_txt"));
            date2.setText(weatherdata.get("date"+2).toString()) ;
            temp_future2.setText(weatherdata.get("tmp_min"+2).toString()+"℃---"+weatherdata.get("tmp_max"+2).toString()+"℃  "+weatherdata.get("cond_txt"));

            dirc.setText("\n"+"风向："+weatherdata.get("wind_dir").toString()+"  风级："+weatherdata.get("wind_sc").toString()+"\n"+"\n"+weatherdata.get("txt"+0).toString());
            /* if(Integer.parseInt(weatherdata.get("cond_code"))==100)
             {
                 weather1.setBackground(R.id.sunny);
             }*/
            weather1.getDrawable().setLevel(Integer.parseInt(weatherdata.get("cond_code_d"+1)));
            weather2.getDrawable().setLevel(Integer.parseInt(weatherdata.get("cond_code_d"+2)));
            super.onPostExecute(data);
        }
    }

    public class ShowIconTask extends AsyncTask<URL, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(URL... params) {
            Bitmap bitmap = null;
            try {
                SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
                String cond_code = preferences.getString("now"," ");
                bitmap = getBitmap(cond_code);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // mWeatherIcon.setImageBitmap(bitmap);
            // weather1.setImageBitmap(bitmap);
            // weather2.setImageBitmap(bitmap);
            //  weather3.setImageBitmap(bitmap);
            super.onPostExecute(bitmap);
        }

    }

    private static Bitmap getBitmap(String cond_code) throws IOException {

        URL url = new URL("https://cdn.heweather.com/cond_icon/"+cond_code+".png");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }

    private void makeBackgroundSeachQuery(){
        URL backGroundSearchUrl = NetWorkUtil_Background.buildUrl();
        new BackGroundQueryTask().execute(backGroundSearchUrl);
        new ShowIconTask().execute();

    }

    public class BackGroundQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... params) {
            String searchResults = null;
            try {
                searchResults = NetWorkUtil_Background.getResponseFromHttpUrl(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String json) {
            String url = null;
            if(json != null && !json.equals(""))
                try {
                    JSONObject urlSearchResult = new JSONObject(json);
                    JSONObject temp = (JSONObject) urlSearchResult.getJSONArray("images").get(0);
                    url = temp.getString("urlbase");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            Glide.with(Weather.this).load("http://s.cn.bing.net"+url+"_1024x768.jpg").centerCrop().into(mBackGround);

            super.onPostExecute(json);
        }
    }



}