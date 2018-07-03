package com.prance.teacher.lib.sunvote.features;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.prance.teacher.lib.sunvote.R;

public class ConfigExam extends Activity {

	private EditText edtExamName;
	private EditText edtExamQuestionNum;
	private Spinner spExamMode;
	private Spinner spExamReportStatus;
	private Button btnQuestionConfig;
	private String[] ary;
	String strQuestion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_exam);
		
		spExamMode = (Spinner) findViewById(R.id.spCfgExamMode);
		spExamReportStatus = (Spinner)findViewById(R.id.spCfgExamReportStatus);
		edtExamName = (EditText)findViewById(R.id.edtExamName);
		edtExamQuestionNum = (EditText)findViewById(R.id.edtExamQuestionNum);
		btnQuestionConfig = (Button)findViewById(R.id.btnQuestionConfig);
		
		String param = getIntent().getStringExtra("param");
		ary = param.split(",");
		/*
		 * VoteStart(VoteType_Examnation,"1,0,5,test")
	  	 * VoteStart(VoteType_Examnation,"4,0,5,1:8,1:8,1:8,1:8,1:8")
		 * */
		int mode = Integer.parseInt(ary[0]);
		spExamMode.setSelection(mode-1);
		
		
		edtExamQuestionNum.setText(ary[2]);
		if(mode < 4){
			edtExamName.setText(ary[3]);
		}else{
			strQuestion="";
			for(int i=3;i<ary.length;i++){
				strQuestion += ary[i];
				if(i<ary.length-1){
					strQuestion += ",";
				}
			}
			
		}
		
		spExamMode.setOnItemSelectedListener(new OnItemSelectedListener() {
			//当选中某一个数据项时触发该方法
            /*
             * parent接收的是被选择的数据项所属的 Spinner对象，
             * view参数接收的是显示被选择的数据项的TextView对象
             * position接收的是被选择的数据项在适配器中的位置
             * id被选择的数据项的行号
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                //System.out.println(spinner==parent);//true
                //System.out.println(view);
                //String data = adapter.getItem(position);//从适配器中获取被选择的数据项
                //String data = list.get(position);//从集合中获取被选择的数据项
                String data = (String)spExamMode.getItemAtPosition(position);//从spinner中获取被选择的数据
                if(position<3){
                	btnQuestionConfig.setEnabled(false);
                }else{
                	btnQuestionConfig.setEnabled(true);
                }
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub    
            }
		});
		spExamReportStatus.setSelection(Integer.parseInt(ary[1]));
		 
		
		Button btnOk = (Button)findViewById(R.id.btnExamConfigOk);
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int qNum =Integer.parseInt( edtExamQuestionNum.getText().toString() );
				int mode = spExamMode.getSelectedItemPosition()+1;
				if(mode==4){
					if(qNum>30){
						Toast toast=	Toast.makeText(getApplicationContext(), "The munber of question must between 1 and 30",Toast.LENGTH_SHORT );
						toast.show(); 
						return;
					}
				}else{
					if(qNum>100){
						Toast toast=	Toast.makeText(getApplicationContext(), "The munber of question must between 1 and 100",Toast.LENGTH_SHORT );
						toast.show(); 
						return;
					}
				}
				String value=null;
				//int mode = spExamMode.getSelectedItemPosition()+1;
				if(mode<4){
					// VoteStart(VoteType_Examnation,"1,0,5,test")
					value = String.format("%d,%d,%s,%s",
							mode,
							spExamReportStatus.getSelectedItemPosition(),
							edtExamQuestionNum.getText().toString(),
							edtExamName.getText().toString());
				}else if(mode == 4){
					if(strQuestion == null){
						setDefaultQuestion();
					}
					value = String.format("%d,%d,%s,%s",
							mode,
							spExamReportStatus.getSelectedItemPosition(),
							edtExamQuestionNum.getText().toString(),
							strQuestion);
					
				}else{
					value = String.format("%d,%d,%s",
							mode,
							spExamReportStatus.getSelectedItemPosition(),
							edtExamQuestionNum.getText().toString() );
				}
				getIntent().setFlags(3);
				getIntent().putExtra("param", value);
				setResult(RESULT_OK, getIntent());
				finish();
				
			}
		});
		
		btnQuestionConfig.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				int qNum =Integer.parseInt( edtExamQuestionNum.getText().toString() );
				int mode = spExamMode.getSelectedItemPosition()+1;
				if(mode>=4){
					if(qNum>30){
						Toast toast=	Toast.makeText(getApplicationContext(), "The munber of question must between 1 and 30",Toast.LENGTH_SHORT );
						toast.show(); 
						return;
					}
				}else{
					if(qNum>100){
						Toast toast=	Toast.makeText(getApplicationContext(), "The munber of question must between 1 and 100",Toast.LENGTH_SHORT );
						toast.show(); 
						return;
					}
				}
				
				if(strQuestion == null ){
					setDefaultQuestion();
				}else{
					
					String[] ary = strQuestion.split(",");
					if(qNum != ary.length){
						setDefaultQuestion();
					}
				}
				Intent it =  new Intent(ConfigExam.this, ConfigQuestion.class); 
				it.putExtra("param", strQuestion);
				startActivityForResult(it, 1);
			}
		});
				
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(resultCode){
		case Activity.RESULT_OK:
			strQuestion = data.getStringExtra("questionStr");
		case Activity.RESULT_CANCELED:
			System.out.println("canceled");
		}
	}



	private void setDefaultQuestion(){
		//VoteStart(VoteType_Examnation,"4,0,5,1:8,1:8,1:8,1:8,1:8")
		strQuestion = "";
		int qNum =Integer.parseInt( edtExamQuestionNum.getText().toString() );
		for(int i=0;i<qNum;i++){
			strQuestion += "1:8";
			if(i<qNum-1){
				strQuestion += ",";
			}
		}
	}
	
	
	
}
