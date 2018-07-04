package com.prance.teacher.lib.sunvote.features;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prance.teacher.lib.sunvote.R;
import com.prance.teacher.lib.sunvote.utils.DataUtil;
import com.prance.teacher.lib.sunvote.utils.LogUtil;
import cn.sunars.sdk.SunARS;


public class MainActivity extends AppCompatActivity implements OnClickListener {
	private final static String TAG = MainActivity.class.getSimpleName();
	
	public static ProgressDialog progressDialog;
	
	private FragmentManager fManager;

	public static String strLogInfo = "";
	public static int connectType;

	private static Connect fConnect;
	private static Application fApplication;
	private static BaseManger fBaseManger;
	private static Keypad fKeypad;

	private FrameLayout flayout;// 帧布局对象,就是用来存放Fragment的容器

	private static NavigationView navigationView;

	private RelativeLayout connect_layout;
	private RelativeLayout application_layout;
	private RelativeLayout base_layout;
	private RelativeLayout keypad_layout;

	private ImageView imgConnect;
	private ImageView imgApplication;
	private ImageView imgBase;
	private ImageView imgKeypad;

	private TextView txtConnect;
	private TextView txtApplication;
	private TextView txtBase;
	private TextView txtKeypad;

	private int white = 0xFFFFFFFF;
	private int gray = 0xFF7597B3;
	private int blue = 0xFF0AB2FB;

	private byte[] SerDataRx = new byte[512];
	int iSerRxN = 0;

	private TextView connLog;

	private final int MSG_BLE_CONNECTED = 1;
	private final int MSG_USB_CONNECTED = 2;

	private static UsbManager mUsbManager;
	private static UsbDevice mUsbDevice;
	private static UsbDeviceConnection mUsbConnection;
	private static InputManager inputManager;
	private static UsbEndpoint epOut, epIn;
	private static UsbInterface mUsbInterface;

	private static final int VendorID = 0x03eb;
	private static final int ProductID = 0x6201;

	private static final int VendorID_2 = 0x0d8c;
	private static final int ProductID_2 = 0xEA10;

	private static final int VendorID_3 = 0x2F70;
	private static final int ProductID_3 = 0xEA10;

	private final String ACTION_USB_PERMISSION = "com.hhd.USB_PERMISSION";
	private UsbPermissionReceiver usbPermissionReceiver;

