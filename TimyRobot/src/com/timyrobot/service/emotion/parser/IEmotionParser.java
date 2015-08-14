package com.timyrobot.service.emotion.parser;

import com.timyrobot.robot.bean.RobotFace;

/**
 * Created by zhangtingting on 15/7/20.
 */
public interface IEmotionParser {
    RobotFace parseEmotion(String name);
}
