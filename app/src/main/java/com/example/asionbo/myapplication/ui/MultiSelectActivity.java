package com.example.asionbo.myapplication.ui;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asionbo.myapplication.R;
import com.example.asionbo.myapplication.event.Event;
import com.example.asionbo.myapplication.ui.adapter.MultiRecyclerAdapter;
import com.example.asionbo.myapplication.utils.Constant;
import com.example.asionbo.myapplication.utils.LogUtils;
import com.example.asionbo.myapplication.utils.RxBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by asionbo on 2017/12/6.
 */

public class MultiSelectActivity extends AppCompatActivity  {

    @BindView(R.id.toolBar)
    Toolbar mToolbar;
    @BindView(R.id.dataList)
    RecyclerView mRecyclerView;

    private MultiRecyclerAdapter mAdapter;
    private List<String> list;
    protected Disposable mDisposable;
    private ActionMode actionMode;
    private boolean currentSelectAll = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_select);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("chart-Test");
        initData();
        initView();
    }

    private void initData() {
        File directory = Environment.getExternalStorageDirectory();
        File[] files = directory.listFiles();
        list = new ArrayList<>();
        if (files == null){
            list.add("null data");
        }else{
            for (File file : files) {
                list.add(file.getName());
            }
        }


    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MultiRecyclerAdapter(this);
        mAdapter.setData(list);
        mRecyclerView.setAdapter(mAdapter);
    }

    TextView text;
    @Override
    protected void onResume() {
        super.onResume();
        if(mDisposable == null || mDisposable.isDisposed())
            mDisposable = RxBus.INSTANCE.toObserverable(Event.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userEvent -> {
                        switch (userEvent.getType()) {
                            case 1:
                                String s = (String) userEvent.getPost();
                                Toast.makeText(this,"跳转:"+s,Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                String ss = (String) userEvent.getPost();
                                Toast.makeText(this,"Long_click:"+ss,Toast.LENGTH_SHORT).show();
                                if (actionMode == null){
                                    mToolbar.startActionMode(new android.view.ActionMode.Callback() {

                                        @Override
                                        public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                                            MenuInflater menuInflater = mode.getMenuInflater();
                                            menuInflater.inflate(R.menu.menu_select_ok,menu);
                                            View inflate = LayoutInflater.from(MultiSelectActivity.this).inflate(R.layout.action_mode_view, null);
                                            mode.setCustomView(inflate);
                                            text = (TextView) inflate.findViewById(R.id.tv_title);
                                            text.setText("MultiSelect");
                                            mAdapter.setMulti(true);
                                            return true;
                                        }

                                        @Override
                                        public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                                            switch (item.getItemId()){
                                                case R.id.select:
                                                    if (currentSelectAll){
                                                        mAdapter.selectAll();
                                                        text.setText("已选中"+mAdapter.getItemCount()+"个");
                                                        currentSelectAll = false;
                                                    }else{
                                                        mAdapter.unSelectAll();
                                                        text.setText("已选中0个");
                                                        currentSelectAll = true;
                                                    }
                                                    break;
                                                case R.id.ok:
                                                    Toast.makeText(MultiSelectActivity.this,"选中"+mAdapter.getSelectItems().size()+"个",Toast.LENGTH_SHORT).show();
                                                    break;
                                            }
                                            return false;
                                        }

                                        @Override
                                        public void onDestroyActionMode(android.view.ActionMode mode) {
                                            actionMode = null;
                                            mAdapter.setMulti(false);
                                        }
                                    });
                                }
                                break;
                            case 3:
                                int t = (int) userEvent.getPost();
                                if (text != null){
                                    text.setText("已选中"+t+"个");
                                }
                                break;
                        }
                    }, throwable -> {
                        LogUtils.e(throwable.getMessage());
                    });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDisposable != null && !mDisposable.isDisposed()) mDisposable.dispose();
        mDisposable = null;
    }

}
