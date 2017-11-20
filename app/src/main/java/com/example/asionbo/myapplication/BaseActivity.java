package com.example.asionbo.myapplication;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import com.example.asionbo.myapplication.ui.fragment.MyKeyBoardFragment;

import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

/**
 * 一个提供键盘的基类，并提供一个结果
 * Created by asionbo on 2017/10/8.
 */

public abstract class BaseActivity extends AppCompatActivity implements MyKeyBoardFragment.FragmentText{

    private FrameLayout flMain;
    protected Disposable mDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initMainView(getContentFragment());
        init();
        showHWBoard();
    }

    private void showHWBoard(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_keyBoard, new MyKeyBoardFragment(), "key_board")
                .commit();
    }

    private void initMainView(Fragment fragment) {
        flMain = (FrameLayout) findViewById(R.id.fl_main);//主页面布局
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_main,fragment ,"main")
                .commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDisposable != null && !mDisposable.isDisposed()) mDisposable.dispose();
        mDisposable = null;
    }

    protected abstract void init();
    protected abstract Fragment getContentFragment();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }
}
