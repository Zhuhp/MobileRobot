package com.timyrobot.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.robot.R;
import com.timyrobot.common.Property;
import com.timyrobot.listener.DataReceiver;
import com.timyrobot.triggersystem.HttpTrigger;

/**
 * Created by zhangtingting on 15/9/16.
 */
public class SendActivity extends Activity implements View.OnClickListener, DataReceiver{

    private EditText mContentET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);
        mContentET = (EditText)findViewById(R.id.et_content);
        findViewById(R.id.btn_send).setOnClickListener(this);
        HttpTrigger.INSTANCE.init(this, Property.ROBOT_CODE, Property.IS_RECEIVER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                HttpTrigger.INSTANCE.sendMsg(mContentET.getText().toString());
                break;
        }
    }

    @Override
    public void onReceive(final String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SendActivity.this, data, Toast.LENGTH_LONG).show();
            }
        });
    }
}
