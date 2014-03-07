package com.syn.queuedisplay.custom;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;

public class QueueApplication extends Application{
	public static final String DB_NAME = "queue.db";
	
	public static final int DB_VERSION = 1;
	
	public static final String WEBSERVICE_NAME = "ws_mpos.asmx";
	
	public static Context sContext;
	
	public static QueueLog sQueueLog;
	
	@Override
	public void onCreate() {
		super.onCreate();
		sContext = getApplicationContext();
		sQueueLog = new QueueLog(sContext);
	}
	
	public static String getFullUrl(){
		return getUrl() + "/" + WEBSERVICE_NAME;
	}
	
	private static String getUrl(){
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(sContext);
		String url = sharedPref.getString(SettingActivity.PREF_URL, "");
		try {
			new URL(url);
		} catch (MalformedURLException e) {
			// not found protocal
			url = "http://" + url;
			e.printStackTrace();
		}
		return url;
	}
	
	public static String getRefresh(){
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(sContext);
		return sharedPref.getString(SettingActivity.PREF_REFRESH, "15000");
	}
	
	public static String getInfoText(){
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(sContext);
		return sharedPref.getString(SettingActivity.PREF_INFO_TEXT, "");
	}
	
	public static String getVDODir(){
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(sContext);
		return sharedPref.getString(SettingActivity.PREF_VDO_DIR, "");
	}
	
	public static String getQueueSpeakDir(){
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(sContext);
		return sharedPref.getString(SettingActivity.PREF_QUEUE_SPEAK_DIR, "");
	}

	public static String getDeviceCode(){
		return Secure.getString(sContext.getContentResolver(),
				Secure.ANDROID_ID);
	}
	
	public static String getShopId(){
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(sContext);
		return sharedPref.getString(SettingActivity.PREF_SHOP_ID, "");
	}
}
