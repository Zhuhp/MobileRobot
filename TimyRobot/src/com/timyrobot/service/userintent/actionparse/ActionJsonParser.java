package com.timyrobot.service.userintent.actionparse;

import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/7/29.
 */
public class ActionJsonParser {

    private ActionJsonParser(){

    }

    public static boolean isSuccess(JSONObject object){
        if(object == null){
            return false;
        }
        return object.optInt("rc",-1) == 0;
    }

    public static String getOperation(JSONObject object){
        if(object == null){
            return "";
        }
        return object.optString("operation");
    }

    public static String getService(JSONObject object){
        if(object == null){
            return "";
        }
        return object.optString("service");
    }

    public static JSONObject getSlots(JSONObject object){
        if(object == null){
            return null;
        }
        JSONObject semantic = object.optJSONObject("semantic");
        if(semantic == null){
            return null;
        }
        return semantic.optJSONObject("slots");
    }

    public static String getText(JSONObject object){
        if(object == null){
            return "";
        }
        return object.optString("text");
    }
}
