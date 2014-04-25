package com.syn.queuedisplay.pizzahut;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class QueueServerSocket implements Runnable{
	public static final int PORT = 6060;
	
	private boolean isSocketClosed = false;
	
	private ServerSocketListener mListener;
	private ServerSocket mSocket; 

	public QueueServerSocket(ServerSocketListener listener) throws IOException{
		mSocket = new ServerSocket(PORT);
		mListener = listener;
	}
	
	@Override
	public void run() {
		Socket socket = null;
		while(!isSocketClosed){
			try {
				socket = mSocket.accept();
				BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String msg = bf.readLine();
				if(msg != null)
					mListener.onReceipt(msg);
			} catch (IOException e) {
				mListener.onAcceptErr(e.getMessage());
			}
		}
	}
	
	public void close() throws IOException{
		mSocket.close();
		isSocketClosed = true;
	}
	
	public static interface ServerSocketListener{
		void onReceipt(String msg);
		void onAcceptErr(String msg);
	}
}