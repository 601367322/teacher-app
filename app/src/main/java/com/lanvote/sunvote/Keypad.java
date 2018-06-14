package com.lanvote.sunvote;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tengyue.teacher.R;

import cn.sunars.sdk.SunARS;

public class Keypad extends Fragment {
	
	private EditText edtKeypadID;
	private Button btnReadKeyId;
	private Button btnWriteKeyId;
	private Button btnStartMatch;
	private Button btnStopMatch;
	
	private Button btnTurnOffAll;
	
	private EditText edtAuthKeyID;
	private Spinner spAuthMode;
	private Button btnAuthrize;
	
	private Button btnKeypadTest;
	private Button btnStoptest;
	private Button btnKeypadClear;
	
	private ScrollView scvKeypad;
	private TextView logKeypad;
	private View contentView;
	private Timer timer ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.keypad, container, false);
		edtKeypadID = (EditText)view.findViewById(R.id.edtKeypadID);
		
		btnReadKeyId =(Button)view.findViewById(R.id.btnReadKeyId);
		btnReadKeyId.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 readParam(0, SunARS.KeyPad_ID);;
			}
		});
		
		btnWriteKeyId = (Button)view.findViewById(R.id.btnWriteKeyId);
		btnWriteKeyId.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				writeParam(0, SunARS.KeyPad_ID, edtKeypadID.getText().toString());
			}
		});
		
		
		btnStartMatch = (Button)view.findViewById(R.id.btnStartMatch);
		btnStartMatch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				voteStart(SunARS.VoteType_KeyPadMatch,"1,0");
			}
		});
		
		btnStopMatch =(Button)view.findViewById(R.id.btnStopMatch);
		btnStopMatch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				voteStop();
			}
		});
		
		btnTurnOffAll = (Button)view.findViewById(R.id.btnTurnOffAll);
		btnTurnOffAll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 writeParam(0, SunARS.KeyPad_PowerOff, "");
				
			}
		});
		
		edtAuthKeyID =(EditText)view.findViewById(R.id.edtAuthKeyID);
		
		spAuthMode = (Spinner)view.findViewById(R.id.spAuthMode);
		spAuthMode.setSelection(1);
		
		btnAuthrize =(Button)view.findViewById(R.id.btnAuthrize);
		btnAuthrize.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int iAuthType = spAuthMode.getSelectedItemPosition()+1;
				 String strParam = String.format("%s,%d", edtAuthKeyID.getText().toString(),iAuthType);
				writeParam(0, SunARS.Keypad_AuthorizeByID, strParam);
			}
			
		});
		
		btnKeypadTest = (Button)view.findViewById(R.id.btnKeypadTest);
		btnKeypadTest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				voteStart(SunARS.VoteType_KeyPadTest, "2,0,1 ");
				
			}
		});
		
		btnStoptest =  (Button)view.findViewById(R.id.btnStoptest);
		btnStoptest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				voteStop();
			}
		});
		
		contentView = view;
		scvKeypad = (ScrollView)view.findViewById(R.id.scvKeypad);
		logKeypad = (TextView)view.findViewById(R.id.logKeypad);
		
		btnKeypadClear =  (Button)view.findViewById(R.id.btnKeypadClear);
		btnKeypadClear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.strLogInfo = "";
				logKeypad.setText("");
			}
		});
		timer  = new Timer(true);
	    timer.schedule(displayTask,50, 300); //延时500ms后执行，300ms执行一次
		return view;
	}
	
	
	public void voteStart(int iMode,String sInfo){
		
		MainActivity.addLog("VoteStart(" + SunARS.getVoteModeName(iMode) +",\""+sInfo+"\")" );
		SunARS.voteStart(iMode, sInfo);
	}
	
	public void voteStop(){
		MainActivity.addLog("VoteStop()");
		SunARS.voteStop();
	}
	
	public void readParam(int baseId,int iMode){
		MainActivity.addLog("ReadHDParam("+baseId +"," + SunARS.getModeName(iMode) +")");
		SunARS.readHDParam(baseId, iMode);
	}
 
	public void writeParam(int baseId,int iMode,String sInfo){
		SunARS.writeHDParam(baseId, iMode, sInfo);
		MainActivity.addLog("writeHDParam("+baseId+","+ SunARS.getModeName(iMode)+",\""+ sInfo+ "\")");
	}
	

	 
	private String tmpString ="-";
	final Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Log.i("Connect", "onTimer");
				logKeypad.setText(MainActivity.strLogInfo);
				if(!tmpString.equals(MainActivity.strLogInfo)){
					tmpString = MainActivity.strLogInfo;
					scrollToBottom();
				}
			break;
		}
		}
	};
	
	TimerTask displayTask = new TimerTask(){  
	      public void run() {  
	    	  if(!Keypad.this.isVisible()){
	    		  return;
	    	  }
	    	  Message message = new Message();      
	    	  message.what = 1;      
	    	  handler.sendMessage(message);    
	   }  
	};
	 

	private void scrollToBottom()
	{

	    scvKeypad.post(new Runnable()
	    { 
	        public void run()
	        { 
	        	scvKeypad.smoothScrollTo(0, logKeypad.getBottom());
	        	contentView.refreshDrawableState();
	        	
	        } 
	    });
	}
	
	
	
	public void onConnectEventCallBack(final int iBaseID, final int iMode, final String sInfo) {
		 
		//appendLog("onConnectEventCallBack>>" + iBaseID + " " + SunARS.getConnectTypeName(iMode) + " " + sInfo);

	}
	
	public void onHDParamCallBack(int iBaseID, final int iMode, final String sInfo) {
		//appendLog("onHDParamCallBack>>" + iBaseID + " " +SunARS.getModeName(iMode)  + " " + sInfo);
		if(iMode==SunARS.KeyPad_ID){
			edtKeypadID.post(new Runnable() {
				public void run() {
					edtKeypadID.setText(sInfo);
				}
			});
		}
	}
	
	public void onVoteEventCallBack(final int iBaseID, final int iMode, final String sInfo)  {
		//appendLog("onVoteEventCallBack>>>" + iBaseID + " " +SunARS.getVoteModeName(iMode)  + " " + sInfo);
		 
	}

	public void onKeyEventCallBack(final String KeyID, final int iMode, final float Time, final String sInfo){
		//appendLog("onKeyEventCallBack>>" + KeyID + " " + SunARS.getKeyEventTypeName(iMode) + " " + Time + " " + sInfo);
		
		 
	}
}
