package com.yangyu.myslidingmenudemo02;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class scene_select extends Fragment{
	int OPEN = 1;
	int CLOSE = 0;
	int[] cateFlag = new int[8];
	LinearLayout cate_layout;
	LinearLayout ll;
	int dps;
	int rowpixels;
	ScrollView sv;
	public static String selectedCategory1 = "全部";
	public static String selectedCategory2 = "全";
	Button cate00 , cate01 ,cate02 ,cate03 ,cate04 ,cate05 ,cate06;
	
	final static String maincategories[] = {"全","生活","交通","醫療","運動","食","休閒"};
	final static String subcategories[][] = { { "" },
		{ "全", "問候", "心情" ,"家事", "衣著","節慶" }, { "全", "問路","交通工具","地點" },
		{ "全", "身體需求", "健康狀況"},
		{ "全", "球類", "極限運動", "健身","水上活動","格鬥","其他" },
		{ "全", "飲品", "點心", "水果", "速食","西餐","小吃","蔬菜","海鮮","肉類","蛋奶","香料" ,"家常菜"},
		{ "全", "3C", "運動", "音樂" , "健康" , "電影" },
		{ "全", "食","衣","住","行","育","樂","禮貌","身" },
		{ "全","地域", "方向" },
		{ "全", "動物", "植物", "環境" }
	};
	
	@Override  
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return returnSceneUI(inflater,  container,  savedInstanceState);  
	}
	public View returnSceneUI(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState){
		int count = 0;
		ll = new LinearLayout(inflater.getContext());
	    ll.setOrientation(LinearLayout.VERTICAL); 
	    //get screen size
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int displayheight = displaymetrics.heightPixels;
        
        
        rowpixels = (int)((displayheight - MainActivity.sentence.getHeight() - MainActivity.bottommenu_height))/4;
	
        
        
        begin:for(int i = 0 ; i< 4  ;i++){
        	LinearLayout subLayout = new LinearLayout(inflater.getContext());
        	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 115);
        	subLayout.setLayoutParams(lp);
        	subLayout.setOrientation(LinearLayout.HORIZONTAL);
        	for(int j = 0 ; j< 2 ; j++){
	        	if(j == 1 && i == 0)  
	        		break;
	        	
        		Button newbtn = new Button(inflater.getContext());
	        	
        		String category = maincategories[count];
        		count++;
	        	newbtn.setText(category);
	        	newbtn.setBackgroundResource(R.drawable.simple_button2);
	        	newbtn.setTextColor(Color.WHITE);
	        	newbtn.setTextSize(25);
	        	
	        	LinearLayout.LayoutParams btnlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT , 1);
	        	int margin_pixel = get_dp_To_pixel(5);
        		btnlp.setMargins(margin_pixel, margin_pixel, margin_pixel, margin_pixel);
		        subLayout.addView(newbtn , btnlp);
		        if(j == 0 && i == 0){
		        	  newbtn.setOnClickListener(new View.OnClickListener() {
			    			@Override
			    			public void onClick(View v) {
			    				selectedCategory1 = "全部";
			    				selectedCategory2 = "全";
			    				Setting.setSelectScene();
			    			}
		        	  });
		        }
		        else{
			        newbtn.setOnClickListener(new View.OnClickListener() {
			    			@Override
			    			public void onClick(View v) {
			    				Button b = (Button)v;
			    				String chosenMainCategory = b.getText().toString();
			    				selectedCategory1 = chosenMainCategory;
			    				Setting.setSelectScene();
			    				final int pos = getPositionOfCate(chosenMainCategory);
			    				//System.out.println("~~len :" + subcategories[count].length + " count = " + count);
			    				View view = getActivity().getLayoutInflater().inflate( R.layout.second_scene, null );
			    				
			    				TextView t = (TextView)view.findViewById(R.id.second_scene_title);
			    				t.setText(chosenMainCategory);
			    				
			    				AlertDialog.Builder dialog = new AlertDialog.Builder( getActivity() );
			    				final AlertDialog alert = dialog.create();
			    				 begin:for(int i = 0 ; i< (subcategories[pos].length-1) / 4 + 1  ;i++){
			    			        	LinearLayout subLayout = new LinearLayout(view.getContext());
			    			        	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 115);
			    			        	subLayout.setLayoutParams(lp);
			    			        	subLayout.setOrientation(LinearLayout.HORIZONTAL);
			    			        	for(int j = 0 ; j< 4 ; j++){
	
			    				        	
			    			        		final Button newbtn = new Button(view.getContext());
			    			        		
			    			        		if(i*4 + j < subcategories[pos].length){
				    			        		String subcategory = subcategories[pos][i*4 + j];
				    				        	newbtn.setText(subcategory);
			    			        		}
			    				        	
			    				        	if((i+j)%2 == 0)
			    								newbtn.setBackgroundColor(Color.WHITE);
			    							else{
			    								int d = getResources().getColor(R.color.simple_blue);
			    								newbtn.setBackgroundColor(d);
			    							}
	
			    				        	
			    				        	LinearLayout.LayoutParams btnlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT , 1);
			    				        	
			    					        subLayout.addView(newbtn , btnlp);
			    					        newbtn.setOnClickListener(new View.OnClickListener() {
			    					    			@Override
			    					    			public void onClick(View v) {
			    					    				selectedCategory2 = newbtn.getText().toString();
			    					    				Setting.setSelectScene();
			    					    				alert.cancel();
			    					    			}
			    					    		});
			    				        	
			    			        	}
			    			        	LinearLayout.LayoutParams layoutlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, rowpixels/3*2 , 1);
			    			        	((ViewGroup) view).addView(subLayout , layoutlp);
			    			        }
			    				
			    				alert.setView(view);
			    				alert.show();  
			    			}
	
							private int getPositionOfCate(String chosenMainCategory) {
								for(int i = 0 ; i <  maincategories.length ; i++){
									if(chosenMainCategory.equals(maincategories[i])){
										return i;
									}
								}
								return -1;
							}
			    		});
		        }
	        	
        	}
        	LinearLayout.LayoutParams layoutlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, rowpixels , 1);
        	ll.addView(subLayout , layoutlp);
        }
        AnimationSet animationSet = new AnimationSet(true);
	    AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
	    alphaAnimation.setDuration(600);
 	    animationSet.addAnimation(alphaAnimation);
 	    ll.startAnimation(animationSet);
        return ll;
	}
	/*@Override  
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = (View)inflater.inflate(R.layout.scene_select_menu, null, false);
		sv = (ScrollView)view.findViewById(R.id.scroll_scene);
		 cate00 = (Button)view.findViewById(R.id.cate00);
		 cate01 = (Button)view.findViewById(R.id.cate01);
		 cate02 = (Button)view.findViewById(R.id.cate02);
		 cate03 = (Button)view.findViewById(R.id.cate03);
		 cate04 = (Button)view.findViewById(R.id.cate04);
		 cate05 = (Button)view.findViewById(R.id.cate05);
		 cate06 = (Button)view.findViewById(R.id.cate06);
		
		for(int i = 0 ; i< 7 ; i++){
			cateFlag[i] = CLOSE;
		}
   
        //get screen size
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int displayheight = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        float density = displaymetrics.density;
        
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, r.getDisplayMetrics());
        
        dps = (int) (((displayheight- px )/7));
        cate00.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, dps));
        cate01.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, dps));
        cate02.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, dps));
        cate03.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, dps));
        cate04.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, dps));
        cate05.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, dps));
        cate06.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, dps));
        
        for(int i = 1 ;i <=6 ; i++){
        	opencategory(i ,view);
        	
        }
        cate_layout = (LinearLayout) view.findViewById(R.id.cate01_open);
        cate_layout.setVisibility(LinearLayout.GONE);
        cate_layout = (LinearLayout) view.findViewById(R.id.cate02_open);
        cate_layout.setVisibility(LinearLayout.GONE);
        cate_layout = (LinearLayout) view.findViewById(R.id.cate03_open);
        cate_layout.setVisibility(LinearLayout.GONE);
        cate_layout = (LinearLayout) view.findViewById(R.id.cate04_open);
        cate_layout.setVisibility(LinearLayout.GONE);
        cate_layout = (LinearLayout) view.findViewById(R.id.cate05_open);
        cate_layout.setVisibility(LinearLayout.GONE);
        cate_layout = (LinearLayout) view.findViewById(R.id.cate06_open);
        
        cate_layout.setVisibility(LinearLayout.GONE);
        cate00.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Button tmpbutton = (Button)v;
				selectedCategory1 = tmpbutton.getText().toString();
				selectedCategory2 = "��";
				Setting.setSelectScene();
			}
        });
        cate01.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Button tmpbutton = (Button)v;
				selectedCategory1 = tmpbutton.getText().toString();
				selectedCategory2 = "";
				Setting.setSelectScene();
				cate_layout = (LinearLayout) view.findViewById(R.id.cate01_open);
				if(cateFlag[1] == CLOSE){
					cate_layout.setVisibility(LinearLayout.VISIBLE);
					cateFlag[1] = OPEN;
				}
				else{
					cate_layout.setVisibility(LinearLayout.GONE);
					cateFlag[1] = CLOSE;
				}
			}
        });
        cate02.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Button tmpbutton = (Button)v;
				selectedCategory1 = tmpbutton.getText().toString();
				selectedCategory2 = "";
				Setting.setSelectScene();
				cate_layout = (LinearLayout) view.findViewById(R.id.cate02_open);
				if(cateFlag[2] == CLOSE){
					cate_layout.setVisibility(LinearLayout.VISIBLE);
					cateFlag[2] = OPEN;
				}
				else{
					cate_layout.setVisibility(LinearLayout.GONE);
					cateFlag[2] = CLOSE;
				}
			}
        });
        cate03.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Button tmpbutton = (Button)v;
				selectedCategory1 = tmpbutton.getText().toString();
				selectedCategory2 = "";
				Setting.setSelectScene();
				cate_layout = (LinearLayout) view.findViewById(R.id.cate03_open);
				if(cateFlag[3] == CLOSE){
					cate_layout.setVisibility(LinearLayout.VISIBLE);
					cateFlag[3] = OPEN;
				}
				else{
					cate_layout.setVisibility(LinearLayout.GONE);
					cateFlag[3] = CLOSE;
				}
			}
        });
        cate04.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Button tmpbutton = (Button)v;
				selectedCategory1 = tmpbutton.getText().toString();
				selectedCategory2 = "";
				Setting.setSelectScene();
				cate_layout = (LinearLayout) view.findViewById(R.id.cate04_open);
				if(cateFlag[4] == CLOSE){
					cate_layout.setVisibility(LinearLayout.VISIBLE);
					cateFlag[4] = OPEN;
				}
				else{
					cate_layout.setVisibility(LinearLayout.GONE);
					cateFlag[4] = CLOSE;
				}
			}
        });
        cate05.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Button tmpbutton = (Button)v;
				selectedCategory1 = tmpbutton.getText().toString();
				selectedCategory2 = "";
				Setting.setSelectScene();
				cate_layout = (LinearLayout) view.findViewById(R.id.cate05_open);
				if(cateFlag[5] == CLOSE){
					cate_layout.setVisibility(LinearLayout.VISIBLE);
					cateFlag[5] = OPEN;
				
				}
				else{
					cate_layout.setVisibility(LinearLayout.GONE);
					cateFlag[5] = CLOSE;
				}
			}
        });
        cate06.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Button tmpbutton = (Button)v;
				selectedCategory1 = tmpbutton.getText().toString();
				selectedCategory2 = "";
				Setting.setSelectScene();
				cate_layout = (LinearLayout) view.findViewById(R.id.cate06_open);
				cate_layout.setVisibility(LinearLayout.VISIBLE);
				
				if(cateFlag[6] == CLOSE){
					cate_layout.setVisibility(LinearLayout.VISIBLE);
					cateFlag[6] = OPEN;
					
				}
				else{
					cate_layout.setVisibility(LinearLayout.GONE);
					cateFlag[6] = CLOSE;
				}
				
			}
        });
        
       
		return view;
    }*/
	protected void opencategory(int select_cate , View view){
		String tag = Integer.toString(select_cate);
		LinearLayout ll = (LinearLayout) view.findViewWithTag(tag);
		LinearLayout newlayout = null;
		if(cateFlag[select_cate] == CLOSE){
			for(int i = 0; i < (subcategories[select_cate].length -1) / 4 + 1 ; i++){
				LinearLayout.LayoutParams newlayoutPar = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dps);
				newlayout = new LinearLayout(view.getContext());
				newlayout.setTag(select_cate);
				newlayout.setOrientation(LinearLayout.HORIZONTAL);	
				ll.addView(newlayout,newlayoutPar);
				for(int j = 0 ; j< 4 ; j++){
					Button newbtn = new Button(view.getContext());
					if((i+j)%2 == 0)
						newbtn.setBackgroundColor(Color.WHITE);
					else
						newbtn.setBackgroundColor(Color.GRAY);
					if(i*4 + j < subcategories[select_cate].length)
						newbtn.setText(subcategories[select_cate][i*4 + j]);
					LinearLayout.LayoutParams btnlp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT , 1);
					newlayout.addView(newbtn , btnlp );
					newbtn.setOnClickListener(new View.OnClickListener() {
		    			@Override
		    			public void onClick(View v) {
		    				Button tmpbutton = (Button)v;
		    				selectedCategory2 = tmpbutton.getText().toString();
		    				Setting.setSelectScene();
		    			}
		    		});
				}
			}
		}
	}
	private int get_dp_To_pixel(int i ) {
		Resources r = getActivity().getResources();
		int px = (int) TypedValue.applyDimension(
		        TypedValue.COMPLEX_UNIT_DIP,
		        i, 
		        r.getDisplayMetrics()
		);
		return px;
	}
}
