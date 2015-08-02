package com.timyrobot.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.robot.R;
import com.example.robot.facedection.CameraInterface;
import com.example.robot.facedection.CameraSurfaceView;
import com.example.robot.facedection.FaceView;
import com.timyrobot.listener.FaceDetectListener;
import com.timyrobot.service.bluetooth.BluetoothManager;
import com.timyrobot.service.userintent.actionparse.Action;
import com.timyrobot.service.userintent.intentparser.IUserIntentParser;
import com.timyrobot.ui.present.IBluetoothPresent;
import com.timyrobot.ui.present.IEmotionPresent;
import com.timyrobot.ui.present.IFaceDectectPresent;
import com.timyrobot.ui.present.iml.BluetoothPresent;
import com.timyrobot.ui.present.iml.EmotionPresent;
import com.timyrobot.ui.present.iml.FaceDectectPresent;
import com.timyrobot.ui.view.IEmotionView;
import com.timyrobot.utils.ToastUtils;

import app.akexorcist.bluetotohspp.library.BluetoothState;

public class EmotionActivity extends Activity implements IEmotionView,
        View.OnClickListener,FaceDetectListener{

    private IEmotionPresent mPresent;
    private IFaceDectectPresent mFacePresent;
    private boolean isFirstStart = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);
        mPresent = new EmotionPresent(this);
        mFacePresent = new FaceDectectPresent(getApplicationContext(),this);
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
        if(!isFirstStart) {
            mFacePresent.startDetect();
        }
        isFirstStart = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFacePresent.stopDetect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_find_blue:
                break;
            case R.id.btn_send_data:
                break;
        }
    }

    @Override
    public void onFaceDetect() {
        mPresent.startTalk();
    }

    @Override
    public void userAction(IUserIntentParser action) {
        if(action == null){
            return;
        }
        Action bean = action.getAction();
        ToastUtils.toastShort(this,bean.service+";"+bean.operation);
        BluetoothManager.INSTANCE.sendData(bean.toString());
    }
}
