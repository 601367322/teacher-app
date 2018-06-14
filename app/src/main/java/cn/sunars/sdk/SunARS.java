package cn.sunars.sdk;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


/**
 * 
 * @author houj
 * @ID
 * @time 2015骞�11鏈�12鏃� 涓婂崍11:27:30
 */
public class SunARS extends Service{

	public final static int BaseStation_RFPower = 1; // 基站功率
	public final static int BaseStation_Channel = 2; // ; 基站主信道
	public final static int BaseStation_CommunicationChannels = 3; // 通讯通道：是否中继，通道数，通道1，通道2，通道3，通道4
	public final static int BaseStation_IP = 4; // IP
	public final static int BaseStation_MAC = 5; // mac地址
	public final static int BaseStation_SubnetMask = 6; // 子网掩码
	public final static int BaseStation_Gateway = 7; // 网关
	public final static int BaseStation_Title = 8; // 基站名称
	public final static int BaseStation_ID = 9; // 基站编号
	public final static int BaseStation_SN = 10; // 硬件序列号（ 只读）
	public final static int BaseStation_Version = 11; // 硬件版本
	//public final static int BaseStation_PairCode = 12; // 配对码
	public final static int BaseStation_MatchCode = 12; // 配对码
	
	public final static int SoftwareDongle_VerifyPWD = 13; // 验证密码
	public final static int SoftwareDongle_A_PWD = 14;// 软件狗密码
	public final static int SoftwareDongle_A_Zone = 15;// 软件狗读写A区
	public final static int SoftwareDongle_B_Zone = 16;

	public final static int KeyPad_Config = 17; // 键盘设置
	//public final static int KeyPad_ConnectionMode = 18; // 键盘连接模式（固定配对、自由配对、免配对）
	public final static int KeyPad_WorkingMode = 18;// 键盘连接模式（固定配对、自由配对、免配对）
	public final static int KeyPad_IdentificationMode = 19; // 键盘识别模式（是否SN号模式）

	public final static int KeyPad_ID = 20; // 键盘编号
	public final static int KeyPad_UserID = 21;// 用户编号
	public final static int KeyPad_SN = 22;// 键盘出厂序列号
	public final static int KeyPad_Version = 23;// 键盘版本信息
	public final static int KeyPad_MatchCode = 24;// 配对码//KeyPad_PairCode

	public final static int WiFi_SSID = 25;// 组合 baseMode;ssid
	public final static int  WiFi_WorkMode =26;
	public final static int WiFi_Password = 27;
	
	public final static int Background_SignIn = 28;
	public final static int	Keypad_AuthorizeByID = 29;
	public final static int	Keypad_AuthorizeBySN = 30;
	public final static int BaseStation_ChannelInterference = 31;
	public final static int BaseStation_AutoChangeChannel = 32;
	
	public final static int BaseStation_Model = 33;
	public final static int KeyPad_Model = 34;
	
	public final static int KeyPad_UserName = 35;					//用户姓名

	public final static int Keypad_MonitorEnable = 36; // 键盘定时监听投票信标
	public final static int Keypad_KeepPowerOn = 37; // 键盘不关机，用于测试和生产
	public final static int Keypad_SignalFrequence = 38; // 考勤信号每秒次数
	public final static int Keypad_SignalPower = 39; // 键盘发送考勤信号功率
	public final static int BaseStation_MatchPassowrd = 40; // 自由模式下基站的密码
	public final static int BaseStation_Time = 41; // 考勤基站时间

	public final static int BaseStation_AttendanceRepeatFilter = 42; // 基站去重配置

	public final static int SoftwareDongle_C_Zone = 43;
	public final static int SoftwareDongle_D_Zone = 44;
	public final static int BaseStation_UID = 45; // 基站唯一硬件编号UID
	public final static int BaseStation_OEM = 46; // 基站OEM硬件信息
	public final static int Keypad_OEM = 47;

	public final static int BaseStation_WhiteList = 48;
	public final static int BaseStation_KeyEventSNAndID = 49;//

	public final static int KeyPad_FirmUpdateMode = 50;//
	public final static int KeyPad_FirmUpdateProgress = 51;

	// VoteMode

