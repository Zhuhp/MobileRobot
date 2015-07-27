package com.timyrobot.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.robot.R;
import com.timyrobot.ui.present.IBluetoothPresent;
import com.timyrobot.ui.present.iml.BluetoothPresent;
import com.timyrobot.ui.view.IEmotionView;

import app.akexorcist.bluetotohspp.library.BluetoothState;

/**
 * Created by zhangtingting on 15/7/28.
 */
public class BluetoothTestActivity extends Activity implements IEmotionView,
        View.OnClickListener{

    private IBluetoothPresent mBluePresent;

    private int key = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);
        mBluePresent = new BluetoothPresent();
        if(!mBluePresent.initBluetoothService(this)){
            finish();
        }
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_find_blue:
                mBluePresent.findBlue(this);
                break;
            case R.id.btn_send_data:
                mBluePresent.sendData("hello"+key);
                key+=13;
                break;
        }
    }

    private void initView(){
        findViewById(R.id.btn_find_blue).setOnClickListener(this);
        findViewById(R.id.btn_send_data).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBluePresent.startBluetoothService(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluePresent.stopBluetoothService();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case BluetoothState.REQUEST_CONNECT_DEVICE:
                if(resultCode == RESULT_OK) {
                    mBluePresent.resolveBlueResult(data);
                }
                break;
            case BluetoothState.REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){
                    mBluePresent.enableBlue();
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
