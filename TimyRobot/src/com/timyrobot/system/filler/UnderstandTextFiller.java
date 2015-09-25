package com.timyrobot.system.filler;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.TextUnderstander;
import com.iflytek.cloud.TextUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;
import com.timyrobot.system.bean.BaseCommand;
import com.timyrobot.service.userintent.actionparse.ActionJsonParser;
import com.timyrobot.service.userintent.intentparser.IUserIntentParser;
import com.timyrobot.service.userintent.intentparser.UserIntentParserFactory;
import com.timyrobot.system.triggersystem.TriggerSwitch;

import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/9/24.
 */
public class UnderstandTextFiller implements IFiller{

    public static final String TAG = UnderstandTextFiller.class.getName();

    private TextUnderstander mUnderstander;
    private Context mCtx;
    private UserIntentParserFactory mUserIntentParser;
    IFiller mFiller;
    private BaseCommand mCurCommand;


    public UnderstandTextFiller(Context context, IFiller filler){
        mCtx = context;
        mUnderstander = TextUnderstander.createTextUnderstander(mCtx,null);
        mUserIntentParser = new UserIntentParserFactory();
        mFiller = filler;
    }

    private void openSwitch(){
        TriggerSwitch.INSTANCE.setCanNext(true);
    }

    @Override
    public void fill(BaseCommand cmd) {
        mCurCommand = cmd;
        if(cmd == null){
            openSwitch();
            return;
        }
        //不需要语义识别
        if(!cmd.isNeedUnderstandText()){
            //下一个填充器是空的
            if(mFiller == null){
                openSwitch();
                return;
            }
            //下一个填充器不为空，交于下一个处理
            mFiller.fill(cmd);
            return;
        }

        String content = cmd.getVoiceReconContent();
        //需要语义识别，但是内容为空，就不执行了
        if(TextUtils.isEmpty(content)){
            openSwitch();
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
                IUserIntentParser parser = mUserIntentParser.getParser(
                        ActionJsonParser.getService(object),
                        ActionJsonParser.getOperation(object));
                boolean isSuccess = parser.parseIntent(resultString, mCurCommand);
                if(isSuccess && (mFiller != null)){
                    mFiller.fill(mCurCommand);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            openSwitch();
            Log.d(TAG, "UnderstanderResult:" + resultString);

        }

        @Override
        public void onError(SpeechError speechError) {
            speechError.printStackTrace();
            openSwitch();
            Log.d(TAG, "UnderstanderResult error:" + speechError.toString());
        }
    };

}
