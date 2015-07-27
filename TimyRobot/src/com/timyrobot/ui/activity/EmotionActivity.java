package com.timyrobot.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.robot.R;
import com.example.robot.facedection.CameraSurfaceView;
import com.example.robot.facedection.FaceView;
import com.timyrobot.listener.FaceDetectListener;
import com.timyrobot.ui.present.IBluetoothPresent;
import com.timyrobot.ui.present.IEmotionPresent;
import com.timyrobot.ui.present.IFaceDectectPresent;
import com.timyrobot.ui.present.iml.BluetoothPresent;
import com.timyrobot.ui.present.iml.EmotionPresent;
import com.timyrobot.ui.present.iml.FaceDectectPresent;
import com.timyrobot.ui.view.IEmotionView;

import app.akexorcist.bluetotohspp.library.BluetoothState;

public class EmotionActivity extends Activity implements IEmotionView,
        View.OnClickListener,FaceDetectListener{

    private IEmotionPresent mPresent;
    private IBluetoothPresent mBluePresent;
    private IFaceDectectPresent mFacePresent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);
        mPresent = new EmotionPresent(this);
        mBluePresent = new BluetoothPresent();
        mFacePresent = new FaceDectectPresent(this,this);
//        if(!mBluePresent.initBluetoothService(this)){
//            finish();
//        }
        mPresent.initEmotionManager((ImageView) findViewById(R.id.iv_emotion), this);
        mPresent.initTalk(this);

        mFacePresent.initFaceDectect((CameraSurfaceView) findViewById(R.id.camera_surfaceview),
                (FaceView) findViewById(R.id.face_view));
        initView();
    }

    private void initView(){
        findViewById(R.id.btn_find_blue).setOnClickListener(this);
        findViewById(R.id.btn_send_data).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFacePresent.startDetect();
//        mBluePresent.startBluetoothService(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mBluePresent.stopBluetoothService();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_find_blue:
//                mBluePresent.findBlue(this);
                break;
            case R.id.btn_send_data:
//                mBluePresent.sendData("hello");
//                mPresent.startTalk();
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

    @Override
    public void onFaceDetect() {
        mPresent.startTalk();
    }
}
