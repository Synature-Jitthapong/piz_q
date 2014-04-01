package com.syn.queuedisplay.custom;

import java.util.ArrayList;
import java.util.List;

import com.syn.pos.QueueDisplayInfo;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class QueueColumnFragment extends Fragment{

	protected QueueDatabase mQueueDatabase;
	protected List<QueueDisplayInfo.QueueInfo> mQueueALst;
	
	protected Handler mNotifyCalling;
	protected ListView mLvQueueA;
	protected TextView mTvCallA;
	protected TextView mTvCallASub;
	protected TextView mTvSumQA;
	
	public static QueueColumnFragment newInstance(){
		QueueColumnFragment f = new QueueColumnFragment();
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNotifyCalling = new Handler();
		mQueueDatabase = new QueueDatabase(QueueApplication.getWritableDatabase());
		mQueueALst = new ArrayList<QueueDisplayInfo.QueueInfo>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.queue_columns_fragment, container, false);
		mLvQueueA = (ListView) v.findViewById(R.id.lvQueueA);
		mTvCallA = (TextView) v.findViewById(R.id.tvCallA);
		mTvCallASub = (TextView) v.findViewById(R.id.tvCallASub);
		mTvSumQA = (TextView) v.findViewById(R.id.tvSumQA);
		return v;
	}

	private Runnable mRunnableNotifyCalling = new Runnable(){

		@Override
		public void run() {
			if(mTvCallA.getVisibility() == View.VISIBLE)
				mTvCallA.setVisibility(View.INVISIBLE);
			else
				mTvCallA.setVisibility(View.VISIBLE);
			mNotifyCalling.postDelayed(this, 1000);
		}
	};
	
	public void setQueueData(QueueDisplayInfo queueDisplayInfo){
		int totalQueueA = 0;
		mQueueALst.clear();
		for(QueueDisplayInfo.QueueInfo queueInfo : queueDisplayInfo.xListQueueInfo){
			if(queueInfo.getiQueueGroupID() == 1){
				mQueueALst.add(queueInfo);
				totalQueueA++;
			}
		}
		
		if(!queueDisplayInfo.getSzCurQueueGroupA().equals("")){
			mTvCallA.setText(queueDisplayInfo.getSzCurQueueGroupA());
			mTvCallASub.setText(queueDisplayInfo.getSzCurQueueCustomerA());
			mQueueDatabase.addCallingQueue(queueDisplayInfo.getSzCurQueueGroupA());
		}else{
			mTvCallA.setText("");
			mTvCallASub.setText("");
		}
		
		mLvQueueA.setAdapter(new TableQueueAdapter(QueueApplication.sContext, mQueueALst));
		mTvSumQA.setText(String.valueOf(totalQueueA));
		mNotifyCalling.removeCallbacks(mRunnableNotifyCalling);
		mNotifyCalling.post(mRunnableNotifyCalling);
	}
}
