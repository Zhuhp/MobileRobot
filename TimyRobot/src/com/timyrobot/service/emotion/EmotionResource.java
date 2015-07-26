package com.timyrobot.service.emotion;

/**
 * Created by zhangtingting on 15/7/22.
 */
public enum EmotionResource {
    BLINK("blink");

    private String resName;

    private EmotionResource(String resName){
        this.resName = resName;
    }

    public String getResName(){
        return resName;
    }

}
