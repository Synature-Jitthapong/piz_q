package com.syn.queuedisplay.afteryou;

import android.content.Context;

import com.j1tth4.mobile.util.Logger;

public class QueueLog extends Logger {
	public static final String LOG_DIR = "pRoMiSeQueueLog";
	public static final String FILE_NAME = "pRoMiSeQueueLog";
	
	public QueueLog(Context c) {
		super(c, LOG_DIR, FILE_NAME);
	}
}
