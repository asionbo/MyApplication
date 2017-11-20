package com.example.asionbo.myapplication.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.example.asionbo.myapplication.R;
import com.example.asionbo.myapplication.view.VirtualKeyboardView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

/**
 * 虚拟键盘碎片
 * <p>
 * Created by asionbo on 2017/10/13.
 */


public class MyKeyBoardFragment extends BaseFragment {

    private Context context;
    private VirtualKeyboardView viewKey;
    private Animation enterAnim;
    private Animation exitAnim;
    private GridView gridView;
    private ArrayList<Map<String, String>> valueList;

    private FragmentText listener;

    public MyKeyBoardFragment(){
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = getActivity();
        if (activity instanceof FragmentText){
            listener = (FragmentText) activity;
        }else{
            throw new IllegalArgumentException("activity must implements FragmentText");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(context).inflate(R.layout.fragment_keyboard, container, false);
        editView = (EditText) view.findViewById(R.id.edit_query);
        viewKey = (VirtualKeyboardView) view.findViewById(R.id.virtual_keyboard);
        valueList = viewKey.getValueList();
        initAnim();
        initView();
        return view;
    }

    private void initAnim() {

        enterAnim = AnimationUtils.loadAnimation(context, R.anim.push_bottom_in);
        exitAnim = AnimationUtils.loadAnimation(context, R.anim.push_bottom_out);
    }

    EditText editView;

    private void initView() {
        // 设置不调用系统键盘
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        try {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
                    boolean.class);
            setShowSoftInputOnFocus.setAccessible(true);
            setShowSoftInputOnFocus.invoke(editView, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewKey.getLayoutBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewKey.startAnimation(exitAnim);
                viewKey.setVisibility(View.GONE);
            }
        });

        gridView = viewKey.getGridView();
        gridView.setOnItemClickListener(onItemClickListener);

        editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewKey.setFocusable(true);
                viewKey.setFocusableInTouchMode(true);

                viewKey.startAnimation(enterAnim);
                viewKey.setVisibility(View.VISIBLE);
            }
        });
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            EditText et = (EditText) editView;
            if (position < 11 && position != 9) {    //点击0~9按钮

                String amount = et.getText().toString().trim();
                amount = amount + valueList.get(position).get("name");

                et.setText(amount);
                listener.show(amount);

                Editable ea = et.getText();
                et.setSelection(ea.length());
            } else {
                if (position == 9) {      //点击小数点
                    String amount = et.getText().toString().trim();
                    if (!amount.contains(".")) {
                        amount = amount + valueList.get(position).get("name");
                        et.setText(amount);
                        listener.show(amount);

                        Editable ea = et.getText();
                        et.setSelection(ea.length());
                    }
                }

                if (position == 11) {      //点击退格键
                    String amount = et.getText().toString().trim();
                    if (amount.length() > 0) {
                        amount = amount.substring(0, amount.length() - 1);
                        et.setText(amount);
                        listener.show(amount);

                        Editable ea = et.getText();
                        et.setSelection(ea.length());
                    }
                }
            }
        }
    };


    public interface FragmentText{
        void show(String str);
    }
}
