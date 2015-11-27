package com.timyrobot.ui.activity.remotecontrol;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;

import com.example.robot.R;
import com.timyrobot.httpserver.router._control;
import com.timyrobot.ui.activity.remotecontrol.speech.FlySpeechRecon;
import com.timyrobot.ui.activity.remotecontrol.speech.INormalSpeechRecon;
import com.timyrobot.ui.activity.remotecontrol.speech.ISpeechResult;
import com.timyrobot.utils.ToastUtils;
import com.zhuhp.base.httpserver.ResponseMsg;
import com.zhuhp.base.nsd.ControlClient;

import java.io.IOException;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by zhangtingting on 15/11/25.
 */
public class ControlActivity extends Activity implements ControlClient.ServerInfoListener,
        View.OnClickListener, ISpeechResult{

    /**
     * 发现服务的状态
     */
    public static final int STATUS_DISCOVERYING = 0x001;

    /**
     * 断开服务的状态
     */
    public static final int STATUS_DISCONNECT = 0x002;

    /**
     * 连接服务的状态
     */
    public static final int STATUS_CONNECT = 0x003;

    private String mServerUrl;
    private ControlClient mControlClient;
    private INormalSpeechRecon mSpeech;

    private WifiManager.MulticastLock multicastLock = null;
    Retrofit retrofit;
    _control.ControlService controlService;

    private int mStatus = STATUS_DISCONNECT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_control);
        getWifiLock();
        //TODO 需要进行英文版确认
        mSpeech = new FlySpeechRecon(this);
        findViewById(R.id.btn_ahead).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_left).setOnClickListener(this);
        findViewById(R.id.btn_right).setOnClickListener(this);
//        initHttpClient("192.168.24.106:8080");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开启寻找服务
        startDiscovery();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //关闭寻找服务
        stopDiscovery();
    }

    private void startDiscovery(){
        setStatus(STATUS_DISCOVERYING);
        try {
            mControlClient = new ControlClient(this);
            mControlClient.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        uiDiscoverying();
    }

    private void stopDiscovery(){
        if(mControlClient != null){
            mControlClient.tearDown();
            mControlClient = null;
        }
    }

    private void discoverySuccess(){
        setStatus(STATUS_CONNECT);
        uiConnect();
    }

    private void disconnect(){
        setStatus(STATUS_DISCONNECT);
        uiDisconnect();
    }

    private synchronized void setStatus(int status){
        mStatus = status;
    }

    /**
     * 发现服务的UI状态
     */
    private void uiDiscoverying(){

    }

    /**
     * 断开服务的UI状态
     */
    private void uiDisconnect(){

    }

    /**
     * 已连接的UI状态
     */
    private void uiConnect(){

    }

    private void getWifiLock(){
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        multicastLock = wifiManager.createMulticastLock("multicast.mobile.service");
        multicastLock.acquire();
    }

    @Override
    public void onHttpServerInfo(String ip, int port) {
        //连接上服务了
        setStatus(STATUS_CONNECT);
        mServerUrl = ip + ":" + port;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.toastShort(ControlActivity.this, "ServerUrl:" + mServerUrl);
                uiConnect();
            }
        });
        initHttpClient(mServerUrl);
    }

    private void initHttpClient(String serverUrl){
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        controlService = retrofit.create(_control.ControlService.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        multicastLock.release();
    }

    @Override
    public void onClick(View v) {
        //TODO 如果不是连接状态，就不进行发送，而是提醒状态
        switch (v.getId()){
            case R.id.btn_ahead:
                sendCmd("ahead");
                break;
            case R.id.btn_back:
                sendCmd("陈桂和胡涛是鸭，我要鼓励你们！");
                break;
            case R.id.btn_left:
                sendCmd("left");
                break;
            case R.id.btn_right:
                sendCmd("right");
                break;

        }
    }

    private synchronized boolean isConnected(){
        return mStatus == STATUS_CONNECT;
    }

    private synchronized boolean isDiscoverying(){
        return mStatus == STATUS_DISCOVERYING;
    }

    private synchronized boolean isDisconnected(){
        return mStatus == STATUS_DISCONNECT;
    }

    private void sendCmd(final String cmd){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<ResponseMsg> msg = controlService.test(cmd);
                try {
                    Response<ResponseMsg> response = msg.execute();
                    response.code();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onResult(String content) {
        //TODO http发送内容，不是连接状态，就不发送了
    }
}
