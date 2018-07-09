package com.prance.lib.sunvote.features;

import java.util.Timer;
import java.util.TimerTask;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.prance.lib.sunvote.R;

import cn.sunars.sdk.SunARS;


public class Application extends Fragment {

    private View rowSignin;
    private View rowChoice;
    private View rowQuiz;
    private View rowExam;

    private Button btnStopAnswer;
    private Spinner spAppStartmode;

    private ScrollView scvApp;
    private TextView txtAppLog;
    private View contentView;

    private View[] tableRows = new View[4];

    private int iSelIndex = 0;
    private int[] aryVoteType = {SunARS.VoteType_Signin, SunARS.VoteType_Choice, SunARS.VoteType_Quiz,
            SunARS.VoteType_Examination};
    private String[] aryParam = {"1", "1,1,0,0,4,1", "0", "2,0,5,test"};
    private Timer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.application, container, false);
        contentView = view;
        spAppStartmode = (Spinner) view.findViewById(R.id.spAppStartmode);
        spAppStartmode.setSelection(1);
        rowSignin = view.findViewById(R.id.rowSignin);
        rowChoice = view.findViewById(R.id.rowChoice);
        rowQuiz = view.findViewById(R.id.rowQuiz);
        rowExam = view.findViewById(R.id.rowExam);

        rowSignin.setBackgroundColor(Color.GRAY);
        tableRows[0] = rowSignin;
        tableRows[1] = rowChoice;
        tableRows[2] = rowQuiz;
        tableRows[3] = rowExam;

        rowSignin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTableRow(0);

            }
        });

        rowChoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTableRow(1);

            }
        });

        rowQuiz.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectTableRow(2);

            }
        });

        rowExam.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectTableRow(3);

            }
        });

        btnStopAnswer = (Button) view.findViewById(R.id.btnStopAnswer);
        btnStopAnswer.setVisibility(View.INVISIBLE);

        btnStopAnswer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String[] ary = aryParam[iSelIndex].split(",");
                String paraStr = "0,";
                for (int i = 0; i < ary.length; i++) {
                    paraStr += ary[i];
                    if (i < ary.length - 1) {
                        paraStr += ",";
                    }
                }
                voteStart(aryVoteType[iSelIndex], paraStr);

            }
        });

        scvApp = (ScrollView) view.findViewById(R.id.scvApp);
        txtAppLog = (TextView) view.findViewById(R.id.txtAppLog);

        Button btnStartVote = (Button) view.findViewById(R.id.btnStartVote);
        btnStartVote.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int iStartMode = spAppStartmode.getSelectedItemPosition();
                if (iStartMode == 0) {
                    voteStart(SunARS.VoteType_Continue, "");
                } else if (iStartMode == 1) {
                    String[] ary = aryParam[iSelIndex].split(",");
                    if (iSelIndex == 1 && ary.length > 6) {
                        String info = aryParam[iSelIndex].substring(0, aryParam[iSelIndex].lastIndexOf(","));
                        voteStart(aryVoteType[iSelIndex], info);
                    } else if (iSelIndex == 3) {

                        if (ary[0].equals("5")) {//多题
                            String strQuestionType = "0:1,1:2,2;0,2;0,3:0";  //题1类型：题1选项，题2类型，题2选项。。。。。。(类型 0-单选 , 1-多选 , 2-数字，3-判断)
                            voteStart(aryVoteType[iSelIndex], String.format("%s,%s,%s,%s", ary[0], ary[1], ary[2], strQuestionType));
                            //VoteStart(VoteType_Examnation,"5,0,5,0:1,1:2,2;0,2;0,3:0")
                        } else if (ary[0].equals("6")) {//高思多题
                            String strQuestionType = "0,1,1,0,0";  //题1类型，题2类型，题3类型。。。。。(类型：0-单选  1-数字)
                            voteStart(aryVoteType[iSelIndex], String.format("%s,%s,%s,%s", ary[0], ary[1], ary[2], strQuestionType));
                            //VoteStart(VoteType_Examnation,"6,0,5,0,1,1,0,0") ""
                        } else {
                            voteStart(aryVoteType[iSelIndex], aryParam[iSelIndex]);
                        }
                    } else {
                        voteStart(aryVoteType[iSelIndex], aryParam[iSelIndex]);
                    }
                } else if (iStartMode == 2) {
                    voteStart(SunARS.VoteType_SubmitAndContinue, "");
                }
            }
        });

        Button btnStopVote = (Button) view.findViewById(R.id.btnStopVote);
        btnStopVote.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String[] ary = aryParam[1].split(",");
                if (iSelIndex == 1 && ary.length > 6) {
                    voteStopByMsg("3,2,3," + ary[6]);
                } else {
                    voteStop();
                }
            }
        });

        Button btnClear = (Button) view.findViewById(R.id.btnAppClear);
        btnClear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.strLogInfo = "";
                txtAppLog.setText("");
            }
        });

        Button btnSigninConfig = (Button) view.findViewById(R.id.btnSigninCfg);
        btnSigninConfig.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent it = new Intent(getActivity(), ConfigSignin.class);
                it.putExtra("param", aryParam[0]);
                startActivityForResult(it, 1);

            }
        });

        Button btnChoiceConfig = (Button) view.findViewById(R.id.btnChoiceCfg);
        btnChoiceConfig.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), ConfigChoice.class);
                it.putExtra("param", aryParam[1]);
                startActivityForResult(it, 1);
            }
        });

        Button btnQuizConfig = (Button) view.findViewById(R.id.btnQuizCfg);
        btnQuizConfig.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), ConfigQuiz.class);
                it.putExtra("param", aryParam[2]);
                startActivityForResult(it, 1);
            }
        });

        Button btnExamConfig = (Button) view.findViewById(R.id.btnExamCfg);
        btnExamConfig.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), ConfigExam.class);
                it.putExtra("param", aryParam[3]);
                startActivityForResult(it, 1);
            }
        });

        timer = new Timer(true);
        timer.schedule(displayTask, 50, 300); // 延时500ms后执行，300ms执行一次

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case Activity.RESULT_OK:
                aryParam[data.getFlags()] = data.getStringExtra("param");
            case Activity.RESULT_CANCELED:
                System.out.println("canceled");
        }
    }

    private void selectTableRow(int index) {
        iSelIndex = index;
        for (int i = 0; i < tableRows.length; i++) {
            if (index == i) {
                tableRows[i].setBackgroundColor(Color.GRAY);
            } else {
                tableRows[i].setBackgroundColor(color.background_light);
            }
        }
        btnStopAnswer.setVisibility(index == 3 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        System.out.println(" Application onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        System.out.println("Application onDestroyView");
        // TODO Auto-generated method stub
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        System.out.println("Application onDetach");
        // TODO Auto-generated method stub
        super.onDetach();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        System.out.println("Application onHiddenChanged");
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onPause() {
        System.out.println("Application onPause");
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onResume() {
        System.out.println("Application onResume");
        super.onResume();
    }

    @Override
    public void onStart() {
        System.out.println("Application onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        System.out.println("Application onStop");
        super.onStop();
    }

    public void voteStart(int iMode, String sInfo) {

        MainActivity.addLog("VoteStart(" + SunARS.getVoteModeName(iMode) + ",\"" + sInfo + "\")");
        SunARS.voteStart(iMode, sInfo);
    }

    public void voteStop() {
        MainActivity.addLog("VoteStop()");
        SunARS.voteStop();
    }

    public void voteStopByMsg(String msg) {
        MainActivity.addLog("VoteStopByMsg(" + msg + ")");
        SunARS.voteStopByMsg(msg);
    }

    public void readParam(int baseId, int iMode) {
        MainActivity.addLog("ReadHDParam(" + baseId + "," + SunARS.getModeName(iMode) + ")");
        SunARS.readHDParam(baseId, iMode);
    }

    public void writeParam(int baseId, int iMode, String sInfo) {
        SunARS.writeHDParam(baseId, iMode, sInfo);
        MainActivity.addLog("writeHDParam(" + baseId + "," + SunARS.getModeName(iMode) + ",\"" + sInfo + "\")");
    }

    private String tmpString = "-";
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.i("Connect", "onTimer");
                    txtAppLog.setText(MainActivity.strLogInfo);
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
            if (!Application.this.isVisible()) {
                return;
            }
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    private void scrollToBottom() {

        scvApp.post(new Runnable() {
            public void run() {
                scvApp.smoothScrollTo(0, txtAppLog.getBottom());
                contentView.refreshDrawableState();

            }
        });
    }

    public void onConnectEventCallBack(final int iBaseID, final int iMode, final String sInfo) {
        System.out.println("onConnectEventCallBack>>" + iBaseID + " " + iMode + " " + sInfo);
        // appendLog("onConnectEventCallBack>>" + iBaseID + " " +
        // SunARS.getConnectTypeName(iMode) + " " + sInfo);

    }

    public void onHDParamCallBack(int iBaseID, final int iMode, final String sInfo) {
        System.out.println("onHDParamCallBack>>" + iBaseID + " " + iMode + " " + sInfo);
        // appendLog("onHDParamCallBack>>" + iBaseID + " " + iMode + " " +
        // sInfo);

    }

    public void onVoteEventCallBack(final int iBaseID, final int iMode, final String sInfo) {
        // appendLog("onVoteEventCallBack>>>" + iBaseID + " "
        // +SunARS.getVoteModeName(iMode) + " " + sInfo);

    }

    public void onKeyEventCallBack(final String KeyID, final int iMode, final float Time, final String sInfo) {
        // appendLog("onKeyEventCallBack>>" + KeyID + " " +
        // SunARS.getKeyEventTypeName(iMode) + " " + Time + " " + sInfo);

    }

}
