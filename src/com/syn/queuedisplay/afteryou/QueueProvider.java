package com.syn.queuedisplay.afteryou;

import java.sql.SQLException;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class QueueProvider {
	public static final String TABLE_CALLING_QUEUE = "CallingQueue";
	public static final String COLUMN_CALLING_QUEUE_NAME = "calling_queue_name";
	
	private SQLiteDatabase mSqlite;
	
	public QueueProvider(SQLiteDatabase sqlite){
		mSqlite = sqlite;
	}
	
	public Cursor getCallingQueueName(){
		return mSqlite.query(TABLE_CALLING_QUEUE, 
				new String[]{
					COLUMN_CALLING_QUEUE_NAME
				}, null, null, null, null, null);
	}
	
	public void deleteQueue(String queueName){
		mSqlite.delete(TABLE_CALLING_QUEUE, 
				COLUMN_CALLING_QUEUE_NAME + "=?", 
				new String[]{
					queueName
				}
		);
	}
	
	public void addCallingQueue(String callingQueueName) throws SQLException{
		deleteQueue(callingQueueName);
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_CALLING_QUEUE_NAME, callingQueueName);
		mSqlite.insertOrThrow(TABLE_CALLING_QUEUE, null, cv);
	}
}
