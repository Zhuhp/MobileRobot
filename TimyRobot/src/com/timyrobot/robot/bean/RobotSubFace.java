package com.timyrobot.robot.bean;

/**
 * Created by zhangtingting on 15/8/14.
 */
public class RobotSubFace {

    private String faceName;
    private long time;

    public String getFaceName() {
        return faceName;
    }

    public void setFaceName(String faceName) {
        this.faceName = faceName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "RobotSubFace{" +
                "faceName='" + faceName + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
