package com.timyrobot.controlsystem;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.timyrobot.bean.ControllCommand;
import com.tuling.util.GetTulingResultThread;
import com.tuling.util.ResultWatcher;
import com.tuling.util.TulingManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/8/6.
 */
public class VoiceControl {

    public static final String TULING_KEY = "777e74f738a03b4d855fe611c3e7fcc3";
    public static final String TAG = "tuling";

    private Context mCtx;
    private SpeechSynthesizer mSST;
    private TulingManager mTulingManager;

    public static boolean isNext = true;

    public VoiceControl(Context ctx){
        mCtx = ctx;
        mSST = SpeechSynthesizer.createSynthesizer(mCtx, null);
        mSST.setParameter(SpeechConstant.VOICE_NAME, "xiaoyu");
        mSST.setParameter(SpeechConstant.SPEED, "35");
        mSST.setParameter(SpeechConstant.VOLUME, "80");
        mSST.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);

        mTulingManager = new TulingManager(mCtx);
    }

    public boolean next(){
        return isNext;
    }

    public void response(ControllCommand cmd){
        isNext = false;
        String content = cmd.getVoiceContent();
        if(TextUtils.isEmpty(content)){
            isNext = true;
            return;
        }
        if(cmd.isNeedTuling()){
            new GetTulingResultThread(TULING_KEY, cmd.getVoiceContent(), mWatcher).start();
        }else{
            if (!mSST.isSpeaking()) {
                mSST.startSpeaking(content, mSynthesizerListener);
            }else{
                isNext = true;
            }
        }
    }

    /**
     * 图灵对话的回调接口
     */
    private ResultWatcher mWatcher = new ResultWatcher() {
        @Override
        public void onResults(String s) {
            Log.d(TAG, "tuling result:" + s);
            try {
                JSONObject object = new JSONObject(s);
                String result = object.getString("text");
                result = result.replace("br", "");
                if (!mSST.isSpeaking()) {
                    mSST.startSpeaking(result, mSynthesizerListener);
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            isNext = true;
        }
    };

    /**
     * 语音播放的回调接口
     */
    private SynthesizerListener mSynthesizerListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {

        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {

        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {

        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {

        }

        @Override
        public void onCompleted(SpeechError speechError) {
            isNext = true;
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };
}
