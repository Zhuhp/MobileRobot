package com.timyrobot.controlsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.widget.ImageView;

import com.timyrobot.bean.ControllCommand;
import com.timyrobot.common.ConstDefine;

/**
 * Created by zhangtingting on 15/8/7.
 */
public class ControlManager {
    private final static String TAG = "ControlManager";
    Activity mActivity;

    private EmotionControl mEmotionCtrl;
    private VoiceControl mVoiceCtrl;
    private RobotControl mRobotCtrl;
    private SystemControl mSystemCtrl;

    public ControlManager(Activity activity,ImageView imageView, Handler handler){
        mActivity = activity;
        mEmotionCtrl = new EmotionControl(mActivity,EmotionControl.EmotionType.DEFAULT,handler);
        mVoiceCtrl = new VoiceControl(mActivity);
        mRobotCtrl = new RobotControl(mActivity);
        mSystemCtrl = new SystemControl(mActivity);
        mEmotionCtrl.changeEmotion("blink");
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
        if(cmd.getRobotAction()!=null)
            mRobotCtrl.doAction(cmd.getRobotAction());

        if(cmd.getVoiceContent()!=null)
            mVoiceCtrl.response(cmd);

        if(cmd.getSystemOperator()!=null)
            mSystemCtrl.doAction(cmd.getSystemOperator());

        if(cmd.getEmotionName()!=null)
            mEmotionCtrl.changeEmotion(cmd.getEmotionName());
    }

    public void changeEmotion(String name){
        mEmotionCtrl.changeEmotion(name);
    }

}
