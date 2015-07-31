package com.timyrobot.service.userintent.intentparser;

import com.timyrobot.service.userintent.actionparse.Action;

/**
 * Created by zhangtingting on 15/7/24.
 */
public interface IUserIntentParser {

    Action parseIntent(String result);
    String getRobotTalkContent();
}
