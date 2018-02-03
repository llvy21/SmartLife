package com.example.ucla.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ucla on 2018/2/2.
 */

public class MySpinnerAdapter extends BaseAdapter {

    List<String> datas =new ArrayList<>(Arrays.asList("交通出行", "服饰美容", "生活日用", "通讯", "饮食", "其他"));

    Context mContext;

    public void setDatas(List<String> datas) {
        this.datas = datas;
    }

    public MySpinnerAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return datas==null?0:datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler hodler = null;
        if (convertView == null) {
            hodler = new ViewHodler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_spinner, null);
            hodler.mTextView = (TextView) convertView;
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag();
        }

        hodler.mTextView.setText(datas.get(position));

        return convertView;
    }

    private static class ViewHodler{
        TextView mTextView;
    }
}
