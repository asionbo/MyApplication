package com.example.asionbo.myapplication.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.asionbo.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by asionbo on 2018/1/16.
 */

public class LottieAniActivity extends AppCompatActivity {

    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_showAnim) void clickShow(){
        if (animationView.isAnimating()){
            animationView.pauseAnimation();
        }else{
            animationView.playAnimation();
        }

    }
}
