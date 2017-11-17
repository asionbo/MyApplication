package com.example.asionbo.myapplication;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asionbo.myapplication.event.Event;
import com.example.asionbo.myapplication.ui.ChartActivity;
import com.example.asionbo.myapplication.ui.TraceActivity;
import com.example.asionbo.myapplication.ui.fragment.MainFragment;
import com.example.asionbo.myapplication.utils.Constant;
import com.example.asionbo.myapplication.utils.LogUtils;
import com.example.asionbo.myapplication.utils.RxBus;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends BaseActivity {

    private Fragment fragment;
    @Override
    protected void init() {

    }

    @Override
    protected Fragment getContentFragment() {
        fragment = new MainFragment();
        return fragment;
    }

    //键盘的录入结果
    @Override
    public void show(String str) {
        TextView tvShow = (TextView) fragment.getView().findViewById(R.id.tv_show);
        tvShow.setText(str);
    }
}
