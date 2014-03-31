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

public class Queue3ColumnFragment extends Fragment{
	protected QueueDatabase mQueueDatabase;
	protected List<QueueDisplayInfo.QueueInfo> mQueueALst;
	protected List<QueueDisplayInfo.QueueInfo> mQueueBLst;
	protected List<QueueDisplayInfo.QueueInfo> mQueueCLst;
	
	protected Handler mNotifyCalling;
	protected ListView mLvQueueA;
	protected ListView mLvQueueB;
	protected ListView mLvQueueC;
	protected TextView mTvCallA;
	protected TextView mTvCallB;
	protected TextView mTvCallC;
	protected TextView mTvCallASub;
	protected TextView mTvCallBSub;
	protected TextView mTvCallCSub;
	protected TextView mTvSumQA;
	protected TextView mTvSumQB;
	protected TextView mTvSumQC;
	
	public static Queue3ColumnFragment newInstance(){
		Queue3ColumnFragment f = new Queue3ColumnFragment();
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNotifyCalling = new Handler();
		mQueueDatabase = new QueueDatabase(QueueApplication.getWritableDatabase());
		mQueueALst = new ArrayList<QueueDisplayInfo.QueueInfo>();
		mQueueBLst = new ArrayList<QueueDisplayInfo.QueueInfo>();
		mQueueCLst = new ArrayList<QueueDisplayInfo.QueueInfo>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.queue3columns_fragment, container, false);

		mLvQueueA = (ListView) v.findViewById(R.id.lvQueueA);
		mLvQueueB = (ListView) v.findViewById(R.id.lvQueueB);
		mLvQueueC = (ListView) v.findViewById(R.id.lvQueueC);
		mTvCallA = (TextView) v.findViewById(R.id.tvCallA);
		mTvCallB = (TextView) v.findViewById(R.id.tvCallB);
		mTvCallC = (TextView) v.findViewById(R.id.tvCallC);
		mTvCallASub = (TextView) v.findViewById(R.id.tvCallASub);
		mTvCallBSub = (TextView) v.findViewById(R.id.tvCallBSub);
		mTvCallCSub = (TextView) v.findViewById(R.id.tvCallCSub);
		mTvSumQB = (TextView) v.findViewById(R.id.tvSumQB);
		mTvSumQA = (TextView) v.findViewById(R.id.tvSumQA);
		mTvSumQC = (TextView) v.findViewById(R.id.tvSumQC);
		
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

			mNotifyCalling.postDelayed(this, 1000);
		}
	};
	
	public void setQueueData(QueueDisplayInfo queueDisplayInfo){
		int totalQueueA = 0;
		int totalQueueB = 0;
		int totalQueueC = 0;
		mQueueALst.clear();
		mQueueBLst.clear();
		mQueueCLst.clear();
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
		
		mLvQueueA.setAdapter(new TableQueueAdapter(QueueApplication.sContext, mQueueALst));
		mLvQueueB.setAdapter(new TableQueueAdapter(QueueApplication.sContext, mQueueBLst));
		mLvQueueC.setAdapter(new TableQueueAdapter(QueueApplication.sContext, mQueueCLst));
		mTvSumQA.setText(String.valueOf(totalQueueA));
		mTvSumQB.setText(String.valueOf(totalQueueB));
		mTvSumQC.setText(String.valueOf(totalQueueC));
		
		mNotifyCalling.removeCallbacks(mRunnableNotifyCalling);
		mNotifyCalling.post(mRunnableNotifyCalling);
	}
}
