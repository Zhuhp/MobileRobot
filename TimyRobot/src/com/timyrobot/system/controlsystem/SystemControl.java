package com.timyrobot.system.controlsystem;

import android.content.Context;
import android.text.TextUtils;

import com.timyrobot.system.bean.BaseCommand;
import com.timyrobot.system.controlsystem.listener.EndListener;
import com.timyrobot.service.music.MusicPlayer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/8/8.
 */
public class SystemControl implements IControlListener{

    Context mCtx;
    private boolean isNext = true;

    public SystemControl(Context context){
        mCtx = context;
    }

    @Override
    public boolean next(){
        return isNext;
    }

    @Override
    public void distributeCMD(BaseCommand cmd) {
        doAction(cmd.getSystemOperator());
    }

    @Override
    public void setEndListener(EndListener listener) {

    }

    public void doAction(String action){
        if(TextUtils.isEmpty(action)){
            return;
        }
        JSONObject object = null;
        try {
            object = new JSONObject(action);
        } catch (JSONException e) {
            e.printStackTrace();
            object = null;
        }
        if(object == null){
            return;
        }
        if(object.has("music")){
            String music = object.optString("music");
            if(TextUtils.isEmpty(music)){
                return;
            }
            int waitTime = object.optInt("music_wait_time");
            int musicTime = object.optInt("music_time");
//            Uri name = Uri.parse("android.resource://"+mCtx.getPackageName()+"/raw/test.mp3");
            MusicPlayer player = new MusicPlayer(mCtx,music,musicTime,waitTime);
            player.start();
        }
    }
}
