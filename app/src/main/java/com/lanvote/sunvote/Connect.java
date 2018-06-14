package com.lanvote.sunvote;




import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tengyue.teacher.R;

import cn.sunars.sdk.RBLService;
import cn.sunars.sdk.SunARS; 


public class Connect extends Fragment {
	private static String TAG = "Connect";
	private TextView txtLog;
	private View contentView;
	private ScrollView scrView;
	private Button btnSearch;
	private Button btnConn; 
	private Button btnDisconn;
	private Button btnClear;
	private EditText edtIP;
	private CheckBox cbDemoMode;
	private EditText edDemoModeKeyIDs;
	
	private  RadioGroup rgConnType;
	
	private RadioButton rbConnTcp ;
	private RadioButton rbConnBle ;
	private RadioButton rbConnSerial ;
	private RadioButton rbConnUsb ;
	
	private MainActivity mainAct;
	private BluetoothAdapter mBluetoothAdapter;  
	private Timer timer ;
	
	private Dialog mDialog;
	public static List<BluetoothDevice> mDevices = new ArrayList<BluetoothDevice>();
	private static final long SCAN_PERIOD = 2000;
	

    private static final int REQUEST_CODE_LOCATION_SETTINGS = 2;
    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("Connect onCreateView");
		mainAct = (MainActivity) getActivity();
		View view = inflater.inflate(R.layout.connect, container,false);
		contentView = view;
		edtIP = (EditText) view.findViewById(R.id.edtIP);
		txtLog = (TextView) view.findViewById(R.id.txtConnLog);
		txtLog.setMovementMethod(new ScrollingMovementMethod());
		scrView = (ScrollView)view.findViewById(R.id.scvConn);
		
		  rgConnType = (RadioGroup) view.findViewById(R.id.rgConnType);
		 rbConnTcp = (RadioButton) view.findViewById(R.id.rbConnTcp);
		 rbConnBle = (RadioButton) view.findViewById(R.id.rbConnBle);
		  rbConnSerial = (RadioButton) view.findViewById(R.id.rbConnSerial);
		  rbConnUsb = (RadioButton) view.findViewById(R.id.rbConnUsb);
		  
