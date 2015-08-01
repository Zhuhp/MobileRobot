package com.timyrobot.service.userintent.intentparser;

import com.timyrobot.service.userintent.actionparse.Action;

/**
 * 解析用户的意图，同时根据意图，提供一些表现形式，比如表情，机器人将要说的话等
 * Created by zhangtingting on 15/7/24.
 */
public interface IUserIntentParser {

    /**
     * 解析用户意图
     * @param result
     * @return
     */
    void parseIntent(String result);

    /**
     * 根据用户意图进行操作
     */
    void doAction();

    /**
     * 获取动作
     * @return
     */
    Action getAction();

    /**
     * 这种意图，机器人需要展示的表情
     * @return
     */
    String getRobotEmotion();

    /**
     * 这种意图，机器人需要说话的内容
     * @return
     */
    String getRobotTalkContent();
}
