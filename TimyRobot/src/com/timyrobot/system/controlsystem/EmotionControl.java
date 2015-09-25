package com.timyrobot.system.controlsystem;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.timyrobot.system.controlsystem.listener.EndListener;
import com.timyrobot.robot.bean.RobotFace;
import com.timyrobot.robot.bean.RobotSubFace;
import com.timyrobot.robot.data.RobotData;
import com.timyrobot.service.emotion.parser.EmotionParserFactory;
import com.timyrobot.service.emotion.parser.IEmotionParser;
import com.timyrobot.service.emotion.provider.EmotionProviderFactory;
import com.timyrobot.service.emotion.provider.IEmotionProvider;
import com.timyrobot.ui.activity.EmotionActivity;

import java.util.ArrayList;

/**
 * Created by zhangtingting on 15/8/6.
 */
public class EmotionControl {

    public final static int CMD_FACE = 0x1001;
    public final static int CMD_FACE_END = 0x1002;

    public enum EmotionType{
        DEFAULT,CUSTOM
    }

    private final static String TAG = EmotionControl.class.getName();
    private Context mContext;
    private IEmotionProvider mProvider;
    private IEmotionParser mParser;

    private AnimationDrawable mDrawable;
    private Handler mHandler;
    private Handler mFaceHandler;

    private boolean isNext = true;

    private EndListener mEndListener;

    public boolean next(){
        return isNext;
    }

    public EmotionControl(Context context, EmotionType type, Handler handler,EndListener listener){
        mContext = context;
        mProvider = EmotionProviderFactory.getEmotionProvider(type);
        mParser = EmotionParserFactory.getEmotionParser(type);
        mHandler = handler;
        HandlerThread thread = new HandlerThread("RobotControl-face");
        thread.start();
        mFaceHandler = new Handler(thread.getLooper(), mCmdCB);
        mEndListener = listener;
    }

    public void randomChangeEmotion(){
        changeEmotion(RobotData.INSTANCE.getRandomFace());
    }

    public void changeEmotion(String name){
        isNext = false;
        RobotFace result = mParser.parseEmotion(name);
        if(result == null){
            isNext = true;
            return;
        }
        //获取动作流
        ArrayList<RobotSubFace> mDatas = result.getActions();
        if((mDatas == null) || (mDatas.isEmpty())){
            isNext = true;
            return;
        }
        //间隔时间
        long totalTime = 0L;
        for(RobotSubFace subAction:mDatas){
            Message msg = new Message();
            msg.obj = subAction.getFaceName();
            msg.what = CMD_FACE;
            //发送动作，动作延时totalTime
            mFaceHandler.sendMessageDelayed(msg,totalTime);
            totalTime += subAction.getTime();
        }
        mFaceHandler.sendEmptyMessageDelayed(CMD_FACE_END, totalTime);

    }
    Handler.Callback mCmdCB = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case CMD_FACE:
                    //通过蓝牙发送命令
                    if((mDrawable != null) && (mDrawable.isRunning())){
                        mDrawable.stop();
                    }
                    mDrawable = mProvider.provideEmotionAnimation(String.valueOf(msg.obj));
                    Message msgFace = new Message();
                    msgFace.what = EmotionActivity.CHANGE_EMOTION;
                    msgFace.obj = mDrawable;
                    mHandler.sendMessage(msgFace);
                    break;
                case CMD_FACE_END:
                    //动作结束，可以开始下一个动作
                    if((mDrawable != null) && (mDrawable.isRunning())){
                        mDrawable.stop();
                    }
                    mDrawable = mProvider.provideEmotionAnimation("blink");
                    Message msgEndFace = new Message();
                    msgEndFace.what = EmotionActivity.CHANGE_EMOTION;
                    msgEndFace.obj = mDrawable;
                    mHandler.sendMessage(msgEndFace);
                    isNext = true;
                    mEndListener.onEnd();
                    break;
            }
            return false;
        }
    };
}
