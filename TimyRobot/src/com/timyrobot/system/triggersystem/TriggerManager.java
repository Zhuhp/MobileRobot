package com.timyrobot.system.triggersystem;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.example.robot.facedection.CameraSurfaceView;
import com.example.robot.facedection.FaceView;
import com.timyrobot.system.bean.BaseCommand;
import com.timyrobot.common.Property;
import com.timyrobot.system.filler.IFiller;
import com.timyrobot.system.triggersystem.listener.DataReceiver;

/**
 * Created by zhangtingting on 15/8/7.
 */
public class TriggerManager implements DataReceiver{

    public static final int START_RUN = 0x001;

    private Handler mRunHandler;

    private IFiller mFiller;

    public TriggerManager(Context ctx, IFiller filler){
        HandlerThread runThread = new HandlerThread("run-cmd");
        runThread.start();
        mRunHandler = new Handler(mSendCB);
        mFiller = filler;
        FaceDectectTrigger.INSTANCE.init(ctx,this);
        TouchTrigger.INSTANCE.init(this);
        HttpTrigger.INSTANCE.init(this, Property.ROBOT_CODE, Property.IS_RECEIVER);
    }

    private Handler.Callback mSendCB = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case START_RUN:
                    mFiller.fill((BaseCommand)msg.obj);
                    break;
            }
            return true;
        }
    };

    public void init(CameraSurfaceView cameraSurfaceView,FaceView faceView){
        FaceDectectTrigger.INSTANCE.initFaceDectect(cameraSurfaceView, faceView);
    }

    public void start(){
        FaceDectectTrigger.INSTANCE.startDetect();
    }

    public void stop(){
        FaceDectectTrigger.INSTANCE.stopDetect();
    }


    @Override
    public void onReceive(BaseCommand cmd) {
        if(!TriggerSwitch.INSTANCE.goNext()){
            return;
        }
        Message msg = new Message();
        msg.what = START_RUN;
        msg.obj = cmd;
        mRunHandler.sendMessage(msg);
    }

}
