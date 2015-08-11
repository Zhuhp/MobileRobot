package com.timyrobot.triggersystem;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.timyrobot.common.ConstDefine;
import com.timyrobot.controlsystem.VoiceControl;
import com.timyrobot.listener.DataReceiver;
import com.timyrobot.service.speechrecongnizer.SpeechJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * Created by zhangtingting on 15/8/5.
 */
public enum VoiceTrigger {
    INSTANCE;

    public final static String TAG = VoiceTrigger.class.getName();

    private RecognizerDialog mIatDialog;
    private Context mCtx;
    private DataReceiver mReceiver;

    //判断是否有一个对话正在进行，有的话就不开起新的对话
    private boolean isConversion = false;

    //判断语音识别框是否有返回结果，如果点击屏幕，识别框会消失
    //导致对话提前结束，这是要设置isConversion为false
    private boolean hasResult = false;

    public void init(Context context, DataReceiver receiver){
        mCtx = context;
        mIatDialog = new RecognizerDialog(mCtx, null);
        mIatDialog.setListener(mRecognizerListener);
        mIatDialog.setOnDismissListener(mDismissListener);
        mReceiver = receiver;
    }

    /**
     *
     */
    public  synchronized void startConversation(){
        if((!isConversion) && (!mIatDialog.isShowing())&& (VoiceControl.isNext)){
            isConversion = true;
            hasResult = false;
            mIatDialog.show();
        }
    }

    /**
     * 语音识别的回调接口
     */
    private RecognizerDialogListener mRecognizerListener = new RecognizerDialogListener() {

        StringBuilder result = null;

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean isLast) {
            hasResult = true;
            Log.d(TAG, "onResult");
            if(result == null){
                result = new StringBuilder();
            }
            String parseResult = SpeechJsonParser.parseIatResult(
                    recognizerResult.getResultString());
            result.append(parseResult);
            Log.d(TAG,result.toString());
            Log.d(TAG, "isLast:" + isLast);
            if(!isLast){
                //并不是最后一次解析的数据
                return;
            }
            String content = result.toString();
            try {
                JSONObject data = new JSONObject();
                data.put(ConstDefine.TriggerDataKey.TYPE,ConstDefine.TriggerDataType.Voice);
                data.put(ConstDefine.TriggerDataKey.CONTENT,content);
                if(mReceiver != null){
                    mReceiver.onReceive(data.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                isConversion = false;
            }
            result = null;
        }

        @Override
        public void onError(SpeechError speechError) {
            if(mIatDialog.isShowing()){
                mIatDialog.dismiss();
            }
            isConversion = false;
        }

    };

    /**
     * 在语音对话框dismiss的时候，判断是否又语音识别的结果，如果没有，就认为对话结束
     */
    private DialogInterface.OnDismissListener mDismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            if(!hasResult){
                isConversion = false;
            }
        }
    };
}
