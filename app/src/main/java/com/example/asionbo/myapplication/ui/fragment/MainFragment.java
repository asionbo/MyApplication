package com.example.asionbo.myapplication.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.asionbo.myapplication.R;
import com.example.asionbo.myapplication.ui.ChartActivity;
import com.example.asionbo.myapplication.ui.MultiSelectActivity;
import com.example.asionbo.myapplication.ui.kotlin.KtTest_Java;
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
        Button mBtnToKt = (Button) view.findViewById(R.id.btn_toKotlin);
        Button toMulti = (Button) view.findViewById(R.id.btn_toMulti);
        Button showProgress = (Button) view.findViewById(R.id.btn_showProgress);
        Button shake = (Button) view.findViewById(R.id.btn_shake);

        mBtn.setOnClickListener(l ->{
            clickToFragment();
        });
        mBtnToKt.setOnClickListener(l ->{
            clickToKt();
        });

        toMulti.setOnClickListener(l->{
            clickMulti();
        });

        shake.setOnClickListener(l->{
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
            shake.startAnimation(animation);
        });

        showProgress.setOnClickListener(l->{
            showDownloadDialog(false);
        });
        return view;
    }

    private void clickMulti() {
        Intent intent = new Intent(getActivity(), MultiSelectActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    void clickToFragment(){
        Intent intent = new Intent(getActivity(), ChartActivity.class);
        startActivity(intent);
    }
    void clickToKt(){
        Intent intent = new Intent(getActivity(), KtTest_Java.class);
        startActivity(intent);
    }

    void showDownloadDialog(boolean isIndeterminate){
        if (isIndeterminate){
            new MaterialDialog.Builder(getContext())
                    .title(R.string.progress_dialog_title)
                    .content(R.string.please_wait)
                    .progress(true, 0)
                    .show();
        }else{
            new MaterialDialog.Builder(getContext())
                    .title(R.string.progress_dialog_title)
                    .content(R.string.please_wait)
                    .progress(false, 150,true)
                    .showListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            MaterialDialog dialog = (MaterialDialog) dialogInterface;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (dialog.getCurrentProgress() != dialog.getMaxProgress()){
                                        if (dialog.isCancelled()){
                                            break;
                                        }
                                        try {
                                            Thread.sleep(50);
                                        } catch (InterruptedException e) {
                                            break;
                                        }
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.incrementProgress(1);
                                            }
                                        });
                                    }
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.setContent(R.string.done);
                                            dialog.setActionButton(DialogAction.NEGATIVE,android.R.string.ok);
                                        }
                                    });
                                }
                            }).start();
                        }
                    }).show();
        }
    }
}
