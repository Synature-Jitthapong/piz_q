package com.syn.queuedisplay;

public interface WebServiceProgressListener {
	void onPre();
	void onPost();
	void onError(String msg);
}
