package com.timyrobot.triggersystem;

import android.content.Context;

import com.iflytek.cloud.ui.RecognizerDialog;
import com.timyrobot.common.ConstDefine;
import com.timyrobot.listener.DataReceiver;
import com.timyrobot.listener.TouchDataReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/8/14.
 */
public enum TouchTrigger {
    INSTANCE;

    private TouchDataReceiver mTouchDataReceiver;
    public void init(TouchDataReceiver touchDataReceiver){
        mTouchDataReceiver = touchDataReceiver;
    }

    public void phoneTouch(){
        try {
            JSONObject data = new JSONObject();
            data.put(ConstDefine.TriggerDataKey.TYPE, ConstDefine.TriggerDataType.Touch);
            data.put(ConstDefine.TriggerDataKey.CONTENT, ConstDefine.TouchCMD.DETECT_TOUCH);
            if(mTouchDataReceiver != null){
                mTouchDataReceiver.onTouchDataReceive(data.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
