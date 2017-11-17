package com.example.asionbo.myapplication.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.asionbo.myapplication.BaseActivity;
import com.example.asionbo.myapplication.R;
import com.example.asionbo.myapplication.model.Trace;
import com.example.asionbo.myapplication.ui.adapter.TraceAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by asionbo on 2017/10/8.
 */

public class TraceActivity extends AppCompatActivity{

    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;

    private TraceAdapter mAdapter;
    private List<Trace> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace);

        initData();
        initView();
    }

    private void initData() {
        for (int i=1;i<6;i++){
            Trace trace = new Trace();
            trace.setTime("2017-10-8 22:22:22");
            trace.setContent("给我"+i+"首歌的时间");
            list.add(trace);
        }
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new TraceAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(list);
    }
}
