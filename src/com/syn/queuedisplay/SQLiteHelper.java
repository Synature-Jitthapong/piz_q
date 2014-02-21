package com.syn.queuedisplay;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper{
	public static final String[] SQL_TABLE_CREATE ={
		QueueSQL.CREATE_CALLING_QUEUE_TABLE
	};
	
	public SQLiteHelper() {
		super(QueueApplication.sContext, QueueApplication.DB_NAME, 
				null, QueueApplication.DB_VERSION);
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
