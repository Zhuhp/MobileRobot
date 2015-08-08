package com.timyrobot.controlsystem;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import com.timyrobot.service.emotion.parser.EmotionParserFactory;
import com.timyrobot.service.emotion.parser.IEmotionParser;
import com.timyrobot.service.emotion.provider.EmotionProviderFactory;
import com.timyrobot.service.emotion.provider.IEmotionProvider;

/**
 * Created by zhangtingting on 15/8/6.
 */
public class EmotionControl {

    public enum EmotionType{
        DEFAULT,CUSTOM
    }

    private Context mContext;
    private ImageView mImageView;
    private IEmotionProvider mProvider;
    private IEmotionParser mParser;

    private AnimationDrawable mDrawable;

    public EmotionControl(Context context, ImageView imageView,
                          EmotionType type){
        mContext = context;
        mImageView = imageView;
        mProvider = EmotionProviderFactory.getEmotionProvider(type);
        mParser = EmotionParserFactory.getEmotionParser(type);
    }

    public void changeEmotion(String name){
        if((mDrawable != null) && (mDrawable.isRunning())){
            mDrawable.stop();
        }
        String result = mParser.parseEmotion(name);
        mDrawable = mProvider.provideEmotionAnimation(result);
        if(mDrawable != null) {
            mImageView.setBackground(mDrawable);
            mDrawable.start();
        }
    }
}
