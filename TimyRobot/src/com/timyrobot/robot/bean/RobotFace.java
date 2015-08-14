package com.timyrobot.robot.bean;

import java.util.ArrayList;

/**
 * Created by zhangtingting on 15/8/14.
 */
public class RobotFace {

    private int faceNum;
    private ArrayList<RobotSubFace> faces;

    public int getActionNum() {
        return faceNum;
    }

    public void setActionNum(int faceNum) {
        this.faceNum = faceNum;
    }

    public ArrayList<RobotSubFace> getActions() {
        return faces;
    }

    public void setActions(ArrayList<RobotSubFace> faces) {
        this.faces = faces;
    }
}
