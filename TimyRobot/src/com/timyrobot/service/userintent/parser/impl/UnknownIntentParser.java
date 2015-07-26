package com.timyrobot.service.userintent.parser.impl;

import com.timyrobot.service.userintent.action.Action;
import com.timyrobot.service.userintent.parser.IUserIntentParser;

/**
 * Created by zhangtingting on 15/7/24.
 */
public class UnknownIntentParser implements IUserIntentParser{
    @Override
    public Action parseIntent(String result) {
        return null;
    }
}
