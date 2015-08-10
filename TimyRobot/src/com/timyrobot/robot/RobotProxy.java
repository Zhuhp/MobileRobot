package com.timyrobot.robot;

import android.util.Log;

import com.timyrobot.robot.bean.RobotAction;
import com.timyrobot.robot.data.RobotData;
import com.timyrobot.service.bluetooth.BluetoothManager;
import com.timyrobot.service.bluetooth.IDataReceiver;

/**
 * Created by zhangtingting on 15/8/8.
 */
public enum RobotProxy {
    INSTANCE;
    private final static String TAG = RobotProxy.class.getName();

    public void sendData(String cmd){
        RobotAction action = RobotData.INSTANCE.getRobotAction(cmd);
        if(action == null){
            Log.e(TAG, "Cannot get robot action!");
            return;
        }
        Log.e(TAG, "Send Bluetooth message:" + action.getActions().get(0).toString());
        //BluetoothManager.INSTANCE.sendData(action.getActions().get(0).toString());
    }

    public void setIDataReceiver(IDataReceiver receiver){
        BluetoothManager.INSTANCE.setDataReceiver(receiver);
    }

}
