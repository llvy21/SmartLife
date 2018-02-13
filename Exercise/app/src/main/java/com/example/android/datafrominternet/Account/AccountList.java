package com.example.android.datafrominternet.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;


import com.example.android.datafrominternet.R;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by ucla on 2018/2/2.
 */

public class AccountList extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountlist);


        Intent intent = getIntent();
        int sort = intent.getIntExtra("sort",7);
        List<AccountData> data;

        if(sort==7){
            data = DataSupport.findAll(AccountData.class);
        }
        else {
            int cnt = 0;
            do{
                data = DataSupport.where
                        ("sort = ?",String.valueOf(cnt)).find(AccountData.class);
                cnt++;
                if (data.isEmpty()){
                    continue;
                }
                sort--;
            }while(sort!=-1);
        }
        AccountListAdapter adapter = new AccountListAdapter(
                AccountList.this, R.layout.item_accountlist, data);

        ListView listView = (ListView) findViewById(R.id.lv_account);
        listView.setAdapter(adapter);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_account);
        setSupportActionBar(myToolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_toolbar_account, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_income:
                Intent intent = new Intent(AccountList.this, AddRecord.class);
                intent.putExtra("status", "income");
                startActivity(intent);
                finish();
                return true;

            case R.id.action_outcome:
                Intent intent1 = new Intent(AccountList.this, AddRecord.class);
                intent1.putExtra("status", "outcome");
                startActivity(intent1);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
