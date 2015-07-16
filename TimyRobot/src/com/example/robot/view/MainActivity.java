package com.example.robot.view;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import it.gerdavax.easybluetooth.LocalDevice;
import it.gerdavax.easybluetooth.ReadyListener;
import it.gerdavax.easybluetooth.RemoteDevice;
import it.gerdavax.easybluetooth.ScanListener;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;
import com.example.robot.R;
import com.example.robot.amarino.AmarinoDbAdapter;
import com.example.robot.amarino.AmarinoIntent;
import com.example.robot.amarino.AmarinoService;
import com.example.robot.amarino.AmarinoService.AmarinoServiceBinder;
import com.example.robot.facedection.CameraInterface;
import com.example.robot.facedection.CameraSurfaceView;
import com.example.robot.facedection.DisplayUtil;
import com.example.robot.facedection.EventUtil;
import com.example.robot.facedection.FaceView;
import com.example.robot.facedection.GoogleFaceDetect;
import com.example.robot.tuling.IRApplication;
import com.example.robot.tuling.JsonParser;
import com.example.robot.tuling.WordsMeanParser;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.d.a.m;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.tuling.util.GetTulingResultThread;
import com.tuling.util.ResultWatcher;
import com.tuling.util.TulingManager;

import android.R.integer;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements RecognizerDialogListener {
	private static final String TAG = "MainActivity";

	/********************** amarino ***********************/
	public static final String DEFAULT_DEVICE_ADDRESS = "20:15:05:07:12:01";
	public static final long TIME = 1000L;
	AmarinoDbAdapter db;
	// DeviceListAdapter devices;
	AmarinoHandler mAmarinoHandler = new AmarinoHandler();
	private LocalDevice localDevice;

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (action == null)
				return;
			Log.d(TAG, action + " received");

			if (AmarinoIntent.ACTION_CONNECTED_DEVICES.equals(action)) {
				Log.d(TAG, "action_connected devices!");
				// updateDeviceStates(intent.getStringArrayExtra(AmarinoIntent.EXTRA_CONNECTED_DEVICE_ADDRESSES));
				return;
			}

			Message msg = new Message();

			if (AmarinoIntent.ACTION_CONNECTED.equals(action)) {

				msg.what = AmarinoHandler.CONNECTED;
				msg.obj = intent
						.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);

			} else if (AmarinoIntent.ACTION_DISCONNECTED.equals(action)) {

				msg.what = AmarinoHandler.DISCONNECTED;
				msg.obj = intent
						.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);

			} else if (AmarinoIntent.ACTION_CONNECTION_FAILED.equals(action)) {

				msg.what = AmarinoHandler.CONNECTION_FAILED;
				msg.obj = intent
						.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);

			} else if (AmarinoIntent.ACTION_PAIRING_REQUESTED.equals(action)) {

				msg.what = AmarinoHandler.PAIRING_REQUESTED;
				msg.obj = intent
						.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);

			} else if (AmarinoIntent.ACTION_RECEIVED.equals(action)) {

				msg.what = AmarinoHandler.ACTION_RECEIVED;
				msg.obj = intent.getStringExtra(AmarinoIntent.EXTRA_DATA);

			} else
				return;

			mAmarinoHandler.sendMessage(msg);
		}
	};
	

	private static final String ROBOT_RIGHT_ARM = "1";
	private static final String ROBOT_RIGHT_LEG = "2";
	private static final String ROBOT_RIGHT_FOOT = "3";
	private static final String ROBOT_LEFT_HAND = "4";
	private static final String ROBOT_LEFT_ARM = "5";
	private static final String ROBOT_LEFT_LEG = "6";
	private static final String ROBOT_LEFT_FOOT = "7";
	private static final String ROBOT_HEAD = "8";
	
	private static final String InitRightArmPosi = "10";
	private static final String InitRightLegPosi = "80";
	private static final String InitRightFootPosi = "80";
	private static final String InitLeftHandPosi = "20";
	private static final String InitLeftArmPosi = "170";
	private static final String InitLeftLegPosi = "70";
	private static final String InitLeftFootPosi = "23";
	private static final String InitHeadPosi = "80";

	/**************** face detect ******************/
	CameraSurfaceView surfaceView = null;
	ImageButton shutterBtn;
	ImageButton switchBtn;
	FaceView faceView;
	float previewRate = -1f;
	private FaceDectHandler mFaceDectHandler = null;
	GoogleFaceDetect googleFaceDetect = null;

	private static boolean bConnected = false;

	/***************** tuling and ifek *****************/
	public static final String TULING_KEY = "1deb50ef7d72b9e73907598a50abc60f";
	public static final int MESSAGE_GET_RESULT = 0x1000;

	private RecognizerDialog mIatDialog;
	private SpeechSynthesizer mSST;
	private TulingManager mManager;
	private boolean isGetResult = false;
	private WordMeanHandler mWordMeanHandler = null;
	private boolean isBindService = false;

	/*********** face eyes ************/
	private GifView norGif, happyGif, surpriseGif, suspectGif, wakeupGif;
	private ImageView angryImg, despiseImg, humsImg, squintImg, upsetImg;
	private View curView = null;
	private AmarinoService mService;
	private UiHandler mUiHandler = null;

	private static boolean bFirstWakeup = true;

	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			AmarinoServiceBinder binder = (AmarinoServiceBinder) service;
			mService = binder.getService();
			isBindService = true;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ���ر���
		setContentView(R.layout.activity_main);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// ����ȫ��
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// //设置为横屏
		// if (getRequestedOrientation() !=
		// ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// }
		mWordMeanHandler = new WordMeanHandler();

		mFaceDectHandler = new FaceDectHandler();
		
		mUiHandler = new UiHandler();

		initUI();
		initBluetoothDev();
		initTulingRobot();


		shutterBtn.setOnClickListener(new BtnListeners());
		switchBtn.setOnClickListener(new BtnListeners());
		
		TimerTask task = new TimerTask() {
			public void run() {
				//mUiHandler.sendEmptyMessage(1);
				//doPratice();
				shakeHead2();

			}
		};
		Timer timer = new Timer(true);
		timer.schedule(task,15000, 30000); //延时1000ms后执行，1000ms执行一次
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onResume() {
		super.onResume();
		googleFaceDetect = new GoogleFaceDetect(getApplicationContext(),
				mFaceDectHandler);
		mFaceDectHandler.sendEmptyMessageDelayed(
				EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);
		
		Intent i = new Intent(MainActivity.this, FloatViewService.class);
		stopService(i);


		// // listen for device state changes
		// IntentFilter intentFilter = new
		// IntentFilter(AmarinoIntent.ACTION_CONNECTED_DEVICES);
		// //intentFilter.addAction(AmarinoIntent.ACTION_CONNECTED);
		// //intentFilter.addAction(AmarinoIntent.ACTION_DISCONNECTED);
		// intentFilter.addAction(AmarinoIntent.ACTION_CONNECTION_FAILED);
		// intentFilter.addAction(AmarinoIntent.ACTION_PAIRING_REQUESTED);
		// registerReceiver(receiver, intentFilter);
		//
		// // request state of devices
		// Intent intent = new Intent(MainActivity.this, AmarinoService.class);
		// intent.setAction(AmarinoIntent.ACTION_GET_CONNECTED_DEVICES);
		// startService(intent);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Intent i = new Intent(MainActivity.this, FloatViewService.class);
		startService(i);

		// unregisterReceiver(receiver);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isBindService) {
			unbindService(conn);
		}

	}

	private void initUI() {
		surfaceView = (CameraSurfaceView) findViewById(R.id.camera_surfaceview);
		shutterBtn = (ImageButton) findViewById(R.id.btn_shutter);
		switchBtn = (ImageButton) findViewById(R.id.btn_switch);
		faceView = (FaceView) findViewById(R.id.face_view);
		switchBtn.setVisibility(View.INVISIBLE);
		shutterBtn.setVisibility(View.INVISIBLE);
		initFaceViews();

	}

	private void initFaceViews() {
		// face eyes
		norGif = (GifView) findViewById(R.id.norGif);
		happyGif = (GifView) findViewById(R.id.happyGif);
		surpriseGif = (GifView) findViewById(R.id.surpriseGif);
		suspectGif = (GifView) findViewById(R.id.suspectGif);
		wakeupGif = (GifView) findViewById(R.id.wakeupGif);

		angryImg = (ImageView) findViewById(R.id.angryImg);
		despiseImg = (ImageView) findViewById(R.id.despiseImg);
		humsImg = (ImageView) findViewById(R.id.humsImg);
		squintImg = (ImageView) findViewById(R.id.squintImg);
		upsetImg = (ImageView) findViewById(R.id.upsetImg);

		norGif.setGifImage(R.drawable.nor); // 从文件流中加载GIF动画
		norGif.setGifImageType(GifImageType.COVER); // 只显示第一帧再显示
		norGif.setVisibility(View.INVISIBLE);
		FaceEyesViewUtil.addFaceView(FaceEyesViewUtil.NORMAL, norGif.getId());

//		happyGif.setGifImage(R.drawable.happy);
//		happyGif.setGifImageType(GifImageType.COVER);
//		happyGif.setVisibility(View.INVISIBLE);
//		FaceEyesViewUtil.addFaceView(FaceEyesViewUtil.HAPPY, happyGif.getId());
//
//		surpriseGif.setGifImage(R.drawable.surprise);
//		surpriseGif.setGifImageType(GifImageType.COVER);
//		surpriseGif.setVisibility(View.INVISIBLE);
//		FaceEyesViewUtil.addFaceView(FaceEyesViewUtil.SURPRISE,
//				surpriseGif.getId());

//		suspectGif.setGifImage(R.drawable.suspect);
//		suspectGif.setGifImageType(GifImageType.COVER);
//		suspectGif.setVisibility(View.INVISIBLE);
//		FaceEyesViewUtil.addFaceView(FaceEyesViewUtil.SUSPECT,
//				suspectGif.getId());
//
//		suspectGif.setGifImage(R.drawable.suspect);
//		suspectGif.setGifImageType(GifImageType.COVER);
//		suspectGif.setVisibility(View.INVISIBLE);
//		FaceEyesViewUtil.addFaceView(FaceEyesViewUtil.SUSPECT,
//				suspectGif.getId());

		wakeupGif.setGifImage(R.drawable.wakeup);
		wakeupGif.setGifImageType(GifImageType.COVER);
		suspectGif.setVisibility(View.INVISIBLE);
//
//		angryImg.setBackgroundResource(R.drawable.angry);
//		angryImg.setVisibility(View.INVISIBLE);
//		FaceEyesViewUtil.addFaceView(FaceEyesViewUtil.ANGRY, angryImg.getId());

		humsImg.setBackgroundResource(R.drawable.hums);
		humsImg.setVisibility(View.INVISIBLE);
		FaceEyesViewUtil.addFaceView(FaceEyesViewUtil.HUMS, humsImg.getId());

//		despiseImg.setBackgroundResource(R.drawable.despise);
//		despiseImg.setVisibility(View.INVISIBLE);
//		FaceEyesViewUtil.addFaceView(FaceEyesViewUtil.DESPISE,
//				despiseImg.getId());
//
//		squintImg.setBackgroundResource(R.drawable.squint);
//		squintImg.setVisibility(View.INVISIBLE);
//		FaceEyesViewUtil
//				.addFaceView(FaceEyesViewUtil.SQUINT, squintImg.getId());
//
//		upsetImg.setImageResource(R.drawable.upset);
//		upsetImg.setBackgroundResource(R.drawable.upset);
//		upsetImg.setVisibility(View.INVISIBLE);
//		FaceEyesViewUtil.addFaceView(FaceEyesViewUtil.UPSET, upsetImg.getId());

		if (bFirstWakeup) {
			wakeupGif.setVisibility(View.VISIBLE);
			curView = wakeupGif;
			// bFirstWakeup = false;
			TimerTask task = new TimerTask() {
				public void run() {
					mUiHandler.sendEmptyMessage(0);
					shakeHead();
				}
			};
			Timer timer = new Timer(true);
			timer.schedule(task,5000); //延时1000ms后执行，1000ms执行一次
		} else {
			norGif.setVisibility(View.VISIBLE);
			curView = norGif;
		}
			
	}

	private void initViewParams() {
		LayoutParams params = surfaceView.getLayoutParams();
		Point p = DisplayUtil.getScreenMetrics(this);
		params.width = p.x;
		params.height = p.y;
		previewRate = DisplayUtil.getScreenRate(this);
		surfaceView.setLayoutParams(params);
	}

	private void initBluetoothDev() {
		localDevice = LocalDevice.getInstance();
		localDevice.init(this, new ReadyListener() {

			@Override
			public void ready() {
				localDevice.scan(new ScanListener() {

					@Override
					public void deviceFound(RemoteDevice device) {
						Log.i(TAG,
								"Find one device, address->"
										+ device.getAddress());
						if (device.getAddress().equals(DEFAULT_DEVICE_ADDRESS)) {
							IntentFilter intentFilter = new IntentFilter(
									AmarinoIntent.ACTION_CONNECTED_DEVICES);
							intentFilter
									.addAction(AmarinoIntent.ACTION_CONNECTED);
							intentFilter
									.addAction(AmarinoIntent.ACTION_DISCONNECTED);
							intentFilter
									.addAction(AmarinoIntent.ACTION_CONNECTION_FAILED);
							intentFilter
									.addAction(AmarinoIntent.ACTION_PAIRING_REQUESTED);
							intentFilter
									.addAction(AmarinoIntent.ACTION_RECEIVED);
							registerReceiver(receiver, intentFilter);

							// request state of devices
							Intent intent = new Intent(MainActivity.this,
									AmarinoService.class);
							intent.putExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS,
									DEFAULT_DEVICE_ADDRESS);
							intent.setAction(AmarinoIntent.ACTION_CONNECT);
							// startService(intent);
							bindService(intent, conn, Context.BIND_AUTO_CREATE);
						}
					}

					@Override
					public void scanCompleted() {
						setProgressBarIndeterminateVisibility(false);
					}
				});
			} // end ready()
		});
	}

	private void initTulingRobot() {
		mIatDialog = new RecognizerDialog(this, null);
		mIatDialog.setListener(this);
		mSST = SpeechSynthesizer.createSynthesizer(this, null);
		mSST.setParameter(SpeechConstant.VOICE_NAME, "xiaoyu");
		mSST.setParameter(SpeechConstant.SPEED, "35");
		mSST.setParameter(SpeechConstant.VOLUME, "80");
		mSST.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);

		mManager = new TulingManager(this);
	}

	private class BtnListeners implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_shutter:
				takePicture();
				break;
			case R.id.btn_switch:
				switchCamera();
				break;
			default:
				break;
			}
		}

	}

	private class AmarinoHandler extends Handler {

		protected static final int CONNECTED = 1;
		protected static final int DISCONNECTED = 2;
		protected static final int CONNECTION_FAILED = 3;
		protected static final int PAIRING_REQUESTED = 4;
		protected static final int ACTION_RECEIVED = 5;

		protected static final int TULING_DIALOG_START = 6;
		protected static final int TULING_DIALOG_STOP = 7;

		protected static final int SEND_DATA = 8;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.d(TAG, "AmarinoHandler handleMessage");
			final int what = msg.what;
			final String data = (String) msg.obj;

			switch (what) {
			case CONNECTED:
				// setDeviceConnected(device, view, btn);
				bConnected = true;
				sendBluetoothData("000,000,255", 'B');
//				Toast.makeText(getApplicationContext(), "Connected!",
//						Toast.LENGTH_LONG).show();
				break;
			case CONNECTION_FAILED:
				
//				Toast.makeText(MainActivity.this, "Connection failed!",
//						Toast.LENGTH_SHORT).show();
			case DISCONNECTED:
//				Toast.makeText(MainActivity.this, "Connection succeed!",
//						Toast.LENGTH_LONG).show();
				// setDeviceDisconnected(device, view, btn);
				break;
			case PAIRING_REQUESTED:
//				Toast.makeText(
//						MainActivity.this,
//						"Device is not paired!\n\nPlease pull-down the notification bar to pair your device.\n\n",
//						Toast.LENGTH_LONG).show();
//				Toast.makeText(getApplicationContext(), "Pairing request!",
//						Toast.LENGTH_LONG).show();
				// setDeviceDisconnected(device, view, btn);
				break;
			case ACTION_RECEIVED:

//				Toast.makeText(getApplicationContext(),
//						"Receive msg->" + msg.obj, Toast.LENGTH_LONG).show();
				Log.i(TAG, "Receive msg->" + msg.obj);
				break;
			case TULING_DIALOG_START:
//				sendBluetoothData(data, 'B');
				break;

			case TULING_DIALOG_STOP:
//				sendBluetoothData(data, 'B');
//				Log.d(TAG, "TULING_DIALOG_STOP");
				break;

			case SEND_DATA:
				Log.d(TAG, "SEND_DATA 0");
				sendBluetoothData(data, 'C');
				Log.d(TAG, "SEND_DATA 1");
				break;
			default:
				break;

			}
		}

	}

	private class WordMeanHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String words = (String) msg.obj;
			switch (msg.what) {
			case WordsMeanParser.NO_MEAN:
				break;
			case WordsMeanParser.HELLO:
				shakeHand();
				break;
			case WordsMeanParser.SEARCH:
				handRightHand();
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri uri = Uri.parse("http://www.baidu.com/s?wd=手机机器人");
				intent.setData(uri);
				startActivity(intent);
				
				break;
			case WordsMeanParser.DANCE:
				doPratice();
			default:
				break;
			}
		}

	}

	private class FaceDectHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case EventUtil.UPDATE_FACE_RECT:
				Face[] faces = (Face[]) msg.obj;
				if (faces == null || faces.length < 1) {
					break;
				}
				// faces[0].rect.left = -500;
				// faces[0].rect.top = -1000;
				// faces[0].rect.right = 500;
				// faces[0].rect.bottom = 0;
				
				//faceView.setFaces(faces);
				
				// for(int i = 0; i< faces.length; i++){
				// Log.e("face position",
				// "("+faces[i].rect.left+","+faces[i].rect.top+","+faces[i].rect.right+","+faces[i].rect.bottom+")");
				// }
				int position = faces[0].rect.left
						+ (faces[0].rect.right - faces[0].rect.left) / 2;

				if ((faces[0].rect.right - faces[0].rect.left) > 700) {
					showTalkDialog();
				}
				// Log.d(TAG, "positon->"+position);
				String data = "";
				if (position > 0) {
					if (position <= 400)
						data = "c";
					else if (position <= 700)
						data = "b";
					else if (position <= 1000)
						data = "a";
				} else {
					if (position >= -400)
						data = "d";
					else if (position >= -700)
						data = "e";
					else if (position >= 1000)
						data = "f";
				}
				// if (bConnected)
				// sendBluetoothData(data);

				break;
			case EventUtil.CAMERA_HAS_STARTED_PREVIEW:
				startGoogleFaceDetect();
				break;
			}
			super.handleMessage(msg);
		}

	}
	
	
	private class UiHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what == 1){
				showFaceEyes(FaceEyesViewUtil.HUMS);
				return;
			}
			wakeupGif.destroyDrawingCache();
			showFaceEyes(FaceEyesViewUtil.NORMAL);
			super.handleMessage(msg);
		}

	}
	

	private void sendBluetoothData(String data, char flag) {
		if(!bConnected)
			return ;
		Intent intent = new Intent(MainActivity.this, AmarinoService.class);
		intent.setAction(AmarinoIntent.ACTION_SEND);
		intent.putExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS,
				DEFAULT_DEVICE_ADDRESS);
		intent.putExtra(AmarinoIntent.EXTRA_FLAG, flag);
		intent.putExtra(AmarinoIntent.EXTRA_DATA_TYPE,
				AmarinoIntent.STRING_EXTRA);
		intent.putExtra(AmarinoIntent.EXTRA_DATA, data);
		// MainActivity.this.startService(intent);
		mService.handleStart(intent);
	}

	private void takePicture() {
		CameraInterface.getInstance().doTakePicture();
		mFaceDectHandler.sendEmptyMessageDelayed(
				EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);
	}

	private void switchCamera() {
		stopGoogleFaceDetect();
		int newId = (CameraInterface.getInstance().getCameraId() + 1) % 2;
		CameraInterface.getInstance().doStopCamera();
		CameraInterface.getInstance().doOpenCamera(null, newId);
		CameraInterface.getInstance().doStartPreview(
				surfaceView.getSurfaceHolder(), previewRate);
		mFaceDectHandler.sendEmptyMessageDelayed(
				EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);
		// startGoogleFaceDetect();

	}

	private void restartPreviewCamera() {
		stopGoogleFaceDetect();
		CameraInterface.getInstance().doStartPreview(
				surfaceView.getSurfaceHolder(), previewRate);
		mFaceDectHandler.sendEmptyMessageDelayed(
				EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);
		startGoogleFaceDetect();
	}

	private void startGoogleFaceDetect() {
		Camera.Parameters params = CameraInterface.getInstance()
				.getCameraParams();
		if (params.getMaxNumDetectedFaces() > 0) {
			if (faceView != null) {
				faceView.clearFaces();
				faceView.setVisibility(View.VISIBLE);
			}
			CameraInterface.getInstance().getCameraDevice()
					.setFaceDetectionListener(googleFaceDetect);
			CameraInterface.getInstance().getCameraDevice()
					.startFaceDetection();
		}
	}

	private void stopGoogleFaceDetect() {
		Camera.Parameters params = CameraInterface.getInstance()
				.getCameraParams();
		if (params.getMaxNumDetectedFaces() > 0) {
			CameraInterface.getInstance().getCameraDevice()
					.setFaceDetectionListener(null);
			CameraInterface.getInstance().getCameraDevice().stopFaceDetection();
			faceView.clearFaces();
		}
	}

	private void showTalkDialog() {
		if ((mIatDialog != null) && (!mIatDialog.isShowing())) {
			if (bConnected) {
				Message msg = new Message();
				msg.what = 6;
				msg.obj = "000,000,255";
				mAmarinoHandler.sendMessage(msg);
				TimerTask task = new TimerTask() {
					public void run() {
						Message message = new Message();
						message.what = 7;
						message.obj = "000,000,000";
						mAmarinoHandler.sendMessage(message);
					}
				};
				Timer timer = new Timer(true);
				timer.schedule(task, 1000); // 延时1000ms后执行，1000ms执行一次
				// timer.cancel(); //退出计时器
			}
			if (!mSST.isSpeaking()) {
				mIatDialog.show();
				//mIatDialog.hide();
			}
			

		}
	}

	private void showFaceEyes(int FaceNameId) {
		int viewToShowId = FaceEyesViewUtil.getFaceView(FaceNameId);
		if (viewToShowId != curView.getId()) {
			findViewById(viewToShowId).setVisibility(View.VISIBLE);
			curView.setVisibility(View.INVISIBLE);
			curView = findViewById(viewToShowId);
		}
	}

	@Override
	public void onError(SpeechError arg0) {
		// TODO Auto-generated method stub
		if (mIatDialog.isShowing()) {
			mIatDialog.dismiss();

		}
	}

	@Override
	public void onResult(RecognizerResult recognizerResult, boolean b) {
		// TODO Auto-generated method stub

		Log.d(TAG, "boolean:" + b);

		String result = JsonParser.parseIatResult(recognizerResult
				.getResultString());
		Log.d(TAG, result);
		int iMeanRet = WordsMeanParser.parserWordsMean(result);
		if (iMeanRet != WordsMeanParser.NO_MEAN) {
			Message msg = new Message();
			msg.what = iMeanRet;
			msg.obj = result;
			mWordMeanHandler.sendMessage(msg);
		}
		// baidu search(tuling not need)
		if (iMeanRet == WordsMeanParser.SEARCH)
			return;
		

		Log.d(TAG, result);
		if (!isGetResult) {
			isGetResult = true;
			new GetTulingResultThread(TULING_KEY, result, new ResultWatcher() {

				@Override
				public void onResults(String s) {
					// TODO Auto-generated method stub
					Log.d(TAG, "tuling result:" + s);
					try {
						JSONObject object = new JSONObject(s);
						String result = object.getString("text");
						result = result.replace("br", "");
						if (!mSST.isSpeaking()) {
							mSST.startSpeaking(result, null);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					} finally {
						isGetResult = false;
					}
				}
			}).start();
		}
	}
	
	private void initRobotPost(){
		new InitPosiThread().run();
	}
	
	private void shakeHand(){
		synchronized (this) {
			new ShakeHandThread().run();
		}

	}
	
	private void shakeHead(){
		synchronized (this) {
			new ShakeHeadThread().run();
		}
	}
	
	private void shakeHead2(){
		synchronized (this) {
			new ShakeHeadThread2().run();
		}
	}
	
	private void handRightHand(){
		synchronized (this) {
			new HandRightHand().run();
		}
	}

	private void callToWakeup() {
		// sendBluetoothData("A#1P1967#2P656#3P1500#4P1344#5P789#6P2167#7P1544#8P1367#9P1367T1000");
		new WakeThread().run();
	}

	private void sendMessage(int what, String cmd, long delayMillis) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = cmd;
		mAmarinoHandler.sendMessageDelayed(msg, delayMillis);
	}
	
	class InitPosiThread extends Thread{
		public InitPosiThread() {
			// TODO Auto-generated constructor stub
			sendMessage(8, ROBOT_HEAD+","+InitHeadPosi, 0);
			sendMessage(8, ROBOT_LEFT_ARM+","+InitLeftArmPosi, TIME);
			sendMessage(8, ROBOT_LEFT_FOOT+","+InitLeftFootPosi, TIME*2);
			sendMessage(8, ROBOT_LEFT_LEG+","+InitLeftLegPosi, TIME*3);
			sendMessage(8, ROBOT_LEFT_HAND+","+InitLeftHandPosi, TIME*4);
			sendMessage(8, ROBOT_RIGHT_ARM+","+InitRightArmPosi, TIME*5);
			sendMessage(8, ROBOT_RIGHT_LEG+","+InitRightLegPosi, TIME*6);
			sendMessage(8, ROBOT_RIGHT_FOOT+","+InitRightFootPosi, TIME*7);
		}
		
	}

	class WakeThread extends Thread {
		public WakeThread() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
		
			super.run();
		}
	}
	
	class ShakeHandThread extends Thread{
		public ShakeHandThread() {
			// TODO Auto-generated constructor stub
			sendMessage(8, "5,10", 0);
			sendMessage(8, "4,40", TIME);
			sendMessage(8, "8,150", TIME);
			sendMessage(8, "4,10", TIME*2);
			sendMessage(8, "8,40", TIME*2);
			sendMessage(8, "4,40", TIME*3);
			sendMessage(8, "8,150", TIME*3);
			sendMessage(8, "5,170", TIME*4);
			sendMessage(8, "8, 40", TIME*4);
			sendMessage(8, "4,20", TIME*5);
			sendMessage(8, "8,80", TIME*5);
		}
		
	}
	
	class ShakeHeadThread extends Thread{
		public ShakeHeadThread() {
			// TODO Auto-generated constructor stub
			long time = 800l;
			sendMessage(8, "8,50", 0);
			sendMessage(8, "8,140", time*1);
			sendMessage(8, "8,80", time*2);
			sendMessage(8, "5, 10", time*3);
			sendMessage(8, "5, 170", time*4);
		}
		
	}
	
	class ShakeHeadThread2 extends Thread{ 
		public ShakeHeadThread2() {
			// TODO Auto-generated constructor stub
			long time = 1200l;
			sendMessage(8, "8,40", 0);
			sendMessage(8, "8,140", time*1);
			sendMessage(8, "8,80", time*2);
			sendMessage(8, "5, 120", time*1);
			sendMessage(8, "1, 50", time*1);
			sendMessage(8, "5, 170", time*2);
			sendMessage(8, "1, 10", time*2);

		}
		
	}
	
	class HandRightHand extends Thread{
		public HandRightHand() {
			// TODO Auto-generated constructor stub
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			sendMessage(8, "1,170", 0);
			sendMessage(8, "1,10", TIME);
			super.run();
		}
	}
	
	private void doPratice(){
		synchronized (this) {
			new Pratice().run();
		}
	}
	
	class Pratice extends Thread{
		public Pratice() {
			// TODO Auto-generated constructor stub
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			sendMessage(8, "1, 150", 0);
			sendMessage(8, "5,10", 0);
			sendMessage(8, "3,50", TIME);
			sendMessage(8, "7,40", TIME);
			sendMessage(8, "1,10", TIME*2);
			sendMessage(8, "1,10", TIME*2);
			sendMessage(8, "5,170", TIME*3);
			sendMessage(8, "3,80", TIME*3);
			sendMessage(8, "7,23", TIME*4);
			sendMessage(8, "1, 150", TIME*4);
			sendMessage(8, "5,10", TIME*5);
			sendMessage(8, "3,50", TIME*5);
			sendMessage(8, "7,40", TIME*6);
			sendMessage(8, "1,10", TIME*6);
			sendMessage(8, "1,10", TIME*7);
			sendMessage(8, "5,170", TIME*7);
			sendMessage(8, "3,80", TIME*8);
			sendMessage(8, "7,23", TIME*8); 
	
			super.run();
		}
	}

}
