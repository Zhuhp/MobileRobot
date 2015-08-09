package com.timyrobot.triggersystem;

import android.content.Context;

import com.example.robot.facedection.CameraSurfaceView;
import com.example.robot.facedection.FaceView;
import com.timyrobot.listener.DataReceiver;

/**
 * Created by zhangtingting on 15/8/7.
 */
public class TriggerManager {

    public TriggerManager(Context ctx,DataReceiver receiver){
        VoiceTrigger.INSTANCE.init(ctx,receiver);
        FaceDectectTrigger.INSTANCE.init(ctx,receiver);
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
}
