package com.timyrobot.service.userintent.intentparser.impl;

import com.timyrobot.service.userintent.actionparse.Action;
import com.timyrobot.service.userintent.actionparse.ActionJsonParser;
import com.timyrobot.service.userintent.intentparser.IUserIntentParser;

import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/7/24.
 */
public class PhoneIntentParser implements IUserIntentParser{

    @Override
    public Action parseIntent(String result) {
        try {
            JSONObject object = new JSONObject(result);
            JSONObject slots = ActionJsonParser.getSlots(object);
            Action action = new Action();
            action.operation = ActionJsonParser.getOperation(object);
            action.service = ActionJsonParser.getService(object);
            action.obj1 = slots.optString("name");
            return action;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getRobotTalkContent() {
        return null;
    }
}
