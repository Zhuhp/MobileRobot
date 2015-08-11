package com.timyrobot.robot.data;

import android.content.Context;
import android.text.TextUtils;

import com.timyrobot.common.RobotServiceKey;
import com.timyrobot.robot.bean.RobotAction;
import com.timyrobot.robot.bean.RobotCmd;
import com.timyrobot.robot.bean.RobotSubAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangtingting on 15/8/8.
 */
public enum RobotData {
    INSTANCE;

    private Map<String,RobotAction> mAction;
    private Map<String,RobotCmd> mCmd;

    public void initRobotData(Context ctx){
        mAction = new HashMap<>();
        mCmd = new HashMap<>();
        try {
            BufferedReader cmdBr = new BufferedReader(new InputStreamReader(ctx.getAssets().open("cmd.txt")));
            BufferedReader actionBr = new BufferedReader(new InputStreamReader(ctx.getAssets().open("action.txt")));
            JSONObject cmdObject = new JSONObject(cmdBr.readLine());
            JSONObject actionObject = new JSONObject(actionBr.readLine());
            initCmd(cmdObject);
            initAction(actionObject);
            cmdBr.close();;
            actionBr.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initCmd(JSONObject object){
        if(object == null){
            return;
        }
        Iterator<String> keys = object.keys();
        if(keys == null){
            return;
        }
        while(keys.hasNext()){
            String key = keys.next();
            JSONObject cmd = object.optJSONObject(key);
            if(cmd != null){
                RobotCmd rc = new RobotCmd();
                rc.setAction(cmd.optString(RobotServiceKey.CmdKey.ACTION));
                rc.setVoice(cmd.optString(RobotServiceKey.CmdKey.VOICE));
                rc.setFace(cmd.optString(RobotServiceKey.CmdKey.FACE));
                mCmd.put(key,rc);
            }
        }
    }


    private void initAction(JSONObject object){
        if(object == null){
            return;
        }
        Iterator<String> keys = object.keys();
        if(keys == null){
            return;
        }
        while(keys.hasNext()){
            String key = keys.next();
            JSONObject action = object.optJSONObject(key);
            if(action != null){
                RobotAction ac = new RobotAction();
                ac.setActionNum(action.optInt(RobotServiceKey.ActionKey.ACTION_NUM));
                ac.setStateLight(action.optString(RobotServiceKey.ActionKey.STATELIGHT));
                JSONArray subAcArray = action.optJSONArray(RobotServiceKey.ActionKey.ACTIONS);
                if(subAcArray != null){
                    ArrayList<RobotSubAction> robotActions = new ArrayList<>();
                    for(int i = 0;i<subAcArray.length();i++) {
                        JSONObject subAc = subAcArray.optJSONObject(i);
                        RobotSubAction subAction = new RobotSubAction();
                        subAction.setPosition(subAc.optString(RobotServiceKey.ActionKey.POSITION));
                        subAction.setTime(subAc.optLong(RobotServiceKey.ActionKey.TIME));
                        robotActions.add(subAction);
                    }
                    ac.setActions(robotActions);
                }
                mAction.put(key,ac);
            }
        }
    }

    public RobotCmd getRobotCmd(String key){
        if(TextUtils.isEmpty(key)){
            return null;
        }
        Set<String> setKey = mCmd.keySet();
        String realKey = "";
        for(String s:setKey){
            if(key.contains(s)){
                realKey = s;
            }
        }
        if(!mCmd.containsKey(realKey)){
            return null;
        }
        return mCmd.get(realKey);
    }

    public RobotAction getRobotAction(String key){
        if(TextUtils.isEmpty(key)){
            return null;
        }
        if(!mAction.containsKey(key)){
            return null;
        }
        return mAction.get(key);
    }
}
