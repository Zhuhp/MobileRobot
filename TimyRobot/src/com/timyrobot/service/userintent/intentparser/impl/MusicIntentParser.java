package com.timyrobot.service.userintent.intentparser.impl;

import android.app.Activity;

import com.timyrobot.bean.ControllCommand;
import com.timyrobot.common.SystemServiceKey;
import com.timyrobot.service.userintent.actionparse.Action;
import com.timyrobot.service.userintent.actionparse.ActionJsonParser;
import com.timyrobot.service.userintent.intentparser.IUserIntentParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/8/2.
 */
public class MusicIntentParser implements IUserIntentParser{

    private ControllCommand mAction;

    @Override
    public ControllCommand parseIntent(String result) {
        try {
            JSONObject object = new JSONObject(result);
            JSONObject systemData = new JSONObject();
            systemData.put(SystemServiceKey.SystemKey.OPERATOR,ActionJsonParser.getOperation(object));
            systemData.put(SystemServiceKey.SystemKey.SERVICE,ActionJsonParser.getService(object));
            JSONObject slot = ActionJsonParser.getSlots(object);
            systemData.put(SystemServiceKey.Music.ARTIST,slot.opt(SystemServiceKey.Music.ARTIST));
            systemData.put(SystemServiceKey.Music.SONG,slot.opt(SystemServiceKey.Music.SONG));
            ControllCommand command = new ControllCommand(null,"好的，主人，很荣幸为你服务",false,systemData.toString(),result,null);
            return command;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
