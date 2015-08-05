package com.timyrobot.service.bluetooth;

/**
 * Created by zhangtingting on 15/7/28.
 */
public interface IBlueConnectListener {
    void connect(String name,String address);
    void recvMsg(String data);
    void connectFailed();
    void disconnect();
}
