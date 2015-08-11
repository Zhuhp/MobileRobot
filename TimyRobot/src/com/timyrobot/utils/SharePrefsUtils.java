package com.timyrobot.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhangtingting on 15/8/8.
 */
public class SharePrefsUtils {

    SharedPreferences sp;

    public SharePrefsUtils(Context context){
       sp = context.getSharedPreferences("blut_data",Context.MODE_PRIVATE);
    }

    public void setBlueAddress(String address){
        SharedPreferences.Editor et = sp.edit();
        et.putString("blue_address",address);
        et.commit();
    }

    public String getBlueAddress(){
        return sp.getString("blue_address",null);
    }
}
