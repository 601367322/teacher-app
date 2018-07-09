package com.prance.lib.sunvote.features;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;

import com.prance.lib.sunvote.R;

public class ConfigQuiz extends Activity {

	private Spinner spQuziMode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_quiz);
		
		spQuziMode = (Spinner) findViewById(R.id.spCfgQuizMode);
		
		Integer val =Integer.parseInt(   getIntent().getStringExtra("param"));
		spQuziMode.setSelection(val);
		
		Button btnOk = (Button)findViewById(R.id.btnQuizConfigOk);
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Integer value = spQuziMode.getSelectedItemPosition();
				getIntent().setFlags(2);
				getIntent().putExtra("param", value.toString());
				setResult(RESULT_OK, getIntent());
				finish();
				
			}
		});
				
	}
	
}
