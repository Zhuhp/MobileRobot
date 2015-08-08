package com.timyrobot.robot.bean;

/**
 * Created by zhangtingting on 15/8/8.
 */
public class RobotSubAction {

    private String position;
    private String time;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "RobotSubAction{" +
                "position='" + position + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
