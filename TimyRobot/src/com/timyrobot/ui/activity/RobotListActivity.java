package com.timyrobot.ui.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.example.robot.R;
import com.timyrobot.common.RobotServiceKey;
import com.timyrobot.httpcom.filedownload.FileDownload;
import com.timyrobot.robot.bean.RobotCmd;
import com.timyrobot.robot.data.RobotData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 * Created by zhangtingting on 15/9/17.
 */
public class RobotListActivity extends Activity{

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robotlist);
        mListView = (ListView)findViewById(R.id.lv_robotlist);
    }

    class DownloadFileTask extends AsyncTask<Object,Integer,Object> {

        private String mFileName;

        public DownloadFileTask(String fileName){
            mFileName = fileName;
        }

        @Override
        protected Object doInBackground(Object... params) {
            FileDownload.downloadFile("http://121.43.226.152:8080/" + mFileName + "/cmd.txt", mFileName, "cmd.txt");
            FileDownload.downloadFile("http://121.43.226.152:8080/"+mFileName+"/action.txt", mFileName, "action.txt");
            FileDownload.downloadFile("http://121.43.226.152:8080/"+mFileName+"/face.txt", mFileName, "face.txt");
            FileDownload.downloadFile("http://121.43.226.152:8080/"+mFileName+"/robotproperty.txt", mFileName, "robotproperty.txt");
            RobotData.INSTANCE.initRobotData(RobotListActivity.this.getApplicationContext(), mFileName);
            return null;
        }
    }

//    class DownloadRobotListFileTask extends AsyncTask<Object,Integer,Object> {
//
//        @Override
//        protected Object doInBackground(Object... params) {
//            FileDownload.downloadFile("http://121.43.226.152:8080/robotlist.txt", "robotlist.txt");
//            try {
//                InputStream stream = null;
//                File file  = FileDownload.getPropertyFileExist("cmd.txt");
//                if(file != null){
//                    stream = new FileInputStream(file);
//                }else {
//                    stream = ctx.getAssets().open("cmd.txt");
//                }
//                BufferedReader cmdBr = new BufferedReader(new InputStreamReader(stream));
//                String line = null;
//                while((line = cmdBr.readLine()) != null) {
//                    JSONObject object = new JSONObject(line);
//                    Iterator<String> keys = object.keys();
//                    if (keys == null) {
//                        return;
//                    }
//                    while (keys.hasNext()) {
//                        String key = keys.next();
//                        JSONObject cmd = object.optJSONObject(key);
//                        if (cmd != null) {
//                            RobotCmd rc = new RobotCmd();
//                            rc.setAction(cmd.optString(RobotServiceKey.CmdKey.ACTION));
//                            rc.setVoice(cmd.optString(RobotServiceKey.CmdKey.VOICE));
//                            rc.setFace(cmd.optString(RobotServiceKey.CmdKey.FACE));
//                            JSONObject sysObject = cmd.optJSONObject(RobotServiceKey.CmdKey.SYSTEM);
//                            if(sysObject != null) {
//                                rc.setSystem(sysObject.toString());
//                            }
//                            mCmd.put(key, rc);
//                        }
//                    }
//                }
//                cmdBr.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
//            super.onPostExecute(o);
//        }
//
//
//    }
}
