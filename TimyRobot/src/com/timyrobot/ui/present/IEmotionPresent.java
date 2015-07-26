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
    void initTalk(Context context);
    void startTalk();

    //表情操作
    void initEmotionManager(ImageView iv,Context context);

    //蓝牙操作
    boolean initBluetoothService(Context context);
    void startBluetoothService(Activity context);
    void sendData(String cmd);
    void stopBluetoothService();
    void findBlue(Activity context);
    void resolveBlueResult(Intent intent);
    void enableBlue();
}
