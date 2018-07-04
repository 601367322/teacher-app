package com.prance.teacher.features.main.sun;

import android.os.Handler;

import cn.sunars.sdk.SunARS;

import com.prance.teacher.lib.sunvote.utils.DataUtil;


public class DataReceiverThread extends Thread {

    private byte[] SerDataRx = new byte[512];
    int iSerRxN = 0;

    IUsbManagerInterface mUsbManagerInterface;

    public DataReceiverThread(IUsbManagerInterface usbManagerInterface) {
        this.mUsbManagerInterface = usbManagerInterface;
    }

    @Override
    public void run() {
        super.run();
        while (!isInterrupted()) {
            final byte[] buffer = mUsbManagerInterface.receiveUsbData(); // receiveUsbRequestData();
            if (buffer != null) {
                onDataReceived(buffer);
            }
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }


    /**
     * 接收到的数据进行处理,并且执行发送数据
     *
     * @param buffer 传过来的数据
     */

    private void onDataReceived(final byte[] buffer) {
        runOnUiThread1(new Runnable() {
            @Override
            public void run() {
                processRecvData(buffer);

            }
        });
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
//                                System.out.println("recv package crc error!\n");
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
}
