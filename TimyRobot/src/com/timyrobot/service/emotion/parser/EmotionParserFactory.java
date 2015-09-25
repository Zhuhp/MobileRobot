package com.timyrobot.service.emotion.parser;

import android.content.Context;

import com.timyrobot.system.controlsystem.EmotionControl;
import com.timyrobot.service.emotion.parser.impl.DefaultEmotionParser;

/**
 * Created by zhangtingting on 15/7/20.
 */
public class EmotionParserFactory {

    private static Context mCtx;

    public static void init(Context ctx){
        mCtx = ctx;
    }

    public static IEmotionParser getEmotionParser(EmotionControl.EmotionType type){
        switch (type){
            case DEFAULT:
                return new DefaultEmotionParser(mCtx);
            default:
                return new DefaultEmotionParser(mCtx);
        }
    }
}
