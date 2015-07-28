package com.timyrobot.service.userintent.parser;

import com.timyrobot.service.userintent.action.Action;

/**
 * Created by zhangtingting on 15/7/24.
 */
public interface IUserIntentParser {

    Action parseIntent(String result);
    String getRobotTalkContent();
}
