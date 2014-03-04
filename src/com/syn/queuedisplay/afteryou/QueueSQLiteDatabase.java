package com.syn.queuedisplay.afteryou;

import android.database.sqlite.SQLiteDatabase;

public class QueueSQLiteDatabase {
	private SQLiteHelper mSqlite;
	
	public QueueSQLiteDatabase(){
		mSqlite = new SQLiteHelper();	
	}
	
	public SQLiteDatabase getReadDatabase(){
		return mSqlite.getReadableDatabase();
	}
	
	public SQLiteDatabase getWriteDatabase(){
		return mSqlite.getWritableDatabase();
	}
}
