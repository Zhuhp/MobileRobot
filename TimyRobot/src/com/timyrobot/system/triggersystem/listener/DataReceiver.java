package com.timyrobot.system.triggersystem.listener;

import com.timyrobot.system.bean.BaseCommand;

/**
 * Created by zhangtingting on 15/8/5.
 */
public interface DataReceiver {
    void onReceive(BaseCommand data);
}
