package com.timyrobot.service.userintent;

import android.app.Activity;
import android.content.Context;

import com.timyrobot.service.userintent.actionparse.Action;

/**
 * Created by zhangtingting on 15/7/22.
 */
public class ActionManager {

    private Activity mContext;

    public ActionManager(Activity activity){
        mContext = activity;
    }

    public void parseAction(Action action){

    }

}
