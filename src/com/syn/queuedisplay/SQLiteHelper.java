package com.syn.queuedisplay;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper{
	public static final String SQL_TABLE_CREATE = 
			"CREATE TABLE CallingQueue(" + 
			" queue_name TEXT, " +
			" calling_time TEXT " +
			");";
	
	public SQLiteHelper() {
		super(QueueApplication.sContext, QueueApplication.DB_NAME, 
				null, QueueApplication.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
