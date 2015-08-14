package com.timyrobot.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.robot.R;
import com.timyrobot.service.bluetooth.IBlueConnectListener;
import com.timyrobot.ui.present.IBluetoothPresent;
import com.timyrobot.ui.present.iml.BluetoothPresent;

import app.akexorcist.bluetotohspp.library.BluetoothState;

/**
 * Created by zhangtingting on 15/8/1.
 */
public class InitActivity extends Activity implements View.OnClickListener{

    private ImageButton mBlueButton;
    IBluetoothPresent mBluePresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        mBluePresent = new BluetoothPresent();
        if(!mBluePresent.initBluetoothService(getApplicationContext())){
            finish();
        }
        mBluePresent.setConnectListener(new IBlueConnectListener() {
            @Override
            public void connect(String name, String address) {
                Intent intent = new Intent(InitActivity.this,EmotionActivity.class);
                startActivity(intent);
            }

            @Override
            public void connectFailed() {

            }

            @Override
            public void disconnect() {

            }

            @Override
            public void recvMsg(String data) {

            }
        });
        initView();
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

    private void initView(){
        mBlueButton = (ImageButton)findViewById(R.id.btn_find_blue);
        mBlueButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_find_blue:
                mBluePresent.findBlue(this);
                break;
        }
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
