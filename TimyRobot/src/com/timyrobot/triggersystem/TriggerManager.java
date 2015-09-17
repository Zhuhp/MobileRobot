package com.timyrobot.triggersystem;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.example.robot.facedection.CameraSurfaceView;
import com.example.robot.facedection.FaceView;
import com.timyrobot.bean.ControllCommand;
import com.timyrobot.common.ConstDefine;
import com.timyrobot.common.Property;
import com.timyrobot.controlsystem.IControlListener;
import com.timyrobot.listener.DataReceiver;
import com.timyrobot.listener.EndListener;
import com.timyrobot.listener.TouchDataReceiver;
import com.timyrobot.listener.VoiceDataReceiver;
import com.timyrobot.parsesystem.ParseManager;

/**
 * Created by zhangtingting on 15/8/7.
 */
public class TriggerManager implements DataReceiver, VoiceDataReceiver, TouchDataReceiver{

    public static final int START_RUN = 0x001;

    private ParseManager mParseManager;

    private Handler mRunHandler;

    public TriggerManager(Context ctx){
        HandlerThread runThread = new HandlerThread("run-cmd");
        runThread.start();
        mRunHandler = new Handler(mSendCB);
        VoiceTrigger.INSTANCE.init(ctx,this,this);
        FaceDectectTrigger.INSTANCE.init(ctx,this);
        TouchTrigger.INSTANCE.init(this);
        HttpTrigger.INSTANCE.init(this, Property.ROBOT_CODE, Property.IS_RECEIVER);
    }

    private Handler.Callback mSendCB = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case START_RUN:
                    parseData(String.valueOf(msg.obj));
                    break;
            }
            return true;
        }
    };

    public void setParseManager(ParseManager manager){
        mParseManager = manager;
    }

    public void init(CameraSurfaceView cameraSurfaceView,FaceView faceView){
        FaceDectectTrigger.INSTANCE.initFaceDectect(cameraSurfaceView, faceView);
    }

    public void start(){
        FaceDectectTrigger.INSTANCE.startDetect();
    }

    public void startConversation(){
        VoiceTrigger.INSTANCE.startConversation();
    }

    public void stop(){
        FaceDectectTrigger.INSTANCE.stopDetect();
    }

    public void startTouch(){
        TouchTrigger.INSTANCE.phoneTouch();
    }

    private void parseData(String data){
        if(mParseManager == null){
            return;
        }
        mParseManager.parse(data);
    }

    @Override
    public void onReceive(String data) {
        if(!TriggerSwitch.INSTANCE.goNext()){
            return;
        }
        Message msg = new Message();
        msg.what = START_RUN;
        msg.obj = data;
        mRunHandler.sendMessage(msg);
//        parseData(data);
    }

    @Override
    public void onVoiceDataReceiver(String data) {
        if(ConstDefine.VoiceCMD.ERROR.equals(data)){
            TriggerSwitch.INSTANCE.setCanNext(true);
            return;
        }
        Message msg = new Message();
        msg.what = START_RUN;
        msg.obj = data;
        mRunHandler.sendMessage(msg);
//        parseData(data);
    }

    @Override
    public void onTouchDataReceive(String data) {
        parseData(data);
    }
}
