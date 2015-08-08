package com.timyrobot.service.userintent.intentparser.impl;

import android.app.Activity;

import com.timyrobot.bean.ControllCommand;
import com.timyrobot.common.SystemServiceKey;
import com.timyrobot.service.speechrecongnizer.SpeechJsonParser;
import com.timyrobot.service.userintent.actionparse.Action;
import com.timyrobot.service.userintent.actionparse.ActionJsonParser;
import com.timyrobot.service.userintent.intentparser.IUserIntentParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/7/24.
 */
public class PhoneIntentParser implements IUserIntentParser{


    @Override
    public ControllCommand parseIntent(String result) {
        try {
            JSONObject object = new JSONObject(result);
            JSONObject systemData = new JSONObject();
            systemData.put(SystemServiceKey.SystemKey.OPERATOR,ActionJsonParser.getOperation(object));
            systemData.put(SystemServiceKey.SystemKey.SERVICE,ActionJsonParser.getService(object));
            JSONObject slot = ActionJsonParser.getSlots(object);
            systemData.put(SystemServiceKey.Phone.CONTACT_NAME,slot.opt(SystemServiceKey.Phone.CONTACT_NAME));
            ControllCommand command = new ControllCommand(null,"好的，主人，很荣幸为你服务",false,systemData.toString(),result,null);
            return command;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