	private static SunARS.SunARSListener al = new SunARS.SunARSListener() {

		@Override
		public void onConnectEventCallBack(final int iBaseID, final int iMode, final String sInfo) {
			System.out.println("onConnectEventCallBack>>" + iBaseID + " " + iMode + " " + sInfo);
			addLog("onConnectEventCallBack>>" + iBaseID + " " + iMode + " " + sInfo);
			
			if (fConnect != null && fConnect.isVisible()) {
				fConnect.onConnectEventCallBack(iBaseID, iMode, sInfo);
			} else if (fBaseManger != null && fBaseManger.isVisible()) {
				fBaseManger.onConnectEventCallBack(iBaseID, iMode, sInfo);
			} else if (fKeypad != null && fKeypad.isVisible()) {
				fKeypad.onConnectEventCallBack(iBaseID, iMode, sInfo);
			} else if (fApplication != null && fApplication.isVisible()) {
				fApplication.onConnectEventCallBack(iBaseID, iMode, sInfo);
			}

			
			SunARS.isConnected = sInfo.equals("1");
			if (sInfo.equals("1")) {
				// SunARS.readHDParam(0, SunARS.KeyPad_IdentificationMode);
				navigationView.post(new Runnable() {
					public void run() {
						//setNavRightTitle("");
					}
				});
			} else {
				if (iMode == 5) {
					HashMap<String, UsbDevice> map = mUsbManager.getDeviceList();
					addLog("devicelist cont:" + map.size());
					if (map.size() == 0) {
						addLog("oncennect event: closeUsb");
						closeUsb();
					}
					

				}
			}

			
		}

		@Override
		public void onHDParamCallBack(int iBaseID, final int iMode, final String sInfo) {
			System.out.println("onHDParamCallBack>>" + iBaseID + " " + iMode + " " + sInfo);
			addLog("onHDParamCallBack>>" + iBaseID + " " + iMode + " " + sInfo);
			if (iMode == SunARS.KeyPad_IdentificationMode) {
				// SunARS.isSnMode = sInfo.equals("1");
				// navigationView.post(new Runnable() {
				// public void run() {
				// setNavRightTitle( SunARS.isSnMode?"SN mode":"ID mode");
				// }
				// });

			}

			if (fConnect != null && fConnect.isVisible()) {
				fConnect.onHDParamCallBack(iBaseID, iMode, sInfo);
			} else if (fBaseManger != null && fBaseManger.isVisible()) {
				fBaseManger.onHDParamCallBack(iBaseID, iMode, sInfo);
			} else if (fKeypad != null && fKeypad.isVisible()) {
				fKeypad.onHDParamCallBack(iBaseID, iMode, sInfo);
			} else if (fApplication != null && fApplication.isVisible()) {
				fApplication.onHDParamCallBack(iBaseID, iMode, sInfo);
			}
		}
		
		@Override
		public void onHDParamBySnCallBack(String KeySn, int iMode, String sInfo) {
			addLog("onHDParamBySnCallBack>>" + KeySn + " " + iMode + " " + sInfo);
			
		}

		@Override
		public void onVoteEventCallBack(final int iBaseID, final int iMode, final String sInfo) {
			System.out.println("onVoteEventCallBack>>>" + iBaseID + " " + iMode + " " + sInfo);
			addLog("onVoteEventCallBack>>>" + iBaseID + " " + SunARS.getVoteModeName(iMode) + " " + sInfo);
			if (fConnect != null && fConnect.isVisible()) {
				fConnect.onVoteEventCallBack(iBaseID, iMode, sInfo);
			} else if (fBaseManger != null && fBaseManger.isVisible()) {
				fBaseManger.onVoteEventCallBack(iBaseID, iMode, sInfo);
			} else if (fKeypad != null && fKeypad.isVisible()) {
				fKeypad.onVoteEventCallBack(iBaseID, iMode, sInfo);
			} else if (fApplication != null && fApplication.isVisible()) {
				fApplication.onVoteEventCallBack(iBaseID, iMode, sInfo);
			}
		}

		@Override
		public void onKeyEventCallBack(final String KeyID, final int iMode, final float Time, final String sInfo) {
			System.out.println("onKeyEventCallBack>>" + KeyID + " " + iMode + " " + Time + " " + sInfo);
			addLog("onKeyEventCallBack>>" + KeyID + " " + SunARS.getKeyEventTypeName(iMode) + " " + Time + " " + sInfo);
			if (fConnect != null && fConnect.isVisible()) {
				fConnect.onKeyEventCallBack(KeyID, iMode, Time, sInfo);
			} else if (fBaseManger != null && fBaseManger.isVisible()) {
				fBaseManger.onKeyEventCallBack(KeyID, iMode, Time, sInfo);
			} else if (fKeypad != null && fKeypad.isVisible()) {
				fKeypad.onKeyEventCallBack(KeyID, iMode, Time, sInfo);
			} else if (fApplication != null && fApplication.isVisible()) {
				fApplication.onKeyEventCallBack(KeyID, iMode, Time, sInfo);
			}
		}

		@Override
		public void onStaEventCallBack(final String sInfo) {
			System.out.println(sInfo);

		}

		@Override
		public void onLogEventCallBack(String sInfo) {
			System.out.println("SDK Log:" + sInfo);
		}

		@Override
		public void onDataTxEventCallBack(byte[] sendData, int dataLen) {
			if (connectType == 5) {
				//addLog("onDataTxEventCallBack");
				if (mUsbDevice != null && mUsbManager.hasPermission(mUsbDevice)) {
					//addLog("sendToUsb");
					sendDataBulkTransfer(sendData);
				}
			}
		}

		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		navigationView = (NavigationView) super.findViewById(R.id.navMain);
		try {
			navigationView.SetRightViewText(getVersionName(this));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		fManager = getSupportFragmentManager();
		initViews();
		setChioceItem(0);

		readThread = new ReadThread();

		readThread.start();

		// MainActivity.this.startService(new
		// Intent(MainActivity.this,SunARS.class));
		// MainActivity.this.bindService(new
		// Intent(MainActivity.this,SunARS.class),
		// MainActivity.this.serviceConnection, Context.BIND_AUTO_CREATE);

		registerUsbBroadcastRecevier();
		// startBleService();

		try {

			String processName = null;
			int pid = android.os.Process.myPid();
			ActivityManager mActivityManager = (ActivityManager) this.getSystemService("activity");
			for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
				if (appProcess.pid == pid) {
					processName = appProcess.processName;
					break;
				}
			}
			Log.e("tag", String.format("当前进程名为：%s进程pid为：%s", processName, pid + ""));

			SunARS.setListener(al);
			int r = SunARS.license(1, "SUNARS2013");
			SunARS.setLogOn(0);
			File fpath = this.getApplicationContext().getFilesDir();
			SunARS.setArchiveDir(fpath.toString());
			System.out.println("license:" + r);

		} catch (Throwable e) {
			System.out.println(e.getMessage());
			System.out.println("loadLibrary Error");

		}
	}

