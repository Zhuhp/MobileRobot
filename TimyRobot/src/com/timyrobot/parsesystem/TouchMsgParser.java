package com.timyrobot.parsesystem;

import com.timyrobot.bean.ControllCommand;
import com.timyrobot.common.ConstDefine;
import com.timyrobot.listener.ParserResultReceiver;

/**
 * Created by zhangtingting on 15/8/14.
 */
public class TouchMsgParser implements IDataParse{


    private static final String TAG = VisionMsgParser.class.getName();
    private ParserResultReceiver mReceiver;

    public TouchMsgParser(ParserResultReceiver receiver){
        mReceiver = receiver;
    }
    @Override
    public void parse(String content) {
        if(ConstDefine.TouchCMD.DETECT_TOUCH.equals(content)) {
            ControllCommand cmd = new ControllCommand(null, null, false, null, null, ConstDefine.TouchCMD.DETECT_TOUCH);
            if (mReceiver != null) {
                mReceiver.parseResult(cmd);
            }
        }
    }
}
