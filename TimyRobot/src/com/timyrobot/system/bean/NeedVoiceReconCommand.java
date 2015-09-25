package com.timyrobot.system.bean;

/**
 * 需要触发语音识别的命令
 * Created by zhangtingting on 15/9/24.
 */
public class NeedVoiceReconCommand extends BaseCommand{

    @Override
    protected void initCommand() {
        setIsNeedVoiceRecon(true);
        setIsNeedUnderstandText(true);
    }

    @Override
    public void parseVoiceContent(String content) {

    }
}
