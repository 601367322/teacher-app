package com.prance.lib.sunvote.platform;

import android.os.Handler;

import cn.sunars.sdk.SunARS;

import com.prance.lib.sunvote.utils.DataUtil;


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
            // receiveUsbRequestData();
            final byte[] buffer = mUsbManagerInterface.receiveUsbData();
            if (buffer != null) {
                onDataReceived(buffer);
            }
            try {
                sleep(10);
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
                try {
                    processRecvData(buffer);
                } catch (Exception e) {
                    resetData();
                    e.printStackTrace();
                }
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
            // 先保存数据
            SerDataRx[iSerRxN] = recvBuf[i];
            switch (iSerRxN) {
                case 0:
                    if (dd == 0xF5) {
                        iSerRxN++;
                    }
                    break;
                case 1:
                    if (dd == 0xAA) {
                        iSerRxN++;
                    } else {
                        if (dd == 0xF5) {
                            iSerRxN = 1;
                        } else {
                            iSerRxN = 0;
                        }
                    }
                    break;
                case 2:
                    if (dd == 0xAA) {
                        iSerRxN++;
                    } else {
                        if (dd == 0xF5) {
                            iSerRxN = 1;
                        } else {
                            iSerRxN = 0;
                        }
                    }
                    break;
                case 3:
                    // C_SERMAXN
                    if (dd > 128) {
                        iSerRxN = 0;
                    } else {
                        iSerRxN++;
                    }
                    break;
                default:
                    iSerRxN++;
                    rxDataLen = SerDataRx[3] + 4;
                    if (rxDataLen <= 3) {
                        break;
                    }
                    if (iSerRxN == rxDataLen) {
                        // 数据接收完整

                        if (DataUtil.checkPack(SerDataRx)) {
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

                            SunARS.DataRx(SerDataRx, rxDataLen);

                            // 收到数据包

                            resetData();
                        }

                    }
                    break;
            }// switch
        } // for

    }

    private void resetData() {
        for (int j = 0; j < SerDataRx.length; j++) {
            SerDataRx[j] = 0;
        }
        iSerRxN = 0;
    }
}
