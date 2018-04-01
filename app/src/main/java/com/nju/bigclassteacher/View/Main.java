package com.nju.bigclassteacher.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nju.bigclassteacher.R;
import com.nju.bigclassteacher.Receiver.BluetoothReceiver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class Main extends AppCompatActivity {
    private static final String TAG = "Main";

    private IntentFilter mFoundFilter;//找到设备过滤器
    private IntentFilter mDiscoverFinishedFilter;//扫描结束过滤器
    private BluetoothReceiver mBluetoothReceiver;//蓝牙接收器
    private static final int BLUETOOTH_PERMISSION = 840;//申请蓝牙权限返回码


    // 蓝牙服务端socket
    private BluetoothServerSocket mServerSocket;
    // 蓝牙客户端socket
    private BluetoothSocket mSocket;
    // 设备
    private BluetoothDevice mDevice;
    private BluetoothAdapter mBluetoothAdapter;//蓝牙适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {

        mBluetoothReceiver = new BluetoothReceiver();//实例化蓝牙接收器
        //动态注册Bluetooth广播接收类
        mFoundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBluetoothReceiver, mFoundFilter);
        mDiscoverFinishedFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mBluetoothReceiver, mDiscoverFinishedFilter);

        //得到蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    //扫描蓝牙设备
    public void scan_device(View view) {
        requestBluetoothPermission();
    }

    /**
     * 申请蓝牙权限
     */
    private void requestBluetoothPermission() {
        PermissionGen.with(this)
                .addRequestCode(BLUETOOTH_PERMISSION)
                .permissions(
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .request();
    }

    //重写权限请求返回方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //解除绑定蓝牙接收器
        unregisterReceiver(mBluetoothReceiver);
    }


    /**
     * 申请权限成功
     */
    @PermissionSuccess(requestCode = BLUETOOTH_PERMISSION)
    public void requestPhotoSuccess() {
        //成功之后的处理
        //判断蓝牙是否打开
        if (!mBluetoothAdapter.isEnabled()) {
            //若没打开则打开蓝牙
            mBluetoothAdapter.enable();
        }
        //开始蓝牙广播，当找到设备或者discover完成时会调用BluetoothReceiver的onReceive方法
        mBluetoothAdapter.startDiscovery();
    }

    /**
     * 申请权限失败
     */
    @PermissionFail(requestCode = BLUETOOTH_PERMISSION)
    public void requestPhotoFail() {
        //失败之后的处理，我一般是跳到设置界面
        Log.e(TAG, "申请权限失败");
    }


}
