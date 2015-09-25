package com.timyrobot.system.triggersystem;

/**
 * 开关，所有的触发器必须先判断这个，才可以进行下一步动作
 * 全局变量
 * Created by zhangtingting on 15/9/14.
 */
public enum TriggerSwitch {
    INSTANCE;

    /**
     * 是否可以进行下一步
     */
    private boolean canNext = true;



    /**
     * 是否进行下一个命令解析
     * @return true 表示可以进行下一步，false 不能进行下一步
     */
    public synchronized boolean goNext() {
        if(!canNext){
            return false;
        }
        canNext = false;
        return true;
    }

    /**
     * 设置下一个命令
     * @param canNext true表示可以进行下一步，false表示不能进行下一步
     */
    public synchronized void setCanNext(boolean canNext) {
        this.canNext = canNext;
    }
}
