package com.timyrobot.ui.activity.register;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.robot.R;

/**
 * Created by zhangtingting on 15/9/27.
 */
public class RegisterActivity extends Activity implements View.OnClickListener{

    private EditText mUserNameET;
    private EditText mPwdET;
    private Button mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUserNameET = (EditText)findViewById(R.id.et_login_username);
        mPwdET = (EditText)findViewById(R.id.et_login_pwd);
        findViewById(R.id.btn_login_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_login:
                break;
        }
    }
}
