package com.timyrobot.robot.debug.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 增加动作的动作集
 * Created by zhangtingting on 15/10/3.
 */
public class DebugActionList {

    private int mActionNum = -1;

    private ArrayList<DebugAction> mActions;

    public DebugActionList(){
        mActionNum = -1;
        mActions = new ArrayList<>();
    }

    public DebugAction getAction(int i){
        if(mActionNum < 0){
            throw new RuntimeException("action is null");
        }
        return mActions.get(i);
    }

    public int getActionNum() {
        return mActionNum + 1;
    }

    public void addAction(DebugAction action){
        if(action == null){
            return;
        }
        mActions.add(action);
        mActionNum++;
    }

    public void replaceAction(int index, DebugAction action){
        if(index > mActionNum){
            throw new RuntimeException("index is out of");
        }
        mActions.set(index, action);
    }

    /**
     * 回退到上一个动作
     * @param currentAction 当前Robot的动作
     * @return 动作命令
     */
    public JSONObject back(DebugAction currentAction){
        if(mActionNum < 0){
            throw new RuntimeException("no action now.");
        }
        DebugAction lastAction = mActions.get(mActionNum);
        mActions.remove(mActionNum);
        mActionNum--;
        return createAction(currentAction, lastAction);
    }

    /**
     * 生成动作
     * @param currentAction 当前动作
     * @param lastAction 上一个动作
     * @return 动作命令
     */
    public static JSONObject createAction(DebugAction currentAction, DebugAction lastAction){
        return null;
    }

    /**
     * 生成JsonArray
     * @return JsonArray
     */
    public JSONArray createJson(){
        if(mActionNum < 0){
            return null;
        }
        JSONArray array = new JSONArray();
        //TODO 初始动作
        DebugAction lastAction = new DebugAction();
        for(DebugAction currentAction:mActions){
            JSONObject action = createAction(currentAction, lastAction);
            lastAction = currentAction;
            array.put(action);
        }
        return array;
    }
}
