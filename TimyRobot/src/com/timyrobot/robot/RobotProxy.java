package com.timyrobot.robot;

import com.timyrobot.listener.DataReceiver;
import com.timyrobot.robot.bean.RobotAction;
import com.timyrobot.robot.data.RobotData;
import com.timyrobot.service.bluetooth.BluetoothManager;
import com.timyrobot.service.bluetooth.IDataReceiver;

import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/8/8.
 */
public enum RobotProxy {
    INSTANCE;

    public void sendData(String cmd){
        BluetoothManager.INSTANCE.sendData(cmd);
    }

    public void setIDataReceiver(IDataReceiver receiver){
        BluetoothManager.INSTANCE.setDataReceiver(receiver);
    }

}
