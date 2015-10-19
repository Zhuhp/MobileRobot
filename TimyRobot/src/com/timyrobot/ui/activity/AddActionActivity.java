package com.timyrobot.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.robot.R;
import com.timyrobot.robot.debug.bean.DebugAction;
import com.timyrobot.robot.debug.bean.DebugActionList;
import com.timyrobot.robot.debug.bean.Engine;
import com.timyrobot.system.controlsystem.DebugRobotControl;
import com.timyrobot.system.controlsystem.listener.EndListener;
import com.timyrobot.system.controlsystem.listener.NextListener;
import com.timyrobot.utils.ToastUtils;

/**
 * Created by zhangtingting on 15/10/3.
 */
public class AddActionActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener,
        EndListener, NextListener{

    private SeekBar seekBar1, seekBar2, seekBar3, seekBar4, seekBar5, seekBar6, seekBar7, seekBar8, seekBar9;
    private ListView mActionsListView;
    private Button mAddBtn, mEditBtn, mSaveBtn, mQuitEditBtn, mDebugBtn, mQuitDebugBtn;

    private Button mDebugNextBtn, mDebugRunBtn, mDebugLastBtn;
    private View mDebugLayout;
    private DebugRobotControl mDebugControl;
    private int mDebugPosition = 0;

    private DebugActionList mActionList;
    private ActionAdapter mAdapter;

    private boolean isEdit = false;
    private boolean isDeubg = false;
    private int editPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_action);
        initView();
        mDebugControl = new DebugRobotControl(this);
        mDebugControl.setEndListener(this);
        mDebugControl.setNextListener(this);
    }

    private void initView(){
        seekBar1 = (SeekBar)findViewById(R.id.seekBar1);
        seekBar2 = (SeekBar)findViewById(R.id.seekBar2);
        seekBar3 = (SeekBar)findViewById(R.id.seekBar3);
        seekBar4 = (SeekBar)findViewById(R.id.seekBar4);
        seekBar5 = (SeekBar)findViewById(R.id.seekBar5);
        seekBar6 = (SeekBar)findViewById(R.id.seekBar6);
        seekBar7 = (SeekBar)findViewById(R.id.seekBar7);
        seekBar8 = (SeekBar)findViewById(R.id.seekBar8);
        seekBar9 = (SeekBar)findViewById(R.id.seekBar9);

        mDebugLayout = findViewById(R.id.ll_debug);
        mDebugLastBtn = (Button)findViewById(R.id.btn_debug_return);
        mDebugRunBtn = (Button)findViewById(R.id.btn_debug_run);
        mDebugNextBtn = (Button)findViewById(R.id.btn_debug_next);
        mDebugLastBtn.setOnClickListener(this);
        mDebugRunBtn.setOnClickListener(this);
        mDebugNextBtn.setOnClickListener(this);

        mAddBtn = (Button)findViewById(R.id.btn_add_action);
        mEditBtn = (Button)findViewById(R.id.btn_edit_action);
        mSaveBtn = (Button)findViewById(R.id.btn_save_action);
        mQuitEditBtn = (Button)findViewById(R.id.btn_quit_edit_action);
        mDebugBtn = (Button)findViewById(R.id.btn_debug_action);
        mQuitDebugBtn = (Button)findViewById(R.id.btn_quit_debug_action);
        mAddBtn.setOnClickListener(this);
        mEditBtn.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
        mQuitEditBtn.setOnClickListener(this);
        mDebugBtn.setOnClickListener(this);
        mQuitDebugBtn.setOnClickListener(this);

        mActionsListView = (ListView)findViewById(R.id.lv_actions);

        mActionList = new DebugActionList();
        mAdapter = new ActionAdapter(this, mActionList);
        mActionsListView.setAdapter(mAdapter);

        addActionState();
    }

    private void addActionState(){
        isEdit = false;
        isDeubg = false;
        mAddBtn.setEnabled(true);
        mEditBtn.setEnabled(true);
        mSaveBtn.setEnabled(false);
        mQuitEditBtn.setEnabled(false);
        mDebugBtn.setEnabled(true);
        mQuitDebugBtn.setEnabled(false);
        mActionsListView.setOnItemClickListener(null);
        mDebugLayout.setVisibility(View.GONE);
    }

    private void editActionState(){
        isEdit = true;
        isDeubg = false;
        mAddBtn.setEnabled(false);
        mEditBtn.setEnabled(false);
        mSaveBtn.setEnabled(true);
        mQuitEditBtn.setEnabled(true);
        mDebugBtn.setEnabled(false);
        mQuitDebugBtn.setEnabled(false);
        mActionsListView.setOnItemClickListener(this);
        mDebugLayout.setVisibility(View.GONE);
    }

    private void debugActionState(){
        if(mActionList.getActionNum() <= 0){
            ToastUtils.toastShort(this, R.string.str_action_empty);
            return;
        }
        isEdit = false;
        isDeubg = true;
        mAddBtn.setEnabled(false);
        mEditBtn.setEnabled(false);
        mSaveBtn.setEnabled(false);
        mQuitEditBtn.setEnabled(false);
        mDebugBtn.setEnabled(true);
        mQuitDebugBtn.setEnabled(true);
        mActionsListView.setOnItemClickListener(this);
        mDebugLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(isEdit) {
            editPosition = position;
            toPosition(editPosition);
        }else if(isDeubg){
            //TODO 回到该位置
            mDebugPosition = position;
            toPosition(mDebugPosition);
        }
    }

    private void initSeekBar(DebugAction action){
        seekBar1.setProgress(action.engine1.angle);
        seekBar2.setProgress(action.engine2.angle);
        seekBar3.setProgress(action.engine3.angle);
        seekBar4.setProgress(action.engine4.angle);
        seekBar5.setProgress(action.engine5.angle);
        seekBar6.setProgress(action.engine6.angle);
        seekBar7.setProgress(action.engine7.angle);
        seekBar8.setProgress(action.engine8.angle);
        seekBar9.setProgress(action.engine9.angle);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_add_action:
                mActionList.addAction(createAction());
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_edit_action:
                editActionState();
                break;
            case R.id.btn_save_action:
                mActionList.replaceAction(editPosition, createAction());
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_quit_edit_action:
                addActionState();
                break;
            case R.id.btn_debug_action:
                debugActionState();
                break;
            case R.id.btn_quit_debug_action:
                addActionState();
                break;
            case R.id.btn_debug_next:
                mDebugControl.doAction(mActionList, mDebugPosition, mDebugPosition+1);
                break;
            case R.id.btn_debug_run:
                mDebugControl.doAction(mActionList, mDebugPosition, mActionList.getActionNum());
                break;
            case R.id.btn_debug_return:
                break;
        }
    }

    private DebugAction createAction(){
        DebugAction action = new DebugAction();
        action.time = 1000;
        action.engine1 = createEngine(seekBar1.getProgress(), 0);
        action.engine2 = createEngine(seekBar2.getProgress(), 0);
        action.engine3 = createEngine(seekBar3.getProgress(), 0);
        action.engine4 = createEngine(seekBar4.getProgress(), 0);
        action.engine5 = createEngine(seekBar5.getProgress(), 0);
        action.engine6 = createEngine(seekBar6.getProgress(), 0);
        action.engine7 = createEngine(seekBar7.getProgress(), 0);
        action.engine8 = createEngine(seekBar8.getProgress(), 0);
        action.engine9 = createEngine(seekBar9.getProgress(), 0);
        return action;
    }

    private Engine createEngine(int angle, int speed){
        Engine enging = new Engine();
        enging.angle = angle;
        enging.speed = speed;
        return enging;
    }

    private void toPosition(int index){
        DebugAction action = mActionList.getAction(index);
        initSeekBar(action);
    }

    @Override
    public void onEnd() {
        mDebugPosition = 0;
        toPosition(mDebugPosition);
    }

    @Override
    public void next() {
        mDebugPosition++;
        toPosition(mDebugPosition);
    }

    private class ActionAdapter extends BaseAdapter{

        private Context mContext;
        private LayoutInflater mInflater;
        private DebugActionList mActionList;

        public ActionAdapter(Context context, DebugActionList actionList){
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mActionList = actionList;
        }

        @Override
        public int getCount() {
            return mActionList.getActionNum();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_action_robot, null);
                holder.button = (Button)convertView.findViewById(R.id.btn_action);
                holder.textView = (TextView)convertView.findViewById(R.id.tv_action);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            convertView.setTag(holder);
            return convertView;
        }
    }

    class ViewHolder{
        public TextView textView;
        public Button button;
    }
}
