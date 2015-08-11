package com.timyrobot.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.robot.R;
import com.example.robot.facedection.CameraSurfaceView;
import com.example.robot.facedection.FaceView;
import com.timyrobot.bean.ControllCommand;
import com.timyrobot.common.ConstDefine;
import com.timyrobot.controlsystem.ControlManager;
import com.timyrobot.listener.DataReceiver;
import com.timyrobot.listener.ParserResultReceiver;
import com.timyrobot.parsesystem.ParseManager;
import com.timyrobot.triggersystem.TriggerManager;

public class EmotionActivity extends Activity implements
        DataReceiver,ParserResultReceiver, View.OnTouchListener{

    public static final String TAG = EmotionActivity.class.getName();

    public final static int SEND_DATA = 0x10001;
    public final static int PARSE_DATA = 0x10002;
    public final static int CHANGE_EMOTION = 0x10003;

    private Handler mSendHandler;
    private Handler mParserResultHandler;

    private EmotionHandler mMainHandler;

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
        mImageView.setOnTouchListener(this);

        initManager();
        registerReceiver(mStartConversation, new IntentFilter(
                ConstDefine.IntentFilterString.BROADCAST_START_CONVERSATION));
        isFirstCreate = true;

    }


    private void initManager(){

        HandlerThread sendThread = new HandlerThread(EmotionActivity.class.getName()+"sendThread");
        sendThread.start();
        mSendHandler = new Handler(sendThread.getLooper(),mSendCB);
        HandlerThread resultThread = new HandlerThread(EmotionActivity.class.getName()+"resultThread");
        resultThread.start();
        mParserResultHandler = new Handler(resultThread.getLooper(), mSendCB);
        mTriggerManager = new TriggerManager(this, this);
        mTriggerManager.init((CameraSurfaceView)findViewById(R.id.camera_surfaceview),
                (FaceView)findViewById(R.id.face_view));
        mParseManager = new ParseManager(this, this);
        mMainHandler = new EmotionHandler(this);
        mCtrlManager = new ControlManager(this, mImageView, mMainHandler);
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTriggerManager.stop();
    }

    @Override
    protected void onDestroy() {
        if(mStartConversation != null){
            unregisterReceiver(mStartConversation);
        }
        super.onDestroy();
    }


    private Handler.Callback mSendCB = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case SEND_DATA:
                    String content = String.valueOf(msg.obj);
                    mParseManager.parse(content);
                    break;
                case PARSE_DATA:
                    ControllCommand cmd = (ControllCommand)msg.obj;
                    mCtrlManager.distribute(cmd);
                    mSendHandler.removeMessages(PARSE_DATA);
                    mParserResultHandler.removeMessages(SEND_DATA);
                    break;
            }
            return false;
        }
    };
    //start vonversation dialog.
    private BroadcastReceiver mStartConversation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mTriggerManager.startConversation();
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if(action==MotionEvent.ACTION_DOWN){
//            mCtrlManager.changeEmotion("laugh2")
            Intent intent = new Intent(ConstDefine.IntentFilterString.BROADCAST_START_CONVERSATION);
            sendBroadcast(intent);
        }
        return false;
    }

    @Override
    public void onReceive(String data) {
        //接收到这个信息，并通过HandlerThread来发送
        //主要是这样可以保证单线程执行
        Message msg = new Message();
        msg.what = SEND_DATA;
        msg.obj = data;
        mSendHandler.sendMessage(msg);
    }

    @Override
    public void parseResult(ControllCommand cmd) {
        //接收到这个信息，并通过HandlerThread来发送
        //主要是这样可以保证单线程执行
        Message msg = new Message();
        msg.what = PARSE_DATA;
        msg.obj = cmd;
        mParserResultHandler.sendMessage(msg);
    }

    private void changeEmotion(AnimationDrawable drawable){
        if(drawable == null){
            return;
        }
        mImageView.setBackground(drawable);
        drawable.start();
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
