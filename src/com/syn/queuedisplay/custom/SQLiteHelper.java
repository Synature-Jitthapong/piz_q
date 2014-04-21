package com.syn.queuedisplay.custom;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper{

	public static final String DB_NAME = "queue.db";
	
	public static final int DB_VERSION = 1;
	
	public static final String[] SQL_TABLE_CREATE ={
		QueueSQL.CALLING_QUEUE_SQL,
		QueueSQL.QUEUE_COLUMNS_SQL
	};
	
	public SQLiteHelper(Context c) {
		super(c, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for(String sql : SQL_TABLE_CREATE){
			db.execSQL(sql);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
