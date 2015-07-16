package com.timyrobot.service.bluetooth;

import android.content.Context;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * 蓝牙控制类
 * Created by zhuhp on 15/7/13.
 */
public enum BluetoothManager {
    INSTANCE;

    private BluetoothSPP mBlueSpp;
    private Context mContext;

    /**
     * 蓝牙初始化
     * @param context
     */
    public void init(Context context){
        mContext = context;
        mBlueSpp = new BluetoothSPP(mContext);
    }

    /**
     * 启动蓝牙服务，链接的不是Android手机
     * @param context
     */
    public void startBlueService(Context context){
        if(mBlueSpp == null){
            init(mContext);
        }
        if(!mBlueSpp.isServiceAvailable()){
            mBlueSpp.setupService();
            mBlueSpp.startService(false);
        }
    }

    public void stopBlueService(){
        if(mBlueSpp != null){
            mBlueSpp.stopService();
        }
    }

    /**
     * 判断蓝牙是否可用
     * @return
     */
    public boolean isBlueAvailable(){
        if(mBlueSpp != null){
            return mBlueSpp.isBluetoothAvailable();
        }
        return false;
    }
}
