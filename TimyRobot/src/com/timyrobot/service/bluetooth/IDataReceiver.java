package com.timyrobot.service.bluetooth;

/**
 * Created by zhangtingting on 15/7/30.
 */
public interface IDataReceiver {
    void onReceive(byte[] data, String msg);
}
