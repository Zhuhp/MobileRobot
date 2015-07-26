package com.timyrobot.service.userintent.parser;

import com.timyrobot.service.userintent.parser.impl.UnknownIntentParser;

/**
 * Created by zhangtingting on 15/7/24.
 */
public enum UserIntentParserFactory {
    INSTANCE;

    public IUserIntentParser getParser(String operator){
        return new UnknownIntentParser();
    }
}
