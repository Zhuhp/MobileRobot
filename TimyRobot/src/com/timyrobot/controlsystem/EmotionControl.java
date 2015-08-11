package com.timyrobot.controlsystem;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;

import com.timyrobot.service.emotion.parser.EmotionParserFactory;
import com.timyrobot.service.emotion.parser.IEmotionParser;
import com.timyrobot.service.emotion.provider.EmotionProviderFactory;
import com.timyrobot.service.emotion.provider.IEmotionProvider;
import com.timyrobot.ui.activity.EmotionActivity;

/**
 * Created by zhangtingting on 15/8/6.
 */
public class EmotionControl {

    public enum EmotionType{
        DEFAULT,CUSTOM
    }

    private final static String TAG = EmotionControl.class.getName();
    private Context mContext;
    private IEmotionProvider mProvider;
    private IEmotionParser mParser;

    private AnimationDrawable mDrawable;
    private Handler mHandler;

    public EmotionControl(Context context, EmotionType type, Handler handler){
        mContext = context;
        mProvider = EmotionProviderFactory.getEmotionProvider(type);
        mParser = EmotionParserFactory.getEmotionParser(type);
        mHandler = handler;
    }

    public void changeEmotion(String name){
        if((mDrawable != null) && (mDrawable.isRunning())){
            mDrawable.stop();
        }
        String result = mParser.parseEmotion(name);
        mDrawable = mProvider.provideEmotionAnimation(result);
        if(mDrawable != null) {
            Message msg = new Message();
            msg.obj = mDrawable;
            msg.what = EmotionActivity.CHANGE_EMOTION;
            mHandler.sendMessage(msg);
        }
    }
}
