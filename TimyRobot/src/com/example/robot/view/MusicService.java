package com.example.robot.view;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

public class MusicService extends Service{
	public final String MUSICURL = "url";
	private MediaPlayer mp;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        
 
    }
    @Override  
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        String url = intent.getStringExtra(MUSICURL);
        Uri u = Uri.parse(url);
        mp=MediaPlayer.create(this, u);
        mp.start(); 
    } 
    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.stop();
    }
}
