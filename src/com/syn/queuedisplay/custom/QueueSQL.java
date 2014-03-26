package com.syn.queuedisplay.custom;

import com.syn.queuedisplay.custom.QueueProvider.QueueColumnsEntry;
import com.syn.queuedisplay.custom.QueueProvider.QueueEntry;

public class QueueSQL {
	public static final String QUEUE_COLUMNS_SQL = 
			"create table " + QueueColumnsEntry.TABLE_QUEUE_COLUMNS + "( "
			+ QueueColumnsEntry.COLUMN_QUEUE_GROUP_ID + " integer primary key, "
			+ QueueColumnsEntry.COLUMN_QUEUE_GROUP_NAME + " text );";
	
	public static final String CALLING_QUEUE_SQL =
			"create table " + QueueEntry.TABLE_CALLING_QUEUE + "( " +
			QueueEntry.COLUMN_CALLING_QUEUE_NAME + " text, " +
			QueueEntry.COLUMN_CALLING_QUEUE_TIMES + " integer default 0, " +
			"primary key (" + QueueEntry.COLUMN_CALLING_QUEUE_NAME + "));";
}
