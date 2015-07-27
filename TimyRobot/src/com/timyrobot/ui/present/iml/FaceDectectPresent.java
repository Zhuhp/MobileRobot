package com.timyrobot.ui.present.iml;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Filter;

import com.example.robot.R;
import com.example.robot.facedection.CameraInterface;
import com.example.robot.facedection.CameraSurfaceView;
import com.example.robot.facedection.EventUtil;
import com.example.robot.facedection.FaceView;
import com.example.robot.facedection.GoogleFaceDetect;
import com.timyrobot.listener.FaceDetectListener;
import com.timyrobot.ui.present.IFaceDectectPresent;

/**
 * Created by zhangtingting on 15/7/27.
 */
public class FaceDectectPresent implements IFaceDectectPresent{

    Context mCtx;

    CameraSurfaceView mSurfaceView = null;
    FaceView mFaceView;
    float previewRate = -1f;
    private FaceDectHandler mFaceDectHandler = null;
    GoogleFaceDetect googleFaceDetect = null;

    private FaceDetectListener mListener;

    public FaceDectectPresent(Context context,FaceDetectListener listener){
        mCtx = context;
        mListener = listener;
    }

    @Override
    public void initFaceDectect(CameraSurfaceView surfaceView,FaceView faceView) {
        mFaceDectHandler = new FaceDectHandler();
        mSurfaceView = surfaceView;
        mFaceView = faceView;
    }

    @Override
    public void startDetect() {
        googleFaceDetect = new GoogleFaceDetect(mCtx,
                mFaceDectHandler);
        mFaceDectHandler.sendEmptyMessageDelayed(
                EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);
    }

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

                    int position = faces[0].rect.left
                            + (faces[0].rect.right - faces[0].rect.left) / 2;

                    if ((faces[0].rect.right - faces[0].rect.left) > 700) {
                        if(mListener != null){
                            mListener.onFaceDetect();
                        }
                        Log.d("facedetect", "dectect");
                    }
                    // Log.d(TAG, "positon->"+position);
                    String data = "";
                    if (position > 0) {
                        if (position <= 400)
                            data = "c";
                        else if (position <= 700)
                            data = "b";
                        else if (position <= 1000)
                            data = "a";
                    } else {
                        if (position >= -400)
                            data = "d";
                        else if (position >= -700)
                            data = "e";
                        else if (position >= 1000)
                            data = "f";
                    }
                    // if (bConnected)
                    // sendBluetoothData(data);

                    break;
                case EventUtil.CAMERA_HAS_STARTED_PREVIEW:
                    startGoogleFaceDetect();
                    break;
            }
            super.handleMessage(msg);
        }

    }

    private void restartPreviewCamera() {
        stopGoogleFaceDetect();
        CameraInterface.getInstance().doStartPreview(
                mSurfaceView.getSurfaceHolder(), previewRate);
        mFaceDectHandler.sendEmptyMessageDelayed(
                EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);
        startGoogleFaceDetect();
    }

    private void startGoogleFaceDetect() {
        Camera.Parameters params = CameraInterface.getInstance()
                .getCameraParams();
        if (params.getMaxNumDetectedFaces() > 0) {
            if (mFaceView != null) {
                mFaceView.clearFaces();
                mFaceView.setVisibility(View.VISIBLE);
            }
            CameraInterface.getInstance().getCameraDevice()
                    .setFaceDetectionListener(googleFaceDetect);
            CameraInterface.getInstance().getCameraDevice()
                    .startFaceDetection();
        }
    }

    private void stopGoogleFaceDetect() {
        Camera.Parameters params = CameraInterface.getInstance()
                .getCameraParams();
        if (params.getMaxNumDetectedFaces() > 0) {
            CameraInterface.getInstance().getCameraDevice()
                    .setFaceDetectionListener(null);
            CameraInterface.getInstance().getCameraDevice().stopFaceDetection();
            mFaceView.clearFaces();
        }
    }
}
