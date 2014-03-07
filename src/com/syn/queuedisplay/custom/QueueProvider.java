package com.syn.queuedisplay.custom;

import java.sql.SQLException;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class QueueProvider extends SQLiteHelper{
	public static final String TABLE_CALLING_QUEUE = "CallingQueue";
	public static final String COLUMN_CALLING_QUEUE_NAME = "calling_queue_name";
	
	public QueueProvider(Context c) {
		super(c);
	}

	public Cursor getCallingQueueName(){
		return getReadableDatabase().query(TABLE_CALLING_QUEUE, 
				new String[]{
					COLUMN_CALLING_QUEUE_NAME
				}, null, null, null, null, null);
	}
	
	public void deleteQueue(String queueName){
		super.getWritableDatabase().delete(TABLE_CALLING_QUEUE, 
				COLUMN_CALLING_QUEUE_NAME + "=?", 
				new String[]{
					queueName
				}
		);
	}
	
	public void addCallingQueue(String callingQueueName) throws SQLException{
		Cursor cursor = super.getReadableDatabase().query(TABLE_CALLING_QUEUE, 
				new String[]{COLUMN_CALLING_QUEUE_NAME}, COLUMN_CALLING_QUEUE_NAME + "=?", 
				new String[]{callingQueueName}, null, null, null);
		if(cursor.moveToFirst()){
			if(!cursor.getString(0).equals(callingQueueName)){
				ContentValues cv = new ContentValues();
				cv.put(COLUMN_CALLING_QUEUE_NAME, callingQueueName);
				super.getWritableDatabase().insertOrThrow(TABLE_CALLING_QUEUE, null, cv);
			}
		}
	}
}
