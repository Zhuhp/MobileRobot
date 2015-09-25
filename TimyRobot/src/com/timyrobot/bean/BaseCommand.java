package com.timyrobot.bean;

/**
 * 控制台需要的命令集合
 * Created by zhangtingting on 15/8/6.
 */
public abstract class BaseCommand {
    protected String emotionName;

    //语音识别的内容
    protected String voiceReconContent;

    //最后将播放出的语音
    protected String voiceContent;
    //是否需要图灵进行问答
    protected boolean isNeedTuling;

    //是否需要语音识别
    protected boolean isNeedVoiceRecon;

    //是否需要语义识别
    protected boolean isNeedUnderstandText;

    protected String robotAction;

    protected String systemOperator;

    protected String cmd;

    public BaseCommand(){
        initCommand();
    }

    /**
     * 由子类继承该方法，生成不一样得执行命令
     */
    protected abstract void initCommand();

    /**
     *
     * @param content 语音识别的内容
     */
    public abstract void parseVoiceContent(String content);


    public String getEmotionName() {
        return emotionName;
    }

    public void setEmotionName(String emotionName) {
        this.emotionName = emotionName;
    }

    public String getVoiceContent() {
        return voiceContent;
    }

    public void setVoiceContent(String voiceContent) {
        this.voiceContent = voiceContent;
    }

    public boolean isNeedTuling() {
        return isNeedTuling;
    }

    public void setIsNeedTuling(boolean isNeedTuling) {
        this.isNeedTuling = isNeedTuling;
    }

    public String getRobotAction() {
        return robotAction;
    }

    public void setRobotAction(String robotAction) {
        this.robotAction = robotAction;
    }

    public String getSystemOperator() {
        return systemOperator;
    }

    public void setSystemOperator(String systemOperator) {
        this.systemOperator = systemOperator;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public boolean isNeedVoiceRecon() {
        return isNeedVoiceRecon;
    }

    public void setIsNeedVoiceRecon(boolean isNeedVoiceRecon) {
        this.isNeedVoiceRecon = isNeedVoiceRecon;
    }

    public String getVoiceReconContent() {
        return voiceReconContent;
    }

    public void setVoiceReconContent(String voiceReconContent) {
        this.voiceReconContent = voiceReconContent;
    }

    public boolean isNeedUnderstandText() {
        return isNeedUnderstandText;
    }

    public void setIsNeedUnderstandText(boolean isNeedUnderstandText) {
        this.isNeedUnderstandText = isNeedUnderstandText;
    }
}
