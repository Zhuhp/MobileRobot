package com.timyrobot.controlsystem;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

import com.timyrobot.bean.ControllCommand;
import com.timyrobot.common.ConstDefine;
import com.timyrobot.robot.RobotProxy;
import com.timyrobot.triggersystem.TriggerManager;

/**
 * Created by zhangtingting on 15/8/7.
 */
public class ControlManager {

    Activity mActivity;

    private EmotionControl mEmotionCtrl;
    private VoiceControl mVoiceCtrl;
    private RobotControl mRobotCtrl;
    private SystemControl mSystemCtrl;

    public ControlManager(Activity activity,ImageView imageView){
        mActivity = activity;
        mEmotionCtrl = new EmotionControl(mActivity,imageView, EmotionControl.EmotionType.DEFAULT);
        mVoiceCtrl = new VoiceControl(mActivity);
        mRobotCtrl = new RobotControl(mActivity);
        mSystemCtrl = new SystemControl(mActivity);
    }

    public void distribute(ControllCommand cmd){
        if(cmd == null){
            return;
        }

        if(!mVoiceCtrl.next()){
            return;
        }
        if(!mRobotCtrl.next()){
            return;
        }
        if(!mSystemCtrl.next()){
            return;
        }
        if(ConstDefine.VisionCMD.DETECT_FACE.equals(cmd.getCmd())){
            Intent intent = new Intent(ConstDefine.IntentFilterString.BROADCAST_START_CONVERSATION);
            mActivity.sendBroadcast(intent);
            return;
        }
        mRobotCtrl.doAction(cmd.getRobotAction());
        mVoiceCtrl.response(cmd);
        mSystemCtrl.doAction(cmd.getSystemOperator());
    }

}
