package com.syn.queuedisplay.custom;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class QueueDatabase{
	
	private SQLiteDatabase mSqlite;
	
	public QueueDatabase(SQLiteDatabase sqlite) {
		mSqlite = sqlite;
	}

	public CallingQueueData getCallingQueueName(String queueName){
		CallingQueueData data = new CallingQueueData();
		Cursor cursor = mSqlite.query(QueueEntry.TABLE_CALLING_QUEUE, 
				new String[]{
				QueueEntry.COLUMN_CALLING_QUEUE_NAME,
				QueueEntry.COLUMN_CALLING_QUEUE_TIMES
				}, QueueEntry.COLUMN_CALLING_QUEUE_NAME + "=?", 
				new String[]{
				QueueEntry.COLUMN_CALLING_QUEUE_NAME
				}, null, null, null);
		if(cursor.moveToFirst()){
			data.setQueueName(cursor.getString(cursor.getColumnIndex(QueueEntry.COLUMN_CALLING_QUEUE_NAME)));
			data.setCallingTime(cursor.getInt(cursor.getColumnIndex(QueueEntry.COLUMN_CALLING_QUEUE_TIMES)));
		}
		cursor.close();
		return data;
	}
	
	public List<CallingQueueData> listCallingQueueName(int callingTime){
		List<CallingQueueData> queueLst = new ArrayList<CallingQueueData>();
		Cursor cursor = mSqlite.query(
				QueueEntry.TABLE_CALLING_QUEUE,
				new String[]{
						QueueEntry.COLUMN_CALLING_QUEUE_NAME,
						QueueEntry.COLUMN_CALLING_QUEUE_TIMES
				}, QueueEntry.COLUMN_CALLING_QUEUE_TIMES + "<?", 
				new String[]{String.valueOf(callingTime)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				CallingQueueData data = new CallingQueueData();
				data.setQueueName(cursor.getString(cursor.getColumnIndex(QueueEntry.COLUMN_CALLING_QUEUE_NAME)));
				data.setCallingTime(cursor.getInt(cursor.getColumnIndex(QueueEntry.COLUMN_CALLING_QUEUE_TIMES)));
				queueLst.add(data);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return queueLst;
	}
	
	public void deleteQueue(){
		mSqlite.delete(QueueEntry.TABLE_CALLING_QUEUE, null, null);
	}
	
	public void deleteQueue(String queueName){
		mSqlite.delete(QueueEntry.TABLE_CALLING_QUEUE, 
				QueueEntry.COLUMN_CALLING_QUEUE_NAME + "=?", 
				new String[]{queueName}
		);
	}
	
	public void updateCallingQueue(String queueName, int callingTimes){
		ContentValues cv = new ContentValues();
		cv.put(QueueEntry.COLUMN_CALLING_QUEUE_TIMES, callingTimes);
		mSqlite.update(QueueEntry.TABLE_CALLING_QUEUE, cv, 
				QueueEntry.COLUMN_CALLING_QUEUE_NAME + "=?", new String[]{queueName});
	}
	
	public void addCallingQueue(String queueName){
		try {
			ContentValues cv = new ContentValues();
			cv.put(QueueEntry.COLUMN_CALLING_QUEUE_NAME, queueName);
			mSqlite.insertOrThrow(QueueEntry.TABLE_CALLING_QUEUE, null, cv);
		} catch (android.database.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static abstract class QueueColumnsEntry{
		public static final String TABLE_QUEUE_COLUMNS = "QeueColumns";
		public static final String COLUMN_QUEUE_GROUP_ID = "queue_group_id";
		public static final String COLUMN_QUEUE_GROUP_NAME = "queue_group_name";
	}
	
	public static abstract class QueueEntry{
		public static final String TABLE_CALLING_QUEUE = "CallingQueue";
		public static final String COLUMN_CALLING_QUEUE_NAME = "calling_queue_name";
		public static final String COLUMN_CALLING_QUEUE_TIMES = "calling_queue_times";
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
