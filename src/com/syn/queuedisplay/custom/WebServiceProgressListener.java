package com.syn.queuedisplay.custom;

public interface WebServiceProgressListener {
	void onPre();
	void onPost();
	void onError(String msg);
}
