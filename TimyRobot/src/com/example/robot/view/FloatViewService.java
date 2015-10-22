package com.example.robot.view;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.robot.R;
import com.timyrobot.IRApplication;
import com.timyrobot.ui.activity.EmotionActivity;

public class FloatViewService extends Service{
	private WindowManager windowManager = null;
	private WindowManager.LayoutParams windowManagerParams = null;
	private FloatView floatView = null;

	private boolean isAdded = false; // 是否已增加悬浮窗
	private static WindowManager wm;
	private static WindowManager.LayoutParams params;
	private Button btn_floatView;

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			boolean isShow = intent.getBooleanExtra("IS_SHOW", false);
			if(isShow){
				if(!isAdded) {
					addView();
				}
			}else{
				if(isAdded) {
					removeView();
				}
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		createFloatView();
		registerReceiver(mReceiver, new IntentFilter("com.jornco.mobilerobot.FLOAT_VIEW_SHOW"));
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mReceiver);
		// 在程序退出(Activity销毁）时销毁悬浮窗口
		if(isAdded) {
			windowManager.removeView(floatView);
		}
	}

	
	private void createFloatView() {

//		floatView = new FloatView(getApplicationContext());
//		floatView.setImageResource(R.drawable.logoimg1);
		//floatView.setBackground(background);
//		floatView.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.setClass(getApplicationContext(),EmotionActivity.class);
//				startActivity(intent);
//
//			}
//		});
		ImageView imageView = new ImageView(getApplicationContext());
		imageView.setImageResource(R.drawable.logoimg1);
		// 获取WindowManager
		windowManager = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);

		// 设置LayoutParams(全局变量）相关参数
		windowManagerParams = new WindowManager.LayoutParams();

		windowManagerParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

		windowManagerParams.type = WindowManager.LayoutParams.TYPE_PHONE; // 设置window
																			// type
		// 设置Window flag
		windowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		/*
		 * 注意，flag的值可以为： LayoutParams.FLAG_NOT_TOUCH_MODAL 不影响后面的事件
		 * LayoutParams.FLAG_NOT_FOCUSABLE 不可聚焦 LayoutParams.FLAG_NOT_TOUCHABLE
		 * 不可触摸
		 */
		// 调整悬浮窗口至左上角，便于调整坐标
		windowManagerParams.gravity = Gravity.LEFT | Gravity.TOP;
		// 以屏幕左上角为原点，设置x、y初始值
		windowManagerParams.x = 0;
		windowManagerParams.y = 0;
		// 设置悬浮窗口长宽数据
//		windowManagerParams.width = LayoutParams.WRAP_CONTENT;
//		windowManagerParams.height = LayoutParams.WRAP_CONTENT;
		windowManagerParams.width = 212;
		windowManagerParams.height = 366;
		windowManager.addView(imageView, windowManagerParams);
//		addView();
	}

	private void addView(){
		isAdded = true;
		// 显示myFloatView图像
		windowManager.addView(floatView, windowManagerParams);
	}

	private void removeView(){
		isAdded = false;
		windowManager.removeView(floatView);
	}

	public static void sendBroadCast(Context context, boolean isShow){
		Intent intent = new Intent("com.jornco.mobilerobot.FLOAT_VIEW_SHOW");
		intent.putExtra("IS_SHOW", isShow);
		context.sendBroadcast(intent);
	}
	
	
}
