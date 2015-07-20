package com.timyrobot.service.emotion.provider;

import android.graphics.drawable.AnimationDrawable;

/**
 * Created by zhangtingting on 15/7/19.
 */
public interface IEmotionProvider {
    AnimationDrawable provideEmotionAnimation(String name);
}
