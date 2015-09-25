package com.timyrobot.service.userintent.intentparser;

import com.timyrobot.bean.BaseCommand;

/**
 * 解析用户的意图，同时根据意图，提供一些表现形式，比如表情，机器人将要说的话等
 * Created by zhangtingting on 15/7/24.
 */
public interface IUserIntentParser {

    /**
     * 解析用户意图
     * @param result 需要解析的内容
     * @param command 需要填充的命令
     * @return 是否解析成功， true成功，false失败
     */
    boolean parseIntent(String result, BaseCommand command);


}
