package com.timyrobot.service.userintent.parser;

import com.timyrobot.service.userintent.parser.impl.PhoneIntentParser;
import com.timyrobot.service.userintent.parser.impl.UnknownIntentParser;

/**
 * Created by zhangtingting on 15/7/24.
 */
public enum UserIntentParserFactory {
    INSTANCE;

    public IUserIntentParser getParser(String service,String operator){
        if("telephone".equalsIgnoreCase(service)){
            if("call".equalsIgnoreCase(operator)){
                return new PhoneIntentParser();
            }
        }
        return new UnknownIntentParser();
    }
}