	public final static int VoteType_Free = 0; // 空闲模式(停止)
	public final static int VoteType_Signin = 1; // 签到
	public final static int VoteType_Vote = 2; // 表决

	public final static int VoteType_Number = 4;// 数值 （单项评分模式）
	public final static int VoteType_TrueFalse = 5; // 5 判断
	public final static int VoteType_KeyPadTest = 9;// 模拟测试
	public final static int VoteType_Choice = 10;// 选择
	public final static int VoteType_Sequence = 11; // 排序
	public final static int VoteType_FillBlanks = 12; // 填空
	public final static int VoteType_Quiz = 13; // 抢答
	public final static int VoteType_Examination = 14;//测验
	public final static int VoteType_Homework = 15;//作业

	//public final static int VoteType_KeyPadPair = 40; // 配对
	public final static int VoteType_KeyPadMatch = 40; // 配对
	
	public final static int VoteType_Continue = 100;
	public final static int VoteType_SubmitAndContinue = 101;
	
	public final static int KeyPad_PowerOff = 222;
	 

	// 键盘状态
	public final static int KeyResult_info = 1;					//结果
	public final static int	KeyResult_status = 2;					//状态
	public final static int	KeyResult_loginInfo = 3;				//配对（登录）信息
	public final static int	KeyResult_remoteControlAnswer = 4;	//表决器遥控指令应答
	public final static int KeyResult_match = 5;	 
	public final static int KeyeResult_attendance = 6;	
	 
	static {
		//System.out.println("java platform:"+  System.getProperty("sun.arch.data.model")+ "bit");
		//if(System.getProperty("sun.arch.data.model").equals("32")){
		System.loadLibrary("stlport_shared");
		System.loadLibrary("SunVoteSDK");
		//System.loadLibrary("SunVoteSDK_x64");
		//}else{
//			System.loadLibrary("SunVoteSDK_x64");
		//}
		init();
 
	}

	private final static Charset UTF8 = Charset.forName("UTF8");
	private final static Charset GBK = Charset.forName("GBK");

	private final static byte[] stringToBytes(String s) {
		byte[] bs = s.getBytes(GBK);
		return Arrays.copyOf(bs, bs.length + 1);
	}

	private final static String bytesToString(byte[] bs) {
		return new String(bs, GBK);
	}

	public interface SunARSListener {
		public void onConnectEventCallBack(int iBaseID, int iMode, String sInfo);

		public void onHDParamCallBack(int iBaseID, int iMode, String sInfo);
		
		public void onHDParamBySnCallBack(String KeySn, int iMode, String sInfo);

		public void onVoteEventCallBack(int iBaseID, int iMode, String sInfo);

		public void onKeyEventCallBack(String KeyID, int iMode, float Time, String sInfo);
		
		public void onStaEventCallBack(String sInfo);
		
		public void onLogEventCallBack(String sInfo);
			
		public void onDataTxEventCallBack(byte[] sendData, int dataLen);

	}
	
	public static boolean isConnected;
	public static boolean isSnMode;

	private static String r_onHDParamCallBack;

	private static void onConnectEventCallBack(int iBaseID, int iMode, byte[] sInfo) {

		if (al != null) {
			al.onConnectEventCallBack(iBaseID, iMode, bytesToString(sInfo));
		}
	}

	private static void onHDParamCallBack(int iBaseID, int iMode, byte[] sInfo) {
		r_onHDParamCallBack = bytesToString(sInfo);
		if (al != null) {
			al.onHDParamCallBack(iBaseID, iMode, r_onHDParamCallBack);
		}
	}
	
	private static void onHDParamBySnCallBack(byte[] keySn, int iMode, byte[] sInfo) {
		r_onHDParamCallBack = bytesToString(sInfo);
		if (al != null) {
			al.onHDParamBySnCallBack(bytesToString(keySn), iMode, r_onHDParamCallBack);
		}
	}

	private static void onVoteEventCallBack(int iBaseID, int iMode, byte[] sInfo) {
		r_onHDParamCallBack = bytesToString(sInfo);
		if (al != null) {
			al.onVoteEventCallBack(iBaseID, iMode, r_onHDParamCallBack);
		}
	}
	
