package com.timyrobot.robot.bean;

import java.util.ArrayList;

/**
 * Created by zhangtingting on 15/8/8.
 */
public class RobotAction {

    private int actionNum;
    private String stateLight;
    private ArrayList<RobotSubAction> actions;

    public int getActionNum() {
        return actionNum;
    }

    public void setActionNum(int actionNum) {
        this.actionNum = actionNum;
    }

    public String getStateLight() {
        return stateLight;
    }

    public void setStateLight(String stateLight) {
        this.stateLight = stateLight;
    }

    public ArrayList<RobotSubAction> getActions() {
        return actions;
    }

    public void setActions(ArrayList<RobotSubAction> actions) {
        this.actions = actions;
    }
}
