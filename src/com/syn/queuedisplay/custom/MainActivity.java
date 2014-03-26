package com.syn.queuedisplay.custom;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j1tth4.mediaplayer.VideoPlayer;
import com.syn.pos.QueueDisplayInfo;
import com.syn.queuedisplay.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class MainActivity extends Activity implements QueueServerSocket.ServerSocketListener,
	SpeakCallingQueue.OnPlaySoundListener, VideoPlayer.MediaPlayerStateListener{
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private int mQueueIdx = -1;
	private boolean isPause = false;
	
	private SystemUiHider mSystemUiHider;
	private Handler mHandlerQueue;
	private Thread mConnThread;
	private Handler mHandlerSpeakQueue;
	private VideoPlayer mVideoPlayer;
	private List<String> mQueueLst;
	private SpeakCallingQueue mSpeakCallingQueue;
	private SurfaceView mSurface;
	private WebView mWebView;
	private ListView mLvQueueA;
	private ListView mLvQueueB;
	private ListView mLvQueueC;
	private TextView mTvCallA;
	private TextView mTvCallB;
	private TextView mTvCallC;
	private TextView mTvCallASub;
	private TextView mTvCallBSub;
	private TextView mTvCallCSub;
	private TextView mTvSumQA;
	private TextView mTvSumQB;
	private TextView mTvSumQC;
	private TextView mTvPlaying;
	private TextView mTvVersion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final View contentView = findViewById(R.id.headerLayout);
		mSurface = (SurfaceView) findViewById(R.id.surfaceView1);
		mWebView = (WebView) findViewById(R.id.webView1);
		mLvQueueA = (ListView) findViewById(R.id.lvQueueA);
		mLvQueueB = (ListView) findViewById(R.id.lvQueueB);
		mLvQueueC = (ListView) findViewById(R.id.lvQueueC);
		mTvCallA = (TextView) findViewById(R.id.tvCallA);
		mTvCallB = (TextView) findViewById(R.id.tvCallB);
		mTvCallC = (TextView) findViewById(R.id.tvCallC);
		mTvCallASub = (TextView) findViewById(R.id.tvCallASub);
		mTvCallBSub = (TextView) findViewById(R.id.tvCallBSub);
		mTvCallCSub = (TextView) findViewById(R.id.tvCallCSub);
		mTvSumQB = (TextView) findViewById(R.id.tvSumQB);
		mTvSumQA = (TextView) findViewById(R.id.tvSumQA);
		mTvSumQC = (TextView) findViewById(R.id.tvSumQC);
		mTvPlaying = (TextView) findViewById(R.id.textViewPlaying);
		mTvVersion = (TextView) findViewById(R.id.tvVersion);
		
//		PackageInfo pInfo;
//		try {
//			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//			mTvVersion.setText("v" + pInfo.versionName);
//		} catch (NameNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//new Thread(this).start();
		
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});
		// init object
		mHandlerQueue = new Handler();
		mHandlerQueue.post(mUpdateQueue);
		mHandlerSpeakQueue = new Handler();
		mSpeakCallingQueue = new SpeakCallingQueue(this);
		try {
			mConnThread = new Thread(new QueueServerSocket(this));
			mConnThread.start();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// init media player
		mVideoPlayer = new VideoPlayer(QueueApplication.sContext, mSurface, 
				QueueApplication.getVDODir(), this);
		createMarqueeText();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	private void createMarqueeText(){
		if (!QueueApplication.getInfoText().equals("")) {
			mWebView.setVisibility(View.VISIBLE);
			StringBuilder strHtml = new StringBuilder();
			strHtml.append("<html><body style=\"background:#000;\"><FONT COLOR=\"#FFF\"><marquee direction=\"Left\" style=\"width: auto;\" >");
			strHtml.append(QueueApplication.getInfoText());
			strHtml.append("</marquee></FONT></body></html>");
			mWebView.setVisibility(View.VISIBLE);
			mWebView.loadData(strHtml.toString(), "text/html", "UTF-8");
		} else {
			mWebView.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.action_settings:
			intent = new Intent(MainActivity.this, SettingActivity.class);
			startActivity(intent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
	
	private Runnable mSpeakQueueRunnable = new Runnable(){

		@Override
		public void run() {
			if(mQueueLst.size() > 0){
				//int callingTimesLimit = Integer.parseInt(QueueApplication.getSpeakTimes());
				//if(mQueueLst.get(mQueueIdx).getCallingTime() <= callingTimesLimit){
					mSpeakCallingQueue.speak(mQueueLst.get(mQueueIdx));
				//}
			}
		}
		
	};
	
	private QueueDisplayService.LoadQueueListener mLoadQueueListener = 
			new QueueDisplayService.LoadQueueListener() {
				
				@Override
				public void onPre() {
				}
				
				@Override
				public void onPost() {
				}
				
				@Override
				public void onError(String msg) {
				}
				
				@Override
				public void onPost(QueueDisplayInfo queueInfo) {
					filterQueueGroup(queueInfo);
				}
	};
	
	private Runnable mUpdateQueue = new Runnable() {

		@Override
		public void run() {
			try {
				new QueueDisplayService(QueueApplication.sContext, 
						mLoadQueueListener).execute(QueueApplication.getFullUrl());
				mHandlerQueue.postDelayed(this, Long.parseLong(QueueApplication.getRefresh()));
			} catch (Exception e) {
				QueueApplication.sQueueLog.appendLog(e.getMessage());
				e.printStackTrace();
			}
		}

	};
	
	private void filterQueueGroup(QueueDisplayInfo queueDisplayInfo){
		int totalQueueA = 0;
		int totalQueueB = 0;
		int totalQueueC = 0;
		List<QueueDisplayInfo.QueueInfo> queueALst = new ArrayList<QueueDisplayInfo.QueueInfo>();
		List<QueueDisplayInfo.QueueInfo> queueBLst = new ArrayList<QueueDisplayInfo.QueueInfo>();
		List<QueueDisplayInfo.QueueInfo> queueCLst = new ArrayList<QueueDisplayInfo.QueueInfo>();
		for(QueueDisplayInfo.QueueInfo queueInfo : queueDisplayInfo.xListQueueInfo){
			if(queueInfo.getiQueueGroupID() == 1){
				queueALst.add(queueInfo);
				totalQueueA++;
			}
			
			if(queueInfo.getiQueueGroupID() == 2){
				queueBLst.add(queueInfo);
				totalQueueB++;
			}
			
			if(queueInfo.getiQueueGroupID() == 3){
				queueCLst.add(queueInfo);
				totalQueueC++;
			}
		}
		
		if(!queueDisplayInfo.getSzCurQueueGroupA().equals("")){
			mTvCallA.setText(queueDisplayInfo.getSzCurQueueGroupA());
			mTvCallASub.setText(queueDisplayInfo.getSzCurQueueCustomerA());
		}else{
			mTvCallA.setText("");
			mTvCallASub.setText("");
		}
		
		if(!queueDisplayInfo.getSzCurQueueGroupB().equals("")){
			mTvCallB.setText(queueDisplayInfo.getSzCurQueueGroupB());
			mTvCallBSub.setText(queueDisplayInfo.getSzCurQueueCustomerB());
		}else{
			mTvCallB.setText("");
			mTvCallBSub.setText("");
		}
		
		if(!queueDisplayInfo.getSzCurQueueGroupC().equals("")){
			mTvCallC.setText(queueDisplayInfo.getSzCurQueueGroupC());
			mTvCallCSub.setText(queueDisplayInfo.getSzCurQueueCustomerC());
		}else{
			mTvCallC.setText("");
			mTvCallCSub.setText("");
		}
		mLvQueueA.setAdapter(new TableQueueAdapter(QueueApplication.sContext, queueALst));
		mLvQueueB.setAdapter(new TableQueueAdapter(QueueApplication.sContext, queueBLst));
		mLvQueueC.setAdapter(new TableQueueAdapter(QueueApplication.sContext, queueCLst));
		mTvSumQA.setText(String.valueOf(totalQueueA));
		mTvSumQB.setText(String.valueOf(totalQueueB));
		mTvSumQC.setText(String.valueOf(totalQueueC));
	}
	
	private void readCallingQueue(){
		if(mQueueIdx == -1){
			mQueueIdx = 0;
			mHandlerSpeakQueue.post(mSpeakQueueRunnable);
		}
	}

	@Override
	protected void onDestroy() {
		try {
			//mHandlerQueue.removeCallbacks(mUpdateQueue);
			mHandlerSpeakQueue.removeCallbacks(mSpeakQueueRunnable);
			mVideoPlayer.releaseMediaPlayer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onDestroy();
	}

	public void videoBackClicked(final View v){
		mVideoPlayer.back();
	}
	
	public void videoPauseClicked(final View v){
		if(!isPause){
			mVideoPlayer.pause();
			((ImageButton)v).setImageResource(android.R.drawable.ic_media_play);
			isPause = true;
		}
		else{
			mVideoPlayer.resume();
			((ImageButton)v).setImageResource(android.R.drawable.ic_media_pause);
			isPause = false;
		}
	}
	
	public void videoNextClicked(final View v){
		mVideoPlayer.next();
	}

	@Override
	public void onReceipt(String msg) {
		Gson gson = new Gson();
		Type type = new TypeToken<QueueDisplayInfo>() {}.getType();
		try {
			final QueueDisplayInfo queueDisplayInfo = 
					(QueueDisplayInfo) gson.fromJson(msg, type);
			runOnUiThread(new Runnable(){

				@Override
				public void run() {
					filterQueueGroup(queueDisplayInfo);	
				}
				
			});
		} catch (Exception e) {
		}
	}

	@Override
	public void onAcceptErr(String msg) {
		
	}

	@Override
	public void onStartPlay() {
		mVideoPlayer.setSoundVolumn(0.0f, 0.0f);
	}

	@Override
	public void onPlayComplete() {
		mVideoPlayer.setSoundVolumn(1.0f, 1.0f);
		if(mQueueIdx < mQueueLst.size() - 1){
			mHandlerSpeakQueue.post(mSpeakQueueRunnable);
			mQueueIdx++;
		}else{
			mQueueIdx = -1;
		}
	}

	@Override
	public void onPlayedFileName(String fileName) {
		mTvPlaying.setText(fileName);
	}

	@Override
	public void onError(Exception e) {
		try {
			mVideoPlayer.releaseMediaPlayer();
			mVideoPlayer.startPlayMedia();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
