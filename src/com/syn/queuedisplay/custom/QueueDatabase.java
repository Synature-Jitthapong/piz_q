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
		Cursor cursor = mSqlite.query(QueueTable.TABLE_CALLING_QUEUE, 
				new String[]{
				QueueTable.COLUMN_CALLING_QUEUE_NAME,
				QueueTable.COLUMN_CALLING_QUEUE_TIMES
				}, QueueTable.COLUMN_CALLING_QUEUE_NAME + "=?", 
				new String[]{
				QueueTable.COLUMN_CALLING_QUEUE_NAME
				}, null, null, null);
		if(cursor.moveToFirst()){
			data.setQueueName(cursor.getString(cursor.getColumnIndex(QueueTable.COLUMN_CALLING_QUEUE_NAME)));
			data.setCallingTime(cursor.getInt(cursor.getColumnIndex(QueueTable.COLUMN_CALLING_QUEUE_TIMES)));
		}
		cursor.close();
		return data;
	}
	
	public List<CallingQueueData> listCallingQueueName(int callingTime){
		List<CallingQueueData> queueLst = new ArrayList<CallingQueueData>();
		Cursor cursor = mSqlite.query(
				QueueTable.TABLE_CALLING_QUEUE,
				new String[]{
						QueueTable.COLUMN_CALLING_QUEUE_NAME,
						QueueTable.COLUMN_CALLING_QUEUE_TIMES
				}, QueueTable.COLUMN_CALLING_QUEUE_TIMES + "<?", 
				new String[]{String.valueOf(callingTime)}, null, null, QueueTable.COLUMN_CALLING_QUEUE_NAME);
		if(cursor.moveToFirst()){
			do{
				CallingQueueData data = new CallingQueueData();
				data.setQueueName(cursor.getString(cursor.getColumnIndex(QueueTable.COLUMN_CALLING_QUEUE_NAME)));
				data.setCallingTime(cursor.getInt(cursor.getColumnIndex(QueueTable.COLUMN_CALLING_QUEUE_TIMES)));
				queueLst.add(data);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return queueLst;
	}
	
	public void deleteQueue(){
		mSqlite.delete(QueueTable.TABLE_CALLING_QUEUE, null, null);
	}
	
	public void deleteQueue(String queueName){
		mSqlite.delete(QueueTable.TABLE_CALLING_QUEUE, 
				QueueTable.COLUMN_CALLING_QUEUE_NAME + "=?", 
				new String[]{queueName}
		);
	}
	
	public void updateCallingQueue(String queueName, int callingTimes){
		ContentValues cv = new ContentValues();
		cv.put(QueueTable.COLUMN_CALLING_QUEUE_TIMES, callingTimes);
		mSqlite.update(QueueTable.TABLE_CALLING_QUEUE, cv, 
				QueueTable.COLUMN_CALLING_QUEUE_NAME + "=?", new String[]{queueName});
	}
	
	public void addCallingQueue(String queueName){
		try {
			ContentValues cv = new ContentValues();
			cv.put(QueueTable.COLUMN_CALLING_QUEUE_NAME, queueName);
			mSqlite.insertOrThrow(QueueTable.TABLE_CALLING_QUEUE, null, cv);
		} catch (android.database.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
