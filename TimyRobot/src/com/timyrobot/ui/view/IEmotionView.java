package com.timyrobot.ui.view;

import com.timyrobot.service.userintent.actionparse.Action;
import com.timyrobot.service.userintent.intentparser.IUserIntentParser;

/**
 * Created by zhangtingting on 15/7/18.
 */
public interface IEmotionView{
    void userAction(IUserIntentParser action);
}