	private static void onStaEventCallBack(byte[] sInfo){
		if (al != null) {
			al.onStaEventCallBack(bytesToString(sInfo));
		}
	}

	private static void onKeyEventCallBack(byte[] KeyID, int iMode, float Time, byte[] sInfo) {
		if (al != null) {
			al.onKeyEventCallBack(bytesToString(KeyID), iMode, (float) Time, bytesToString(sInfo));
		}
	}
	
	private static void onLogEventCallBack(byte[] sInfo){
		if(al != null){
			al.onLogEventCallBack(bytesToString(sInfo));
		}
	}
	
	private static void onDataTxEventCallBack(byte[] sendData, int dataLen) {

		if (al != null) {
			al.onDataTxEventCallBack(sendData,dataLen);
		}
	}

	public static int connect(int mode, String params) {
		return connect0(mode, stringToBytes(params));
	}

	public static int disconnect(int baseID) {
		return disconnect0(baseID);
	}

	public static int checkBaseConnection(){
		return checkBaseConnection0();
	}
	
	public static String writeHDParam(int BaseID, int iMode, String sSetting) {
		r_onHDParamCallBack = null;
		writeHDParam0(BaseID, iMode, stringToBytes(sSetting));
		return r_onHDParamCallBack;
	}

	// int ReadHDParam(int BaseID,int iMode);
	public static String readHDParam(int BaseID, int iMode) {
		r_onHDParamCallBack = null;
		readHDParam0(BaseID, iMode);
		return r_onHDParamCallBack;
	}
	
		public static String readHDParam2(int BaseID, int iMode , String sSetting) {
			r_onHDParamCallBack = null;
			readHDParam20(BaseID, iMode,stringToBytes(sSetting));
			return r_onHDParamCallBack;
		}

	public static String WriteHDParamBySn(String keySn, int iMode,
			String sSetting) {
		r_onHDParamCallBack = null;
		writeHDParamBySn0(stringToBytes(keySn), iMode, stringToBytes(sSetting));
		return r_onHDParamCallBack;
	}

	// int ReadHDParam(int BaseID,int iMode);
	public static String ReadHDParamBySn(String keySn, int iMode) {
		r_onHDParamCallBack = null;
		readHDParamBySn0(stringToBytes(keySn), iMode);
		return r_onHDParamCallBack;
	}

	public static String voteStart(int iMode, String sSetting) {
		r_onHDParamCallBack = null;
		if (sSetting != null) {
			voteStart0(iMode, stringToBytes(sSetting));
		} else {
			voteStart0(iMode, stringToBytes(""));
		}
		return r_onHDParamCallBack;
	}

	public static int voteStop() {
		return voteStop0();
	}

	public static int voteStopByMsg(String sInfo) {
		return voteStopByMsg0(stringToBytes(sInfo));
	}

	public static int sendExamMultiAnswer(String sInfo) {
		return sendExamMultiAnswer0(stringToBytes(sInfo));
	}

	public static int getResultByID(int keyId, int resultType) {
		return getResultByID0(keyId, resultType);
	}
	
	public static int getMultiResultByID(String keyIDs,int resultType,int resultId){
		return getMultiResultByID0(stringToBytes(keyIDs), resultType, resultId);
	}
	
	public static int getResultBySN(String keySn ,int resultType,int resultId){
		return getResultBySN0(stringToBytes(keySn),resultType,resultId);
	}
	
	public static int exitGetResult() {
		return exitGetResult0();
	}
	
	public static int getStaIP() {
		return getStaIP0();
	}
	
	public static int setDemoMode(int isEnable,String keyIDs){
		return setDemoMode0(isEnable,stringToBytes(keyIDs));
	}
	
	public static int setLogOn(int isEnable){
		return setLogOn0(isEnable);
	}
	

	public static int license(int Mode, String sInfo) {
		return license0(Mode, stringToBytes(sInfo));
	}

	private static SunARSListener al;

	public static void setListener(SunARSListener al) {
		SunARS.al = al;
	}
	
	public static int DataRx(byte[] recvData,int dataLen){
		return DataRx0(recvData,dataLen);
	}
	

