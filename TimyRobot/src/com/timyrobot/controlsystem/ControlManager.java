package com.timyrobot.controlsystem;

import android.app.Activity;

import com.timyrobot.bean.BaseCommand;
import com.timyrobot.filler.IFiller;
import com.timyrobot.listener.EndListener;
import com.timyrobot.triggersystem.TriggerSwitch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by zhangtingting on 15/8/7.
 */
public class ControlManager implements EndListener, IFiller{
    private final static String TAG = "ControlManager";
    Activity mActivity;

    private List<IControlListener> mListenerList;

    private VoiceControl mVoiceCtrl;
    private RobotControl mRobotCtrl;
    private SystemControl mSystemCtrl;

    public ControlManager(Activity activity){
        mActivity = activity;
        mListenerList = new ArrayList<>();
        mVoiceCtrl = new VoiceControl(mActivity);
        mVoiceCtrl.setEndListener(this);
        mRobotCtrl = new RobotControl(mActivity);
        mRobotCtrl.setEndListener(this);
        mSystemCtrl = new SystemControl(mActivity);
        mSystemCtrl.setEndListener(this);

        addControlListener(mVoiceCtrl);
        addControlListener(mRobotCtrl);
        addControlListener(mSystemCtrl);
    }

    public void addControlListener(IControlListener listener){
        mListenerList.add(listener);
    }

    @Override
    public void fill(BaseCommand cmd) {
        distribute(cmd);
    }

    private void distribute(BaseCommand cmd){
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

        for(IControlListener listener : mListenerList){
            listener.distributeCMD(cmd);
        }
    }

    public synchronized boolean isEnd(){
        boolean end = true;
        for(IControlListener listener : mListenerList){
            if(!listener.next()){
                end = false;
                break;
            }
        }
        return end;
    }

//    public void startIdle(){
//        if((mCurrentFuture != null) && (!mCurrentFuture.isCancelled())){
//            mCurrentFuture.cancel(true);
//        }
//       mCurrentFuture = mService.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                mActivity.sendBroadcast(new Intent(ConstDefine.IntentFilterString.BROADCAST_IDLE_CONVERSATION));
//            }
//        }, 0, 40, TimeUnit.SECONDS);
//    }
//
//    public void endIdle(){
//        if((mCurrentFuture != null) && (!mCurrentFuture.isCancelled())){
//            mCurrentFuture.cancel(true);
//        }
//        mCurrentFuture = null;
//    }

    @Override
    public void onEnd() {
        if(isEnd()){
            TriggerSwitch.INSTANCE.setCanNext(true);
        }
    }

}
