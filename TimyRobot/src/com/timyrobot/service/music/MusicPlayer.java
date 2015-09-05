package com.timyrobot.service.music;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;

import com.example.robot.R;

import java.io.IOException;

/**
 * Created by zhangtingting on 15/9/2.
 */
public class MusicPlayer extends Thread implements MediaPlayer.OnCompletionListener{

    private MediaPlayer mPlayer;
    private Context mContext;
    private String mMusicUri;

    public MusicPlayer(Context context, String musicName){
        mContext = context.getApplicationContext();
//        mPlayer = new MediaPlayer();
//        mPlayer.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);
//        mPlayer.setOnCompletionListener(this);
        mMusicUri = musicName;
    }

    @Override
    public void run() {
        super.run();
//        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        try {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mPlayer = MediaPlayer.create(mContext, R.raw.test);
//            mPlayer.setDataSource(mContext, mMusicUri);
//            mPlayer.setLooping(false);
//            mPlayer.prepare();
            mPlayer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(mp.isPlaying()){
            mp.stop();
        }
        mp.release();
        mContext = null;
        mPlayer = null;
    }

}
