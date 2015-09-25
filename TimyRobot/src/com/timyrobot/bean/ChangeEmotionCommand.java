package com.timyrobot.bean;

import com.timyrobot.robot.data.RobotData;

/**
 * Created by zhangtingting on 15/9/24.
 */
public class ChangeEmotionCommand extends BaseCommand{
    @Override
    protected void initCommand() {
        setIsNeedVoiceRecon(false);
        setIsNeedUnderstandText(false);
        setEmotionName(RobotData.INSTANCE.getRandomFace());
    }

    @Override
    public void parseVoiceContent(String content) {

    }
}
