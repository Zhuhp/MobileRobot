package com.timyrobot.listener;

import com.timyrobot.bean.BaseCommand;

/**
 * Created by zhangtingting on 15/8/5.
 */
public interface DataReceiver {
    void onReceive(BaseCommand data);
}
