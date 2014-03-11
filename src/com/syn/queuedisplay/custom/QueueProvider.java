package com.syn.queuedisplay.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class QueueProvider extends SQLiteHelper{
	public static final String TABLE_CALLING_QUEUE = "CallingQueue";
	public static final String COLUMN_CALLING_QUEUE_NAME = "calling_queue_name";
	public static final String COLUMN_CALLING_QUEUE_TIMES = "calling_queue_times";
	
	public QueueProvider(Context c) {
		super(c);
	}

	public CallingQueueData getCallingQueueName(String queueName){
		CallingQueueData data = new CallingQueueData();
		Cursor cursor = getReadableDatabase().query(TABLE_CALLING_QUEUE, 
				new String[]{
					COLUMN_CALLING_QUEUE_NAME,
					COLUMN_CALLING_QUEUE_TIMES
				}, COLUMN_CALLING_QUEUE_NAME + "=?", 
				new String[]{
					COLUMN_CALLING_QUEUE_NAME
				}, null, null, null);
		if(cursor.moveToFirst()){
			data.setQueueName(cursor.getString(cursor.getColumnIndex(COLUMN_CALLING_QUEUE_NAME)));
			data.setCallingTime(cursor.getInt(cursor.getColumnIndex(COLUMN_CALLING_QUEUE_TIMES)));
		}
		cursor.close();
		return data;
	}
	
	public List<CallingQueueData> listCallingQueueName(){
		List<CallingQueueData> queueLst = new ArrayList<CallingQueueData>();
		Cursor cursor = getReadableDatabase().query(
				TABLE_CALLING_QUEUE,
				new String[]{
					COLUMN_CALLING_QUEUE_NAME,
					COLUMN_CALLING_QUEUE_TIMES
				}, null, null, null, null, null);
		if(cursor.moveToFirst()){
			CallingQueueData data = new CallingQueueData();
			do{
				data.setQueueName(cursor.getString(cursor.getColumnIndex(COLUMN_CALLING_QUEUE_NAME)));
				data.setCallingTime(cursor.getInt(cursor.getColumnIndex(COLUMN_CALLING_QUEUE_TIMES)));
				queueLst.add(data);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return queueLst;
	}
	
	public void deleteQueue(String queueName){
		super.getWritableDatabase().delete(TABLE_CALLING_QUEUE, 
				COLUMN_CALLING_QUEUE_NAME + "=?", 
				new String[]{queueName}
		);
	}
	
	public void updateCallingQueue(String queueName, int callingTimes){
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_CALLING_QUEUE_TIMES, callingTimes);
		super.getWritableDatabase().update(TABLE_CALLING_QUEUE, cv, 
				COLUMN_CALLING_QUEUE_NAME + "=?", new String[]{queueName});
	}
	
	public void addCallingQueue(String queueName) throws SQLException{
		deleteQueue(queueName);
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_CALLING_QUEUE_NAME, queueName);
		super.getWritableDatabase().insertOrThrow(TABLE_CALLING_QUEUE, null, cv);
	}
	
	public static class CallingQueueData{
		private String queueName;
		private int callingTime;
		
		public String getQueueName() {
			return queueName;
		}
		public void setQueueName(String queueName) {
			this.queueName = queueName;
		}
		public int getCallingTime() {
			return callingTime;
		}
		public void setCallingTime(int callingTime) {
			this.callingTime = callingTime;
		}
	}
}
