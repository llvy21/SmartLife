package com.example.android.datafrominternet.Account;

import android.content.Context;
import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ucla on 2018/2/14.
 */

public class XFormattedValue implements IAxisValueFormatter {

    private String[] mValues;

    public XFormattedValue(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        return mValues[(int) value];
    }

}
