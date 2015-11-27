package com.zhuhp.base.mutilcastsocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by zhangtingting on 15/11/23.
 */
public class NSDSender {

    private InetAddress mAddr;
    DatagramSocket mServerSocket;

    public void init() throws SocketException, UnknownHostException {
        if(mServerSocket == null){
            mServerSocket = new DatagramSocket();
        }
        if(mAddr == null){
            mAddr = InetAddress.getByName(NSDConst.IP);
        }
    }

    public void broadcast(String info) throws IOException {
        byte[] infoByte = info.getBytes();
        DatagramPacket msgPacket = new DatagramPacket(infoByte, infoByte.length, mAddr, NSDConst.PORT);
        mServerSocket.send(msgPacket);
    }

    public void tearDown(){
        mServerSocket.disconnect();
        mServerSocket.close();
        mServerSocket = null;
        mAddr = null;
    }
}
