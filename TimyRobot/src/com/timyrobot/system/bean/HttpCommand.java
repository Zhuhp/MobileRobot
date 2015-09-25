package com.timyrobot.system.bean;

/**
 * Created by zhangtingting on 15/9/24.
 */
public class HttpCommand extends BaseCommand{

    @Override
    protected void initCommand() {
        setIsNeedVoiceRecon(false);
        setIsNeedUnderstandText(true);
    }

    @Override
    public void parseVoiceContent(String content) {

    }
}
