package com.timyrobot.service.emotion.parser.impl;

import android.content.Context;

import com.timyrobot.service.emotion.parser.IEmotionParser;

/**
 * Created by zhangtingting on 15/7/20.
 */
public class DefaultEmotionParser implements IEmotionParser{

    private Context mCtx;

    public DefaultEmotionParser(Context ctx){
        mCtx = ctx;
    }

    @Override
    public String parseEmotion(String name) {
        return name;
    }
}
