package com.timyrobot.ui.present.iml;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.robot.R;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.timyrobot.service.bluetooth.BluetoothManager;
import com.timyrobot.service.emotion.provider.EmotionProviderFactory;
import com.timyrobot.service.emotion.EmotionManager;
import com.timyrobot.service.speechrecongnizer.SpeechJsonParser;
import com.timyrobot.service.speechrecongnizer.SpeechManager;
import com.timyrobot.ui.present.IEmotionPresent;
import com.timyrobot.ui.view.IEmotionView;
import com.timyrobot.utils.ToastUtils;

import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

/**
 * Created by zhangtingting on 15/7/18.
 */
public class EmotionPresent implements IEmotionPresent {

    private IEmotionView mView;
    private EmotionManager mEmotionManager;
    private SpeechManager mSpeechManager;

    public EmotionPresent(IEmotionView view){
        mView = view;
    }

    @Override
    public void initTalk(Context context) {
        mSpeechManager = new SpeechManager(context);
    }

    @Override
    public void initEmotionManager(ImageView iv,Context ctx){
        mEmotionManager = new EmotionManager(ctx, iv, EmotionManager.EmotionType.DEFAULT);
        mEmotionManager.changeEmotion("blink");
    }

    @Override
    public boolean initBluetoothService(Context context) {
        if(BluetoothManager.INSTANCE.init(context)){
            return true;
        }else{
            ToastUtils.toastShort(context,R.string.blue_not_avaliable);
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
