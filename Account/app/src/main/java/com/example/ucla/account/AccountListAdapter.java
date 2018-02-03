package com.example.ucla.account;

import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ucla on 2018/2/2.
 */

public class AccountListAdapter extends ArrayAdapter<AccountData>{

    private int resID;

    public AccountListAdapter(@NonNull Context context, int resource, @NonNull List<AccountData> objects) {
        super(context, resource, objects);
        resID = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AccountData accountData = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resID, parent, false);
        TextView money = view.findViewById(R.id.tv_money);
        TextView date = view.findViewById(R.id.tv_date);
        TextView remark = view.findViewById(R.id.tv_remark);
        TextView sort = view.findViewById(R.id.tv_sort);

        money.setText(String.valueOf(accountData.getMoney()));
        if (accountData.getSort()==-1){

            money.setTextColor(Color.rgb(255,0,0));
        }
        date.setText(accountData.datetoString());
        remark.setText(accountData.getRemark());
        sort.setText(accountData.getSortString());

        return view;
    }
}