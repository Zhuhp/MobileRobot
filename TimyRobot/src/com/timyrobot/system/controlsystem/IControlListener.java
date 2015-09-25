package com.timyrobot.system.controlsystem;

import com.timyrobot.system.bean.BaseCommand;
import com.timyrobot.system.controlsystem.listener.EndListener;

/**
 * 控制器的listener，每一个控制器都要实现这个接口
 * Created by zhangtingting on 15/9/15.
 */
public interface IControlListener {

    /**
     * 是否可以执行下一个命令
     * @return true可以进行下一步，false还在执行命令中
     */
    boolean next();

    /**
     * 通过该方法向控制器发送命令
     * @param cmd 命令
     */
    void distributeCMD(BaseCommand cmd);

    /**
     * 设置结束的监听事件
     * @param listener listener
     */
    void setEndListener(EndListener listener);
}
