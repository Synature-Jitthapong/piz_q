package com.syn.queuedisplay.custom;

import java.io.File;
import java.io.IOException;
import com.j1tth4.mobile.util.MediaManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;

public class SpeakCallingQueue implements OnCompletionListener, OnPreparedListener{
	private String mSoundPath;
	private MediaManager mMediaManager;
	private MediaPlayer mMediaPlayer;
	private OnPlaySoundListener mOnPlaySoundListener;
	
	public SpeakCallingQueue(OnPlaySoundListener onPlayListener){
		mMediaManager = new MediaManager(QueueApplication.sContext, 
				QueueApplication.getQueueSpeakDir());
		mMediaPlayer = new MediaPlayer();
		mOnPlaySoundListener = onPlayListener;
	}
	
	public void speak(String queueText){
		getCallingSoundPath(queueText);
		playSound();
	}
	
	private void playSound(){
		try {
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(mSoundPath);
			mMediaPlayer.prepare();
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
	
	private File[] listFiles(){
		File sdCard = mMediaManager.getSdCard();
		return sdCard.listFiles(new MediaManager.MP3ExtensionFilter());
	}
	
	private void getCallingSoundPath(String queueText){
		File[] files = listFiles();
		if(files.length > 0){
			for(int i = 0; i < files.length; i++){
				File f = files[i];
				String fileName = f.getName().replaceFirst("[.][^.]+$", "");
				if(fileName.equalsIgnoreCase(queueText)){
					mSoundPath = f.getPath();
				}
			}
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
		mOnPlaySoundListener.onPlayComplete();
	}
	
	public static interface OnPlaySoundListener{
		void onStartPlay();
		void onPlayComplete();
	}
}
