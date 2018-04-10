package com.example.asionbo.myapplication.ui.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
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
import com.example.asionbo.myapplication.ui.LoginActivity;
import com.example.asionbo.myapplication.ui.LottieAniActivity;
import com.example.asionbo.myapplication.ui.MultiSelectActivity;
import com.example.asionbo.myapplication.ui.MyPrinterActivity;
import com.example.asionbo.myapplication.ui.kotlin.KtTest_Java;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import static android.content.Context.TELEPHONY_SERVICE;


/**
 * Created by asionbo on 2017/10/13.
 */

public class MainFragment extends BaseFragment {

    private String mac, imei;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        Button mBtn = (Button) view.findViewById(R.id.btn_toFragment);
        Button mBtnToKt = (Button) view.findViewById(R.id.btn_toKotlin);
        Button toMulti = (Button) view.findViewById(R.id.btn_toMulti);
        Button showProgress = (Button) view.findViewById(R.id.btn_showProgress);
        Button print = (Button) view.findViewById(R.id.btn_printer);
        Button shake = (Button) view.findViewById(R.id.btn_shake);
        Button login = (Button) view.findViewById(R.id.btn_login);
        Button getMac = (Button) view.findViewById(R.id.btn_get_mac);


        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            imei = TextUtils.isEmpty(telephonyManager.getDeviceId()) ? "-1" : telephonyManager.getDeviceId();
        }

        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);


            for (; null != str;) {
                str = input.readLine();
                if (str != null) {
                    mac = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            mac = "233333333";
            // 赋予默认值
            ex.printStackTrace();
        }

        getMac.setOnClickListener(l->{
            new MaterialDialog.Builder(getActivity())
                    .title("IMEI-MAC信息")
                    .content("IMEI:"+imei+"\n"+"MAC:"+mac)
                    .positiveText("了解").show();
        });
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
            startActivity(new Intent(getContext(),LottieAniActivity.class));
        });

        showProgress.setOnClickListener(l->{
            showDownloadDialog(false);
        });
        print.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(),MyPrinterActivity.class);
            startActivity(intent);
        });
        login.setOnClickListener(l->{
            startActivity(new Intent(getContext(),LoginActivity.class));
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
