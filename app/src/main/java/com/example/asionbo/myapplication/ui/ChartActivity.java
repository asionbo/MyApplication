package com.example.asionbo.myapplication.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.asionbo.myapplication.R;
import com.example.asionbo.myapplication.utils.ChartTimeFormatter;
import com.example.asionbo.myapplication.utils.LogUtils;
import com.example.asionbo.myapplication.view.MyMarkerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asionbo on 2017/10/9.
 */

public class ChartActivity extends AppCompatActivity {

    @BindView(R.id.bc_content) BarChart mChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ButterKnife.bind(this);
//        setSupportActionBar(mToolbar);
//        ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("chart-Test");
        initView();
    }


    private void initView() {
        mChart.setDrawBarShadow(false);
        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        ChartTimeFormatter indexF = new ChartTimeFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // Set a minimum interval for the axis when zooming in. The axis is not allowed to go below
//     that limit. This can be used to avoid label duplicating when zooming in.
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(indexF);

        IAxisValueFormatter iAxisF = (value, axis) -> value+"";

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setLabelCount(8,false);
        yAxis.setValueFormatter(iAxisF);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setSpaceTop(15f);
        yAxis.setAxisMinimum(0f);

        YAxis axisRight = mChart.getAxisRight();
        axisRight.setEnabled(false);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        MyMarkerView mv = new MyMarkerView(this,indexF);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        setData(11,50);
        mChart.animateY(2000);
    }

    private void setData(int count, float range) {

        float start = 1f;

        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        for (int i = (int) start; i < start + count + 1; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);

            if (Math.random() * 100 < 25) {
                yVals1.add(new BarEntry(i, val, getResources().getDrawable(R.drawable.ic_lock_outline_white_24dp)));
            } else {
                yVals1.add(new BarEntry(i, val));
            }
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "2017年销售额（¥）");

            set1.setDrawIcons(false);

            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);
            data.setValueTextColor(0xff000000);

            mChart.setData(data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.save:
                checkLimit();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    //检查权限
    private void checkLimit() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){//大于6.0申请权限
            int i = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (PackageManager.PERMISSION_DENIED == i){
                getLimit();
            }else if (PackageManager.PERMISSION_GRANTED == i){
                //do something
                if (mChart.saveToGallery("title" + System.currentTimeMillis(), 80)){
                    Snackbar.make(mChart,"保存成功",Snackbar.LENGTH_LONG).show();
                }else{
                    Snackbar.make(mChart,"保存失败，请检查权限",Snackbar.LENGTH_LONG).show();
                }
            }
        }else{
            if (mChart.saveToGallery("title" + System.currentTimeMillis(), 80)){
                Snackbar.make(mChart,"保存成功",Snackbar.LENGTH_LONG).show();
            }else{
                Snackbar.make(mChart,"保存失败，请检查权限",Snackbar.LENGTH_LONG).show();
            }
        }
    }

    //请求权限
    private void getLimit() {
        // Should we show an explanation?
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            LogUtils.e("we shouldShowRequestPermissionRationale");
            new MaterialDialog.Builder(this)
                    .title("提示")
                    .content("我需要保存到存储的权限，这样才能保存图片，按下确定，来获取吧")
                    .positiveText(android.R.string.yes)
                    .onPositive((dialog,which)->{
                        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},111);
                    }).show();
        }else{
            LogUtils.e("shouldShowRequestPermissionRationale == false");
            new MaterialDialog.Builder(this)
                    .title("提示")
                    .content("由于没有权限，我不能保存图片了，如果想保存，去设置开启吧")
                    .negativeText(android.R.string.cancel)
                    .positiveText("皮皮虾，我们走")
                    .onPositive((dialog,which)->{
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package",getPackageName(),null));
                        startActivity(intent);

                    }).show();
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},111);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 111:
                // If request is cancelled, the result arrays are empty.
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (mChart.saveToGallery("title" + System.currentTimeMillis(), 80)){
                        Snackbar.make(mChart,"保存成功",Snackbar.LENGTH_LONG).show();
                    }else{
                        Snackbar.make(mChart,"保存失败，请检查权限",Snackbar.LENGTH_LONG).show();
                    }
                }
                return;
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
