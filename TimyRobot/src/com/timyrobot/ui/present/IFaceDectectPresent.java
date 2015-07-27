package com.timyrobot.ui.present;

import com.example.robot.facedection.CameraSurfaceView;
import com.example.robot.facedection.FaceView;

/**
 * Created by zhangtingting on 15/7/27.
 */
public interface IFaceDectectPresent {

    void initFaceDectect(CameraSurfaceView surfaceView,FaceView faceView);
    void startDetect();
}
