package com.timyrobot.ui.present.iml;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.timyrobot.service.userintent.intentparser.IUserIntentParser;
import com.timyrobot.ui.present.IEmotionPresent;
import com.timyrobot.ui.view.IEmotionView;

/**
 * Created by zhangtingting on 15/7/18.
 */
public class EmotionPresent implements IEmotionPresent{

    private IEmotionView mView;

    public EmotionPresent(IEmotionView view){
        mView = view;
    }

    @Override
    public void initTalk(Activity context) {
    }

    @Override
    public void startTalk() {
    }

    @Override
    public void initEmotionManager(ImageView iv,Context ctx){
//        mEmotionManager.changeEmotion(EmotionResource.BLINK.getResName());
    }



}
