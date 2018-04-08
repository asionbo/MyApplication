package com.example.asionbo.myapplication.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.asionbo.myapplication.R;
import com.example.asionbo.myapplication.event.Event;
import com.example.asionbo.myapplication.model.BluetoothAddress;
import com.example.asionbo.myapplication.service.BluetoothChatService;
import com.example.asionbo.myapplication.service.BluetoothService;
import com.example.asionbo.myapplication.ui.adapter.BluetoothAdapter_;
import com.example.asionbo.myapplication.utils.Constant;
import com.example.asionbo.myapplication.utils.LogUtils;
import com.example.asionbo.myapplication.utils.PrintUtils;
import com.example.asionbo.myapplication.utils.RxBus;
import com.example.asionbo.myapplication.view.MySheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by asionbo on 2018/3/14.
 */

public class MyPrinterActivity extends AppCompatActivity {

    @BindView(R.id.toolBar)
    Toolbar mToolbar;
    @BindView(R.id.status)
    TextView mStatus;
    @BindView(R.id.et_text)
    EditText printText;
    @BindView(R.id.cb_bold)
    CheckBox cbBold;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothAdapter_ mAdapter;
    private MaterialDialog mDialog;
    private Intent mBluetoothIntent;
    private String mBluetoothName;
    protected Disposable mDisposable;
    private MaterialSheetFab materialSheetFab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mBluetoothIntent = new Intent(this, BluetoothService.class);
        mBluetoothIntent.putExtra("type", 0);
        startService(mBluetoothIntent);
        MySheetFab fab = (MySheetFab) findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.colorWhite);
        int fabColor = getResources().getColor(R.color.colorPrimary);

        // Initialize material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay,
                sheetColor, fabColor);

        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                LogUtils.e("onShowSheet");
                // Called when the material sheet's "show" animation starts.
            }

            @Override
            public void onSheetShown() {
                LogUtils.e("onSheetShown");
                // Called when the material sheet's "show" animation ends.
            }

            @Override
            public void onHideSheet() {
                LogUtils.e("onHideSheet");
                // Called when the material sheet's "hide" animation starts.
            }
            @Override
            public void onSheetHidden() {
                LogUtils.e("onSheetHidden");
                // Called when the material sheet's "hide" animation ends.
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDisposable == null || mDisposable.isDisposed()) {
            mDisposable = RxBus.INSTANCE.toObserverable(Event.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userEvent -> {
                        switch (userEvent.getType()) {
                            case Constant.MESSAGE_CONNECT:
                                mDialog.dismiss();
                                BluetoothAddress address = (BluetoothAddress) userEvent.getPost();
                                mStatus.setText("已连接上:" + address.getAddress());
                                mBluetoothIntent.putExtra("type", 1);
                                mBluetoothIntent.putExtra("address", address.getAddress());
                                startService(mBluetoothIntent);
                                break;
                            case Constant.MESSAGE_STATE_CHANGE:
                                mStatus.setVisibility(View.VISIBLE);
                                if ((Integer) userEvent.getPost() == BluetoothChatService.STATE_CONNECTED) {
                                    mStatus.setText("已连接：" + mBluetoothName);
                                } else if ((Integer) userEvent.getPost() == BluetoothChatService.STATE_CONNECTING) {
                                    mStatus.setText("正在连接。。。。");
                                } else {
                                    mStatus.setText("连接失败");
                                }
                                break;
                            case Constant.MESSAGE_DEVICE_NAME:
                                mBluetoothName = (String) userEvent.getPost();
                                break;
                            case Constant.MESSAGE_READ:
                                String readMessage = (String) userEvent.getPost();
                                //蓝牙打印机也会返回信息过来，这里判断一下。
                                if (readMessage == null || "".equals(readMessage.trim())) {
                                    return;
                                }
                                break;
                            case Constant.MESSAGE_TOAST:
                                mStatus.setText((String) userEvent.getPost());
                                break;
                            default:
                                break;
                        }
                    }, throwable -> {
                        LogUtils.e(throwable.getMessage());
                    });
        }

    }

    @OnClick(R.id.connect)
    void clickConnect() {
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, Constant.REQUEST_ENABLE_BT2);
        } else {
            showBluetoothListDialog();
        }
    }

    private void showBluetoothListDialog() {
        if (mBluetoothAdapter == null) {
            mStatus.setText("蓝牙初始化失败");
            return;
        }
        mAdapter = new BluetoothAdapter_(this);
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            BluetoothAddress address = new BluetoothAddress();
            address.setName(device.getName() + "\n" + device.getAddress());
            address.setAddress(device.getAddress());
            mAdapter.addOne(address);
        }
        mDialog = new MaterialDialog.Builder(this)
                .title("请选择设备进行连接")
                .customView(R.layout.dialog_bluetooth, true)
                .build();
        RecyclerView dataList = (RecyclerView) mDialog.getCustomView().findViewById(R.id.dataList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        dataList.setLayoutManager(linearLayoutManager);
        dataList.setAdapter(mAdapter);
        mDialog.show();
    }

    @OnClick(R.id.print)
    void clickPrint() {
        printTest();
    }

    @OnClick(R.id.print_demo)
    void clickPrintDemo() {
        printJhd();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_ENABLE_BT2:
                    if (resultCode == Activity.RESULT_OK) {
                        showBluetoothListDialog();
                    } else {
                        mStatus.setText("蓝牙没启动");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()){
            mDisposable.dispose();
        }
        mDisposable = null;
    }

    private void printBluetooth() {
        int count = 0;
        try {
            Intent intent = new Intent(this, BluetoothService.class);
            intent.putExtra("type", 2);
            intent.putExtra("title", "门店：ddddddd" + "\n");
            intent.putExtra("subtitle", "订单拣货单\n");
            StringBuffer sb = new StringBuffer();
            sb.append("\n");
            intent.putExtra("data", sb.toString());
            startService(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void printTest() {
        PrintUtils.selectCommand(PrintUtils.RESET);
        PrintUtils.selectCommand(PrintUtils.LINE_SPACING_DEFAULT);
        PrintUtils.printFourData("商品","数量","单价","成交价\n");
        PrintUtils.printText("五粮液贡品出产\n");
        PrintUtils.printFourData("585858595959","2","35.5","70\n");
        PrintUtils.printLine();
        PrintUtils.printNewLine();

//        PrintUtils.printQR(PrintUtils.createQRCode(printText.getText().toString(),240));
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_2);
//        PrintUtils.printBitmap(bitmap);
//
//        if (cbBold.isChecked()) {
//            PrintUtils.printBigMidText(printText.getText().toString() + "\n");
//        } else {
//            PrintUtils.printNormalMidText(printText.getText().toString() + "\n");
//        }
    }

    private void printJhd() {
        PrintUtils.selectCommand(PrintUtils.RESET);
        PrintUtils.selectCommand(PrintUtils.LINE_SPACING_DEFAULT);
        PrintUtils.printNormalMidText("天河华景软件园\n");
        PrintUtils.printNormalMidText("订单拣货单\n");
        PrintUtils.printTwoData("单号：", "st18030303\n");
        PrintUtils.printBigMidText("*st18030303*\n");
        PrintUtils.selectCommand(PrintUtils.NORMAL);
        PrintUtils.printThreeData("商品", "申请", "缺货\n");
        PrintUtils.printText("五粮液畅享500ml（42度）醇香型\n");
        PrintUtils.printThreeData("68568595959", "3", "0\n");
        PrintUtils.printThreeData("合计：", "3", "0\n");
        PrintUtils.printTwoData("拣货人：", "Mr.Zhang\n\n\n");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_2);
        PrintUtils.printBitmap(bitmap,320,240);
        PrintUtils.printQR(PrintUtils.createQRCode(printText.getText().toString(),240));
    }

    @Override
    public void onBackPressed() {
        if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();
        } else {
            super.onBackPressed();
        }
    }
}
