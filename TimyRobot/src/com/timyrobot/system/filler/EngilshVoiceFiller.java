package com.timyrobot.system.filler;

import android.content.Context;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.timyrobot.common.AIConfig;
import com.timyrobot.system.bean.BaseCommand;
import com.timyrobot.system.triggersystem.TriggerSwitch;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ai.api.AIConfiguration;
import ai.api.GsonFactory;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Fulfillment;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.ui.AIDialog;

/**
 * Created by zhangtingting on 15/10/12.
 */
public class EngilshVoiceFiller implements IFiller ,AIDialog.AIDialogListener{

    private Context mCtx;
    private IFiller mFiller;
    //当前需要填充的命令
    private BaseCommand mCurCommand;

    private AIDialog mAIDialog;
    private Gson mGson = GsonFactory.getGson();
    private ExecutorService mThreadService = Executors.newSingleThreadExecutor();


    public EngilshVoiceFiller(Context context, IFiller filler){
        mCtx = context;
        mFiller = filler;
        final AIConfiguration config = new AIConfiguration(AIConfig.ACCESS_TOKEN,
                AIConfig.SUBSCRIPTION_KEY, AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.Speaktoit);
        mAIDialog = new AIDialog(mCtx, config);
        mAIDialog.setResultsListener(this);
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
        mAIDialog.showAndListen();
    }

    private void openSwitch(){
        TriggerSwitch.INSTANCE.setCanNext(true);
    }

    @Override
    public void onResult(AIResponse response) {
        String resStr = mGson.toJson(response);
        final Result result = response.getResult();
        final Metadata metadata = result.getMetadata();
        final HashMap<String, JsonElement> params = result.getParameters();
        Fulfillment fulfillment = result.getFulfillment();
        String voice = fulfillment.getSpeech();
        if(TextUtils.isEmpty(voice)){
            mCurCommand.setVoiceContent("sorry, unknown");
        }else {
            mCurCommand.setVoiceContent(voice);
        }
        mCurCommand.setVoiceReconContent(resStr);
        Log.d("EnglishVoiceFiller", resStr);
        if(mFiller != null) {
            mThreadService.execute(new Runnable() {
                @Override
                public void run() {
                    mFiller.fill(mCurCommand);
                }
            });
        }else{
            openSwitch();
        }
    }

    @Override
    public void onError(AIError error) {
        openSwitch();
    }

    @Override
    public void onCancelled() {
        openSwitch();
    }
}
