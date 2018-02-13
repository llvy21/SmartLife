package com.example.android.datafrominternet.Account;

import org.litepal.crud.DataSupport;

import java.util.Calendar;

/**
 * Created by ucla on 2018/2/2.
 */

public class AccountData extends DataSupport {

    private String sortString;
    private int sort;
    private float money;
    private String remark;
    private int year;
    private int month;
    private int day;


    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public void setDate(){
        Calendar calendar = Calendar.getInstance();
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public String datetoString(){
        return year+"-"+month+"-"+day;
    }

    public String getSortString() {
        return sortString;
    }

    public void setSortString(String sortString) {
        this.sortString = sortString;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getSort() {
        return sort;
    }
}
