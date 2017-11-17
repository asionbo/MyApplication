package com.example.asionbo.myapplication.utils;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by asionbo on 2017/10/9.
 */

public class ChartTimeFormatter implements IAxisValueFormatter {

    private BarLineChartBase<?> chart;

    public ChartTimeFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        DecimalFormat format = new DecimalFormat("###");
        return format.format(value)+"月";
    }

   private String getCurrentTime(){
       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
       Date date = new Date(System.currentTimeMillis());
       return format.format(date);
   }
}
