package com.example.android.datafrominternet;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.datafrominternet.utilities.NetworkUtils;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchBoxEditText;

    private TextView mUrlDisplayTextView;

    private TextView mSearchResultsTextView;

    private ImageView mWeatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_weather_search_results_json);
        mWeatherIcon = (ImageView) findViewById(R.id.imageView);

    }

    private void makeWeatherSeachQuery(){
        String cityQuery = mSearchBoxEditText.getText().toString();
        URL weatherSearchUrl = NetworkUtils.buildUrl(cityQuery);
        mUrlDisplayTextView.setText(weatherSearchUrl.toString());
        new WeatherQueryTask().execute(weatherSearchUrl);
        new ShowIconTask().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
                weatherSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
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
                    weatherdata = ParseJSON.parseJson(data);
                    if (weatherdata.get("status").equals("unknown city")){
                        Toast.makeText(MainActivity.this,"请重新输入城市",Toast.LENGTH_SHORT).show();
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