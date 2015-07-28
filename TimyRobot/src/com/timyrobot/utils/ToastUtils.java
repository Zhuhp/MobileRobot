package com.timyrobot.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zhangtingting on 15/7/19.
 */
public class ToastUtils {

    public static final void toastShort(Context context,int resId){
        Toast.makeText(context,resId,Toast.LENGTH_SHORT).show();
    }

    public static final void toastShort(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }
}
