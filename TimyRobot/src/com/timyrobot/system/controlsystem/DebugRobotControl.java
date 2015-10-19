package com.timyrobot.system.controlsystem;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.timyrobot.robot.debug.bean.DebugAction;
import com.timyrobot.robot.debug.bean.DebugActionList;
import com.timyrobot.system.controlsystem.listener.EndListener;
import com.timyrobot.system.controlsystem.listener.NextListener;

/**
 * Created by zhangtingting on 15/10/4.
 */
public class DebugRobotControl {

    public final static int CMD_ACTION = 0x0001;
    public final static int CMD_ACTION_END = 0x0002;

    Context mCtx;

    private Handler mCmdHandler;

    private EndListener mEndListener;
    private NextListener mNextListener;

    public DebugRobotControl(Context context){
        mCtx = context;
        HandlerThread thread = new HandlerThread("DebugRobotControl-cmd");
        thread.start();
        mCmdHandler = new Handler(thread.getLooper(), mCmdCB);
    }

    public void setEndListener(EndListener listener) {
        mEndListener = listener;
    }
    public void setNextListener(NextListener listener) {
        mNextListener = listener;
    }

    public void doAction(DebugActionList action, int startPosition, int endPosition){

        //间隔时间
        long totalTime = 0L;
        for(int  i=startPosition; i<endPosition;i++){
            Message msg = new Message();
            DebugAction subAction = action.getAction(i);
            msg.obj = "";
            msg.what = CMD_ACTION;
            //发送动作，动作延时totalTime
            mCmdHandler.sendMessageDelayed(msg,totalTime);
            totalTime += subAction.time;
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
//                    String cmd = String.valueOf(msg.obj);
//                    RobotProxy.INSTANCE.sendData(cmd);
                    mNextListener.next();
                    break;
                case CMD_ACTION_END:
                    //动作结束，可以开始下一个动作
                    mEndListener.onEnd();
                    break;
            }
            return false;
        }
    };
}
