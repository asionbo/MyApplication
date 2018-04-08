package com.example.asionbo.myapplication.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

public class BluetoothService extends Service {

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothChatService mChatService;

//    private User mUser;
//    private Realm mRealm;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(getClass().getName(),"onCreate");
//        mRealm = Realm.getDefaultInstance();
//        mUser = mRealm.where(User.class).findFirst();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public void onDestroy() {
        Log.d(getClass().getName(),"onDestroy");
        super.onDestroy();
        if (mChatService != null) mChatService.stop();
//        if (!mRealm.isClosed()) mRealm.close();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(getClass().getName(),"onStartCommand");
        int type = 0;
        try {
            type = intent.getIntExtra("type", 1);
        }catch (Exception e){
            type = 0;
        }
        if(1 == type){
            if (mChatService == null) mChatService = new BluetoothChatService();
            String address = intent.getStringExtra("address");
            if (!TextUtils.isEmpty(address)) {
                connect(address);
            }
        }else if(2 == type){
            String data = intent.getStringExtra("data");
            String title = intent.getStringExtra("title");
            String subtitle = intent.getStringExtra("subtitle");
            byte[] qr = intent.getByteArrayExtra("qr");
            sendMessage(title,subtitle,data,qr);
        }else if(3 == type){
            byte[] data = intent.getByteArrayExtra("data");
            sendByte(data);
        }else if(4 == type){
            String data = intent.getStringExtra("data");
            sendCode(data);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void connect(String address){
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (mChatService != null) mChatService.connect(device);
    }

    private void sendMessage(String title,String subtitle, String message,byte[] qr) {
        new Thread() {
            @Override
            public void run() {
                if (message.length() > 0) {
                    try {
                        byte[] send = message.getBytes();    //发送给本地文字框的数据缓冲区
//                        byte[] buffer = message.substring(message.length() / 2).getBytes();    //发送给对端蓝牙设备的数据缓冲区
                        byte[] buffer = message.getBytes("GB2312");
                        if (mChatService != null) {
                            mChatService.write(new byte[] { 0x1B, 0x40 });
                            mChatService.write(new byte[] { 0x1D, 0x21, 0x11 });
                            mChatService.write(title.getBytes("GBK"));
                            mChatService.write(new byte[] { 0x1D, 0x21, 0x00 });
                            mChatService.write(subtitle.getBytes("GBK"));
                            mChatService.write(buffer);
                            sendCode("0A");

                            mChatService.write(qr);
                            mChatService.write(new byte[] { 0x1B, 0x40 });
                            mChatService.write("\n\n\n".getBytes());

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void sendCode(String message){
        byte[] send = message.getBytes();    //发送给本地文字框的数据缓冲区
        byte[] buffer = message.substring(message.length() / 2).getBytes();    //发送给对端蓝牙设备的数据缓冲区
        byte temp = 0;                //转换十六进制的临时变量
        Boolean unite = false;        //转换十六进制的临时变量

        for (int j = 0; j < message.length() / 2; j++)        //初始化发送缓冲区
            buffer[j] = 0;
        for (int i = 0; i < message.length(); i++)    //对十六进制发送数据组合
        {
            if (send[i] != ' ') {
                if (send[i] >= '0' && send[i] <= '9') {
                    temp = (byte) (send[i] - '0');
                    if (unite) {
                        buffer[i / 2] += temp;
                        unite = !unite;
                    } else {
                        buffer[i / 2] = (byte) (temp << 4);
                        unite = !unite;
                    }
                } else if (send[i] >= 'a' && send[i] <= 'f') {
                    temp = (byte) (send[i] - 'a' + 10);
                    if (unite) {
                        buffer[i / 2] += temp;
                        unite = !unite;
                    } else {
                        buffer[i / 2] = (byte) (temp << 4);
                        unite = !unite;
                    }
                } else if (send[i] >= 'A' && send[i] <= 'F') {
                    temp = (byte) (send[i] - 'A' + 10);
                    if (unite) {
                        buffer[i / 2] += temp;
                        unite = !unite;
                    } else {
                        buffer[i / 2] = (byte) (temp << 4);
                        unite = !unite;
                    }
                }
            }
        }
        try {
            mChatService.write(buffer);
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    private void sendByte(byte[] message) {
        new Thread() {
            @Override
            public void run() {
                if (message.length > 0) {
                    try {
                        if (mChatService != null) {
                            mChatService.write(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
