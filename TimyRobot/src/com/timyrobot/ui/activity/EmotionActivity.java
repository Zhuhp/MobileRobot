package com.timyrobot.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.robot.R;
import com.example.robot.facedection.CameraSurfaceView;
import com.example.robot.facedection.FaceView;
import com.example.robot.view.FloatViewService;
import com.timyrobot.bean.ControllCommand;
import com.timyrobot.common.ConstDefine;
import com.timyrobot.controlsystem.ControlManager;
import com.timyrobot.controlsystem.EmotionControl;
import com.timyrobot.controlsystem.IControlListener;
import com.timyrobot.httpcom.filedownload.FileDownload;
import com.timyrobot.listener.DataReceiver;
import com.timyrobot.listener.EndListener;
import com.timyrobot.listener.ParserResultReceiver;
import com.timyrobot.parsesystem.ParseManager;
import com.timyrobot.triggersystem.TriggerManager;

import org.json.JSONException;
import org.json.JSONObject;

public class EmotionActivity extends Activity implements
        View.OnClickListener, View.OnLongClickListener, IControlListener{

    public static final String TAG = EmotionActivity.class.getName();

    public final static int CHANGE_EMOTION = 0x10003;

    private EmotionHandler mMainHandler;
    private EmotionControl mEmotionControl;
    //点击屏幕时，向TriggerManager传递事件
    private DataReceiver mDataReceiver;

    private TriggerManager mTriggerManager;
    private ParseManager mParseManager;
    private ControlManager mCtrlManager;

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
    }

    private void initManager(){
        mTriggerManager = new TriggerManager(this);
        mDataReceiver = mTriggerManager;
        mTriggerManager.init((CameraSurfaceView) findViewById(R.id.camera_surfaceview),
                (FaceView) findViewById(R.id.face_view));
        mParseManager = new ParseManager(this);
        mTriggerManager.setParseManager(mParseManager);
        mParseManager.setTriggerManager(mTriggerManager);
        mMainHandler = new EmotionHandler(this);
        mCtrlManager = new ControlManager(this);
        mParseManager.setControlManager(mCtrlManager);
        mCtrlManager.addControlListener(this);
        setEndListener(mCtrlManager);
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
        Intent i = new Intent(getApplicationContext(), FloatViewService.class);
        startService(i);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTriggerManager.stop();
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
        JSONObject object = new JSONObject();
        try {
            object.put(ConstDefine.TriggerDataKey.TYPE, ConstDefine.TriggerDataType.TriggerAnotherCmd);
            object.put(ConstDefine.TriggerDataKey.CONTENT, ConstDefine.TouchCMD.DETECT_TOUCH);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mDataReceiver.onReceive(object.toString());
    }

    @Override
    public boolean onLongClick(View v) {
        mTriggerManager.startTouch();
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
    public void distributeCMD(ControllCommand cmd) {
        if(cmd == null){
            return;
        }
        if(ConstDefine.TouchCMD.DETECT_TOUCH.equals(cmd.getCmd())){
            mEmotionControl.randomChangeEmotion();
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
