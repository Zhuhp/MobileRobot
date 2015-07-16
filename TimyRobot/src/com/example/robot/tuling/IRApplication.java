package com.example.robot.tuling;

import android.app.Application;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by Administrator on 2015/6/20.
 */
public class IRApplication extends Application{
	
	private WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();

	public WindowManager.LayoutParams getWindowParams() {
		return windowParams;
	}

    @Override
    public void onCreate() {
        super.onCreate();
        SpeechUtility.createUtility(this, SpeechConstant.APPID+"=5584c37d");
    }


}
