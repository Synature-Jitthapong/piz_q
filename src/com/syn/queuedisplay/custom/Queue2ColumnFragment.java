package com.syn.queuedisplay.custom;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.syn.pos.QueueDisplayInfo;

public class Queue2ColumnFragment extends QueueColumnFragment{
	
	protected List<QueueDisplayInfo.QueueInfo> mQueueBLst;
	protected ListView mLvQueueB;
	protected TextView mTvCallB;
	protected TextView mTvCallBSub;
	protected TextView mTvSumQB;
	
	public static Queue2ColumnFragment newInstance(){
		Queue2ColumnFragment f = new Queue2ColumnFragment();
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mQueueBLst = new ArrayList<QueueDisplayInfo.QueueInfo>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.queue2columns_fragment, container, false);
		mLvQueueA = (ListView) v.findViewById(R.id.lvQueueA);
		mLvQueueB = (ListView) v.findViewById(R.id.lvQueueB);
		mTvCallA = (TextView) v.findViewById(R.id.tvCallA);
		mTvCallB = (TextView) v.findViewById(R.id.tvCallB);
		mTvCallASub = (TextView) v.findViewById(R.id.tvCallASub);
		mTvCallBSub = (TextView) v.findViewById(R.id.tvCallBSub);
		mTvSumQB = (TextView) v.findViewById(R.id.tvSumQB);
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
			
			if(mTvCallB.getVisibility() == View.VISIBLE)
				mTvCallB.setVisibility(View.INVISIBLE);
			else
				mTvCallB.setVisibility(View.VISIBLE);
			mNotifyCalling.postDelayed(this, 1000);
		}
	};
	
	public void setQueueData(QueueDisplayInfo queueDisplayInfo){
		int totalQueueA = 0;
		int totalQueueB = 0;
		mQueueALst.clear();
		mQueueBLst.clear();
		for(QueueDisplayInfo.QueueInfo queueInfo : queueDisplayInfo.xListQueueInfo){
			if(queueInfo.getiQueueGroupID() == 1){
				mQueueALst.add(queueInfo);
				totalQueueA++;
			}
			
			if(queueInfo.getiQueueGroupID() == 2){
				mQueueBLst.add(queueInfo);
				totalQueueB++;
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
		
		if(!queueDisplayInfo.getSzCurQueueGroupB().equals("")){
			mTvCallB.setText(queueDisplayInfo.getSzCurQueueGroupB());
			mTvCallBSub.setText(queueDisplayInfo.getSzCurQueueCustomerB());
			mQueueDatabase.addCallingQueue(queueDisplayInfo.getSzCurQueueGroupB());
		}else{
			mTvCallB.setText("");
			mTvCallBSub.setText("");
		}
		
		mLvQueueA.setAdapter(new TableQueueAdapter(QueueApplication.sContext, mQueueALst));
		mLvQueueB.setAdapter(new TableQueueAdapter(QueueApplication.sContext, mQueueBLst));
		mTvSumQA.setText(String.valueOf(totalQueueA));
		mTvSumQB.setText(String.valueOf(totalQueueB));
		mNotifyCalling.removeCallbacks(mRunnableNotifyCalling);
		mNotifyCalling.post(mRunnableNotifyCalling);
	}
}
