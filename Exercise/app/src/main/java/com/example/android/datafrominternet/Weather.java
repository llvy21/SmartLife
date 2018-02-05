package com.example.android.datafrominternet;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.android.datafrominternet.utilities.NetworkUtil_Weather;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class Weather extends AppCompatActivity {

    private EditText mSearchBoxEditText;

    private TextView textView;

    private TextView mSearchResultsTextView;

    private ImageView mWeatherIcon,mBackGround;

    private SearchView searchView;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_weather);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        textView = (TextView) findViewById(R.id.textView);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_weather_search_results_json);
        mWeatherIcon = (ImageView) findViewById(R.id.iv_icon_weather);
        mBackGround = (ImageView) findViewById(R.id.iv_background_weather);

        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        Glide.with(this).load("http://api.dujin.org/bing/1366.php").centerCrop().into(mBackGround);

    }

    private void makeWeatherSeachQuery(){
        String cityQuery = mSearchBoxEditText.getText().toString();
        URL weatherSearchUrl = NetworkUtil_Weather.buildUrl(cityQuery);
        new WeatherQueryTask().execute(weatherSearchUrl);
        new ShowIconTask().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        searchView = (SearchView) findViewById(R.id.action_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            makeWeatherSeachQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class WeatherQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String weatherSearchResults = null;
            try {
                weatherSearchResults = NetworkUtil_Weather.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return weatherSearchResults;
        }

        @Override
        protected void onPostExecute(String data) {
            HashMap<String,String> weatherdata = new HashMap<>();
            if(data != null && !data.equals(""))
                try {
                    weatherdata = ParseJSON_Weather.parseJson(data);
                    if (weatherdata.get("status").equals("unknown city")){
                        Toast.makeText(Weather.this,"请重新输入城市",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putString("now",weatherdata.get("cond_code"));
                    editor.apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            mSearchResultsTextView.setText(weatherdata.toString());
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
                Log.d("test"," "+cond_code);
                bitmap = getBitmap(cond_code);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mWeatherIcon.setImageBitmap(bitmap);
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



}