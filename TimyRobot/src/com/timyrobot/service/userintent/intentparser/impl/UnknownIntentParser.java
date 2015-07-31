package com.timyrobot.service.userintent.intentparser.impl;

import com.timyrobot.service.userintent.actionparse.Action;
import com.timyrobot.service.userintent.intentparser.IUserIntentParser;

/**
 * Created by zhangtingting on 15/7/24.
 */
public class UnknownIntentParser implements IUserIntentParser{
    @Override
    public Action parseIntent(String result) {
        return null;
    }

    @Override
    public String getRobotTalkContent() {
        return null;
    }
}
