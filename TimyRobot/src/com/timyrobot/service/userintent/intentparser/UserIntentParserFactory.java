package com.timyrobot.service.userintent.intentparser;

import android.app.Activity;

import com.timyrobot.service.userintent.intentparser.impl.PhoneIntentParser;
import com.timyrobot.service.userintent.intentparser.impl.UnknownIntentParser;

/**
 * 表情解析工厂类，需要与被解析的activity绑定
 * Created by zhangtingting on 15/7/24.
 */
public class UserIntentParserFactory {

    private Activity mActivity;

    public UserIntentParserFactory(Activity activity){
        mActivity = activity;
    }

    public IUserIntentParser getParser(String service,String operator){
        if("telephone".equalsIgnoreCase(service)){
            if("call".equalsIgnoreCase(operator)){
                return new PhoneIntentParser(mActivity);
            }
        }
        return new UnknownIntentParser();
    }
}
