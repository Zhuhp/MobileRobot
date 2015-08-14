package com.timyrobot.robot;

import com.timyrobot.service.bluetooth.BluetoothManager;
import com.timyrobot.service.bluetooth.IDataReceiver;

/**
 * Created by zhangtingting on 15/8/8.
 */
public enum RobotProxy {
    INSTANCE;
    private final static String TAG = RobotProxy.class.getName();

    public void sendData(String cmd){

        BluetoothManager.INSTANCE.sendData(cmd);
    }

    public void setIDataReceiver(IDataReceiver receiver){
        BluetoothManager.INSTANCE.setDataReceiver(receiver);
    }

}
