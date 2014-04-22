package com.syn.queuedisplay.custom;

import android.database.sqlite.SQLiteDatabase;

public class QueueColumnsTable{
	public static final String TABLE_QUEUE_COLUMNS = "QeueColumns";
	public static final String COLUMN_QUEUE_GROUP_ID = "queue_group_id";
	public static final String COLUMN_QUEUE_GROUP_NAME = "queue_group_name";
	
	private static final String SQL_CREATE = 
			"create table " + TABLE_QUEUE_COLUMNS + "( "
			+ COLUMN_QUEUE_GROUP_ID + " integer primary key, "
			+ COLUMN_QUEUE_GROUP_NAME + " text );";
	
	public static void onCreate(SQLiteDatabase db){
		db.execSQL(SQL_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		
	}
}
