package com.prance.teacher.lib.sunvote.features;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.prance.teacher.lib.sunvote.R;

public class ConfigChoice extends Activity {
	
	private Spinner spCfgOptionType;
	private Spinner spCfgModifyMode;
	private Spinner spCfgSecrecyMode;
	private Spinner spCfgSubmissionMode;
	private Spinner spCfgOptionNum;
	private Spinner spCfgSelectNum;
	private EditText edtRightAnswer;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_choice);
		
		spCfgOptionType = (Spinner) findViewById(R.id.spCfgOptionType);
		spCfgModifyMode = (Spinner) findViewById(R.id.spCfgModifyMode);
		spCfgSecrecyMode = (Spinner) findViewById(R.id.spCfgSecrecyMode);
		spCfgSubmissionMode = (Spinner) findViewById(R.id.spCfgSubmissionMode);
		spCfgOptionNum = (Spinner) findViewById(R.id.spCfgOptionNum);
		spCfgSelectNum = (Spinner) findViewById(R.id.spCfgSelectNum);
		edtRightAnswer = (EditText) findViewById(R.id.edtRightAnswer);
		
		
		String param = getIntent().getStringExtra("param");
		String[] ary = param.split(",");
		int val = Integer.parseInt(ary[0])-1;
		spCfgOptionType.setSelection(val);
		val = Integer.parseInt(ary[1]);
		spCfgModifyMode.setSelection(val);
		val = Integer.parseInt(ary[2]);
		spCfgSecrecyMode.setSelection(val);
		val = Integer.parseInt(ary[3]);
		spCfgSubmissionMode.setSelection(val);
		val = Integer.parseInt(ary[4])-1;
		spCfgOptionNum.setSelection(val);
		val = Integer.parseInt(ary[5])-1;
		spCfgSelectNum.setSelection(val);
		if(ary.length>6){
			edtRightAnswer.setText(ary[6]);
		}
		 
		
		Button btnOk = (Button)findViewById(R.id.btnCfgChoiceOK);
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String value = String.format("%d,%d,%d,%d,%d,%d", 
						spCfgOptionType.getSelectedItemPosition()+1,
						spCfgModifyMode.getSelectedItemPosition(),
						spCfgSecrecyMode.getSelectedItemPosition(),
						spCfgSubmissionMode.getSelectedItemPosition(),
						spCfgOptionNum.getSelectedItemPosition()+1,
						spCfgSelectNum.getSelectedItemPosition()+1
						);
				if(edtRightAnswer.getText().toString().length()>0){
					value = value + ","+ edtRightAnswer.getText().toString();
				}
				getIntent().setFlags(1);
				getIntent().putExtra("param", value.toString());
				setResult(RESULT_OK, getIntent());
				finish();
				
			}
		});
				
	}
	

}
