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
            if(object.get(ConstDefine.TriggerDataKey.FACE_TGR_TYPE).equals(ConstDefine.VisionCMD.DETECT_FACE_LOCATION)) {
//                int position = object.getInt(ConstDefine.TriggerDataKey.NUMBER);
//                Log.d(TAG, "positon->"+position);
//                int data = 0;
//                if (position > 0) {
//                    if (position <= 700)
//                        data = 60;
//                    else if (position <= 1000)
//                        data = 50;
//                } else {
//                    if (position >= -700)
//                        data = 100;
//                    else if (position >= -1000)
//                        data = 110;
//                }
//                ControllCommand cmd = new ControllCommand("blink", null, false, "A0_"+data+"_2", null, null);
//                if (mReceiver != null) {
//                    mReceiver.parseResult(cmd);
//                }

            }else if(object.get(ConstDefine.TriggerDataKey.FACE_TGR_TYPE).equals(ConstDefine.VisionCMD.DETECT_FACE)){
                ControllCommand cmd = new ControllCommand("blink", null, false, null, null, ConstDefine.VisionCMD.DETECT_FACE);
                if (mReceiver != null) {
                    mReceiver.parseResult(cmd);
                }

            }else if(object.get(ConstDefine.TriggerDataKey.FACE_TGR_TYPE).equals(ConstDefine.VisionCMD.DETECT_MANY_FACES)){
//                ControllCommand cmd = new ControllCommand("laugh", "哈,这么多人看我呀！", false, "slide", null, null);
//                if (mReceiver != null) {
//                    mReceiver.parseResult(cmd);
//                }
            }

            }catch(JSONException e){
                Log.e(TAG, "trans vision content to json farmat error!");
            }
        }
    }

