package com.timyrobot.system.filler;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.timyrobot.system.bean.BaseCommand;
import com.timyrobot.service.speechrecongnizer.SpeechJsonParser;
import com.timyrobot.system.triggersystem.TriggerSwitch;

/**
 * 语音识别内容填充器
 * Created by zhangtingting on 15/9/24.
 */
public class VoiceFiller implements IFiller{

    public final static String TAG = VoiceFiller.class.getName();

    private RecognizerDialog mIatDialog;
    private Context mCtx;
    private IFiller mFiller;
    //当前需要填充的命令
    private BaseCommand mCurCommand;

    //判断是否有一个对话正在进行，有的话就不开起新的对话
    private boolean isConversion = false;

    //判断语音识别框是否有返回结果，如果点击屏幕，识别框会消失
    //导致对话提前结束，这是要设置isConversion为false
    private boolean hasResult = false;

    public VoiceFiller(Context context, IFiller filler){
        mCtx = context;
        mIatDialog = new RecognizerDialog(mCtx, null);
        mIatDialog.setListener(mRecognizerListener);
        mIatDialog.setOnDismissListener(mDismissListener);
        mFiller = filler;
    }

    @Override
    public void fill(BaseCommand cmd) {
        mCurCommand = cmd;
        if(cmd == null){
            openSwitch();
            return;
        }
        //不需要语音识别
        if(!cmd.isNeedVoiceRecon()){
            //下一个填充器是空的
            if(mFiller == null){
                openSwitch();
                return;
            }
            //下一个填充器不为空，交于下一个处理
            mFiller.fill(cmd);
            return;
        }
        //需要语音识别
        startConversation();
    }

    /**
     * 开始语音对话
     */
    public  synchronized void startConversation(){
        if((!isConversion) && (!mIatDialog.isShowing())){
            isConversion = true;
            hasResult = false;
            mIatDialog.show();
            return;
        }
        //如果不能进行语音识别，就开启开关
        openSwitch();
    }

    private void openSwitch(){
        TriggerSwitch.INSTANCE.setCanNext(true);
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
            //填充语音识别的内容
            mCurCommand.setVoiceReconContent(content);
            isConversion = false;
            result = null;
            if(mFiller != null) {
                mFiller.fill(mCurCommand);
            }else{
                openSwitch();
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            if(mIatDialog.isShowing()){
                mIatDialog.dismiss();
            }
            isConversion = false;
            openSwitch();
        }

    };

    /**
     * 在语音对话框dismiss的时候，判断是否有语音识别的结果，如果没有，就认为对话结束
     */
    private DialogInterface.OnDismissListener mDismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            if(!hasResult){
                isConversion = false;
                openSwitch();
            }
        }
    };

}
