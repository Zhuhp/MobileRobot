package com.timyrobot.ui.present;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

/**
 * Created by zhangtingting on 15/7/18.
 */
public interface IEmotionPresent {

    //语音，智能对话
    void initTalk(Activity context);
    void startTalk();

    //表情操作
    void initEmotionManager(ImageView iv,Context context);


}
