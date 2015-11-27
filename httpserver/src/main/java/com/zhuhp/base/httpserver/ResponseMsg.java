package com.zhuhp.base.httpserver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/11/27.
 */
public class ResponseMsg {
    private String msg;
    private int code;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String createJson(){
        JSONObject object = new JSONObject();
        try {
            object.put("msg", msg);
            object.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}
