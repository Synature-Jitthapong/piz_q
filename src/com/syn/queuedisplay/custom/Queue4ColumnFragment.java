package com.syn.queuedisplay.custom;

import java.util.ArrayList;
import java.util.List;

import com.syn.pos.QueueDisplayInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class Queue4ColumnFragment extends Queue3ColumnFragment{
	protected List<QueueDisplayInfo.QueueInfo> mQueueDLst;
	
	protected ListView mLvQueueD;
	protected TextView mTvCallD;
	protected TextView mTvCallDSub;
	protected TextView mTvSumQD;
	
	public static Queue4ColumnFragment newInstance(){
		Queue4ColumnFragment f = new Queue4ColumnFragment();
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mQueueDLst = new ArrayList<QueueDisplayInfo.QueueInfo>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.queue4columns_fragment, container, false);

		mLvQueueA = (ListView) v.findViewById(R.id.lvQueueA);
		mLvQueueB = (ListView) v.findViewById(R.id.lvQueueB);
		mLvQueueC = (ListView) v.findViewById(R.id.lvQueueC);
		mLvQueueD = (ListView) v.findViewById(R.id.lvQueueD);
		mTvCallA = (TextView) v.findViewById(R.id.tvCallA);
		mTvCallB = (TextView) v.findViewById(R.id.tvCallB);
		mTvCallC = (TextView) v.findViewById(R.id.tvCallC);
		mTvCallD = (TextView) v.findViewById(R.id.tvCallD);
		mTvCallASub = (TextView) v.findViewById(R.id.tvCallASub);
		mTvCallBSub = (TextView) v.findViewById(R.id.tvCallBSub);
		mTvCallCSub = (TextView) v.findViewById(R.id.tvCallCSub);
		mTvCallDSub = (TextView) v.findViewById(R.id.tvCallDSub);
		mTvSumQB = (TextView) v.findViewById(R.id.tvSumQB);
		mTvSumQA = (TextView) v.findViewById(R.id.tvSumQA);
		mTvSumQC = (TextView) v.findViewById(R.id.tvSumQC);
		mTvSumQD = (TextView) v.findViewById(R.id.tvSumQD);
		
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
			
			if(mTvCallC.getVisibility() == View.VISIBLE)
				mTvCallC.setVisibility(View.INVISIBLE);
			else
				mTvCallC.setVisibility(View.VISIBLE);

			if(mTvCallD.getVisibility() == View.VISIBLE)
				mTvCallD.setVisibility(View.INVISIBLE);
			else
				mTvCallD.setVisibility(View.VISIBLE);
			mNotifyCalling.postDelayed(this, 1000);
		}
	};
	
	public void setQueueData(QueueDisplayInfo queueDisplayInfo){
		int totalQueueA = 0;
		int totalQueueB = 0;
		int totalQueueC = 0;
		int totalQueueD = 0;
		mQueueALst.clear();
		mQueueBLst.clear();
		mQueueCLst.clear();
		mQueueDLst.clear();
		for(QueueDisplayInfo.QueueInfo queueInfo : queueDisplayInfo.xListQueueInfo){
			if(queueInfo.getiQueueGroupID() == 1){
				mQueueALst.add(queueInfo);
				totalQueueA++;
			}
			
			if(queueInfo.getiQueueGroupID() == 2){
				mQueueBLst.add(queueInfo);
				totalQueueB++;
			}
			
			if(queueInfo.getiQueueGroupID() == 3){
				mQueueCLst.add(queueInfo);
				totalQueueC++;
			}
			
			if(queueInfo.getiQueueGroupID() == 4){
				mQueueDLst.add(queueInfo);
				totalQueueD++;
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
		
		if(!queueDisplayInfo.getSzCurQueueGroupC().equals("")){
			mTvCallC.setText(queueDisplayInfo.getSzCurQueueGroupC());
			mTvCallCSub.setText(queueDisplayInfo.getSzCurQueueCustomerC());
			mQueueDatabase.addCallingQueue(queueDisplayInfo.getSzCurQueueGroupC());
		}else{
			mTvCallC.setText("");
			mTvCallCSub.setText("");
		}
		
		if(!queueDisplayInfo.getSzCurQueueGroupD().equals("")){
			mTvCallD.setText(queueDisplayInfo.getSzCurQueueGroupD());
			mTvCallDSub.setText(queueDisplayInfo.getSzCurQueueCustomerD());
			mQueueDatabase.addCallingQueue(queueDisplayInfo.getSzCurQueueGroupD());
		}else{
			mTvCallD.setText("");
			mTvCallDSub.setText("");
		}
		
		mLvQueueA.setAdapter(new TableQueueAdapter(QueueApplication.sContext, mQueueALst));
		mLvQueueB.setAdapter(new TableQueueAdapter(QueueApplication.sContext, mQueueBLst));
		mLvQueueC.setAdapter(new TableQueueAdapter(QueueApplication.sContext, mQueueCLst));
		mLvQueueD.setAdapter(new TableQueueAdapter(QueueApplication.sContext, mQueueDLst));
		mTvSumQA.setText(String.valueOf(totalQueueA));
		mTvSumQB.setText(String.valueOf(totalQueueB));
		mTvSumQC.setText(String.valueOf(totalQueueC));
		mTvSumQD.setText(String.valueOf(totalQueueD));
		
		mNotifyCalling.removeCallbacks(mRunnableNotifyCalling);
		mNotifyCalling.post(mRunnableNotifyCalling);
	}
}