		  rbConnBle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(TAG, "rbConnBle onclick");
				 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//如果 API level 是大于等于 23(Android 6.0) 时
			            //判断是否具有权限
			            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			                //判断是否需要向用户解释为什么需要申请该权限
			                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
			                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
			                    Toast.makeText(getActivity(), "自Android 6.0开始需要打开位置权限才可以搜索到Ble设备", Toast.LENGTH_SHORT).show();
			                }
			                //请求权限
			                ActivityCompat.requestPermissions(getActivity(),
			                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
			                        REQUEST_CODE_ACCESS_COARSE_LOCATION);
			            }
			        }
			}
		});
		  
		 

	        Boolean haha =isLocationEnable(getActivity());
	        if (haha){
	        	Log.i(TAG, "LocationEnabled...");
	        }else {
	            setLocationService();
	        }
	        
		
		btnSearch= (Button)view.findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(rgConnType.getCheckedRadioButtonId() == rbConnBle.getId()){
					MainActivity.connectType = 3;
					if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
					    Toast.makeText(getActivity(), "Ble not support", Toast.LENGTH_SHORT).show();
					}else{
						mainAct.startBleService();
						final BluetoothManager bluetoothManager =
					        (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
						 mBluetoothAdapter = bluetoothManager.getAdapter();
						 
						 mDevices.clear();
						 scanLeDevice();

							showRoundProcessDialog(getActivity(), R.layout.loading_process_dialog_anim);

							Timer mTimer = new Timer();
							mTimer.schedule(new TimerTask() {

								@Override
								public void run() {
									Device.mainAct =(MainActivity) getActivity();
									Intent deviceListIntent = new Intent(getActivity().getApplicationContext(),
											Device.class);
									startActivity(deviceListIntent);
									mDialog.dismiss();
								}
							}, SCAN_PERIOD);
					}
				}else if(rgConnType.getCheckedRadioButtonId() == rbConnUsb.getId()){
					MainActivity.connectType = 5;
					mainAct.checkUsbDevice();
				}
			}
		});
		
		btnConn = (Button)view.findViewById(R.id.btnConnect);
		btnConn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(rgConnType.getCheckedRadioButtonId() == rbConnTcp.getId()){
					MainActivity.connectType = 2;
					SunARS.connect(2, edtIP.getText().toString());
					MainActivity.addLog("connect");
				}else if(rgConnType.getCheckedRadioButtonId() == rbConnSerial.getId()){
					MainActivity.connectType = 4;
					String path = "/dev/ttyMT1";
					SunARS.connect(4, path);
				}else if(rgConnType.getCheckedRadioButtonId() == rbConnBle.getId()){
					MainActivity.connectType = 3;
					btnSearch.callOnClick();
				}else if(rgConnType.getCheckedRadioButtonId() == rbConnUsb.getId()){
					MainActivity.addLog("connect(5,usb)");
					MainActivity.connectType = 5;
					SunARS.connect(5, "usb");
				}
			}
		});
		
		btnDisconn = (Button)view.findViewById(R.id.btnDisconnect);
		btnDisconn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SunARS.disconnect(0);
				MainActivity.addLog("Disconnect");
				if(rgConnType.getCheckedRadioButtonId() == rbConnBle.getId()){
					mainAct.disconnectBleDevice();
				}
			}
		});
		
		edDemoModeKeyIDs = (EditText)view.findViewById(R.id.edDemoModeKeyIDs);
		
		
		cbDemoMode =(CheckBox)view.findViewById(R.id.cbDemoMode);
		cbDemoMode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(SunARS.isConnected){
					cbDemoMode.setChecked(false);
					return;
				}
				if(cbDemoMode.isChecked()){
			       
					SunARS.setDemoMode(1, edDemoModeKeyIDs.getText().toString());
			        MainActivity.addLog("SetDemoMode(1,\""+edDemoModeKeyIDs.getText().toString()+"\")");
			        setNavRightTitle("Demo mode");
			        
			    }else{
			       SunARS.setDemoMode(0, "");
			       MainActivity.addLog("SetDemoMode(0, \"\")");
			       setNavRightTitle("");
//			       if(SunARS.isConnected){
//			    	   setNavRightTitle(SunARS.isSnMode?"SN mode":"ID mode");
//			       }else{
//			    	   setNavRightTitle("");
//			       }
			       
			        
			    }

			}
		});
		
		btnClear = (Button)view.findViewById(R.id.btnClear);
		btnClear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.strLogInfo = "";
				txtLog.setText("");
				
			}
		});
		
		timer  = new Timer(true);
	    timer.schedule(displayTask,50, 300); //延时50ms后执行，300ms执行一次
		
		return view;
	}
	
	
