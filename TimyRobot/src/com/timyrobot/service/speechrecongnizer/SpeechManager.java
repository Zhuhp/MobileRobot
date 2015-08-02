package com.timyrobot.service.speechrecongnizer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.TextUnderstander;
import com.iflytek.cloud.TextUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.timyrobot.service.userintent.actionparse.Action;
import com.timyrobot.service.userintent.actionparse.ActionJsonParser;
import com.timyrobot.service.userintent.intentparser.IUserIntentParser;
import com.timyrobot.service.userintent.intentparser.UserIntentParserFactory;
import com.tuling.util.GetTulingResultThread;
import com.tuling.util.ResultWatcher;
import com.tuling.util.TulingManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/7/20.
 */
public class SpeechManager {

    public static final String TULING_KEY = "777e74f738a03b4d855fe611c3e7fcc3";
    public static final String TAG = "tuling";

    private Activity mCtx;
    private RecognizerDialog mIatDialog;
    private SpeechSynthesizer mSST;
    private TextUnderstander mUnderstander;
    private TulingManager mTulingManager;
    private UserIntentParserFactory mUserIntentParser;

    private ConversationListener mListener;

    //判断是否有一个对话正在进行，有的话就不开起新的对话
    private boolean isConversion = false;

    //判断语音识别框是否有返回结果，如果点击屏幕，识别框会消失
    //导致对话提前结束，这是要设置isConversion为false
    private boolean hasResult = false;

    public SpeechManager(Activity ctx,ConversationListener listener){
        mCtx = ctx;
        mIatDialog = new RecognizerDialog(mCtx, null);
        mIatDialog.setListener(mRecognizerListener);
        mIatDialog.setOnDismissListener(mDismissListener);
        mSST = SpeechSynthesizer.createSynthesizer(mCtx, null);
        mSST.setParameter(SpeechConstant.VOICE_NAME, "xiaoyu");
        mSST.setParameter(SpeechConstant.SPEED, "35");
        mSST.setParameter(SpeechConstant.VOLUME, "80");
        mSST.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mUnderstander = TextUnderstander.createTextUnderstander(mCtx,null);
        mTulingManager = new TulingManager(mCtx);
        mUserIntentParser = new UserIntentParserFactory(mCtx);
        mListener = listener;
    }

    /**
     * 开始一次对话
     */
    public void startConversation(){
        if((!isConversion) && (!mSST.isSpeaking()) && (!mIatDialog.isShowing())){
            isConversion = true;
            hasResult = false;
            mIatDialog.show();
        }
    }

    /**
     * 语义识别的回调接口
     */
    private TextUnderstanderListener mUnderstanderListener = new TextUnderstanderListener() {
        @Override
        public void onResult(UnderstanderResult understanderResult) {
            String resultString = understanderResult.getResultString();
            String userTalk = "";
            try {
                JSONObject object = new JSONObject(resultString);
                userTalk = ActionJsonParser.getText(object);
                if(ActionJsonParser.isSuccess(object)&&(mListener!=null)){
                    IUserIntentParser parser = mUserIntentParser.getParser(
                            ActionJsonParser.getService(object),
                            ActionJsonParser.getOperation(object));
                    parser.parseIntent(resultString);
                    if(!TextUtils.isEmpty(parser.getRobotTalkContent())){
                        userTalk = parser.getRobotTalkContent();
                    }
                    mListener.onUserIntent(parser);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG,"UnderstanderResult:"+resultString);
            if(!TextUtils.isEmpty(userTalk)) {
                new GetTulingResultThread(TULING_KEY, userTalk, mWatcher).start();
            }else{
                isConversion = false;
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            speechError.printStackTrace();
            Log.d(TAG, "UnderstanderResult error:" + speechError.toString());
            isConversion = false;
        }
    };

    /**
     * 语音识别的回调接口
     */
    private RecognizerDialogListener mRecognizerListener = new RecognizerDialogListener() {

        StringBuilder result = null;

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean isLast) {
            hasResult = true;
            Log.d(TAG,"onResult");
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
            if(mListener != null){
                mListener.onUserTalk(content);
            }
            mUnderstander.understandText(content, mUnderstanderListener);
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
                if(mListener != null){
                    mListener.onRobotTalk(result);
                }
                if (!mSST.isSpeaking()) {
                    mSST.startSpeaking(result, mSynthesizerListener);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                isConversion = false;
            }
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
            isConversion = false;
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    /**
     * 对话的监听流程
     */
    public interface ConversationListener{

        /**
         * 用户意图
         */
        void onUserIntent(IUserIntentParser action);

        /**
         * 用户谈话的内容
         * @param content
         */
        void onUserTalk(String content);

        /**
         * 机器人谈话的内容
         * @param content
         */
        void onRobotTalk(String content);
    }
}
