package com.prance.teacher.lib.sunvote.utils;

import com.prance.teacher.lib.sunvote.core.SunARS;
import com.prance.teacher.lib.sunvote.core.SunARS.SunARSListener;

/**
 * 
 * @author houj
 * @ID
 * @time 2015骞?11鏈?12鏃? 涓婂崍11:27:41
 */
public class SunARSTest {

	private static SunARSListener al = new SunARSListener() {

		@Override
		public void onConnectEventCallBack(int iBaseID, int iMode, String sInfo) {
			System.out.println("onConnectEventCallBack>>" + iBaseID + " " + iMode + " " + sInfo);
		}

		@Override
		public void onHDParamCallBack(int iBaseID, int iMode, String sInfo) {
			System.out.println("onHDParamCallBack>>" + iBaseID + " " + iMode + " " + sInfo);
		}

		@Override
		public void onVoteEventCallBack(int iBaseID, int iMode, String sInfo) {
			System.out.println("onVoteEventCallBack>>>" + iBaseID + " " + iMode + " " + sInfo);
		}

		@Override
		public void onKeyEventCallBack(String KeyID, int iMode, float Time, String sInfo) {
			System.out.println("onKeyEventCallBack>>" + KeyID + " " + iMode + " " + Time + " " + sInfo);
		}
		
		@Override
		public void onDataTxEventCallBack(byte[] sendData, int dataLen){
			 
		}
		
		@Override
		public void onLogEventCallBack(String sInfo) {
			System.out.println(sInfo);
			
		}
		@Override
		public void onStaEventCallBack(String sInfo) {
			// TODO Auto-generated method stub
			System.out.println("onStaIpEventCallBack>>>" +  sInfo);
		}

		@Override
		public void onHDParamBySnCallBack(String KeySn, int iMode, String sInfo) {
			System.out.println("onHDParamBySnCallBack>>>" +  sInfo);
		}
		
		

	};

	public static void main(String[] args) throws Exception {
		SunARS.setListener(al);
		int r = SunARS.license(1, "SUNARS2013");
		System.out.println(r);

		Thread.sleep(1000);

		System.out.println("Begin test Connect...");
		r = SunARS.connect(1, "1");
		System.out.println(r);

		System.out.println("getSN");
		String sn=SunARS.readHDParam(0, 10);
		System.out.println("SN:"+sn);
		Thread.sleep(100);

		Thread.sleep(10000000);
	}
}
