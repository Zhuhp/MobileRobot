package com.timyrobot.parsesystem;

import android.content.Context;
import android.text.TextUtils;

import com.timyrobot.common.ConstDefine;
import com.timyrobot.listener.ParserResultReceiver;
import com.timyrobot.service.bluetooth.IDataReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/8/7.
 */
public class ParseManager {

    private Context mCtx;

    private VisionMsgParser visionMsgParser;
    private VoiceMsgParser voiceMsgParser;

    public ParseManager(Context context,ParserResultReceiver receiver){
        mCtx = context;
        visionMsgParser = new VisionMsgParser(receiver);
        voiceMsgParser = new VoiceMsgParser(mCtx,receiver);
    }

    public void parse(String content){
        if(TextUtils.isEmpty(content)){
            return;
        }
        try {
            JSONObject object = new JSONObject(content);
            String data = object.optString(ConstDefine.TriggerDataKey.CONTENT);
            String type = object.optString(ConstDefine.TriggerDataKey.TYPE);

            if(ConstDefine.TriggerDataType.Voice.equals(type)){
                //语音信息
                voiceMsgParser.parse(data);
            }else if(ConstDefine.TriggerDataType.Vision.equals(type)){
                //视觉信息
                visionMsgParser.parse(data);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
