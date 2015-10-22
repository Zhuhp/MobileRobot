package com.timyrobot.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.robot.R;
import com.example.robot.facedection.CameraSurfaceView;
import com.example.robot.facedection.FaceView;
import com.example.robot.view.FloatView;
import com.example.robot.view.FloatViewService;
import com.timyrobot.system.bean.BaseCommand;
import com.timyrobot.system.bean.ChangeEmotionCommand;
import com.timyrobot.system.bean.NeedVoiceReconCommand;
import com.timyrobot.system.controlsystem.ControlManager;
import com.timyrobot.system.controlsystem.EmotionControl;
import com.timyrobot.system.controlsystem.IControlListener;
import com.timyrobot.system.controlsystem.listener.EndListener;
import com.timyrobot.system.filler.EngilshVoiceFiller;
import com.timyrobot.system.filler.IFiller;
import com.timyrobot.system.triggersystem.TriggerManager;
import com.timyrobot.system.triggersystem.listener.DataReceiver;

public class EmotionActivity extends Activity implements
        View.OnClickListener, View.OnLongClickListener, IControlListener{

    public static final String TAG = EmotionActivity.class.getName();

    public final static int CHANGE_EMOTION = 0x10003;

    private EmotionHandler mMainHandler;
    private EmotionControl mEmotionControl;
    //点击屏幕时，向TriggerManager传递事件
    private DataReceiver mDataReceiver;

    private TriggerManager mTriggerManager;
    private ControlManager mCtrlManager;

    private IFiller mFiller1;
    private IFiller mFiller2;

    private boolean isFirstCreate = false;

    private ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mImageView = (ImageView)findViewById(R.id.iv_emotion);
        mImageView.setOnClickListener(this);
        mImageView.setOnLongClickListener(this);

        initManager();
        initEmotion();
        isFirstCreate = true;

//        Intent i = new Intent(this, FloatViewService.class);
//        startService(i);
    }

    private void initManager(){

        //控制模块
        mMainHandler = new EmotionHandler(this);
        mCtrlManager = new ControlManager(this);
        mCtrlManager.addControlListener(this);
        setEndListener(mCtrlManager);

//        mFiller1 = new UnderstandTextFiller(this, mCtrlManager);
//        mFiller2 = new VoiceFiller(this, mFiller1);
        mFiller2 = new EngilshVoiceFiller(this, mCtrlManager);

        mTriggerManager = new TriggerManager(this, mFiller2);
        mDataReceiver = mTriggerManager;
        mTriggerManager.init((CameraSurfaceView) findViewById(R.id.camera_surfaceview),
                (FaceView) findViewById(R.id.face_view));
    }

    private void initEmotion(){
        AnimationDrawable drawable = (AnimationDrawable)getResources().getDrawable(R.anim.anim_blink);
        mImageView.setBackground(drawable);
        drawable.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!isFirstCreate) {
            mTriggerManager.start();
        }
//        FloatViewService.sendBroadCast(this, false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = new Intent(this, FloatViewService.class);
        stopService(i);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Intent i = new Intent(getApplicationContext(), FloatViewService.class);
//        startService(i);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTriggerManager.stop();
//        FloatViewService.sendBroadCast(this, true);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Intent i = new Intent(getApplicationContext(), FloatViewService.class);
//        stopService(i);
    }

    private void changeEmotion(AnimationDrawable drawable){
        if(drawable == null){
            return;
        }
        mImageView.setBackground(drawable);
        drawable.start();
    }

    @Override
    public void onClick(View v) {
        mDataReceiver.onReceive(new NeedVoiceReconCommand());
    }

    @Override
    public boolean onLongClick(View v) {
        mDataReceiver.onReceive(new ChangeEmotionCommand());
        return true;
    }

    @Override
    public boolean next() {
        if(mEmotionControl == null){
            return true;
        }
        return mEmotionControl.next();
    }

    @Override
    public void distributeCMD(BaseCommand cmd) {
        if(cmd == null){
            return;
        }
        mEmotionControl.changeEmotion(cmd.getEmotionName());
    }

    @Override
    public void setEndListener(EndListener listener) {
        mEmotionControl = new EmotionControl(this, EmotionControl.EmotionType.DEFAULT,
                mMainHandler, listener);
    }

    /**
     *
     */
    private static class EmotionHandler extends Handler{

        EmotionActivity mActivity;

        public EmotionHandler(EmotionActivity activity){
            mActivity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CHANGE_EMOTION:
                    AnimationDrawable drawable = (AnimationDrawable)msg.obj;
                    mActivity.changeEmotion(drawable);
                    break;
            }
        }
    }
}
