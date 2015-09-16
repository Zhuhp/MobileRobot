package com.timyrobot.service.music;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.util.Log;

import com.example.robot.R;
import com.timyrobot.common.ConstDefine;
import com.timyrobot.common.GlobalSetting;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangtingting on 15/9/2.
 */
public class MusicPlayer extends Thread implements MediaPlayer.OnCompletionListener{

    private MediaPlayer mPlayer;
    private Context mContext;
    private String mMusicUri;
    private int mMusicTime;
    private int mMusicWaitTime;

    public MusicPlayer(Context context, String musicName, int musicTime, int musicWaitTime){
        mContext = context.getApplicationContext();
//        mPlayer = new MediaPlayer();
//        mPlayer.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);
//        mPlayer.setOnCompletionListener(this);
        mMusicUri = musicName;
        if(musicTime <= 0){
            mMusicTime = -1;
        }else {
            mMusicTime = musicTime;
        }
        if(musicWaitTime < 0){
            mMusicWaitTime = 0;
        }else {
            mMusicWaitTime = musicWaitTime;
        }
    }

    @Override
    public void run() {
        super.run();
//        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        try {
        try {
            Thread.sleep(mMusicWaitTime*1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mPlayer = MediaPlayer.create(mContext,
        mContext.getResources().getIdentifier(mMusicUri,"raw",mContext.getPackageName()));
        mPlayer.start();
//            mPlayer.setDataSource(mContext, mMusicUri);
//            mPlayer.setLooping(false);
//            mPlayer.prepare();
        if(mMusicTime > 0) {
            Executors.newScheduledThreadPool(1).schedule(new Runnable() {
                @Override
                public void run() {
                    Log.d(ConstDefine.TAG, "music run stop");
                    if (mPlayer.isPlaying()) {
                        mPlayer.stop();
                    }
                    mPlayer.release();
                    mContext = null;
                    mPlayer = null;
                }
            }, mMusicTime, TimeUnit.SECONDS);
        }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(ConstDefine.TAG,"music run complete");
        if(mp.isPlaying()){
            mp.stop();
        }
        mp.release();
        mContext = null;
        mPlayer = null;
    }

}