	private void initViews() {
		imgConnect = (ImageView) findViewById(R.id.img_connect);
		txtConnect = (TextView) findViewById(R.id.txt_connect);

		imgApplication = (ImageView) findViewById(R.id.img_application);
		txtApplication = (TextView) findViewById(R.id.txt_application);

		imgBase = (ImageView) findViewById(R.id.img_base);
		txtBase = (TextView) findViewById(R.id.txt_base);

		imgKeypad = (ImageView) findViewById(R.id.img_keypad);
		txtKeypad = (TextView) findViewById(R.id.txt_keypad);

		connect_layout = (RelativeLayout) findViewById(R.id.connect_layout);
		application_layout = (RelativeLayout) findViewById(R.id.application_layout);
		base_layout = (RelativeLayout) findViewById(R.id.base_layout);
		keypad_layout = (RelativeLayout) findViewById(R.id.keypad_layout);

		connect_layout.setOnClickListener(this);
		application_layout.setOnClickListener(this);
		base_layout.setOnClickListener(this);
		keypad_layout.setOnClickListener(this);

	}

	public static void setNavRightTitle(String text) {
		navigationView.SetRightViewText(text);
	}
	
	public static String getVersionName(Context context) throws Exception {
		String version = "";
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
		version = "V"+packInfo.versionName + "." + packInfo.versionCode;
		return version;
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(usbReceiver);
		super.onDestroy();

	}

	// 重写onClick事件
	@Override
	public void onClick(View view) {
		int i = view.getId();
		if (i == R.id.connect_layout) {
			setChioceItem(0);

		} else if (i == R.id.application_layout) {
			setChioceItem(1);

		} else if (i == R.id.base_layout) {
			setChioceItem(2);

		} else if (i == R.id.keypad_layout) {
			setChioceItem(3);

		} else {// fConnect.appendLog("Connect");


		}

	}

	// 定义一个选中一个item后的处理
	public void setChioceItem(int index) {
		FragmentTransaction transaction = fManager.beginTransaction();
		clearChioce();
		hideFragments(transaction);

		switch (index) {
		case 0:
			// imgConnect.setImageResource(R.drawable.ic_tabbar_course_pressed);
			txtConnect.setTextColor(blue);
			connect_layout.setBackgroundResource(R.drawable.ic_tabbar_bg_click);
			if (fConnect == null) {
				// 如果fg1为空，则创建一个并添加到界面上
				fConnect = new Connect();

				transaction.add(R.id.content, fConnect);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(fConnect);
			}
			break;
		case 1:
			txtApplication.setTextColor(blue);
			application_layout.setBackgroundResource(R.drawable.ic_tabbar_bg_click);
			if (fApplication == null) {
				fApplication = new Application();
				transaction.add(R.id.content, fApplication);
			} else {
				transaction.show(fApplication);
			}
			break;

		case 2:
			txtBase.setTextColor(blue);
			base_layout.setBackgroundResource(R.drawable.ic_tabbar_bg_click);
			if (fBaseManger == null) {
				fBaseManger = new BaseManger();
				transaction.add(R.id.content, fBaseManger);
			} else {
				transaction.show(fBaseManger);
			}
			// if(SunARS.isConnected){
			// fBaseManger.readBaseConfig();
			// fBaseManger.readWifiConfig();
			// }
			break;

		case 3:
			txtKeypad.setTextColor(blue);
			keypad_layout.setBackgroundResource(R.drawable.ic_tabbar_bg_click);
			if (fKeypad == null) {
				fKeypad = new Keypad();
				transaction.add(R.id.content, fKeypad);
			} else {
				transaction.show(fKeypad);
			}
			break;
		}
		transaction.commit();

	}

