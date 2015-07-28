package com.timyrobot.ui.present.iml;

import android.content.Context;
import android.widget.ImageView;

import com.timyrobot.service.emotion.EmotionManager;
import com.timyrobot.service.speechrecongnizer.SpeechManager;
import com.timyrobot.service.userintent.action.Action;
import com.timyrobot.ui.present.IEmotionPresent;
import com.timyrobot.ui.view.IEmotionView;
import com.timyrobot.utils.ToastUtils;

/**
 * Created by zhangtingting on 15/7/18.
 */
public class EmotionPresent implements IEmotionPresent, SpeechManager.ConversationListener{

    private IEmotionView mView;
    private EmotionManager mEmotionManager;
    private SpeechManager mSpeechManager;

    public EmotionPresent(IEmotionView view){
        mView = view;
    }

    @Override
    public void initTalk(Context context) {
        mSpeechManager = new SpeechManager(context,this);
    }

    @Override
    public void startTalk() {
        mSpeechManager.startConversation();
    }

    @Override
    public void initEmotionManager(ImageView iv,Context ctx){
        mEmotionManager = new EmotionManager(ctx, iv, EmotionManager.EmotionType.DEFAULT);
//        mEmotionManager.changeEmotion(EmotionResource.BLINK.getResName());
    }


    @Override
    public void onUserIntent(Action action) {
        mView.userAction(action);
    }

    @Override
    public void onUserTalk(String content) {

    }

    @Override
    public void onRobotTalk(String content) {

    }
}
