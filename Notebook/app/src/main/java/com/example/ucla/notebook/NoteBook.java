package com.example.ucla.notebook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class NoteBook extends AppCompatActivity {

    private MyAdapter mAdapter;
    private RecyclerView mrv;
    List<Note> tofind = DataSupport.findAll(Note.class);
    private ArrayList<Note> noteList = (ArrayList<Note>) tofind;
    private Bundle bundle = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notebook);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        mAdapter = new MyAdapter(noteList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mrv = (RecyclerView) findViewById(R.id.rv);

        mrv.setLayoutManager(layoutManager);
        mrv.setHasFixedSize(false);

        mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
                public void onItemClick(View view, int position) {
                Toast.makeText(NoteBook.this, "Long click to edit" ,Toast.LENGTH_SHORT).show();
                }
        });
        mAdapter.setOnItemLongClickListener(new MyAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Intent intent = new Intent(NoteBook.this, text.class);
                intent.putExtra("position", (Integer)position);
                intent.putExtra("title",noteList.get(position).getTitle());
                intent.putExtra("content",noteList.get(position).getContent());
                startActivityForResult(intent, 1);
            }

        });
        mrv.setAdapter(mAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(NoteBook.this, text.class);
                intent.putExtra("position", noteList.size());
                startActivityForResult(intent, 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                List<Note> tofind = DataSupport.findAll(Note.class);
                noteList.clear();
                noteList.addAll(tofind);
                mAdapter.notifyDataSetChanged();
            default:
        }

    }
}