//	private void scanLeDevice(final boolean enable) {
//        if (enable) {
//            // Stops scanning after a pre-defined scan period.
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mScanning = false;
//                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                }
//            }, SCAN_PERIOD);
//
//            mScanning = true;
//            mBluetoothAdapter.startLeScan(mLeScanCallback);
//        } else {
//            mScanning = false;
//            mBluetoothAdapter.stopLeScan(mLeScanCallback);
//        }
//   
//    }
//	
//	// Device scan callback.
//	private BluetoothAdapter.LeScanCallback mLeScanCallback =
//	        new BluetoothAdapter.LeScanCallback() {
//	    @Override
//	    public void onLeScan(final BluetoothDevice device, int rssi,
//	            byte[] scanRecord) {
//	        runOnUiThread(new Runnable() {
//	           @Override
//	           public void run() {
//	               mLeDeviceListAdapter.addDevice(device);
//	               mLeDeviceListAdapter.notifyDataSetChanged();
//	           }
//	       });
//	   }
//	};
	
	public void setUsbCheck(){
		rgConnType.check(rbConnUsb.getId());
	}
	
	
	public void setNavRightTitle(String text){
		 MainActivity  mainAct = (MainActivity )getActivity();
	     mainAct.setNavRightTitle(text);
	}

	
	 

	 
	@Override
	public void onDestroy() {
		if (timer != null) {
			timer.cancel( );
			timer = null;
		}
		System.out.println("Connect onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		System.out.println("Connect onDestroyView");
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		System.out.println("Connect onDetach");
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		System.out.println("Connect onHiddenChanged");
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onPause() {
		System.out.println("Connect onPause");
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		System.out.println("Connect onResume");
		super.onResume();
	}

	@Override
	public void onStart() {
		System.out.println("Connect onStart");
		super.onStart();
	}

	@Override
	public void onStop() {
		System.out.println("Connect onStop");
		super.onStop();
	}
	
	
	private String tmpString ="-";
	
	final Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				//Log.i("Connect", "onTimer");
				txtLog.setText(MainActivity.strLogInfo);
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
	    	  if(!Connect.this.isVisible()){
	    		  return;
	    	  }
	    	  Message message = new Message();      
	    	  message.what = 1;      
	    	  handler.sendMessage(message);    
	   }  
	};


	private void scrollToBottom()
	{
	    scrView.post(new Runnable()
	    { 
	        public void run()
	        { 
	        	scrView.smoothScrollTo(0, txtLog.getBottom());
	        	contentView.refreshDrawableState();
	        } 
	    });
	}
	
	
	public void onConnectEventCallBack(final int iBaseID, final int iMode, final String sInfo) {
		System.out.println("onConnectEventCallBack>>" + iBaseID + " " + iMode + " " + sInfo);
	//	appendLog("onConnectEventCallBack>>" + iBaseID + " " + SunARS.getConnectTypeName(iMode) + " " + sInfo);
		contentView.post(new Runnable() {
			public void run() {

				if(sInfo.equals("1")){
					 cbDemoMode.setChecked(false);
					 btnConn.setEnabled(false);
					 btnDisconn.setEnabled(true);
				}else{
					btnConn.setEnabled(true);
					btnDisconn.setEnabled(false);
				}
			}
		});
		
		
	}
	
	public void onHDParamCallBack(int iBaseID, final int iMode, final String sInfo) {
		System.out.println("onHDParamCallBack>>" + iBaseID + " " + iMode + " " + sInfo);
		//appendLog("onHDParamCallBack>>" + iBaseID + " " + SunARS.getModeName(iMode) + " " + sInfo);
 
	}
	
	public void onHDParamBySnCallBack(final String keySn, final int iMode, final String sInfo) {
		System.out.println("onHDParamBySnCallBack>>" + keySn + " " + iMode + " " + sInfo);
		//appendLog("onHDParamCallBack>>" + iBaseID + " " + SunARS.getModeName(iMode) + " " + sInfo);
 
	}
	
	public void onVoteEventCallBack(final int iBaseID, final int iMode, final String sInfo)  {
		//appendLog("onVoteEventCallBack>>>" + iBaseID + " " +SunARS.getVoteModeName(iMode)  + " " + sInfo);
		 
	}

	public void onKeyEventCallBack(final String KeyID, final int iMode, final float Time, final String sInfo){
		//appendLog("onKeyEventCallBack>>" + KeyID + " " + SunARS.getKeyEventTypeName(iMode) + " " + Time + " " + sInfo);
		
		 
	}
	
	public void showRoundProcessDialog(Context mContext, int layout) {
		OnKeyListener keyListener = new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_HOME
						|| keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				}
				return false;
			}
		};

		mDialog = new AlertDialog.Builder(mContext).create();
		mDialog.setOnKeyListener(keyListener);
		mDialog.show();
		// 濞夈劍锟斤拷濮濄倕锟斤拷鐟曪拷锟斤拷鎯э拷锟絪how娑旓拷锟斤拷锟� 锟斤拷锟斤拷锟斤拷娴硷拷锟斤拷銉ワ拷锟界敮锟�
		mDialog.setContentView(layout);
	}


    //判断定位
    public static final boolean isLocationEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean networkProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean gpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (networkProvider || gpsProvider) return true;
        return false;
    }

    private void setLocationService() {
        Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        this.startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
    }

    
	private void scanLeDevice() {
		new Thread() {

			@Override
			public void run() {
				mBluetoothAdapter.startLeScan(mLeScanCallback);

				try {
					Thread.sleep(SCAN_PERIOD);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				mBluetoothAdapter.stopLeScan(mLeScanCallback);
			}
		}.start();
	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi,
				byte[] scanRecord) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (device != null) {
						if (mDevices.indexOf(device) == -1)
							mDevices.add(device);
					}
				}
			});
		}
	};

	
}
