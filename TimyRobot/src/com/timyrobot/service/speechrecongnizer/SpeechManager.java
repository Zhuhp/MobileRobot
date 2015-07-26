package com.timyrobot.service.speechrecongnizer;

import android.content.Context;
import android.util.Log;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.TextUnderstander;
import com.iflytek.cloud.TextUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
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

    private Context mCtx;
    private RecognizerDialog mIatDialog;
    private SpeechSynthesizer mSST;
    private TextUnderstander mUnderstander;
    private TulingManager mTulingManager;

    private ConversationListener mListener;

    public SpeechManager(Context ctx,ConversationListener listener){
        mCtx = ctx;
        mIatDialog = new RecognizerDialog(mCtx, null);
        mIatDialog.setListener(mRecognizerListener);
        mSST = SpeechSynthesizer.createSynthesizer(mCtx, null);
        mSST.setParameter(SpeechConstant.VOICE_NAME, "xiaoyu");
        mSST.setParameter(SpeechConstant.SPEED, "35");
        mSST.setParameter(SpeechConstant.VOLUME, "80");
        mSST.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mUnderstander = TextUnderstander.createTextUnderstander(mCtx,null);
        mTulingManager = new TulingManager(mCtx);
        mListener = listener;
    }

    public void startConversation(){
        if((!mSST.isSpeaking()) && (!mIatDialog.isShowing())){
            mIatDialog.show();
        }
    }

    private TextUnderstanderListener mUnderstanderListener = new TextUnderstanderListener() {
        @Override
        public void onResult(UnderstanderResult understanderResult) {

        }

        @Override
        public void onError(SpeechError speechError) {

        }
    };

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
            new GetTulingResultThread(TULING_KEY, content, mWatcher).start();
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
                if(mListener != null){
                    mListener.onRobotTalk(result);
                }
                if (!mSST.isSpeaking()) {
                    mSST.startSpeaking(result, null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 对话的监听流程
     */
    public interface ConversationListener{

        /**
         * 用户意图
         */
        void onUserIntent();

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
