package com.example.android.datafrominternet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.android.datafrominternet.Account.Chart;
import com.example.android.datafrominternet.Calendar.Calendar;
import com.example.android.datafrominternet.HaveFun.MainInterface;
import com.example.android.datafrominternet.LBS.Map;
import com.example.android.datafrominternet.Movie.Movie;
import com.example.android.datafrominternet.Notebook.NoteBook;
import com.example.android.datafrominternet.Weather.Weather;

/**
 * Created by ucla on 2018/2/11.
 */

public class MainActivity extends AppCompatActivity {

    //  Button btn1,btn2,btn3,btn4,btn5,btn6;
    ImageView weather,notebook,calender,accountbook,map,movie,title,emojify;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weather = (ImageView) findViewById(R.id.weather_main);
        notebook = (ImageView) findViewById(R.id.notebook_main);
        map = (ImageView) findViewById(R.id.map_main);
        calender = (ImageView) findViewById(R.id.calender_main);
        accountbook = (ImageView) findViewById(R.id.accountbook_main);
        movie = (ImageView) findViewById(R.id.movie_main);
        title = (ImageView) findViewById(R.id.title_main);
        emojify = (ImageView) findViewById(R.id.emojify_main);
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Weather.class);
                startActivity(intent);
            }
        });
        movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Movie.class);
                startActivity(intent);
            }
        });
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Calendar.class);
                startActivity(intent);
            }
        });
        notebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NoteBook.class);
                startActivity(intent);
            }
        });
        accountbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Chart.class);
                startActivity(intent);
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Map.class);
                startActivity(intent);
            }
        });
        emojify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainInterface.class);
                startActivity(intent);
            }
        });

    }
}
