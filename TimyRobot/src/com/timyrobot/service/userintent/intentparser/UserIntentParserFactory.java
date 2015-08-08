package com.timyrobot.service.userintent.intentparser;

import android.app.Activity;

import com.timyrobot.service.userintent.intentparser.impl.MusicIntentParser;
import com.timyrobot.service.userintent.intentparser.impl.PhoneIntentParser;
import com.timyrobot.service.userintent.intentparser.impl.SmsIntentParser;
import com.timyrobot.service.userintent.intentparser.impl.UnknownIntentParser;
import com.timyrobot.service.userintent.intentparser.impl.VideoIntentParser;
import com.timyrobot.service.userintent.intentparser.impl.WebSearchIntentParser;

/**
 * 表情解析工厂类，需要与被解析的activity绑定
 * Created by zhangtingting on 15/7/24.
 */
public class UserIntentParserFactory {

    public static IUserIntentParser getParser(String service,String operator){
        if("telephone".equalsIgnoreCase(service)){
            if("call".equalsIgnoreCase(operator)){
                return new PhoneIntentParser();
            }
        }else if("websearch".equalsIgnoreCase(service)){
            if("query".equalsIgnoreCase(operator)){
                return new WebSearchIntentParser();
            }
        }else if("message".equalsIgnoreCase(service)){
            if("send".equalsIgnoreCase(operator)){
                return new SmsIntentParser();
            }
        }else if("music".equalsIgnoreCase(service)){
            if("play".equalsIgnoreCase(operator)){
                return new MusicIntentParser();
            }
        }else if("video".equalsIgnoreCase(service)){
            if("play".equalsIgnoreCase(operator)){
                return new VideoIntentParser();
            }
        }
        return new UnknownIntentParser();
    }
}
