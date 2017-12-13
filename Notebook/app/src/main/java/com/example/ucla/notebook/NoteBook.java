package com.example.ucla.notebook;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;


public class NoteBook extends AppCompatActivity {

    private MyAdapter mAdapter;
    private RecyclerView mrv;
    private List<User> userList = new ArrayList<User>() {};
    public ArrayList<String> title = new ArrayList<String>(){};
    public ArrayList<String> content = new ArrayList<String>(){};
    private Bundle bundle = null;
    private String s = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notebook);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        mAdapter = new MyAdapter(userList);


        init(getIntent().getBundleExtra("userInfo"));

        mrv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mrv.setLayoutManager(layoutManager);

        mrv.setHasFixedSize(false);


        mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(NoteBook.this, "Long click to edit " ,Toast.LENGTH_SHORT).show();
            }
        });
        mAdapter.setOnItemLongClickListener(new MyAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Intent intent = new Intent(NoteBook.this, text.class);
                intent.putExtra("position", position);
                intent.putExtra("title",title.get(position));
                Log.d("test","hey"+position+title.get(position));
                intent.putExtra("content",content.get(position));
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
                intent.putExtra("position", userList.size());
                startActivityForResult(intent, 1);
                title.add(" ");
                content.add(" ");
                User initi = new User(" "," ");
                userList.add(userList.size(),initi );
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void init(Bundle bundle) {
        Log.d("test",title.size()+"ha"+userList.size());
        for (int i=1;i<=userList.size();i++) {
            if (bundle!=null)
            {

                int pos = (int) bundle.get("position");
                title.set(pos, (String) bundle.get("title"));
                content.set(pos, (String) bundle.get("content"));
                User x = new User(title.get(pos), content.get(pos));
                userList.set(pos, x);

            }
//            else
//            {
//                Log.d("test","getintoelse");
//                User a = new User(title.get(i), content.get(i));
//                userList.add(a);
//            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    bundle = data.getBundleExtra("userInfo");
                }
                init(bundle);
                mAdapter.notifyDataSetChanged();
                break;
            default:
        }

    }



}
