package com.syn.queuedisplay.afteryou;

public interface WebServiceProgressListener {
	void onPre();
	void onPost();
	void onError(String msg);
}
