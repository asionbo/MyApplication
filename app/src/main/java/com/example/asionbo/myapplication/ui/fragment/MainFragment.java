package com.example.asionbo.myapplication.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.asionbo.myapplication.R;
import com.example.asionbo.myapplication.ui.ChartActivity;
import com.example.asionbo.myapplication.utils.LogUtils;


/**
 * Created by asionbo on 2017/10/13.
 */

public class MainFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        Button mBtn = (Button) view.findViewById(R.id.btn_toFragment);

        mBtn.setOnClickListener(l ->{
            clickToFragment();
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    void clickToFragment(){
        LogUtils.e("to chart");
        Intent intent = new Intent(getActivity(), ChartActivity.class);
        startActivity(intent);
    }
}