	private void hideFragments(FragmentTransaction transaction) {
		if (fConnect != null) {
			transaction.hide(fConnect);
		}
		if (fApplication != null) {
			transaction.hide(fApplication);
		}
		if (fBaseManger != null) {
			transaction.hide(fBaseManger);
		}

		if (fKeypad != null) {
			transaction.hide(fKeypad);
		}
	}

	public void clearChioce() {
		connect_layout.setBackgroundColor(white);
		txtConnect.setTextColor(gray);

		application_layout.setBackgroundColor(white);
		txtApplication.setTextColor(gray);

		base_layout.setBackgroundColor(white);
		txtBase.setTextColor(gray);

		keypad_layout.setBackgroundColor(white);
		txtKeypad.setTextColor(gray);
	}

	public static void addLog(String log) {
		if (MainActivity.strLogInfo.length() >= 4000) {
			strLogInfo = "";
		}
		if (strLogInfo.length() == 0) {
			strLogInfo = log;
			System.out.println("set text");
		} else {
			strLogInfo = strLogInfo + "\n" + log;

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.out.println("on KEYCODE_BACK");
			this.exitDialog();
		}
		return false;
	}

	private void exitDialog() {
		// Dialog dialog = new AlertDialog.Builder(this);
		AlertDialog dialog;
		dialog = new AlertDialog.Builder(MainActivity.this).setTitle("退出").setMessage("是否退出程序")
				.setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						MainActivity.this.stopService(new Intent(MainActivity.this, SunARS.class));
						MainActivity.this.finish();
					}
				})

				.create();
		dialog.show();

	}

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_BLE_CONNECTED:
				System.out.println("handleMessage:ble connect");
				connectType = 3;
				SunARS.connect(3, "BLE");
				break;
			case MSG_USB_CONNECTED:
				System.out.println("handleMessage:usb connect");
				addLog("connect(5,usb)");
				SunARS.connect(5, "usb");
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void processRecvData(byte[] recvBuf) {
		int recvLen = recvBuf.length;

		int rxDataLen = 0;
		for (int i = 0; i < recvLen; i++) {
			int dd = DataUtil.getUnsignedByte(recvBuf[i]);
			SerDataRx[iSerRxN] = recvBuf[i];// 先保存数据
			switch (iSerRxN) {
			case 0:
				if (dd == 0xF5) {
					iSerRxN++;
				}
				break;
			case 1:
				if (dd == 0xAA)
					iSerRxN++;
				else {
					if (dd == 0xF5)
						iSerRxN = 1;
					else
						iSerRxN = 0;
				}
				break;
			case 2:
				if (dd == 0xAA)
					iSerRxN++;
				else {
					if (dd == 0xF5)
						iSerRxN = 1;
					else
						iSerRxN = 0;
				}
				break;
			case 3:// len
				if (dd > 128) // C_SERMAXN
					iSerRxN = 0;
				else {
					iSerRxN++;
				}
				break;
			default:
				iSerRxN++;
				rxDataLen = SerDataRx[3] + 4;// len
				if (rxDataLen <= 3)
					break;
				// System.out.println(String.format("iSerRxN:%d",iSerRxN));
				// printDataBuf(SerDataRx,"readed");
				if (iSerRxN == rxDataLen) {
					// 数据接收完整

					if (DataUtil.checkPack(SerDataRx)) {

						//DataUtil.printDataBuf(SerDataRx, "recv");
						// System.out.println("rxDataLen:"+rxDataLen);
						int crcValue = DataUtil.getUnsignedShort(DataUtil.Crc16(SerDataRx, rxDataLen - 4 - 2));
						int originCrcValue = 0;
						originCrcValue |= (SerDataRx[rxDataLen - 2] & 0xff) << 8;
						originCrcValue |= SerDataRx[rxDataLen - 1] & 0xff;

						if (crcValue != originCrcValue) {
//							System.out.println("recv package crc error!\n");
							// System.out.println("keyData:", SerDataRx
							// , pack.len);
							// for(int k=3;k<8;k++){
							// SerDataRx[k] = 0;
							// }
							// addLog("CRC error");
						}
						// addLog("getData:"+ rxDataLen);

						SunARS.DataRx(SerDataRx, rxDataLen);

						// 收到数据包

						for (int j = 0; j < SerDataRx.length; j++) {
							SerDataRx[j] = 0;
						}
						iSerRxN = 0;
					}

				}
				break;
			}// switch
		} // for

	}

	/* USB begin *****************************************************/

	private void registerUsbBroadcastRecevier() {
		IntentFilter filter = new IntentFilter();
		// filter.addAction(ACTION_USB_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		registerReceiver(usbReceiver, filter);
	}

	private long lastOpenTime;

	private class UsbPermissionReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					if (device.getDeviceName().equals(mUsbDevice.getDeviceName())) {
						if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
							// 授权成功,在这里进行打开设备操作
							addLog("授权成功");
							if (System.currentTimeMillis() - lastOpenTime > 1000) {
								lastOpenTime = System.currentTimeMillis();
								openUsbDevice();
							}
						} else {
							// 授权失败
							Toast.makeText(MainActivity.this, "授权失败!", Toast.LENGTH_LONG).show();
						}
					}
				}
			}
		}
	}

	// 设备连接或移除的广播
	BroadcastReceiver usbReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action))/* 连接 */ {
				UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE); // 拿到连接的设备
				inputManager = (InputManager) MainActivity.this.getSystemService(Context.INPUT_SERVICE);
				int[] id_device = inputManager.getInputDeviceIds();// 获取所有的设备id
				InputDevice inputDevice = inputManager.getInputDevice(id_device[id_device.length - 1]);
				System.out.println("找到设备：" + inputDevice);
				addLog("插入设备");
				if (SunARS.checkBaseConnection() == 0) {
					if (fConnect != null) {
						connectType = 5;
						fConnect.setUsbCheck();
					}
					checkUsbDevice();
				}
				// if (device.getVendorId() == VendorID && device.getProductId()
				// == ProductID) {

				// close();
				// setDevice(device);
				// device_null = false;
				// }
			} else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action))/* 移除 */ {
				addLog("移除设备");

				/* 关闭连接 */
				// closeUsb();
			}
		}
	};

	public void checkUsbDevice() {
		if (mUsbManager == null) {
			mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
		}
		HashMap<String, UsbDevice> map = mUsbManager.getDeviceList();
		addLog("devicelist cont:" + map.size());
		for (UsbDevice device : map.values()) {
			Log.d("checkDevice", "找到基站: Vid:" + device.getVendorId() + "  Pid:" + device.getProductId());
			LogUtil.d(TAG, "找到基站");
			addLog(String.format("找到基站: Vid:%x  Pid:%x", device.getVendorId(), device.getProductId()));

			if (device.getVendorId() == VendorID && device.getProductId() == ProductID
					|| device.getVendorId() == VendorID_2 && device.getProductId() == ProductID_2
					|| device.getVendorId() == VendorID_3 && device.getProductId() == ProductID_3) {
				System.out.println("找到基站");
				addLog("找到基站");
				mUsbDevice = device;

				if (!mUsbManager.hasPermission(device)) {
					if (usbPermissionReceiver == null) {
						usbPermissionReceiver = new UsbPermissionReceiver();
					}
					// 申请权限
					Intent intent = new Intent(ACTION_USB_PERMISSION);
					PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
					IntentFilter permissionFilter = new IntentFilter(ACTION_USB_PERMISSION);
					registerReceiver(usbPermissionReceiver, permissionFilter);
					mUsbManager.requestPermission(device, mPermissionIntent);
				} else {
					openUsbDevice();
				}
			}
		}
	}

	/**
	 * 打开连接
	 *
	 * @paramdevice
	 */
	private boolean openUsbDevice() {

		if (mUsbDevice == null)
			return false;

		if(mUsbDevice.getInterfaceCount() == 0){
			return false;
		}
		mUsbInterface = mUsbDevice.getInterface(0);
		setEndpoint(mUsbInterface);
		// closeDevice();
		mUsbConnection = mUsbManager.openDevice(mUsbDevice);
		System.out.println("打开连接：" + mUsbConnection);
		addLog("打开连接");
		if (mUsbConnection != null) {

			boolean ret = mUsbConnection.claimInterface(mUsbInterface, true);
			addLog("打开设备");

			new Thread() {
				@Override
				public void run() {
					// mBluetoothAdapter.startLeScan(mLeScanCallback);

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					Message message = new Message();
					message.what = MSG_USB_CONNECTED;

					MainActivity.this.myHandler.sendMessage(message);
				}
			}.start();

			return ret;
		}

		return false;
	}

	/**
	 * UsbInterface 进行端点设置和通讯
	 *
	 * @param intf
	 */
	private void setEndpoint(UsbInterface intf) {
		if (intf == null)
			return;
		// 设置接收数据的端点
		if (intf.getEndpoint(0) != null) {
			epIn = intf.getEndpoint(0);
		}
		// 当端点为2的时候
		if (intf.getEndpointCount() == 2) {
			// 设置发送数据的断点
			if (intf.getEndpoint(1) != null)
				epOut = intf.getEndpoint(1);
		}
	}

	/*-----------------------------------------------------------------------*/
	private ReadThread readThread;

	/**
	 * 开启一个线程来处理数据，或对权限的判断
	 *
	 * @author FLT-PC
	 */
	private class ReadThread extends Thread {
		@Override
		public void run() {
			super.run();
			while (!isInterrupted()) {
				final byte[] buffer = recvUsbData(); // receiveUsbRequestData();
				if (buffer != null) {
					onDataReceived(buffer);
				}
				try {
					sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 关闭连接，情况部分对象
	 */
	// private static void closeUsbDevice() {
	// if (mUsbConnection != null) {
	// synchronized (mUsbConnection) {
	// if (mUsbConnection != null && mUsbInterface != null) {
	// mUsbConnection.releaseInterface(mUsbInterface);
	// mUsbConnection.close();
	// mUsbConnection = null;
	// epOut = null;
	// epIn = null;
	// }
	// }
	// }
	// }

	private static void closeUsbDevice() {
		new Thread() {
			@Override
			public void run() {
				// mBluetoothAdapter.startLeScan(mLeScanCallback);

				try {
					Thread.sleep(1000);
					if (mUsbConnection != null && mUsbInterface != null) {
						addLog("closeUsbDevice()");
						mUsbConnection.releaseInterface(mUsbInterface);
						mUsbConnection.close();
						mUsbConnection = null;
						epOut = null;
						epIn = null;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}.start();
	}

	private static void closeUsb() {
		addLog("closeUsb()");
		if (mUsbConnection != null) {
			synchronized (mUsbConnection) {
				mUsbConnection.releaseInterface(mUsbInterface);
				mUsbConnection.close();
				mUsbConnection = null;
				epOut = null;
				epIn = null;
				mUsbInterface = null;
				mUsbDevice = null;
				addLog("closeUsb() ok");
			}
		}

	}

	/**
	 * 用UsbRequest 接收数据
	 *
	 * @return
	 */
	private ByteBuffer byteBuffer = ByteBuffer.allocate(64);

	private byte[] receiveUsbRequestData() {
		// 当对象为空时下面步骤将不执行
		if (mUsbConnection == null || epIn == null) {
			// addLog(" mUsbConnection or epIn is null");
			return null;
		}

		final UsbRequest usbRequest = new UsbRequest();
		usbRequest.initialize(mUsbConnection, epIn);
		usbRequest.queue(byteBuffer, 64);
		if (mUsbConnection.requestWait() == usbRequest) {
			System.out.println("拿到数据了");
			byte[] retData = byteBuffer.array();

			// for(Byte byte1 : retData){
			// System.err.println(byte1);
			// }
			usbRequest.close();

			return byteBuffer.array();
		}
		usbRequest.close();

		return null;
	}
	
	private byte [] recvBuffer = new byte[64];
	private byte [] recvUsbData(){
		if (mUsbConnection == null || epIn == null) {
			// addLog(" mUsbConnection or epIn is null");
			return null;
		}

		Arrays.fill(recvBuffer, (byte) 0x0);
		int ret = mUsbConnection.bulkTransfer(epIn, recvBuffer, 64, 3000);  
		Log.e("ret", "ret:"+ret);  
 
		return recvBuffer;
	}

	/**
	 * 接收到的数据进行处理,并且执行发送数据
	 *
	 * @param buffer
	 *            传过来的数据
	 */

	private void onDataReceived(final byte[] buffer) {
		runOnUiThread1(new Runnable() {
			@Override
			public void run() {
				processRecvData(buffer);

			}
		});
	}

	/*
	 * //检测到设备，并赋值 private void setDevice(UsbDevice device) { if (device ==
	 * null) return; if (mUsbDevice != null) mUsbDevice = null; mUsbDevice =
	 * device;
	 * //获取设备接口，一般都是一个接口，可以打印getInterfaceCount()查看接口个数，在这个接口上有两个端点，OUT和IN
	 * mUsbInterface = mUsbDevice.getInterface(0); setEndpoint(mUsbInterface); }
	 */
	private boolean b = false;

	/*--------------------------发送数据-----------------------------------*/
	// 该方法是为了，应对某种设备需要先发数据才可以接受
	/*
	 * private int sendDataBulkTransfer() { byte[] buffer = new byte[0x1f + 4];
	 * buffer[0] = (byte) 0xF5; buffer[1] = (byte) 0xAA; buffer[2] = (byte)
	 * 0xAA; buffer[3] = (byte) 0x1F;
	 * 
	 * buffer[4] = (byte) 0x60; buffer[5] = 0x0; buffer[6] = 0x01;
	 * 
	 * final int length = buffer.length; int ref = -100; if (epOut != null) {
	 * ref = mUsbConnection.bulkTransfer(epOut, buffer, length, 1000);
	 * mUsbConnection.claimInterface(mUsbInterface, true); } //
	 * Log.d("成功了没------>", "ref=" + ref); // addLog("发送数据:len" + ref +
	 * ",Data: " + getDataBufString(buffer)); return ref; }
	 */

	private static int sendDataBulkTransfer(byte[] buffer) {

		final int length = buffer.length;
		int ref = -100;
		if (epOut != null) {
			ref = mUsbConnection.bulkTransfer(epOut, buffer, length, 1000);
			mUsbConnection.claimInterface(mUsbInterface, true);
		}
		// Log.d("成功了没------>", "ref=" + ref);
		// addLog("发送数据:len" + ref + ",Data: " + getDataBufString(buffer));
		return ref;
	}

	private static String getDataBufString(byte[] buf) {
		String tmpStr = new String();
		for (int i = 0; i < buf.length; i++) {
			tmpStr += String.format("%x ", buf[i]);
		}
		return tmpStr;
	}

	private Thread mUiThread;
	private Handler handler = new Handler();

	public final void runOnUiThread1(Runnable runnable) {
		if (Thread.currentThread() != mUiThread) {
			handler.post(runnable);
		} else {
			runnable.run();
		}
	}

	/* USB end *****************************************************/

}
