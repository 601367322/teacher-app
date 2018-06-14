package com.lanvote.sunvote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.tengyue.teacher.R;

public class ListViewAdapter extends BaseAdapter {
	
	private  List<Map<String,String>> aryList = new ArrayList<Map<String,String>>();
	private Context  context   = null;
	
	public ListViewAdapter(String param ,  Context context){
		this.context = context;
		String[] aryParam = param.split(",");
		for(int i=0;i<aryParam.length;i++){
			Map<String,String> map = new HashMap<String, String>();
			String[] aryTmp=aryParam[i].split(":");
			map.put("mul", aryTmp[0]);
			map.put("opt", aryTmp[1]);
			aryList.add(map);
		}
		
	}
	
	public  List<Map<String,String>> getMapData(){
		return aryList;
	}

	@Override
	public int getCount() {
		
		return aryList.size();
	}

	@Override
	public Object getItem(int position) {
		 
		return aryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		 
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//装载view
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.my_table_row, null);
        
        TextView txtIndex = (TextView)view.findViewById(R.id.tbrow_txtIndex);
        final  CheckBox ckMulti = (CheckBox)view.findViewById(R.id.tbrow_ckSelect);
        Spinner spOptions = (Spinner)view.findViewById(R.id.tbrow_spOptions);
        int num = position+1;
        txtIndex.setText("NO."+ num);
        String strMul= aryList.get(position).get("mul");

        ckMulti.setChecked(strMul.equals("1"));
        spOptions.setSelection(Integer.parseInt( aryList.get(position).get("opt")) -1 );
        
        final int pos = position;
        ckMulti.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String isCk = ckMulti.isChecked()?"1":"0";
				Map<String,String> map = aryList.get(pos);
				map.put("mul", isCk);
				System.out.println(aryList);
			}
		});
        
        
 

        spOptions.setOnItemSelectedListener(new OnItemSelectedListener() {
			//当选中某一个数据项时触发该方法
            /*
             * parent接收的是被选择的数据项所属的 Spinner对象，
             * view参数接收的是显示被选择的数据项的TextView对象
             * position接收的是被选择的数据项在适配器中的位置
             * id被选择的数据项的行号
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                
            	Integer val = position+1;
            	aryList.get(pos).put("opt", val.toString());
            	System.out.println(aryList);
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub    
            }
		});

		return view;
	}

}