	public static int setArchiveDir(String path){
		return setArchiveDir0(stringToBytes(path));
	}

	// =原生方法开始=========================

	private static native int connect0(int mode, byte[] params);

	private static native int disconnect0(int baseID);
	
	private static native int checkBaseConnection0();

	private static native int writeHDParam0(int BaseID, int iMode, byte[] sSetting);

	private static native int readHDParam0(int BaseID, int iMode);
	
	private static native int readHDParam20(int BaseID, int iMode ,byte[] sSetting);

	private static native int writeHDParamBySn0(byte[] keySn, int iMode,
			byte[] sSetting);

	private static native int readHDParamBySn0(byte[] keySn, int iMode);

	private static native int voteStart0(int iMode, byte[] sSetting);

	private static native int voteStop0();
	
	private static native int voteStopByMsg0(byte[] sInfo);
	private static native int sendExamMultiAnswer0( byte[] sInfo);
	
	private static native int  getResultByID0(int keyId ,int resultType);
	
	private static native int getResultBySN0(byte[] keySn,int resultType,int resultId);
	
	private static native int getMultiResultByID0(byte[] keyIDs,int resultType,int resultId);
	
	private static native int exitGetResult0();
	
	private static native int getStaIP0();
	
	private static native int setDemoMode0(int isEnable,byte[] keyIDs);
	
	private static native int DataRx0(byte[] recvData,int dataLen);
	
	private static native int setArchiveDir0(byte[] path);
	
	private static native int setLogOn0(int isEnable);

	private static native int license0(int Mode, byte[] sInfo);

	private static native int init();

	
	private IBinder myBinder = new Binder(){
		@Override
		public String getInterfaceDescriptor(){
			return "SunARS class";
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("service onBind");
		return myBinder;
	}

	@Override
	public void onCreate() {
		System.out.println("service onCreate");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("service onStartCommand intent:"+intent);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		System.out.println("service onDestroy");
		super.onDestroy();
	}

	private static HashMap<Integer, String> modeNameMap;
	public static String getModeName(int i) {
		if (modeNameMap == null) {
			modeNameMap = new HashMap<Integer, String>();
			Class c = SunARS.class;
			for (Field f : c.getFields()) {
				if (f.getType() == Integer.TYPE) {
					String name = f.getName();																	
					if (name.startsWith("BaseStation_") || name.startsWith("SoftwareDongle_") || name.startsWith("KeyPad_") ||name.startsWith("Keypad_")|| name.startsWith("WiFi_")|| name.startsWith("Background")) {
						try {
							int v = f.getInt(null);
							modeNameMap.put(v, name);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return modeNameMap.get(i);
	}


	public static String getVoteModeName(int i) {
		switch (i) {
		case 0:
			return "VoteType_Free";
		case 1:
			return "VoteType_Signin";
		case 2:
			return "VoteType_Vote";
		case 4:
			return "VoteType_Number";
		case 5:
			return "VoteType_TrueFalse";
		case 9:
			return "VoteType_KeyPadTest";
		case 10:
			return "VoteType_Choice";
		case 11:
			return "VoteType_Sequence";
		case 12:
			return "VoteType_FillBlanks";
		case 13:
			return "VoteType_Quiz";
		case 14:
			return "VoteType_Examnation";
		case 15:
			return "VoteType_Homework";
		case 40:
			return "VoteType_KeyPadMatch";
		case 100:
			return "VoteType_continue";
		case 101:
			return "VoteType_submitAndContinue";

		}
		return "VoteType_End";
	}

	public static String getKeyEventTypeName(int i) {

		switch (i) {
		case 1:
			return "KeyResult_info";
		case 2:
			return "KeyResult_status";
		case 3:
			return "KeyResult_loginInfo";
		case 4:
			return "KeyResult_remoteControl";
		case 5:
			return "KeyResult_match";

		default:
			break;
		}

		return "" + i;

	}
	
	public static String getConnectTypeName(int i){
		switch(i){
			case 1:
				return "Connect_type_usb";
			case 2:
				return "Connect_type_tcp/ip";
			case 3:
				return "Connect_type_ble";
			default:
				break;
				
					
		}
		return ""+i;
	}
}
