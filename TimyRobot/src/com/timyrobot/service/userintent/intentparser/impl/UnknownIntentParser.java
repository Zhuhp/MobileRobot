package com.timyrobot.service.userintent.intentparser.impl;

import com.timyrobot.system.bean.BaseCommand;
import com.timyrobot.robot.bean.RobotCmd;
import com.timyrobot.robot.data.RobotData;
import com.timyrobot.service.userintent.actionparse.ActionJsonParser;
import com.timyrobot.service.userintent.intentparser.IUserIntentParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/7/24.
 */
public class UnknownIntentParser implements IUserIntentParser{
    @Override
    public boolean parseIntent(String result, BaseCommand command) {
        if(command == null){
            return false;
        }
        try {
            JSONObject object = new JSONObject(result);
            String text = ActionJsonParser.getText(object);
            RobotCmd cmd = RobotData.INSTANCE.getRobotCmd(text);
            if(cmd != null){
                command.setEmotionName(cmd.getFace());
                command.setVoiceContent(cmd.getVoice());
                command.setIsNeedTuling(false);
                command.setRobotAction(cmd.getAction());
                command.setSystemOperator(cmd.getSystem());
                command.setCmd(null);
            }else{
                command.setEmotionName(null);
                command.setVoiceContent(text);
                command.setIsNeedTuling(true);
                command.setRobotAction("unkonwn");
                command.setSystemOperator(result);
                command.setCmd(null);
            }

            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

}
