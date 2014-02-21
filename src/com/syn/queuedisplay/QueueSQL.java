package com.syn.queuedisplay;

public class QueueSQL {
	public static final String CREATE_CALLING_QUEUE_TABLE =
			"create table " + QueueProvider.TABLE_CALLING_QUEUE + "( " +
			QueueProvider.COLUMN_CALLING_QUEUE_NAME + " text );";
}
