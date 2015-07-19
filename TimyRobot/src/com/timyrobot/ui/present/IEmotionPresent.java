package com.timyrobot.ui.present;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by zhangtingting on 15/7/18.
 */
public interface IEmotionPresent {
    boolean initBluetoothService(Context context);
    void startBluetoothService(Activity context);
    void sendData(String cmd);
    void stopBluetoothService();
    void findBlue(Activity context);
    void resolveBlueResult(Intent intent);
    void enableBlue();
}
