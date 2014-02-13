package com.syn.queuedisplay;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import com.j1tth4.mobile.util.MediaManager;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;

public class SpeakCallingQueue implements OnCompletionListener, OnPreparedListener{
	private int mSoundIdx = -1;
	private String[] mSoundPath;
	private MediaManager mMediaManager;
	private MediaPlayer mMediaPlayer;
	private OnPlaySoundListener mOnPlaySoundListener;
	
	public SpeakCallingQueue(OnPlaySoundListener onPlayListener){
		mMediaManager = new MediaManager(QueueApplication.sContext, 
				QueueApplication.getQueueSpeakDir());
		mMediaPlayer = new MediaPlayer();
		mOnPlaySoundListener = onPlayListener;
	}
	
	public synchronized void speak(String queueText){
		getCallingSoundPath(queueText);
		playSound();
	}
	
	private void playSound(){
		try {
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(mSoundPath[mSoundIdx]);
			mMediaPlayer.prepare();
			mMediaPlayer.setScreenOnWhilePlaying(true);
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setOnCompletionListener(this);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getSuffixSoundPath(){
		File[] files = listFiles();
	}
	
	private void getPrefixSoundPath(){
		
	}
	
	private File[] listFiles(){
		File sdCard = mMediaManager.getSdCard();
		return sdCard.listFiles(new MediaManager.SoundFileExtensionFilter());
	}
	
	private void getCallingSoundPath(String queueText){
		char[] ch = queueText.toLowerCase(Locale.getDefault()).toCharArray();
		File[] files = listFiles();
		if(files.length > 0){
			mSoundIdx = 0;
			mSoundPath = new String[ch.length];
			for(int i = 0; i < files.length; i++){
				File f = files[i];
				for(int j = 0; j < ch.length; j ++){
					String fileName = String.valueOf(ch[j]) + MediaManager.WAV_EXTENSION;
					if(fileName.equals(f.getName())){
						mSoundPath[j] = f.getPath();
					}
				}
			}
		}
	}
	
	private void nextTrack(){
		try {
			if(mSoundIdx < (mSoundPath.length - 1)){
				mSoundIdx++;
				playSound();
			}else{
				mSoundIdx = 0;
				mOnPlaySoundListener.onPlayComplete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void startPlayback(){
		mMediaPlayer.start();
	}
	
	@Override
	public void onPrepared(MediaPlayer mp) {
		startPlayback(); 
		mOnPlaySoundListener.onStartPlay();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		nextTrack();
	}
	
	public static interface OnPlaySoundListener{
		void onStartPlay();
		void onPlayComplete();
	}
}
