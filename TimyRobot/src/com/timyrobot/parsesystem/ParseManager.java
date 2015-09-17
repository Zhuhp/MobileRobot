package com.timyrobot.parsesystem;

import android.content.Context;
import android.text.TextUtils;

import com.timyrobot.bean.ControllCommand;
import com.timyrobot.common.ConstDefine;
import com.timyrobot.controlsystem.ControlManager;
import com.timyrobot.listener.ParserResultReceiver;
import com.timyrobot.service.bluetooth.IDataReceiver;
import com.timyrobot.triggersystem.TriggerManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/8/7.
 */
public class ParseManager implements ParserResultReceiver{

    private Context mCtx;

    private VisionMsgParser visionMsgParser;
    private VoiceMsgParser voiceMsgParser;
    private TouchMsgParser touchMsgParser;

    private ControlManager mControlManager;
    private TriggerManager mTriggerManager;

    public ParseManager(Context context){
        mCtx = context;
        visionMsgParser = new VisionMsgParser(this);
        voiceMsgParser = new VoiceMsgParser(mCtx,this);
        touchMsgParser = new TouchMsgParser(this);
    }

    public void setControlManager(ControlManager manager){
        mControlManager = manager;
    }

    public void setTriggerManager(TriggerManager manager){
        mTriggerManager = manager;
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
            }else if(ConstDefine.TriggerDataType.Touch.equals(type)){
                touchMsgParser.parse(data);
            }else if(ConstDefine.TriggerDataType.TriggerAnotherCmd.equals(type)){
                triggerCMD(data);
            }else if(ConstDefine.TriggerDataType.Http.equals(type)){
                JSONObject httpdata = new JSONObject();
                httpdata.put(ConstDefine.TriggerDataKey.TYPE,ConstDefine.TriggerDataType.Voice);
                httpdata.put(ConstDefine.TriggerDataKey.CONTENT,data);
                parse(httpdata.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void triggerCMD(String data){
        if(mTriggerManager == null){
            return;
        }
        if(ConstDefine.VisionCMD.DETECT_FACE.equals(data)){
            mTriggerManager.startConversation();
        }
        if(ConstDefine.TouchCMD.DETECT_TOUCH.equals(data)){
            mTriggerManager.startTouch();
        }
    }

    private void distributeCMD(ControllCommand cmd){
        if(mControlManager == null){
            return;
        }
        mControlManager.distribute(cmd);
    }

    @Override
    public void parseResult(ControllCommand cmd) {
        distributeCMD(cmd);
    }
}
