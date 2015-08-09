package com.timyrobot.triggersystem;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.example.robot.facedection.CameraInterface;
import com.example.robot.facedection.CameraSurfaceView;
import com.example.robot.facedection.EventUtil;
import com.example.robot.facedection.FaceView;
import com.example.robot.facedection.GoogleFaceDetect;
import com.timyrobot.common.ConstDefine;
import com.timyrobot.listener.DataReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/7/27.
 */
public enum FaceDectectTrigger {
    INSTANCE;
    private final static String TAG = FaceDectectTrigger.class.getName();


    Context mCtx;

    CameraSurfaceView mSurfaceView = null;
    FaceView mFaceView;
    float previewRate = -1f;
    private FaceDectHandler mFaceDectHandler = null;
    GoogleFaceDetect googleFaceDetect = null;
    private CameraInterface mInterface;

    private DataReceiver mReceiver;

    public void init(Context context, DataReceiver receiver) {
        mCtx = context;
        mReceiver = receiver;
        mInterface = CameraInterface.getInstance();
    }

    public void initFaceDectect(CameraSurfaceView surfaceView, FaceView faceView) {
        mFaceDectHandler = new FaceDectHandler();
        mSurfaceView = surfaceView;
        mFaceView = faceView;
        googleFaceDetect = new GoogleFaceDetect(mCtx,
                mFaceDectHandler);
        mFaceDectHandler.sendEmptyMessageDelayed(
                EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);
    }

    public void startDetect() {
        startGoogleFaceDetect();
    }

    public void stopDetect() {
        stopGoogleFaceDetect();
    }

    private boolean bFirstDectect = true;
    private int lastPosition = 0;

    private class FaceDectHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case EventUtil.UPDATE_FACE_RECT:
                    Camera.Face[] faces = (Camera.Face[]) msg.obj;
                    if (faces == null || faces.length < 1) {
                        break;
                    }
                    mFaceView.setFaces(faces);

                    int position = faces[0].rect.left
                            + (faces[0].rect.right - faces[0].rect.left) / 2;

//                    if ((faces[0].rect.right - faces[0].rect.left) > 700) {
////                        if(bFirstDectect) {
////                            bFirstDectect = false;
////                            lastPosition = position;
////                            break;
////                        }else{
////                            if(Math.abs(position-lastPosition)<100){
//                                if(mReceiver != null){
//                                    try {
//                                        JSONObject object = new JSONObject();
//                                        object.put(ConstDefine.TriggerDataKey.TYPE,
//                                                ConstDefine.TriggerDataType.Vision);
//                                        JSONObject faceObj = new JSONObject();
//                                        faceObj.put(ConstDefine.TriggerDataKey.FACE_TGR_TYPE, ConstDefine.VisionCMD.DETECT_FACE);
//                                        faceObj.put(ConstDefine.TriggerDataKey.NUMBER, String.valueOf(position));
//                                        object.put(ConstDefine.TriggerDataKey.CONTENT,
//                                                faceObj.toString());
//                                        mReceiver.onReceive(object.toString());
//                                        break;
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
////                                }
////                            }
//                        }


                    if (faces.length >= 3) {
                        Log.d(TAG, "Dectect more than 3 face");
                        if (mReceiver != null) {
                            try {
                                JSONObject object = new JSONObject();
                                object.put(ConstDefine.TriggerDataKey.TYPE,
                                        ConstDefine.TriggerDataType.Vision);
                                JSONObject faceObj = new JSONObject();
                                faceObj.put(ConstDefine.TriggerDataKey.FACE_TGR_TYPE, ConstDefine.VisionCMD.DETECT_MANY_FACES);
                                faceObj.put(ConstDefine.TriggerDataKey.NUMBER, String.valueOf(faces.length));
                                object.put(ConstDefine.TriggerDataKey.CONTENT,
                                        faceObj.toString());
                                mReceiver.onReceive(object.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
//                        lastPosition = 0;
//                        bFirstDectect = true;
                    break;


//                    // Log.d(TAG, "positon->"+position);
//                    String data = "";
//                    if (position > 0) {
//                        if (position <= 400)
//                            data = "c";
//                        else if (position <= 700)
//                            data = "b";
//                        else if (position <= 1000)
//                            data = "a";
//                    } else {
//                        if (position >= -400)
//                            data = "d";
//                        else if (position >= -700)
//                            data = "e";
//                        else if (position >= 1000)
//                            data = "f";
//                    }
            case EventUtil.CAMERA_HAS_STARTED_PREVIEW:
            startGoogleFaceDetect();
            break;
        }

        super.

        handleMessage(msg);
    }

}

    private void restartPreviewCamera() {
        stopGoogleFaceDetect();
        mInterface.doStartPreview(
                mSurfaceView.getSurfaceHolder(), previewRate);
        mFaceDectHandler.sendEmptyMessageDelayed(
                EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);
        startGoogleFaceDetect();
    }

    private void startGoogleFaceDetect() {
        Camera.Parameters params = mInterface.getCameraParams();
        if(params == null){
            googleFaceDetect = new GoogleFaceDetect(mCtx,
                    mFaceDectHandler);
            mFaceDectHandler.sendEmptyMessageDelayed(
                    EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);
            return;
        }
        if (params.getMaxNumDetectedFaces() > 0) {
            if (mFaceView != null) {
                mFaceView.clearFaces();
                mFaceView.setVisibility(View.VISIBLE);
            }
            mInterface.getCameraDevice().setFaceDetectionListener(googleFaceDetect);
            mInterface.getCameraDevice().startFaceDetection();
        }
    }

    private void stopGoogleFaceDetect() {
        if(mInterface.getCameraDevice() == null) {
            return;
        }
        mInterface.getCameraDevice().setFaceDetectionListener(null);
        mInterface.getCameraDevice().stopFaceDetection();
        mFaceView.clearFaces();
    }
}
