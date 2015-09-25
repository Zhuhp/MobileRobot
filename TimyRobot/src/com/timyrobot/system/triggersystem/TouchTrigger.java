package com.timyrobot.system.triggersystem;

import com.timyrobot.system.bean.NeedVoiceReconCommand;
import com.timyrobot.system.triggersystem.listener.DataReceiver;

/**
 * Created by zhangtingting on 15/8/14.
 */
public enum TouchTrigger {
    INSTANCE;

    private DataReceiver mDataReceiver;
    public void init(DataReceiver touchDataReceiver){
        mDataReceiver = touchDataReceiver;
    }

    public void phoneTouch(){
        if(mDataReceiver != null){
            mDataReceiver.onReceive(new NeedVoiceReconCommand());
        }
    }

}
