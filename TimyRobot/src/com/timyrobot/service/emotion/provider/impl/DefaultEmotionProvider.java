package com.timyrobot.service.emotion.provider.impl;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;

import com.timyrobot.service.emotion.provider.IEmotionProvider;

/**
 * Created by zhangtingting on 15/7/19.
 */
public class DefaultEmotionProvider implements IEmotionProvider {

    private Context mContext;

    public DefaultEmotionProvider(Context context){
        mContext = context;
    }

    @Override
    public AnimationDrawable provideEmotionAnimation(String name) {
        Resources res = mContext.getResources();
        int id = res.getIdentifier("anim_"+name,"anim",
                mContext.getPackageName());
        return (AnimationDrawable)mContext.getResources().getDrawable(id);
    }
}
