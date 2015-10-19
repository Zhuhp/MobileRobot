package com.timyrobot.robot.data;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.timyrobot.common.Property;
import com.timyrobot.common.RobotServiceKey;
import com.timyrobot.httpcom.filedownload.FileDownload;
import com.timyrobot.robot.bean.RobotAction;
import com.timyrobot.robot.bean.RobotCmd;
import com.timyrobot.robot.bean.RobotFace;
import com.timyrobot.robot.bean.RobotSubAction;
import com.timyrobot.robot.bean.RobotSubFace;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by zhangtingting on 15/8/8.
 */
public enum RobotData {
    INSTANCE;
    private final static String TAG  = RobotData.class.getName();
    private Map<String,RobotAction> mAction;
    private Map<String,RobotCmd> mCmd;
    private Map<String,RobotFace> mFace;

    public void initRobotData(Context ctx, String name){
        Log.i(TAG, "Init Robot Data!!");
        mAction = new HashMap<>();
        mCmd = new HashMap<>();
        mFace = new HashMap<>();
        initRobotProperty(ctx, name);
        initCmd(ctx, name);
        initAction(ctx, name);
        initFace(ctx, name);
    }

    /**
     * 根据角色获取输入流，如果没有该角色的数据，就用默认的
     * @param context context
     * @param name 角色名，同时也是文件名
     * @param propertyName 配置文件名
     * @return 输入流
     */
    private InputStream getInputStreamByName(Context context, String name, String propertyName){
        try {
            InputStream stream = null;
            File file = FileDownload.getPropertyFileExist(name, propertyName);
            if (file != null) {
                stream = new FileInputStream(file);
            } else {
                stream = context.getAssets().open(propertyName);
            }
            return stream;
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initRobotProperty(Context ctx, String name){
        try {
            InputStream stream = getInputStreamByName(ctx, name, "robotproperty.txt");
            BufferedReader cmdBr = new BufferedReader(new InputStreamReader(stream));
            String line = null;
            while((line = cmdBr.readLine()) != null) {
                JSONObject object = new JSONObject(line);
                Property.VOICE_NAME = object.optString("voice_name");
                Property.VOICE_VOLUMN = object.optString("voice_volumn");
                Property.VOICE_SPEED = object.optString("voice_speed");
                Property.ROBOT_CODE = object.optInt("robot_code");
                Property.IS_RECEIVER = object.optBoolean("is_receiver");
            }
            cmdBr.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initCmd(Context ctx, String name){
        try {
            InputStream stream = getInputStreamByName(ctx, name, "cmd.txt");
            BufferedReader cmdBr = new BufferedReader(new InputStreamReader(stream));
            String line = null;
            while((line = cmdBr.readLine()) != null) {
                JSONObject object = new JSONObject(line);
                Iterator<String> keys = object.keys();
                if (keys == null) {
                    return;
                }
                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONObject cmd = object.optJSONObject(key);
                    if (cmd != null) {
                        RobotCmd rc = new RobotCmd();
                        rc.setAction(cmd.optString(RobotServiceKey.CmdKey.ACTION));
                        rc.setVoice(cmd.optString(RobotServiceKey.CmdKey.VOICE));
                        rc.setFace(cmd.optString(RobotServiceKey.CmdKey.FACE));
                        JSONObject sysObject = cmd.optJSONObject(RobotServiceKey.CmdKey.SYSTEM);
                        if(sysObject != null) {
                            rc.setSystem(sysObject.toString());
                        }
                        mCmd.put(key, rc);
                    }
                }
            }
            cmdBr.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void initFace(Context ctx, String name){
        try {
            InputStream stream = getInputStreamByName(ctx, name, "face.txt");
            BufferedReader faceBr = new BufferedReader(new InputStreamReader(stream));
            String line;
            while((line = faceBr.readLine()) != null) {
                JSONObject object = new JSONObject(line);
                Iterator<String> keys = object.keys();
                if (keys == null) {
                    return;
                }
                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONObject face = object.optJSONObject(key);
                    if (face != null) {
                        RobotFace af = new RobotFace();
                        af.setActionNum(face.optInt(RobotServiceKey.FaceKey.FACE_NUM));
                        JSONArray subAcArray = face.optJSONArray(RobotServiceKey.FaceKey.FACES);
                        if (subAcArray != null) {
                            ArrayList<RobotSubFace> robotFaces = new ArrayList<>();
                            for (int i = 0; i < subAcArray.length(); i++) {
                                JSONObject subAc = subAcArray.optJSONObject(i);
                                RobotSubFace subAction = new RobotSubFace();
                                subAction.setFaceName(subAc.optString(RobotServiceKey.FaceKey.FACE_NAME));
                                subAction.setTime(subAc.optLong(RobotServiceKey.FaceKey.TIME));
                                robotFaces.add(subAction);
                            }
                            af.setActions(robotFaces);
                        }
                        mFace.put(key, af);
                    }
                }
            }
            faceBr.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initAction(Context ctx, String name){
        try {
            InputStream stream = getInputStreamByName(ctx, name, "action.txt");
            BufferedReader actionBr = new BufferedReader(new InputStreamReader(stream));
            String line;
            while((line = actionBr.readLine()) != null) {
                JSONObject object = new JSONObject(line);
                Iterator<String> keys = object.keys();
                if (keys == null) {
                    return;
                }
                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONObject action = object.optJSONObject(key);
                    if (action != null) {
                        RobotAction ac = new RobotAction();
                        ac.setActionNum(action.optInt(RobotServiceKey.ActionKey.ACTION_NUM));
                        ac.setStateLight(action.optString(RobotServiceKey.ActionKey.STATELIGHT));
                        JSONArray subAcArray = action.optJSONArray(RobotServiceKey.ActionKey.ACTIONS);
                        if (subAcArray != null) {
                            ArrayList<RobotSubAction> robotActions = new ArrayList<>();
                            for (int i = 0; i < subAcArray.length(); i++) {
                                JSONObject subAc = subAcArray.optJSONObject(i);
                                RobotSubAction subAction = new RobotSubAction();
                                subAction.setPosition(subAc.optString(RobotServiceKey.ActionKey.POSITION));
                                subAction.setTime(subAc.optLong(RobotServiceKey.ActionKey.TIME));
                                robotActions.add(subAction);
                            }
                            ac.setActions(robotActions);
                        }
                        mAction.put(key, ac);
                        Log.i(TAG, "mAction put item->" + key + ":" + mAction.get(key));
                    }
                }
            }
            actionBr.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public RobotCmd getRobotCmd(String key){
        if(TextUtils.isEmpty(key)){
            return null;
        }
        Set<String> setKey = mCmd.keySet();
        String realKey = "";
        for(String s:setKey){
            if(key.contains(s)){
                realKey = s;
            }
        }
        if(!mCmd.containsKey(realKey)){
            return null;
        }
        return mCmd.get(realKey);
    }

    public RobotAction getRobotAction(String key){
        Log.d(TAG, "Get robot action for key:" + key);
        if(TextUtils.isEmpty(key)){
            return null;
        }
        if(!mAction.containsKey(key)){
            return null;
        }
        return mAction.get(key);
    }

    public RobotFace getRobotFace(String key){
        if(TextUtils.isEmpty(key)){
            return null;
        }
        if(!mFace.containsKey(key)){
            return null;
        }
        return mFace.get(key);
    }

    public String getRandomFace(){
        Set<String> data = mFace.keySet();
        int pos = new Random().nextInt(data.size());
        for(String faceTmp:data){
            if(pos == 0){
                return faceTmp;
            }
            pos--;
        }
        return null;
    }
}
