package com.zhuhp.base.nsd;

import android.text.TextUtils;

import com.zhuhp.base.mutilcastsocket.NSDReceiver;
import com.zhuhp.base.mutilcastsocket.NSDRecevieDataCallback;
import com.zhuhp.base.mutilcastsocket.NSDSender;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by zhangtingting on 15/11/24.
 */
public class ControlClient implements NSDRecevieDataCallback{

    private NSDReceiver nsdReceiver;
    private NSDSender nsdSender;

    private ServerInfoListener mListener;

    private boolean isPause = false;

    public ControlClient(ServerInfoListener listener) throws IOException {
        nsdReceiver = new NSDReceiver();
        nsdSender = new NSDSender();
        nsdSender.init();
        mListener = listener;
    }

    public void start(){
        startReceiver();
        startSender();
    }

    public void startReceiver(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    nsdReceiver.receiveData(ControlClient.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void startSender(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isPause){
                    try {
                        nsdSender.broadcast(NsdMobileRobotProtocol.createControlClientProtocol());
                        Thread.sleep(500);
                    } catch (IOException e) {
                        e.printStackTrace();
                        nsdSender.tearDown();
                        nsdSender = new NSDSender();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    @Override
    public boolean onDataReceive(String msg) {
        if(TextUtils.isEmpty(msg)){
            return false;
        }
        try {
            JSONObject object = new JSONObject(msg);
            if(NsdMobileRobotProtocol.isMobileRobotClientService(object)){
                if(mListener != null) {
                    String ip = object.optString(NsdMobileRobotProtocol.KEY_IP);
                    int port = object.optInt(NsdMobileRobotProtocol.KEY_PORT);
                    mListener.onHttpServerInfo(ip, port);
                }
                tearDown();
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void tearDown(){
        mListener = null;
        isPause = true;
        if(nsdSender != null) {
            nsdSender.tearDown();
        }
        if(nsdReceiver != null) {
            nsdReceiver.tearDown();
        }
    }

    public interface ServerInfoListener{
        void onHttpServerInfo(String ip, int port);
    }
}
