package com.timyrobot.controlsystem;

import android.content.Context;

import com.timyrobot.robot.RobotProxy;

/**
 * Created by zhangtingting on 15/8/8.
 */
public class RobotControl {

    Context mCtx;
    private boolean isNext = true;

    public RobotControl(Context context){
        mCtx = context;
    }

    public boolean next(){
        return isNext;
    }

    public void doAction(String action){
        RobotProxy.INSTANCE.sendData(action);
    }
}
