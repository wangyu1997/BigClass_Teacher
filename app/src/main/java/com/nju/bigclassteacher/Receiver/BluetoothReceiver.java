package com.nju.bigclassteacher.Receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 蓝牙广播接收器
 */
public class BluetoothReceiver extends BroadcastReceiver {

    private static final String TAG = "BluetoothReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null)
            return;
        if (action.equals(BluetoothDevice.ACTION_FOUND)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device.getBondState() == BluetoothDevice.BOND_BONDED) {    //显示已配对设备
                Log.e(TAG, device.getName() + "==>" + device.getAddress());
            } else if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                Log.e(TAG, device.getName() + "!=>" + device.getAddress());
            }
        } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
            Log.e(TAG, "搜索完成...");
        }

    }
}
