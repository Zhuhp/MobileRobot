package com.timyrobot.service.speechrecongnizer;

import android.content.Context;
import android.util.Log;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.tuling.util.GetTulingResultThread;
import com.tuling.util.ResultWatcher;
import com.tuling.util.TulingManager;

import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.transform.Result;

/**
 * Created by zhangtingting on 15/7/20.
 */
public class SpeechManager {

    public static final String TULING_KEY = "1deb50ef7d72b9e73907598a50abc60f";
    public static final String TAG = "tuling";

    private Context mCtx;
    private RecognizerDialog mIatDialog;
    private SpeechSynthesizer mSST;
    private TulingManager mTulingManager;

    public SpeechManager(Context ctx){
        mCtx = ctx;
        mIatDialog = new RecognizerDialog(mCtx, null);
        mIatDialog.setListener(mRecognizerListener);
        mSST = SpeechSynthesizer.createSynthesizer(mCtx, null);
        mSST.setParameter(SpeechConstant.VOICE_NAME, "xiaoyu");
        mSST.setParameter(SpeechConstant.SPEED, "35");
        mSST.setParameter(SpeechConstant.VOLUME, "80");
        mSST.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mTulingManager = new TulingManager(mCtx);
    }

    public void startConversation(){
        if((!mSST.isSpeaking()) && (!mIatDialog.isShowing())){
            mIatDialog.show();
        }
    }

    private RecognizerDialogListener mRecognizerListener = new RecognizerDialogListener() {

        StringBuilder result = null;

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean isLast) {
            if(result == null){
                result = new StringBuilder();
            }
            String parseResult = SpeechJsonParser.parseIatResult(
                    recognizerResult.getResultString());
            result.append(parseResult);
            if(!isLast){
                //并不是最后一次解析的数据
                return;
            }
            new GetTulingResultThread(TULING_KEY, result.toString(),mWatcher);
            result = null;
        }

        @Override
        public void onError(SpeechError speechError) {
            if(mIatDialog.isShowing()){
                mIatDialog.dismiss();
            }
        }
    };

    private ResultWatcher mWatcher = new ResultWatcher() {
        @Override
        public void onResults(String s) {
            Log.d(TAG, "tuling result:" + s);
            try {
                JSONObject object = new JSONObject(s);
                String result = object.getString("text");
                result = result.replace("br", "");
                if (!mSST.isSpeaking()) {
                    mSST.startSpeaking(result, null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
