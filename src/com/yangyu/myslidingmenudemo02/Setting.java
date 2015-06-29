package com.yangyu.myslidingmenudemo02;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Setting extends FragmentActivity{
	private ImageView return_icon;
	private ImageView scene;
	private ImageView tools;
	int SCENE = 0;
	int TOOLS = 1;
	int selectedMode = SCENE;
	LinearLayout Toptext;
	static TextView Scenetext;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting);
		Toptext = (LinearLayout)findViewById(R.id.TopSceneText);
		Scenetext = (TextView) findViewById(R.id.Scenetext);
		return_icon = (ImageView)findViewById(R.id.return_icon);
		scene = (ImageView)findViewById(R.id.scene);
		tools = (ImageView)findViewById(R.id.tools);
		//scene.setBackgroundDrawable(null);
		scene.setBackgroundColor(Color.WHITE);
		return_icon.setBackgroundDrawable(null);
		tools.setBackgroundDrawable(null);
		
		 LinearLayout menu = (LinearLayout)this.findViewById(R.id.bottom_row);
	     RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
	    		    RelativeLayout.LayoutParams.WRAP_CONTENT, 
	    		    MainActivity.bottommenu_height*3/4);
	     lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	     menu.setLayoutParams(lay);
		return_icon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MainActivity.saveCategory(scene_select.selectedCategory1 , scene_select.selectedCategory2);
		        Setting.this.finish();
			}
		});
		scene.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(selectedMode == TOOLS){
					scene.setBackgroundColor(Color.WHITE);
					tools.setBackgroundDrawable(null);
					selectedMode = SCENE;
					setFragment(selectedMode);
				}
			}
		});
		tools.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(selectedMode == SCENE){
					scene.setBackgroundDrawable(null);
					//tools.setBackgroundDrawable(null);
					tools.setBackgroundColor(Color.WHITE);
					selectedMode = TOOLS;
					setFragment(selectedMode);
				}
			}
		});		
		
		getSupportFragmentManager().beginTransaction().replace(R.id.scene_and_advancesetting, new scene_select()).commit();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		setSelectScene();
		/*Scenetext.setTextColor(Color.WHITE);
		Toptext.addView(Scenetext, lp);
		Toptext.setBackgroundColor(R.color.dark_gray);*/
	
	}
	private void setFragment(int selectedMode) {
		if(selectedMode == SCENE){
			Scenetext = (TextView) findViewById(R.id.Scenetext);
			getSupportFragmentManager().beginTransaction().replace(R.id.scene_and_advancesetting, new scene_select()).commit();	
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			setSelectScene();
			/*Scenetext.setTextColor(Color.WHITE);
			Toptext.addView(Scenetext, lp);
			Toptext.setBackgroundColor(R.color.dark_gray);*/
		}
		else{
			getSupportFragmentManager().beginTransaction().replace(R.id.scene_and_advancesetting, new advance_setting()).commit();
			Scenetext.setText("進階設定 :");
		}
		
	}
	public static void setSelectScene(){
		Scenetext.setText("選擇情境為 : \n" + scene_select.selectedCategory1 + " / " + scene_select.selectedCategory2);
	}
}
