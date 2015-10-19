package com.timyrobot.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhangtingting on 15/8/8.
 */
public class SharePrefsUtils {

    SharedPreferences sp;

    public SharePrefsUtils(Context context, String name){
       sp = context.getSharedPreferences(name,Context.MODE_PRIVATE);
    }

    public void setStringData(String key, String data){
        SharedPreferences.Editor et = sp.edit();
        et.putString(key, data);
        et.commit();
    }

    public String getStringData(String key){
        return sp.getString(key, null);
    }
}
