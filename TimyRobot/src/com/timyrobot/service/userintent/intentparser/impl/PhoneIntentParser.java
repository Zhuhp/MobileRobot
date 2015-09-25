package com.timyrobot.service.userintent.intentparser.impl;

import com.timyrobot.bean.BaseCommand;
import com.timyrobot.common.SystemServiceKey;
import com.timyrobot.service.userintent.actionparse.ActionJsonParser;
import com.timyrobot.service.userintent.intentparser.IUserIntentParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/7/24.
 */
public class PhoneIntentParser implements IUserIntentParser{


    @Override
    public boolean parseIntent(String result, BaseCommand command) {
        if(command == null){
            return false;
        }
        try {
            JSONObject object = new JSONObject(result);
            JSONObject systemData = new JSONObject();
            systemData.put(SystemServiceKey.SystemKey.OPERATOR,ActionJsonParser.getOperation(object));
            systemData.put(SystemServiceKey.SystemKey.SERVICE,ActionJsonParser.getService(object));
            JSONObject slot = ActionJsonParser.getSlots(object);
            systemData.put(SystemServiceKey.Phone.CONTACT_NAME,slot.opt(SystemServiceKey.Phone.CONTACT_NAME));
            command.setEmotionName(null);
            command.setVoiceContent("好的，主人，很荣幸为你服务");
            command.setIsNeedTuling(false);
            command.setRobotAction(systemData.toString());
            command.setSystemOperator(result);
            command.setCmd(null);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

}
