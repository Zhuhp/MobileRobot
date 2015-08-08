package com.timyrobot.parsesystem;

import com.timyrobot.bean.ControllCommand;
import com.timyrobot.listener.ParserResultReceiver;

/**
 * Created by zhangtingting on 15/8/6.
 */
public class VisionMsgParser implements IDataParse{

    private ParserResultReceiver mReceiver;

    public VisionMsgParser(ParserResultReceiver receiver){
        mReceiver = receiver;
    }
    @Override
    public void parse(String content) {
        ControllCommand cmd = new ControllCommand(null,null,false,null,null,content);
        if(mReceiver != null){
            mReceiver.parseResult(cmd);
        }
    }
}
