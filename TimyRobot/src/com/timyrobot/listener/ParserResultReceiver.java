package com.timyrobot.listener;

import com.timyrobot.bean.ControllCommand;

/**
 * Created by zhangtingting on 15/8/6.
 */
public interface ParserResultReceiver {
    void parseResult(ControllCommand cmd);
}
