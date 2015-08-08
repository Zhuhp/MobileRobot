package com.timyrobot.service.emotion.provider;

import android.content.Context;

import com.timyrobot.controlsystem.EmotionControl;
import com.timyrobot.service.emotion.provider.impl.DefaultEmotionProvider;

/**
 * Created by zhangtingting on 15/7/19.
 */
public class EmotionProviderFactory {

    private static Context mCtx;

    public static void init(Context ctx){
        mCtx = ctx;
    }

    public static IEmotionProvider getEmotionProvider(EmotionControl.EmotionType type){
        switch(type){
            case DEFAULT:
                return new DefaultEmotionProvider(mCtx);
            default:
                return new DefaultEmotionProvider(mCtx);
        }

    }
}
