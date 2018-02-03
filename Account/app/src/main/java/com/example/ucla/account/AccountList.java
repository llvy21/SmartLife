package com.example.ucla.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

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

        List<AccountData> data = DataSupport.findAll(AccountData.class);
        AccountListAdapter adapter = new AccountListAdapter(
                AccountList.this, R.layout.item_accountlist, data);

        ListView listView = findViewById(R.id.lv_account);
        listView.setAdapter(adapter);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
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
                return true;

            case R.id.action_outcome:
                Intent intent1 = new Intent(AccountList.this, AddRecord.class);
                intent1.putExtra("status", "outcome");
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
