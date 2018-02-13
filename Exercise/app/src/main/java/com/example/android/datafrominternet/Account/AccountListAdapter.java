package com.example.android.datafrominternet.Account;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.datafrominternet.R;

import java.util.List;

/**
 * Created by ucla on 2018/2/2.
 */

public class AccountListAdapter extends ArrayAdapter<AccountData> {

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
        TextView money = (TextView) view.findViewById(R.id.tv_money);
        TextView date = (TextView) view.findViewById(R.id.tv_date);
        TextView remark = (TextView) view.findViewById(R.id.tv_remark);
        TextView sort = (TextView) view.findViewById(R.id.tv_sort);

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