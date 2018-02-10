package com.example.android.datafrominternet.Notebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.datafrominternet.R;

import org.litepal.crud.DataSupport;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by ucla on 2017/11/7.
 */

public class AddNote extends AppCompatActivity {

    private EditText title,content;
    private Button btn,dlbtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);
        title = (EditText) findViewById(R.id.title);
        btn = (Button) findViewById(R.id.save);
        dlbtn = (Button) findViewById(R.id.delete);
        content = (EditText) findViewById(R.id.content);

        Bmob.initialize(this, "c12ad56b69a6e30cb8cc89b566379d19");

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

            if(pos==tofind.size())
            {
                final Note add_litepal = new Note();
                add_litepal.setTitle(title.getText().toString());
                add_litepal.setContent(content.getText().toString());
                NoteData add_bmob = new NoteData();
                add_bmob.setTitle(title.getText().toString());
                add_bmob.setContent(content.getText().toString());
                add_bmob.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if(e==null){
                            add_litepal.setObjectId(objectId);
                        } else Toast.makeText(AddNote.this,"同步添加数据失败",Toast.LENGTH_SHORT).show();
                    }
                });
                add_litepal.save();

            }

            else
            {
                Note update = new Note();
                update.setTitle(title.getText().toString());
                update.setContent(content.getText().toString());
                update.updateAll("title = ? and content = ?",titl,con);
                List<Note> update_find;
                update_find = DataSupport.where("title = ? and content = ?",titl,con).find(Note.class);
                for(int i=0; i<update_find.size(); i++) {
                    NoteData update_bmob = new NoteData();
                    update_bmob.setTitle(title.getText().toString());
                    update_bmob.setContent(content.getText().toString());
                    if (update_find.get(i).getObjectId().equals(null)){
                        update_bmob.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                Toast.makeText(AddNote.this,"同步修改数据失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        update_bmob.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (!(e.equals(null))){
                                    Toast.makeText(AddNote.this,"同步修改数据失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }
            }

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();

            }
        });

        dlbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            List<Note> tofind = DataSupport.findAll(Note.class);
            if(pos!=tofind.size()) {
                tofind.get(pos).delete();

                List<Note> delete_list;
                delete_list = DataSupport.where("title = ? and content = ?", titl, con).find(Note.class);
                for (int i = 0; i < delete_list.size(); i++) {
                    if (!delete_list.get(i).getObjectId().equals(null)) {
                        NoteData delete = new NoteData();
                        delete.setObjectId(delete_list.get(i).getObjectId());
                        delete.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (!(e.equals(null))) {
                                    Toast.makeText(AddNote.this, "同步修改数据失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();

            }
        });

    }
}
