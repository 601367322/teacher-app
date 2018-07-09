package com.prance.lib.sunvote.features;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import com.prance.lib.sunvote.R;
import cn.sunars.sdk.SunARS;

public class BaseManger extends Fragment {
    private View contentView;
    private CheckBox cbBgSignin;
    private EditText edtBaseID;
    private EditText edtChannel;
    private EditText edtOffTime;

    private Spinner spReportMode;
    private Spinner spRfPower;
    private Spinner spSubmission;
    private Spinner spBuzzer;
    private Spinner spIdentifyMode;

    private Button btnWriteBase;
    private Button btnReadBase;

    private Spinner spBaseMode;
    private EditText edtRoterName;
    private EditText edtRoterPwd;

    private Button btnReadWifi;
    private Button btnWriteWifi;
    private Button btnClear;

    private TextView txtBase;
    private ScrollView scrView;
    private Timer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base, container, false);
        contentView = view;
        txtBase = (TextView) view.findViewById(R.id.txtBase);
        scrView = (ScrollView) view.findViewById(R.id.scvBase);

        cbBgSignin = (CheckBox) view.findViewById(R.id.cbBgSignin);
        edtBaseID = (EditText) view.findViewById(R.id.edtBaseID);
        edtChannel = (EditText) view.findViewById(R.id.edtChannel);
        edtOffTime = (EditText) view.findViewById(R.id.edtOffTime);
        spReportMode = (Spinner) view.findViewById(R.id.spReportMode);
        spIdentifyMode = (Spinner) view.findViewById(R.id.spIdentifyMode);
        spRfPower = (Spinner) view.findViewById(R.id.spRfPower);
        spSubmission = (Spinner) view.findViewById(R.id.spSubmission);
        spBuzzer = (Spinner) view.findViewById(R.id.spBuzzer);

        spBaseMode = (Spinner) view.findViewById(R.id.spBaseMode);
        edtRoterName = (EditText) view.findViewById(R.id.edtRoterName);
        edtRoterPwd = (EditText) view.findViewById(R.id.edtRoterPwd);


        cbBgSignin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                if (cbBgSignin.isChecked()) {
                    cbBgSignin.setChecked(!cbBgSignin.isChecked());
                    MainActivity.addLog("WriteHDParam(0,Background_SignIn, \"1,2\")");
                    SunARS.writeHDParam(0, SunARS.Background_SignIn, "1,2");//1:开启,2:要输入数字签到码

                } else {
                    cbBgSignin.setChecked(!cbBgSignin.isChecked());
                    MainActivity.addLog("WriteHDParam(0,Background_SignIn, \"0\")");
                    SunARS.writeHDParam(0, SunARS.Background_SignIn, "0");//关闭
                }


            }
        });

        btnWriteBase = (Button) view.findViewById(R.id.btnWriteBase);
        btnWriteBase.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                writeBaseConfig();
            }
        });

        btnReadBase = (Button) view.findViewById(R.id.btnReadBase);
        btnReadBase.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                readBaseConfig();

            }
        });

        btnReadWifi = (Button) view.findViewById(R.id.btnReadWifi);
        btnReadWifi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                readWifiConfig();

            }
        });

        btnWriteWifi = (Button) view.findViewById(R.id.btnWriteWifi);
        btnWriteWifi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                writeWifiConfig();
            }
        });

        btnClear = (Button) view.findViewById(R.id.btnBaseClear);
        btnClear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.strLogInfo = "";
                txtBase.setText("");
            }
        });

        if (SunARS.isConnected) {
            readBaseConfig();
            readWifiConfig();
        }
        timer = new Timer(true);
        timer.schedule(displayTask, 50, 300); //延时500ms后执行，300ms执行一次
        return view;
    }

    private String tmpString = "-";
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.i("Connect", "onTimer");
                    txtBase.setText(MainActivity.strLogInfo);
                    if (!tmpString.equals(MainActivity.strLogInfo)) {
                        tmpString = MainActivity.strLogInfo;
                        scrollToBottom();
                    }
                    break;
            }
        }
    };

    TimerTask displayTask = new TimerTask() {
        public void run() {
            if (!BaseManger.this.isVisible()) {
                return;
            }
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };


    @Override
    public void onResume() {

        super.onResume();
//		if(SunARS.isConnected){
//			readBaseConfig();
//			readWifiConfig();
//		}
    }


    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        System.out.println("Connect onDestroy");
        super.onDestroy();
    }

    @Override
    public void onStart() {
//		if(SunARS.isConnected){
//			readBaseConfig();
//			readWifiConfig();
//		}
        super.onStart();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden == false && SunARS.isConnected) {
            readBaseConfig();
            readWifiConfig();
        }

        super.onHiddenChanged(hidden);
    }


    public void readBaseConfig() {
        readParam(0, SunARS.Background_SignIn);
        readParam(0, SunARS.KeyPad_IdentificationMode);
        readParam(0, SunARS.BaseStation_ID);
        readParam(0, SunARS.BaseStation_Channel);
        readParam(0, SunARS.BaseStation_RFPower);
        readParam(0, SunARS.KeyPad_Config);
    }

    private int iRfPower;

    private int iKeypadRepMode;
    private int iSubmissionMode;
    private int iBuzzerMode;
    private int iLcd;
    private int iVib;
    private int iLang;

    public void writeBaseConfig() {
        Integer offtime = Integer.parseInt(edtOffTime.getText().toString());
        if (offtime > 255) {
            Toast toast = Toast.makeText(getContext(), "The off time must between 0 and 255", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        writeParam(0, SunARS.BaseStation_ID, edtBaseID.getText().toString());
        Integer iIdentify = spIdentifyMode.getSelectedItemPosition();
        writeParam(0, SunARS.KeyPad_IdentificationMode, iIdentify.toString());
        writeParam(0, SunARS.BaseStation_Channel, edtChannel.getText().toString());


        Integer iPower = spRfPower.getSelectedItemPosition();
        writeParam(0, SunARS.BaseStation_RFPower, iPower.toString());
        iKeypadRepMode = spReportMode.getSelectedItemPosition();
        iSubmissionMode = spSubmission.getSelectedItemPosition();
        iBuzzerMode = spBuzzer.getSelectedItemPosition();

        String strOffTime = edtOffTime.getText().length() > 0 ? edtOffTime.getText().toString() : "0";
        String strParam = String.format("%d,%s,%d,%d,%d,%d,%d", iKeypadRepMode, strOffTime,
                iSubmissionMode, iBuzzerMode, iLcd, iVib, iLang);
        writeParam(0, SunARS.KeyPad_Config, strParam);


    }

    public void readWifiConfig() {

        readParam(0, SunARS.WiFi_SSID);
        readParam(0, SunARS.WiFi_Password);

    }

    public void writeWifiConfig() {
        Integer connMode = spBaseMode.getSelectedItemPosition() + 1;
        String param_str = connMode + "," + edtRoterName.getText().toString();
        writeParam(0, SunARS.WiFi_SSID, param_str);

        writeParam(0, SunARS.WiFi_Password, edtRoterPwd.getText().toString());

    }

    public void readParam(int baseId, int iMode) {
        MainActivity.addLog("ReadHDParam(" + baseId + "," + SunARS.getModeName(iMode) + ")");
        SunARS.readHDParam(baseId, iMode);
    }

    public void writeParam(int baseId, int iMode, String sInfo) {
        SunARS.writeHDParam(baseId, iMode, sInfo);
        MainActivity.addLog("writeHDParam(" + baseId + "," + SunARS.getModeName(iMode) + ",\"" + sInfo + "\")");
    }


    private void scrollToBottom() {

        scrView.post(new Runnable() {
            public void run() {
                scrView.smoothScrollTo(0, txtBase.getBottom());
                contentView.refreshDrawableState();

            }
        });
    }


    public void onConnectEventCallBack(final int iBaseID, final int iMode, final String sInfo) {
        //appendLog("onConnectEventCallBack>>" + iBaseID + " " + SunARS.getConnectTypeName(iMode) + " " + sInfo);
    }

    public void onHDParamCallBack(int iBaseID, final int iMode, final String sInfo) {
        //appendLog("onHDParamCallBack>>" + iBaseID + " " +SunARS.getModeName(iMode)  + " " + sInfo);
        if (sInfo.equals("FAIL")) {
            return;
        }
        if (iMode == SunARS.BaseStation_ID) {
            edtBaseID.post(new Runnable() {
                public void run() {
                    edtBaseID.setText(sInfo);
                }
            });

        } else if (iMode == SunARS.BaseStation_Channel) {

            edtChannel.post(new Runnable() {
                @Override
                public void run() {
                    edtChannel.setText(sInfo);
                }
            });
        } else if (iMode == SunARS.BaseStation_RFPower) {
            spRfPower.post(new Runnable() {
                public void run() {
                    iRfPower = Integer.parseInt(sInfo);

                    spRfPower.setSelection(iRfPower);


                }
            });
        } else if (iMode == SunARS.KeyPad_IdentificationMode) {
            spIdentifyMode.post(new Runnable() {
                public void run() {
                    if (sInfo.equals("FAIL")) {
                        return;
                    }
                    Integer iIdentify = Integer.parseInt(sInfo);
                    spIdentifyMode.setSelection(iIdentify);

                }
            });

        } else if (iMode == SunARS.KeyPad_Config) {
            if (sInfo.length() == 0) {
                return;
            }
            final String[] ary = sInfo.split(",");

            if (ary == null) {
                return;
            }
            if (ary != null && ary.length > 0 && ary[0] != null) { // ReportMode
                iKeypadRepMode = Integer.parseInt(ary[0]);
                spReportMode.post(new Runnable() {
                    public void run() {
                        spReportMode.setSelection(iKeypadRepMode);
                    }
                });

            }
            if (ary != null && ary.length > 1 && ary[1] != null) { // offtime
                //Integer mm = Integer.parseInt(ary[1]);
                edtOffTime.post(new Runnable() {
                    public void run() {
                        edtOffTime.setText(ary[1]);
                    }
                });

            }

            if (ary.length > 2 && ary[2] != null) { // SubmisMode
                iSubmissionMode = Integer.parseInt(ary[2]);
                spSubmission.post(new Runnable() {
                    public void run() {
                        spSubmission.setSelection(iSubmissionMode);
                    }
                });
            }
            if (ary.length > 3 && ary[3] != null) { // Buzzer
                iBuzzerMode = Integer.parseInt(ary[3]);
                spBuzzer.post(new Runnable() {
                    public void run() {
                        spBuzzer.setSelection(iBuzzerMode);
                    }
                });
            }

            if (ary.length > 4 && ary[4] != null) { // LCD
                iLcd = Integer.parseInt(ary[4]);
            }

            if (ary.length > 5 && ary[5] != null) { // Vib

                iVib = Integer.parseInt(ary[5]);
            }

            if (ary.length > 6 && ary[6] != null) { // Lang

                iLang = Integer.parseInt(ary[6]);

            }
        } else if (iMode == SunARS.WiFi_WorkMode) {

        } else if (iMode == SunARS.WiFi_SSID) {
            if (sInfo.length() == 0) {
                return;
            }
            final String[] ary = sInfo.split(",");
            if (ary.length > 0) {
                spBaseMode.post(new Runnable() {
                    public void run() {
                        Integer iwMode = Integer.parseInt(ary[0]) - 1;
                        spBaseMode.setSelection(iwMode);
                    }
                });
            }
            if (ary.length > 1) {
                edtRoterName.post(new Runnable() {
                    public void run() {
                        edtRoterName.setText(ary[1]);
                    }
                });
            }
        } else if (iMode == SunARS.WiFi_Password) {
            edtRoterPwd.post(new Runnable() {
                public void run() {
                    edtRoterPwd.setText(sInfo);
                }
            });
        } else if (iMode == SunARS.Background_SignIn) {
            cbBgSignin.post(new Runnable() {
                public void run() {
                    cbBgSignin.setChecked(sInfo.startsWith("1"));
                }
            });

        }
    }


    public void onVoteEventCallBack(final int iBaseID, final int iMode, final String sInfo) {
        //appendLog("onVoteEventCallBack>>>" + iBaseID + " " +SunARS.getVoteModeName(iMode)  + " " + sInfo);

    }

    public void onKeyEventCallBack(final String KeyID, final int iMode, final float Time, final String sInfo) {
        //appendLog("onKeyEventCallBack>>" + KeyID + " " + SunARS.getKeyEventTypeName(iMode) + " " + Time + " " + sInfo);


    }
}
