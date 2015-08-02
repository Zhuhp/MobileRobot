package com.timyrobot.service.userintent.intentparser.impl;

import android.app.Activity;

import com.timyrobot.service.userintent.actionparse.Action;
import com.timyrobot.service.userintent.actionparse.ActionJsonParser;
import com.timyrobot.service.userintent.intentparser.IUserIntentParser;

import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/8/2.
 */
public class VideoIntentParser implements IUserIntentParser{
    /**
     * 片名
     */
    private String mMovie;

    private String mField;

    private Activity mActivity;

    private Action mAction;

    public VideoIntentParser(Activity activity){
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
            mAction.obj1 = slots.optString("keywords");
            mAction.obj2 = slots.optString("queryField");
            mMovie = mAction.obj1;
            mField = mAction.obj2;
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
