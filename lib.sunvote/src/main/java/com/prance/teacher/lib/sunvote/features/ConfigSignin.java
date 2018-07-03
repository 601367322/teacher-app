package com.prance.teacher.lib.sunvote.features;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;

import com.prance.teacher.lib.sunvote.R;

public class ConfigSignin extends Activity {
	private Spinner spSigninType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_signin);
		
		spSigninType = (Spinner) findViewById(R.id.spCfgSignin);
		
		Integer val =Integer.parseInt(   getIntent().getStringExtra("param"));
		spSigninType.setSelection(val-1);
		
		Button btnOk = (Button)findViewById(R.id.btnSigninConfigOk);
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Integer value = spSigninType.getSelectedItemPosition()+1;
				getIntent().setFlags(0);
				getIntent().putExtra("param", value.toString());
				setResult(RESULT_OK, getIntent());
				finish();
				
			}
		});
				
	}
	
	
	
}
