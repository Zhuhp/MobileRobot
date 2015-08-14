package com.timyrobot.controlsystem;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.timyrobot.robot.RobotProxy;
import com.timyrobot.robot.bean.RobotAction;
import com.timyrobot.robot.bean.RobotSubAction;
import com.timyrobot.robot.data.RobotData;

import java.util.ArrayList;

/**
 * Created by zhangtingting on 15/8/8.
 */
public class RobotControl {

    public final static int CMD_ACTION = 0x0001;
    public final static int CMD_ACTION_END = 0x0002;

    Context mCtx;
    private boolean isNext = true;

    private Handler mCmdHandler;

    public RobotControl(Context context){
        mCtx = context;
        HandlerThread thread = new HandlerThread("RobotControl-cmd");
        thread.start();
        mCmdHandler = new Handler(thread.getLooper(), mCmdCB);
        doAction("stand");
    }

    public boolean next(){
        return isNext;
    }

    public void doAction(String action){
        isNext = false;
        //解析动作
        RobotAction rAction = RobotData.INSTANCE.getRobotAction(action);
        if(rAction == null){
            isNext = true;
            return;
        }
        //获取动作流
        ArrayList<RobotSubAction> mDatas = rAction.getActions();
        if((mDatas == null) || (mDatas.isEmpty())){
            isNext = true;
            return;
        }
        //间隔时间
        long totalTime = 0L;
        for(RobotSubAction subAction:mDatas){
            Message msg = new Message();
            msg.obj = subAction.getPosition();
            msg.what = CMD_ACTION;
            //发送动作，动作延时totalTime
            mCmdHandler.sendMessageDelayed(msg,totalTime);
            totalTime += subAction.getTime();
        }
        //发送动作结束信号
        mCmdHandler.sendEmptyMessageDelayed(CMD_ACTION_END, totalTime);
    }

    Handler.Callback mCmdCB = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case CMD_ACTION:
                    //通过蓝牙发送命令
                    String cmd = String.valueOf(msg.obj);
                    RobotProxy.INSTANCE.sendData(cmd);
                    break;
                case CMD_ACTION_END:
                    //动作结束，可以开始下一个动作
                    isNext = true;
                    break;
            }
            return false;
        }
    };



}
