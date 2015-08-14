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
                createFaceFile();
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
                cmdObject.put("你好", createCmd("wave", "你好，我是小黑，请多执教", "laugh"));
                cmdObject.put("跳舞", createCmd("dance", "那我就献丑了", "laugh"));
                cmdObject.put("摇头", createCmd("shake_head", "哦，好", "blink"));
                cmdObject.put("拍手", createCmd("five", "来，吧啦啦啦", "blink"));
                cmdObject.put("扭两下", createCmd("slide", "来，吧啦啦啦啦啦啦啦", "laugh"));

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
                array.put(createSubAction("C82,3,10,3,170,3,145,3,45,3,77,3,82,3,90,3,82,3", 0));
                actionObject.put("stand", createAction(2, "0,0,0", array));

                JSONArray array2 = new JSONArray();
                array2.put(createSubAction("D0,50,2,1,10,2,2,170,2,3,145,2,4,45,2", 1000));
                array2.put(createSubAction("D0,120,2,1,10,2,2,170,2,3,145,2,4,45,2", 1000));
                array2.put(createSubAction("D0,50,2,1,10,2,2,170,2,3,145,2,4,45,2", 1000));
                array2.put(createSubAction("D0,82,2,1,10,2,2,170,2,3,145,2,4,45,2", 0));
                actionObject.put("shake_head", createAction(4, "0,0,0", array2));

                JSONArray array3 = new JSONArray();
                array3.put(createSubAction("D0,82,3,1,10,3,2,170,3,3,145,3,4,45,3", 1000));
                array3.put(createSubAction("A1,127,2", 500));
                array3.put(createSubAction("A3,110,2", 500));
                array3.put(createSubAction("A3,160", 500));
                array3.put(createSubAction("A3,110,2", 500));
                array3.put(createSubAction("A3,160,2", 500));
                array3.put(createSubAction("A3,110,2", 500));
                array3.put(createSubAction("D0,82,3,1,10,3,2,170,3,3,145,3,4,45,3", 0));
                actionObject.put("wave", createAction(8, "0,0,0", array3));

                JSONArray array4 = new JSONArray();
                array4.put(createSubAction("D0,82,2,1,10,2,2,170,2,3,145,2,4,45,2", 500));
                array4.put(createSubAction("A1,120,2", 500));
                array4.put(createSubAction("A1,90,3", 1200));
                array4.put(createSubAction("A1,30,2", 800));
                array4.put(createSubAction("A1,90,2", 800));
                array4.put(createSubAction("A3,80,2", 800));
                array4.put(createSubAction("A3,145,3", 1200));
                array4.put(createSubAction("A1,120,2", 800));
                array4.put(createSubAction("A1,10,2", 0));
                actionObject.put("five", createAction(8, "0,0,0", array4));

                JSONArray array5 = new JSONArray();
                array5.put(createSubAction("C82,3,10,3,170,3,145,3,45,3,77,3,82,3,90,3,82,3", 1000));
                array5.put(createSubAction("D1,80,2,2,66,2,5,97,2,6,107,2", 1000));
                array5.put(createSubAction("D1,63,2,2,130,2,5,63,2,6,63,2", 1000));
                array5.put(createSubAction("D1,80,2,2,66,2,5,97,2,6,107,2", 1000));
                array5.put(createSubAction("D1,63,2,2,130,2,5,63,2,6,63,2", 1000));
                array5.put(createSubAction("D1,80,2,2,66,2,5,97,2,6,107,2", 1000));
                array5.put(createSubAction("D1,63,2,2,130,2,5,63,2,6,63,2", 1000));
                array5.put(createSubAction("D1,80,2,2,66,2,5,97,2,6,107,2", 1000));
                array5.put(createSubAction("D1,63,2,2,130,2,5,63,2,6,63,2", 1000));
                array5.put(createSubAction("C82,3,10,3,170,3,145,3,45,3,77,3,82,3,90,3,82,3", 1000));
                actionObject.put("slide", createAction(10, "0,0,0", array5));


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

    public void createFaceFile(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File fileDir = new File(Environment.getExternalStorageDirectory(),"RobotData");
            File fileAction = new File(fileDir,"face.txt");
            try {
                if(!fileDir.exists()){
                    fileDir.mkdir();
                }
                if(!fileAction.exists()){
                    fileAction.createNewFile();
                }
                JSONObject actionObject = new JSONObject();

                JSONArray array = new JSONArray();
                array.put(createSubFace("bling", 1000));
                array.put(createSubFace("laugh", 1000));
                actionObject.put("wave", createFace(2, array));

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

    public JSONObject createFace(int actionNum,JSONArray actions){
        JSONObject object = new JSONObject();
        try {
            object.put(RobotServiceKey.FaceKey.FACE_NAME,actionNum);
            object.put(RobotServiceKey.FaceKey.FACES,actions);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }



    public JSONObject createSubFace(String faceName,long time){
        JSONObject object = new JSONObject();
        try {
            object.put(RobotServiceKey.FaceKey.TIME,time);
            object.put(RobotServiceKey.FaceKey.FACE_NAME,faceName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
