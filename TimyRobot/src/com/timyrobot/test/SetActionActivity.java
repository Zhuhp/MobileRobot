package com.timyrobot.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

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
    private static final String TAG = "SetActionActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "Create Setting Files!!!");
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
                BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileCmd,false)));
                br.write(createCmd("你是谁", null, "我叫小黑，英文名字，叫肖嘿，我是人类，人类，人类，重要的事情，要说三遍", "blink").toString());
                br.newLine();
                br.write(createCmd("名字", null, "我叫小黑，英文名字，叫肖嘿", "blink").toString());
                br.newLine();
                br.write(createCmd("你好", "wave", "你好，我是小黑，请多执教", "laugh").toString());
                br.newLine();
                br.write(createCmd("爸爸", "slide", "我的爸爸们是帅气的红富士团队", "laugh").toString());
                br.newLine();
                br.write(createCmd("站好", "stand", "是", "blink").toString());
                br.newLine();
                br.write(createCmd("跳舞", "dance", "那我就献丑了", "laugh").toString());
                br.newLine();
                br.write(createCmd("摇头", "shake_head", "哦，好", "blink").toString());
                br.newLine();
                br.write(createCmd("拍手", "five", "来，吧啦啦啦", "blink").toString());
                br.newLine();
                br.write(createCmd("自我介绍", "jieshao", "大家好，我是小黑，可能主人觉得我不像大白那么大，那么白，所以就叫我小黑了。我是一个手机机器人，能听，会说，好动，爱讲笑话。大家等下可以过来，找我玩呀。", "blink").toString());
                br.newLine();
                br.write(createCmd("你害不害怕", "fear", "呀，我好害怕啊", "fear").toString());
                br.newLine();
                br.write(createCmd("走两步可以吗", "shake_head", "我很忙的，没空搭理你", "blink").toString());
                br.newLine();
                br.write(createCmd("给我走两步", "walk2step", "你早这么说不就完了吗,怪我咯", "blink").toString());
                br.newLine();
                br.write(createCmd("现在几点", "shake_head", "你有手机，不会自己看吗", "blink").toString());
                br.newLine();
                br.write(createCmd("手机", "chayao", "你才是手机，你全家都是手机", "hums").toString());
                br.newLine();
                br.write(createCmd("现在到底几点了", null, "两点半，不客气", "blink").toString());
                br.newLine();
                br.write(createCmd("我喜欢你", null, "吓死本宝宝了，我是男生啊", "fear").toString());
                br.flush();
                br.close();
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public JSONObject createCmd(String name, String action, String voice, String emotion){
        JSONObject object = new JSONObject();
        JSONObject cmdObject = new JSONObject();
        try {
            object.put(RobotServiceKey.CmdKey.ACTION, action);
            object.put(RobotServiceKey.CmdKey.VOICE, voice);
            object.put(RobotServiceKey.CmdKey.FACE, emotion);
            cmdObject.put(name,object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cmdObject;
    }

    public void createActionFile(){
        Log.e(TAG,"createActionFile");
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
                BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileAction,false)));
                JSONObject actionObject = new JSONObject();

                JSONArray array = new JSONArray();
                array.put(createSubAction("C82,3,10,3,170,3,145,3,45,3,77,3,82,3,90,3,82,3", 0));
                actionObject.put("stand", createAction(2, "0,0,0", array));
                br.write(actionObject.toString());
                br.newLine();

                JSONArray array2 = new JSONArray();
                actionObject = new JSONObject();
                array2.put(createSubAction("D0,50,2,1,10,2,2,170,2,3,145,2,4,45,2", 1000));
                array2.put(createSubAction("D0,120,2,1,10,2,2,170,2,3,145,2,4,45,2", 1000));
                array2.put(createSubAction("D0,50,2,1,10,2,2,170,2,3,145,2,4,45,2", 1000));
                array2.put(createSubAction("D0,82,2,1,10,2,2,170,2,3,145,2,4,45,2", 1000));
                actionObject.put("shake_head", createAction(4, "0,0,0", array2));
                br.write(actionObject.toString());
                br.newLine();

                JSONArray array3 = new JSONArray();
                actionObject = new JSONObject();
                array3.put(createSubAction("D0,82,3,1,10,3,2,170,3,3,145,3,4,45,3", 1000));
                array3.put(createSubAction("A1,127,2", 500));
                array3.put(createSubAction("A3,90,2", 500));
                array3.put(createSubAction("A3,150,2", 500));
                array3.put(createSubAction("A3,90,2", 500));
                array3.put(createSubAction("A3,150,2", 500));
                array3.put(createSubAction("A3,90,2", 500));
                array3.put(createSubAction("D0,82,3,1,10,3,2,170,3,3,145,3,4,45,3", 1000));
                actionObject.put("wave", createAction(8, "0,0,0", array3));
                br.write(actionObject.toString());
                br.newLine();

                JSONArray array4 = new JSONArray();
                actionObject = new JSONObject();
                array4.put(createSubAction("D0,82,2,1,10,2,2,170,2,3,145,2,4,45,2", 500));
                array4.put(createSubAction("A1,120,2", 500));
                array4.put(createSubAction("A1,90,3", 1500));
                array4.put(createSubAction("A1,30,2", 800));
                array4.put(createSubAction("A1,90,3", 1500));
                array4.put(createSubAction("A3,80,2", 800));
                array4.put(createSubAction("A3,145,3", 1500));
                array4.put(createSubAction("A1,150,2", 100));
                array4.put(createSubAction("A1,10,2", 1000));
                actionObject.put("five", createAction(8, "0,0,0", array4));
                br.write(actionObject.toString());
                br.newLine();

                JSONArray array5 = new JSONArray();
                actionObject = new JSONObject();
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
                br.write(actionObject.toString());
                br.newLine();

                JSONArray array6 = new JSONArray();
                actionObject = new JSONObject();
                array6.put(createSubAction("D0,60,2,1,10,2,2,170,2,3,145,2,4,45,2", 3000));
                array6.put(createSubAction("D0,82,2,1,10,2,2,170,2,3,145,2,4,45,2", 1000));
                actionObject.put("A0_60_2", createAction(2, "0,0,0", array6));
                br.write(actionObject.toString());
                br.newLine();

                JSONArray array7 = new JSONArray();
                actionObject = new JSONObject();
                array7.put(createSubAction("D0,70,2,1,10,2,2,170,2,3,145,2,4,45,2", 3000));
                array7.put(createSubAction("D0,82,2,1,10,2,2,170,2,3,145,2,4,45,2", 1000));
                actionObject.put("A0_70_2", createAction(2, "0,0,0", array7));
                br.write(actionObject.toString());
                br.newLine();

                JSONArray array8 = new JSONArray();
                actionObject = new JSONObject();
                array8.put(createSubAction("D0,100,2,1,10,2,2,170,2,3,145,2,4,45,2", 3000));
                array8.put(createSubAction("D0,82,2,1,10,2,2,170,2,3,145,2,4,45,2", 1000));
                actionObject.put("A0_100_2", createAction(2, "0,0,0", array8));
                br.write(actionObject.toString());
                br.newLine();

                JSONArray array9 = new JSONArray();
                actionObject = new JSONObject();
                array9.put(createSubAction("D0,110,2,1,10,2,2,170,2,3,145,2,4,45,2", 3000));
                array9.put(createSubAction("D0,82,2,1,10,2,2,170,2,3,145,2,4,45,2", 1000));
                actionObject.put("A0_110_2", createAction(2, "0,0,0", array9));
                br.write(actionObject.toString());
                br.newLine();

                JSONArray array10 = new JSONArray();
                actionObject = new JSONObject();
                array10.put(createSubAction("D1,40,2,2,140,2", 2000));
                array10.put(createSubAction("D1,10,2,2,170,2", 1000));
                actionObject.put("handup1", createAction(2, "0,0,0", array10));
                br.write(actionObject.toString());
                br.newLine();

                JSONArray array11 = new JSONArray();
                actionObject = new JSONObject();
                array11.put(createSubAction("C82,3,10,3,170,3,145,3,45,3,77,3,82,3,90,3,82,3", 1000));
                array11.put(createSubAction("D7,105,4,8,125,2", 1000));
                array11.put(createSubAction("D5,56,2,6,52,2,8,100,2", 1000));
                array11.put(createSubAction("D7,90,2,8,82,2", 1000));
                array11.put(createSubAction("D7,50,2,8,70,4", 1000));
                array11.put(createSubAction("D5,102,2,6,109,2,7,80,2", 1000));
                array11.put(createSubAction("D7,90,2,8,82,2", 1000));
                array11.put(createSubAction("D7,105,4,8,125,2", 1000));
                array11.put(createSubAction("D5,77,2,6,82,2,8,100,2", 1000));
                array11.put(createSubAction("C82,3,10,3,170,3,145,3,45,3,77,3,82,3,90,3,82,3", 1000));
                actionObject.put("walk2step", createAction(10, "0,0,0", array11));
                br.write(actionObject.toString());
                br.newLine();

                JSONArray array12 = new JSONArray();
                actionObject = new JSONObject();
                array12.put(createSubAction("C99,2,3,2,180,2,124,2,39,2,91,2,91,2,75,2,91,2", 2000));
                array12.put(createSubAction("D1,33,3,2,150,3,3,90,3,4,70,3", 2000));
                array12.put(createSubAction("D1,3,3,2,175,3,3,160,2,4,16,2", 1000));
                actionObject.put("chayao", createAction(3, "0,0,0", array12));
                br.write(actionObject.toString());
                br.newLine();

                JSONArray array13 = new JSONArray();
                actionObject = new JSONObject();
                array13.put(createSubAction("C82,3,10,3,170,3,145,3,45,3,77,3,82,3,90,3,82,3", 2000));
                array13.put(createSubAction("D2,158,2,3,93,2,4,32,2", 1000));
                array13.put(createSubAction("A2,135,2", 1200));
                array13.put(createSubAction("A3,145,2", 1000));
                array13.put(createSubAction("A3,88,2", 1500));
                array13.put(createSubAction("A3,145,2", 1000));
                array13.put(createSubAction("A3,88,2", 1000));
                array13.put(createSubAction("D1,90,2,2,90,2,4,88,2", 1000));
                array13.put(createSubAction("D1,70,2,4,50,2", 800));
                array13.put(createSubAction("D1,80,3,2,70,2,4,88,2", 1000));
                array13.put(createSubAction("D1,70,2,4,50,2", 1000));
                array13.put(createSubAction("D3,88,2,4,88,2,7,55,3,8,125,3", 1000));
                array13.put(createSubAction("D0,60,3,1,140,3,2,38,3", 2000));
                array13.put(createSubAction("D0,120,3,2,104,1", 1000));
                array13.put(createSubAction("C82,3,10,3,170,3,145,3,45,3,77,3,82,3,90,3,82,3", 1000));
                actionObject.put("jieshao", createAction(15, "0,0,0", array13));
                br.write(actionObject.toString());
                br.newLine();

                JSONArray array14 = new JSONArray();
                actionObject = new JSONObject();
                array14.put(createSubAction("C82,3,10,3,170,3,145,3,45,3,77,3,82,3,90,3,82,3", 1000));
                array14.put(createSubAction("D7,60,3,8,60,3,1,180,1,2,0,1", 1500));
                array14.put(createSubAction("C82,3,10,3,170,3,145,3,45,3,77,3,82,3,90,3,82,3", 1000));
                actionObject.put("fear", createAction(3, "0,0,0", array12));
                br.write(actionObject.toString());
                br.newLine();

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
                BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileAction,false)));
                JSONObject actionObject = new JSONObject();

                JSONArray array = new JSONArray();
                array.put(createSubFace("blink", 200));
                array.put(createSubFace("laugh", 3000));
                actionObject.put("blink", createFace(2, array));
                br.write(actionObject.toString());
                br.newLine();
                JSONArray array2 = new JSONArray();

                actionObject = new JSONObject();
                array2.put(createSubFace("fear", 1000));
                array2.put(createSubFace("hums", 1000));
                actionObject.put("fear", createFace(2, array2));
                br.write(actionObject.toString());
                br.newLine();


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
            object.put(RobotServiceKey.FaceKey.FACE_NUM,actionNum);
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
