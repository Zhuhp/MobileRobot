package com.timyrobot.service.userintent.intentparser.impl;

import com.timyrobot.service.userintent.actionparse.Action;
import com.timyrobot.service.userintent.intentparser.IUserIntentParser;

/**
 * Created by zhangtingting on 15/7/24.
 */
public class UnknownIntentParser implements IUserIntentParser{
    @Override
    public void parseIntent(String result) {
    }

    @Override
    public void doAction() {

    }

    @Override
    public Action getAction() {
        return null;
    }

    @Override
    public String getRobotEmotion() {
        return null;
    }

    @Override
    public String getRobotTalkContent() {
        return null;
    }
}
