package com.timyrobot.ui.activity.remotecontrol.speech;

import android.content.Context;
import android.util.Log;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.timyrobot.service.speechrecongnizer.SpeechJsonParser;

/**
 * Created by zhangtingting on 15/11/26.
 */
public class FlySpeechRecon implements INormalSpeechRecon {

    private Context mContext;
    private RecognizerDialog mIatDialog;
    private ISpeechResult mResult;

    public FlySpeechRecon(Context context){
        mContext = context;
        mIatDialog = new RecognizerDialog(mContext, null);
        mIatDialog.setListener(mRecognizerListener);
    }

    /**
     * 语音识别的回调接口
     */
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
            String content = result.toString();
            if(mResult != null){
                mResult.onResult(content);
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            if(mResult != null){
                mResult.onResult(null);
            }
            if(mIatDialog.isShowing()){
                mIatDialog.dismiss();
            }
        }

    };


    @Override
    public void startRecongnizer() {
        if(!mIatDialog.isShowing()){
            mIatDialog.show();
        }
    }

    @Override
    public void setCallback(ISpeechResult callback) {
        mResult = callback;
    }
}
