package com.timyrobot.service.userintent.intentparser;

import com.timyrobot.bean.ControllCommand;

/**
 * 解析用户的意图，同时根据意图，提供一些表现形式，比如表情，机器人将要说的话等
 * Created by zhangtingting on 15/7/24.
 */
public interface IUserIntentParser {

    /**
     * 解析用户意图
     * @param result
     * @return
     */
    ControllCommand parseIntent(String result);


}
