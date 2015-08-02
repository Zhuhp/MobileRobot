package com.timyrobot.service.userintent.intentparser.impl;

import android.app.Activity;

import com.timyrobot.service.userintent.actionparse.Action;
import com.timyrobot.service.userintent.actionparse.ActionJsonParser;
import com.timyrobot.service.userintent.intentparser.IUserIntentParser;

import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/8/2.
 */
public class SmsIntentParser implements IUserIntentParser{

    /**
     * 联系人名字
     */
    private String mContactName;

    private String mContent;

    private String mMsgType;

    private Activity mActivity;

    private Action mAction;

    public SmsIntentParser(Activity activity){
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
            mAction.obj2 = slots.optString("content");
            mAction.obj3 = slots.optString("messageType");
            mContactName = mAction.obj1;
            mContent = mAction.obj2;
            mMsgType = mAction.obj3;
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
