package com.timyrobot.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

import com.timyrobot.common.RobotServiceKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by zhangtingting on 15/8/8.
 */
public class SetActionActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(){
            @Override
            public void run() {
                createActionFile();
                createCmdFile();
            }
        }.start();
    }

    public void createCmdFile(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File fileDir = new File(Environment.getExternalStorageDirectory(),"RobotData");
            File fileCmd = new File(fileDir,"cmd.txt");
            try {
                if(!fileDir.exists()){
                    fileDir.mkdir();
                }
                if(!fileCmd.exists()){
                    fileCmd.createNewFile();
                }
                JSONObject cmdObject = new JSONObject();
                cmdObject.put("你好", createCmd("wave", "你好，我是小黑，请多执教", "smile"));
                cmdObject.put("跳舞", createCmd("dance", "那我就献丑了", "smile"));

                BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileCmd,false)));
                br.write(cmdObject.toString());
                br.flush();
                br.close();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public JSONObject createCmd(String action,String voice,String emotion){
        JSONObject object = new JSONObject();
        try {
            object.put(RobotServiceKey.CmdKey.ACTION,action);
            object.put(RobotServiceKey.CmdKey.VOICE,voice);
            object.put(RobotServiceKey.CmdKey.FACE,emotion);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public void createActionFile(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File fileDir = new File(Environment.getExternalStorageDirectory(),"RobotData");
            File fileAction = new File(fileDir,"action.txt");
            try {
                if(!fileDir.exists()){
                    fileDir.mkdir();
                }
                if(!fileAction.exists()){
                    fileAction.createNewFile();
                }
                JSONObject actionObject = new JSONObject();

                JSONArray array = new JSONArray();
                array.put(createSubAction("73,5,16,5,180,5,180,5,28,5,75,5,28,5,73,5,9,5", 1000));
                array.put(createSubAction("73,5,16,5,180,5,180,5,28,5,75,5,28,5,73,5,9,5", 1000));
                actionObject.put("wave", createAction(2, "0,0,0", array));

                BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileAction,false)));
                br.write(actionObject.toString());
                br.flush();
                br.close();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public JSONObject createAction(int actionNum,String statelight,JSONArray actions){
        JSONObject object = new JSONObject();
        try {
            object.put(RobotServiceKey.ActionKey.ACTION_NUM,actionNum);
            object.put(RobotServiceKey.ActionKey.STATELIGHT,statelight);
            object.put(RobotServiceKey.ActionKey.ACTIONS,actions);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }



    public JSONObject createSubAction(String position,long time){
        JSONObject object = new JSONObject();
        try {
            object.put(RobotServiceKey.ActionKey.TIME,time);
            object.put(RobotServiceKey.ActionKey.POSITION,position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
