package com.prance.teacher.lib.sunvote.features;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prance.teacher.lib.sunvote.R;

public class NavigationView extends RelativeLayout {

	public NavigationView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	 private ImageView iconView;
	 private TextView titleView;
	 private TextView rightView;
	    public NavigationView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        View view = LayoutInflater.from(context).inflate(R.layout.nav_bar, this, true);
	        iconView = (ImageView) view.findViewById(R.id.iv_nav_icon);
	        titleView = (TextView)view.findViewById(R.id.tv_nav_title);
	        rightView = (TextView)view.findViewById(R.id.tv_nav_right);
//	        backView.setOnClickListener(this);
//	        titleView = (TextView) view.findViewById(R.id.tv_nav_title);
//	        rightView = (ImageView) view.findViewById(R.id.iv_nav_right);
//	        rightView.setOnClickListener(this);
	    }
	    
	    public ImageView getIconView() {
	        return iconView;
	    }
	    
	    /**
	     * 获取标题控件
	     * @return
	     */
	    public TextView getTitleView() {
	        return titleView;
	    }
	    
	    /**
	     * 设置标题
	     * @param title
	     */
	    public void setTitle(String title){
	        titleView.setText(title);
	    }
	    
	    /**
	     * 获取右侧按钮,默认不显示
	     * @return
	     */
	    public TextView getRightView() {
	        return rightView;
	    }
	    
	    public void SetRightViewText(String text){
	    	rightView.setText(text);
	    	
	    }

}
