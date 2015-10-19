package com.timyrobot.httpcom.filedownload;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.timyrobot.common.ConstDefine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 下载配置文件
 * Created by zhangtingting on 15/9/15.
 */
public class FileDownload {

    /**
     * 下载文件
     * @param url 文件路径
     * @param fileName 文件名
     * @return 本地文件
     */
    public static File downloadFile(String url, String robotName, String fileName){
        try {
            final URL downloadFileUrl = new URL(url);
            final URLConnection urlConnection = downloadFileUrl.openConnection();
            File file = getPropertySaveFile(robotName, fileName);
            if(file == null){
                return null;
            }
            final FileOutputStream fileOutputStream = new FileOutputStream(file);
            final byte buffer[] = new byte[16 * 1024];

            final InputStream inputStream = urlConnection.getInputStream();

            int len1 = 0;
            while ((len1 = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, len1);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            return file;
        } catch (final Exception exception) {
            Log.i(ConstDefine.TAG, "doInBackground - exception" + exception.getMessage());
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * 获取配置文件
     * @param fileName 文件名
     * @return File
     */
    private static File getPropertySaveFile(String robotName, String fileName){
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return null;
        }
        File file = new File(Environment.getExternalStorageDirectory(), "MobileRobot");
        if(!file.exists()){
            file.mkdirs();
        }
        file = new File(file, "Property");
        if(!file.exists()){
            file.mkdirs();
        }
        file = new File(file, robotName);
        if(!file.exists()){
            file.mkdirs();
        }
        file = new File(file,fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取配置文件
     * @param fileName 文件名
     * @return 返回本地配置文件
     */
    public static File getPropertyFileExist(String robotName, String fileName){
        if(TextUtils.isEmpty(robotName)){
            return null;
        }
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return null;
        }
        File file = new File(Environment.getExternalStorageDirectory(), "MobileRobot");
        if(!file.exists()){
            return null;
        }
        file = new File(file, "Property");
        if(!file.exists()){
            return null;
        }
        file = new File(file, robotName);
        if(!file.exists()){
            return null;
        }
        file = new File(file,fileName);
        if (!file.exists()) {
            return null;
        }
        return file;
    }
}
