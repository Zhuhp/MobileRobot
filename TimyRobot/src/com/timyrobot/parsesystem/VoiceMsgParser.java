package com.timyrobot.parsesystem;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.TextUnderstander;
import com.iflytek.cloud.TextUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;
import com.timyrobot.bean.ControllCommand;
import com.timyrobot.listener.ParserResultReceiver;
import com.timyrobot.service.userintent.actionparse.ActionJsonParser;
import com.timyrobot.service.userintent.intentparser.IUserIntentParser;
import com.timyrobot.service.userintent.intentparser.UserIntentParserFactory;

import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/8/6.
 */
public class VoiceMsgParser implements IDataParse{

    public static final String TAG = VoiceMsgParser.class.getName();

    private TextUnderstander mUnderstander;
    private Context mCtx;
    private UserIntentParserFactory mUserIntentParser;
    private ParserResultReceiver mReceiver;


    public VoiceMsgParser(Context context, ParserResultReceiver receiver){
        mCtx = context;
        mUnderstander = TextUnderstander.createTextUnderstander(mCtx,null);
        mUserIntentParser = new UserIntentParserFactory();
        mReceiver = receiver;
    }

    @Override
    public void parse(String content) {
        if(TextUtils.isEmpty(content)){
            return;
        }
        mUnderstander.understandText(content,mUnderstanderListener);
    }

    /**
     * 语义识别的回调接口
     */
    private TextUnderstanderListener mUnderstanderListener = new TextUnderstanderListener() {
        @Override
        public void onResult(UnderstanderResult understanderResult) {
            String resultString = understanderResult.getResultString();
            try {
                JSONObject object = new JSONObject(resultString);
                if(ActionJsonParser.isSuccess(object)){
                    IUserIntentParser parser = mUserIntentParser.getParser(
                            ActionJsonParser.getService(object),
                            ActionJsonParser.getOperation(object));
                    ControllCommand command = parser.parseIntent(resultString);
                    if(mReceiver != null){
                        mReceiver.parseResult(command);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "UnderstanderResult:" + resultString);

        }

        @Override
        public void onError(SpeechError speechError) {
            speechError.printStackTrace();
            Log.d(TAG, "UnderstanderResult error:" + speechError.toString());
        }
    };


}
