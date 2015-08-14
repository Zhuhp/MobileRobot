package com.timyrobot.service.userintent.intentparser.impl;

import com.timyrobot.bean.ControllCommand;
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
    public ControllCommand parseIntent(String result) {
        try {
            JSONObject object = new JSONObject(result);
            String text = ActionJsonParser.getText(object);
            RobotCmd cmd = RobotData.INSTANCE.getRobotCmd(text);
            ControllCommand command = null;
            if(cmd != null){
                command = new ControllCommand(cmd.getFace(),cmd.getVoice(),false,cmd.getAction(),null,null);
            }else{

                command = new ControllCommand(null,text,true,"unkonwn",result,null);
            }

            return command;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
