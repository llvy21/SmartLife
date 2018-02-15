package com.example.android.datafrominternet.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.android.datafrominternet.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ucla on 2018/2/2.
 */

public class AddRecord extends AppCompatActivity {

    private Spinner spinner;

    private EditText et_money,et_remark;

    private Button btn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();
        final String status = intent.getStringExtra("status");

        final Spinner spinner = (Spinner) findViewById(R.id.spinnner);
        final List<String> datas =new ArrayList<>
                (Arrays.asList("交通出行", "服饰美容", "生活日用", "通讯", "饮食", "其他"));

        MySpinnerAdapter adapter = new MySpinnerAdapter(this);
        spinner.setAdapter(adapter);
        adapter.setDatas(datas);

        if (status.equals("income")){
            spinner.setVisibility(View.GONE);
        }


        btn = (Button) findViewById(R.id.btn_Add);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et_money = (EditText) findViewById(R.id.et_Money);
                et_remark = (EditText) findViewById(R.id.et_Addremark);
                AccountData accountData = new AccountData();
                if (status.equals("outcome")) {
                    accountData.setSort(spinner.getSelectedItemPosition());
                    accountData.setSortString(spinner.getSelectedItem().toString());
                }
                else accountData.setSort(-1);

                accountData.setMoney(Float.parseFloat(et_money.getText().toString()));
                accountData.setRemark(et_remark.getText().toString());
                accountData.setDate();
                accountData.save();

                Intent intent = new Intent(AddRecord.this,AccountList.class);
                startActivity(intent);
                finish();

            }
        });



    }
}
