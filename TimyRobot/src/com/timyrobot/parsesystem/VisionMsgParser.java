package com.timyrobot.parsesystem;

import android.util.Log;

import com.timyrobot.bean.ControllCommand;
import com.timyrobot.common.ConstDefine;
import com.timyrobot.listener.ParserResultReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/8/6.
 */
public class VisionMsgParser implements IDataParse{
    private static final String TAG = VisionMsgParser.class.getName();
    private ParserResultReceiver mReceiver;

    public VisionMsgParser(ParserResultReceiver receiver){
        mReceiver = receiver;
    }
    @Override
    public void parse(String content) {
        Log.i(TAG, "Vison message->"+content);
        try {
            JSONObject object = new JSONObject(content);
            if(object.get(ConstDefine.TriggerDataKey.FACE_TGR_TYPE).equals(ConstDefine.VisionCMD.DETECT_FACE)) {
                ControllCommand cmd = new ControllCommand(null, null, false, null, null, ConstDefine.VisionCMD.DETECT_FACE);
                if (mReceiver != null) {
                    mReceiver.parseResult(cmd);
                }
            }else{
                ControllCommand cmd = new ControllCommand("laugh", null, false, null, null, null);
                if (mReceiver != null) {
                    mReceiver.parseResult(cmd);
                }
            }
        }catch(JSONException e){
            Log.d(TAG, "trans vision content to json farmat error!");
            return;

        }
    }
}
