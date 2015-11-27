package com.zhuhp.base.mutilcastsocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by zhangtingting on 15/11/23.
 */
public class NSDReceiver {

    private MulticastSocket mClientSocket;
    private InetAddress mAddr;
    private boolean isPause = false;

    public NSDReceiver() throws IOException {
        mClientSocket = new MulticastSocket(NSDConst.PORT);
        mAddr = InetAddress.getByName(NSDConst.IP);
        mClientSocket.joinGroup(mAddr);
    }

    public void receiveData(NSDRecevieDataCallback callback) throws IOException {
        if(callback == null){
            throw new NullPointerException("callback is null");
        }
        byte[] buff = new byte[256];
        while (!isPause){
            DatagramPacket packet = new DatagramPacket(buff, buff.length);
            mClientSocket.receive(packet);
            String msg = new String(buff, packet.getOffset(), packet.getLength());
            if(callback.onDataReceive(msg)){
                break;
            }

        }
    }

    public void tearDown(){
        isPause = false;
        mClientSocket.disconnect();
        mClientSocket.close();
    }

}
