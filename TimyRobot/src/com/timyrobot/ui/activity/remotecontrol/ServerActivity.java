package com.timyrobot.ui.activity.remotecontrol;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.robot.R;
import com.timyrobot.utils.ToastUtils;
import com.zhuhp.base.httpserver.RobotServer;
import com.zhuhp.base.nsd.RobotClient;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by zhangtingting on 15/11/25.
 */
public class ServerActivity extends Activity implements RobotServer.HttpMsgReceiver{

    TextView mStatusTV;

    private RobotServer mServer;
    private RobotClient mClient;

    private WifiManager.MulticastLock multicastLock = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_server);
        getWifiLock();
        mStatusTV = (TextView)findViewById(R.id.tv_status);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mClient = new RobotClient(ServerActivity.this);
                    mClient.start();
                    mServer = new RobotServer();
                    mServer.setHttpMsgReceiver(ServerActivity.this);
                    mServer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(ServerActivity.class.getName(), "sorry, init client failed");
                }
            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        multicastLock.release();
    }

    public void getWifiLock(){
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        multicastLock = wifiManager.createMulticastLock("multicast.test");
        multicastLock.acquire();
    }

    @Override
    public String onReceive(NanoHTTPD.IHTTPSession session) {
        return null;
    }

//    @Override
//    public void onReceive(final String msg) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mStatusTV.setText(msg);
//            }
//        });
//    }
}
