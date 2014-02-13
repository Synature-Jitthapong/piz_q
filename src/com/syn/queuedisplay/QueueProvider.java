package com.syn.queuedisplay;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class QueueProvider {
	private SQLiteDatabase mSqlite;
	
	public QueueProvider(SQLiteDatabase sqlite){
		mSqlite = sqlite;
	}
	
	public void addCallingQueue(String callingQueueName, String callingTime){
		ContentValues cv = new ContentValues();
		
	}
}
