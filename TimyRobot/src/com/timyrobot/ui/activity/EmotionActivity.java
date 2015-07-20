package com.timyrobot.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.robot.R;
import com.timyrobot.service.emotion.EmotionManager;
import com.timyrobot.ui.present.IEmotionPresent;
import com.timyrobot.ui.present.iml.EmotionPresent;
import com.timyrobot.ui.view.IEmotionView;

import app.akexorcist.bluetotohspp.library.BluetoothState;

public class EmotionActivity extends Activity implements IEmotionView,View.OnClickListener{

    private IEmotionPresent mPresent;
    private EmotionManager mEmotionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);
        mPresent = new EmotionPresent(this);
        if(!mPresent.initBluetoothService(this)){
            finish();
        }
        mPresent.initEmotionManager((ImageView)findViewById(R.id.iv_emotion),this);
        initView();
    }

    private void initView(){
        findViewById(R.id.btn_find_blue).setOnClickListener(this);
        findViewById(R.id.btn_send_data).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresent.startBluetoothService(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresent.stopBluetoothService();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_find_blue:
                mPresent.findBlue(this);
                break;
            case R.id.btn_send_data:
                mPresent.sendData("hello");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case BluetoothState.REQUEST_CONNECT_DEVICE:
                if(resultCode == RESULT_OK) {
                    mPresent.resolveBlueResult(data);
                }
                break;
            case BluetoothState.REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){
                    mPresent.enableBlue();
                }else{
                    Toast.makeText(this
                            , R.string.blue_not_enable
                            , Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }
}
