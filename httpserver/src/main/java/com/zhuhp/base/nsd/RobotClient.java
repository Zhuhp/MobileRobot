package com.zhuhp.base.nsd;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.zhuhp.base.mutilcastsocket.NSDReceiver;
import com.zhuhp.base.mutilcastsocket.NSDRecevieDataCallback;
import com.zhuhp.base.mutilcastsocket.NSDSender;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by zhangtingting on 15/11/24.
 */
public class RobotClient implements NSDRecevieDataCallback{

    private NSDReceiver nsdReceiver;
    private NSDSender nsdSender;
    private Context mCtx;
    private String mLocalIp;

    public RobotClient(Context ctx) throws IOException {
        nsdReceiver = new NSDReceiver();
        nsdSender = new NSDSender();
        nsdSender.init();
        mCtx = ctx;
    }

    public void start() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    nsdReceiver.receiveData(RobotClient.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public boolean onDataReceive(final String msg) {
        try {
            JSONObject object = new JSONObject(msg);
            if(NsdMobileRobotProtocol.isMobileControlClientService(object)) {
                if(TextUtils.isEmpty(mLocalIp)) {
                    mLocalIp = getLocalIpAddress(mCtx).getHostAddress();
                }
                //TODO rtsp的信息需要添加
                nsdSender.broadcast(NsdMobileRobotProtocol.createRobotClientProtocol(mLocalIp, 8080, null));
            }
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static InetAddress getLocalIpAddress(Context context) throws UnknownHostException {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return InetAddress.getByName(String.format("%d.%d.%d.%d",
                (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff)));
    }

}
