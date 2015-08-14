package com.timyrobot.triggersystem;

import android.content.Context;

import com.iflytek.cloud.ui.RecognizerDialog;
import com.timyrobot.common.ConstDefine;
import com.timyrobot.listener.DataReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/8/14.
 */
public enum TouchTrigger {
    INSTANCE;

    private DataReceiver mReceiver;
    public void init(DataReceiver receiver){
        mReceiver = receiver;
    }

    public void phoneTouch(){
        try {
            JSONObject data = new JSONObject();
            data.put(ConstDefine.TriggerDataKey.TYPE, ConstDefine.TriggerDataType.Touch);
            data.put(ConstDefine.TriggerDataKey.CONTENT, ConstDefine.TouchCMD.DETECT_TOUCH);
            if(mReceiver != null){
                mReceiver.onReceive(data.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
