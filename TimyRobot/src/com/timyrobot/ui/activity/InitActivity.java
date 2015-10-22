package com.timyrobot.ui.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.robot.R;
import com.timyrobot.common.SharedPrefs;
import com.timyrobot.httpcom.filedownload.FileDownload;
import com.timyrobot.robot.data.RobotData;
import com.timyrobot.service.bluetooth.IBlueConnectListener;
import com.timyrobot.ui.present.IBluetoothPresent;
import com.timyrobot.ui.present.iml.BluetoothPresent;
import com.timyrobot.utils.SharePrefsUtils;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothState;

/**
 * Created by zhangtingting on 15/8/1.
 */
public class InitActivity extends FragmentActivity implements View.OnClickListener, OnMenuItemClickListener{

    private ImageButton mBlueButton;
    private Button mBtnP1;
    IBluetoothPresent mBluePresent;

    private DialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        fragmentManager = getSupportFragmentManager();
        mBluePresent = new BluetoothPresent();
        if(!mBluePresent.initBluetoothService(getApplicationContext())){
            finish();
        }
        mBluePresent.setConnectListener(new IBlueConnectListener() {
            @Override
            public void connect(String name, String address) {
                Intent intent = new Intent(InitActivity.this, EmotionActivity.class);
                startActivity(intent);
            }

            @Override
            public void connectFailed() {

            }

            @Override
            public void disconnect() {

            }

            @Override
            public void recvMsg(String data) {

            }
        });
        initView();
//        Intent i = new Intent(this, FloatViewService.class);
//        startService(i);
        initMenuFragment();

    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    }

    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.icn_close);

        MenuObject addFr = new MenuObject("小黑1");
        BitmapDrawable bd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.icn_3));
        addFr.setDrawable(bd);

        MenuObject addFav = new MenuObject("小黑2");
        addFav.setResource(R.drawable.icn_4);


        menuObjects.add(close);
        menuObjects.add(addFr);
        menuObjects.add(addFav);
        return menuObjects;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBluePresent.startBluetoothService(this);
//        FloatViewService.sendBroadCast(this,true);
    }



    @Override
    protected void onStop() {
        super.onStop();
//        FloatViewService.sendBroadCast(this, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluePresent.stopBluetoothService();
//        Intent i = new Intent(this, FloatViewService.class);
//        stopService(i);
    }

    private void initView(){
        mBlueButton = (ImageButton)findViewById(R.id.btn_find_blue);
        mBlueButton.setOnClickListener(this);
        findViewById(R.id.btn_p1).setOnClickListener(this);
        findViewById(R.id.btn_p2).setOnClickListener(this);
        findViewById(R.id.ibtn_main_activity_personal).setOnClickListener(this);
        findViewById(R.id.ibtn_main_activity_about).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_find_blue:
//                mBluePresent.findBlue(this);
                Intent intent = new Intent(this, EmotionActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_p1:
                new DownloadFileTask("hei01").execute();
                setRobotName("hei01");
//                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.btn_p2:
                new DownloadFileTask("hei02").execute();
                setRobotName("hei02");
                break;
            case R.id.ibtn_main_activity_personal:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
            case R.id.ibtn_main_activity_about:
                mBluePresent.findBlue(this);
                break;
        }
    }

    private void setRobotName(String name){
        SharePrefsUtils utils = new SharePrefsUtils(this, SharedPrefs.ROBOT_PROPERTY_FILE_NAME);
        utils.setStringData(SharedPrefs.ROBOTKEY.CURRENT_ROBOT_NAME, name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case BluetoothState.REQUEST_CONNECT_DEVICE:
                if(resultCode == RESULT_OK) {
                    mBluePresent.resolveBlueResult(data);
                }
                break;
            case BluetoothState.REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){
                    mBluePresent.enableBlue();
                }else{
                    Toast.makeText(this
                            , R.string.blue_not_enable
                            , Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        if(position == 1){
            new DownloadFileTask("hei01").execute();
            setRobotName("hei01");
        }else if(position == 2){
            new DownloadFileTask("hei02").execute();
            setRobotName("hei02");
        }
    }

    class DownloadFileTask extends AsyncTask<Object,Integer,Object> {

        private String mFileName;

        public DownloadFileTask(String fileName){
            mFileName = fileName;
        }

        @Override
        protected Object doInBackground(Object... params) {
            FileDownload.downloadFile("http://121.43.226.152:8080/"+mFileName+"/cmd.txt", mFileName, "cmd.txt");
            FileDownload.downloadFile("http://121.43.226.152:8080/"+mFileName+"/action.txt", mFileName,"action.txt");
            FileDownload.downloadFile("http://121.43.226.152:8080/"+mFileName+"/face.txt", mFileName, "face.txt");
            FileDownload.downloadFile("http://121.43.226.152:8080/"+mFileName+"/robotproperty.txt", mFileName, "robotproperty.txt");
            RobotData.INSTANCE.initRobotData(InitActivity.this.getApplicationContext(), mFileName);
            return null;
        }
    }
}
