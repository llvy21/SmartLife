package com.example.ucla.notebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ucla on 2017/11/7.
 */

public class text extends Activity {

    private EditText title,content;
    private Button btn;
    private ArrayList<Note> noteList = new ArrayList<Note>() {};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text1);
        title = (EditText) findViewById(R.id.title);
        btn = (Button) findViewById(R.id.save);
        content = (EditText) findViewById(R.id.content);

        Intent intent = getIntent();
        final String titl = intent.getStringExtra("title");
        final String con = intent.getStringExtra("content");
        final int pos = intent.getIntExtra("position",-1);


        title.setText(titl);
        content.setText(con);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            List<Note> tofind = DataSupport.findAll(Note.class);
            noteList = (ArrayList)tofind;
            if(pos==noteList.size()){

                Note add = new Note();
                add.setTitle(title.getText().toString());
                add.setContent(content.getText().toString());
                boolean yor = add.save();
            }

            else{
                Note update = new Note();
                update.setTitle(title.getText().toString());
                update.setContent(content.getText().toString());
                update.update(pos+1);
            }

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();

            }
        });

    }
}
