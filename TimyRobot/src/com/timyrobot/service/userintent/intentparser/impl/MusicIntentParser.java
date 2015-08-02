package com.timyrobot.service.userintent.intentparser.impl;

import android.app.Activity;

import com.timyrobot.service.userintent.actionparse.Action;
import com.timyrobot.service.userintent.actionparse.ActionJsonParser;
import com.timyrobot.service.userintent.intentparser.IUserIntentParser;

import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/8/2.
 */
public class MusicIntentParser implements IUserIntentParser{

    /**
     * 歌手
     */
    private String mArtist;

    private String mSong;

    private Activity mActivity;

    private Action mAction;

    public MusicIntentParser(Activity activity){
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
            mAction.obj1 = slots.optString("artist");
            mAction.obj2 = slots.optString("song");
            mArtist = mAction.obj1;
            mSong = mAction.obj2;
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
