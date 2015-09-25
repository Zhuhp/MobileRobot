package com.timyrobot.triggersystem;

import com.timyrobot.bean.NeedVoiceReconCommand;
import com.timyrobot.listener.DataReceiver;

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
