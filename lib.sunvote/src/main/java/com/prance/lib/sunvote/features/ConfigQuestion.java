package com.prance.lib.sunvote.features;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.prance.lib.sunvote.R;

public class ConfigQuestion extends Activity {
	private String strParam;
	private ListView listview;
	ListViewAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_question);
		
		strParam = getIntent().getStringExtra("param");
		
		adapter = new ListViewAdapter(strParam, this);
		
		this.listview = (ListView)findViewById(R.id.listV);
		this.listview.setAdapter(adapter);
		
		Button btnOK =(Button)findViewById(R.id.btnQuestionOk);
		btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				List<Map<String,String>> list = adapter.getMapData();
				String strTmp = "";
				for(int i = 0; i < list.size(); i++)  
		        {  
					Map<String,String> map =   list.get(i);  
		            strTmp += map.get("mul")+":"+map.get("opt");
		            if(i<list.size()-1){
		            	strTmp += "," ;
		            }
		        }  
				
				getIntent().putExtra("questionStr", strTmp);
				setResult(RESULT_OK, getIntent());
				finish();
				
			}
		});
		
		
	}
	
	

}
