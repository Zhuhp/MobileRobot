package com.example.robot.view;

import com.ant.liao.GifView;
import com.example.robot.R;
import com.example.robot.tuling.IRApplication;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.widget.ImageView;

public class FloatView extends ImageView {
	private final static String TAG = "FloatView";
	private float mTouchX;
	private float mTouchY;
	private float x;
	private float y;
	private float mStartX;
	private float mStartY;
	static AnimationDrawable  frameAnimation  = null; 
	 /* 定义一个Drawable对象 */  
    Drawable mBitAnimation  = null;
    Context  mContext  = null; 
    static   OnPreDrawListener opdl; 
	private OnClickListener mClickListener;
	private WindowManager windowManager = (WindowManager) getContext()
			.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
	// 此windowManagerParams变量为获取的全局变量，用以保存悬浮窗口的属性
	private WindowManager.LayoutParams windowManagerParams = ((IRApplication) getContext()
			.getApplicationContext()).getWindowParams();

	@SuppressWarnings("deprecation")
	public FloatView(Context context) {
		super(context);
		Log.i(TAG, "FloatView!");
		/* 实例化AnimationDrawable对象 */  
        frameAnimation = new AnimationDrawable();  
          
        /* 装载资源 */  
        //这里用一个循环了装载所有名字类似的资源  
        //如“a1.......15.png”的图片  
        //这个方法用处非常大  

        /* 为动画添加一帧 */  
        //参数mBitAnimation是该帧的图片  
        //参数100是该帧显示的时间,按毫秒计算  
        mBitAnimation = getResources().getDrawable(R.drawable.logoimg1);  
        frameAnimation.addFrame(mBitAnimation, 150);  
        mBitAnimation = getResources().getDrawable(R.drawable.logoimg2);  
        frameAnimation.addFrame(mBitAnimation, 150);  
        mBitAnimation = getResources().getDrawable(R.drawable.logoimg3);  
        frameAnimation.addFrame(mBitAnimation, 150);  
        mBitAnimation = getResources().getDrawable(R.drawable.logoimg4);  
        frameAnimation.addFrame(mBitAnimation, 150);  
        mBitAnimation = getResources().getDrawable(R.drawable.logoimg5);  
        frameAnimation.addFrame(mBitAnimation, 150);  
        mBitAnimation = getResources().getDrawable(R.drawable.logoimg6);  
        frameAnimation.addFrame(mBitAnimation, 150);  
        mBitAnimation = getResources().getDrawable(R.drawable.logoimg7);  
        frameAnimation.addFrame(mBitAnimation, 150);  
        mBitAnimation = getResources().getDrawable(R.drawable.logoimg8);  
        frameAnimation.addFrame(mBitAnimation, 150);  
        
        frameAnimation.setOneShot( false );  /* 设置播放模式是否循环false表示循环而true表示不循环 */  
        this.setBackground(frameAnimation);  

        opdl=new OnPreDrawListener(){    
        	  
            @Override   
            public boolean onPreDraw() {    
                   
                 frameAnimation.start();   
                return true;    
  
            }    
  
        };    
         this.getViewTreeObserver().addOnPreDrawListener(opdl);
         
	}

	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 获取到状态栏的高度
		Rect frame = new Rect();
		getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		System.out.println("statusBarHeight:" + statusBarHeight);
		// 获取相对屏幕的坐标，即以屏幕左上角为原点
		x = event.getRawX();
		y = event.getRawY() - statusBarHeight; // statusBarHeight是系统状态栏的高度
		Log.i("tag", "currX" + x + "====currY" + y);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
			// 获取相对View的坐标，即以此View左上角为原点
			mTouchX = event.getX();
			mTouchY = event.getY();
			mStartX = x;
			mStartY = y;
			Log.i("tag", "startX" + mTouchX + "====startY" + mTouchY);
			break;
		case MotionEvent.ACTION_MOVE: // 捕获手指触摸移动动作
			updateViewPosition();
			break;
		case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作
			updateViewPosition();
			mTouchX = mTouchY = 0;
			if ((x - mStartX) < 5 && (y - mStartY) < 5) {
				if (mClickListener != null) {
					mClickListener.onClick(this);
				}
			}
			break;
		}
		return true;
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		this.mClickListener = l;
	}

	private void updateViewPosition() {
		// 更新浮动窗口位置参数
		windowManagerParams.x = (int) (x - mTouchX);
		windowManagerParams.y = (int) (y - mTouchY);
		windowManager.updateViewLayout(this, windowManagerParams); // 刷新显示
	}

}
