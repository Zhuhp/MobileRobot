package com.timyrobot.bean;

/**
 * 控制台需要的命令集合
 * Created by zhangtingting on 15/8/6.
 */
public class ControllCommand {
    private String emotionName;

    private String voiceContent;
    private boolean isNeedTuling;

    private String robotAction;

    private String systemOperator;

    private String cmd;

    private boolean isNeedEnd = false;

    public ControllCommand(String emotionName, String voiceContent, boolean isNeedTuling,
                           String robotAction, String systemOperator,String cmd) {
        this.emotionName = emotionName;
        this.voiceContent = voiceContent;
        this.isNeedTuling = isNeedTuling;
        this.robotAction = robotAction;
        this.systemOperator = systemOperator;
        this.cmd = cmd;
    }

    public boolean isNeedEnd(){
        return isNeedEnd;
    }

    public void setNeedEnd(boolean isNeedEnd){
        this.isNeedEnd = isNeedEnd;
    }

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
}
