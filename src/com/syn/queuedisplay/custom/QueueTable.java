package com.syn.queuedisplay.custom;

import android.database.sqlite.SQLiteDatabase;

public class QueueTable{
	public static final String TABLE_CALLING_QUEUE = "CallingQueue";
	public static final String COLUMN_CALLING_QUEUE_NAME = "calling_queue_name";
	public static final String COLUMN_CALLING_QUEUE_TIMES = "calling_queue_times";
	
	private static final String SQL_CREATE =
			"create table " + TABLE_CALLING_QUEUE + "( " +
			COLUMN_CALLING_QUEUE_NAME + " text, " +
			COLUMN_CALLING_QUEUE_TIMES + " integer default 0, " +
			"primary key (" + COLUMN_CALLING_QUEUE_NAME + "));";
	
	public static void onCreate(SQLiteDatabase db){
		db.execSQL(SQL_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		
	}
}
