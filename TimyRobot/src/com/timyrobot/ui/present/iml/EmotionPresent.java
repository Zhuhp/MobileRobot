package com.timyrobot.ui.present.iml;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.robot.R;
import com.timyrobot.service.bluetooth.BluetoothManager;
import com.timyrobot.ui.present.IEmotionPresent;
import com.timyrobot.ui.view.IEmotionView;

import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

/**
 * Created by zhangtingting on 15/7/18.
 */
public class EmotionPresent implements IEmotionPresent{

    private IEmotionView mView;

    public EmotionPresent(IEmotionView view){
        mView = view;
    }

    @Override
    public boolean initBluetoothService(Context context) {
        if(BluetoothManager.INSTANCE.init(context)){
            return true;
        }else{
            Toast.makeText(context, R.string.blue_not_avaliable,Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void startBluetoothService(Activity context) {
        BluetoothManager.INSTANCE.startBlueService(context);
    }

    @Override
    public void sendData(String cmd) {
        BluetoothManager.INSTANCE.sendData(cmd);
    }

    @Override
    public void stopBluetoothService() {
        BluetoothManager.INSTANCE.stopBlueService();
    }


    @Override
    public void findBlue(Activity context) {
        Intent intent = new Intent(context, DeviceList.class);
        context.startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
    }

    @Override
    public void resolveBlueResult(Intent intent) {
        BluetoothManager.INSTANCE.connect(intent);
    }

    @Override
    public void enableBlue() {
        BluetoothManager.INSTANCE.enableBlue();
    }


}
