package com.timyrobot.controlsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.timyrobot.bean.ControllCommand;
import com.timyrobot.common.ConstDefine;
import com.timyrobot.listener.EndListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangtingting on 15/8/7.
 */
public class ControlManager implements EndListener{
    private final static String TAG = "ControlManager";
    Activity mActivity;

    private EmotionControl mEmotionCtrl;
    private VoiceControl mVoiceCtrl;
    private RobotControl mRobotCtrl;
    private SystemControl mSystemCtrl;

    ScheduledExecutorService mService = Executors.newScheduledThreadPool(1);
    ScheduledFuture mCurrentFuture;

    public ControlManager(Activity activity,Handler handler){
        mActivity = activity;
        mEmotionCtrl = new EmotionControl(mActivity,EmotionControl.EmotionType.DEFAULT,handler,this);
        mVoiceCtrl = new VoiceControl(mActivity,this);
        mRobotCtrl = new RobotControl(mActivity,this);
        mSystemCtrl = new SystemControl(mActivity);
        mEmotionCtrl.changeEmotion("blink",false);
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

        if(ConstDefine.TouchCMD.DETECT_TOUCH.equals(cmd.getCmd())){
            mEmotionCtrl.randomChangeEmotion();
            return;
        }
        if(cmd.getEmotionName() != null)
            mEmotionCtrl.changeEmotion(cmd.getEmotionName(),cmd.isNeedEnd());

        if(cmd.getRobotAction() != null)
            mRobotCtrl.doAction(cmd.getRobotAction(),cmd.isNeedEnd());

        if(cmd.getVoiceContent() != null)
            mVoiceCtrl.response(cmd, cmd.isNeedEnd());

        if(cmd.getSystemOperator() != null)
            mSystemCtrl.doAction(cmd.getSystemOperator());
    }

    public synchronized boolean isEnd(){
        if(mVoiceCtrl.next() && mRobotCtrl.next() && mSystemCtrl.next()){
            return true;
        }
        return false;
    }

    public void startIdle(){
        if((mCurrentFuture != null) && (!mCurrentFuture.isCancelled())){
            mCurrentFuture.cancel(true);
        }
       mCurrentFuture = mService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                mActivity.sendBroadcast(new Intent(ConstDefine.IntentFilterString.BROADCAST_IDLE_CONVERSATION));
            }
        }, 0, 40, TimeUnit.SECONDS);
    }

    public void endIdle(){
        if((mCurrentFuture != null) && (!mCurrentFuture.isCancelled())){
            mCurrentFuture.cancel(true);
        }
        mCurrentFuture = null;
    }

    @Override
    public void onEnd() {
        if(isEnd() && ((mCurrentFuture == null) || (!mCurrentFuture.isCancelled()))){
            endIdle();
            startIdle();
        }
    }
}
