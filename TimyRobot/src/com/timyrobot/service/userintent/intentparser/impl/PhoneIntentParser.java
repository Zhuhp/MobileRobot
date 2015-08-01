package com.timyrobot.service.userintent.intentparser.impl;

import android.app.Activity;

import com.timyrobot.service.userintent.actionparse.Action;
import com.timyrobot.service.userintent.actionparse.ActionJsonParser;
import com.timyrobot.service.userintent.intentparser.IUserIntentParser;

import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/7/24.
 */
public class PhoneIntentParser implements IUserIntentParser{

    /**
     * 联系人名字
     */
    private String mContactName;

    private Activity mActivity;

    private Action mAction;

    public PhoneIntentParser(Activity activity){
        mActivity = activity;
    }

    @Override
    public void parseIntent(String result) {
        try {
            JSONObject object = new JSONObject(result);
            JSONObject slots = ActionJsonParser.getSlots(object);
            mAction = new Action();
            mAction.operation = ActionJsonParser.getOperation(object);
            mAction.service = ActionJsonParser.getService(object);
            mAction.obj1 = slots.optString("name");
            mContactName = mAction.obj1;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doAction() {

    }

    @Override
    public Action getAction() {
        return mAction;
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
