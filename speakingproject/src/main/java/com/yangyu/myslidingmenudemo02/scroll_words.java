package com.yangyu.myslidingmenudemo02;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import android.app.ActionBar.LayoutParams;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;



public class scroll_words extends Fragment{
	
	private List<Word> offline_words = null;
	static Hashtable<String, ArrayList<String>> wordIndex = null;
	static final String[] test_word = {"但(C)", "在(P)", "他(N)", "這(DET)", "也(ADV)",
										"而(C)", "讓(Vt)", "並(C)", "我(N)", "還(ADV)",
										"不過(C)", "她(N)", "就(ADV)", "有(Vt)", "一(DET)"};
	static final int total_words_num = 15;
	static int level = -1, loc = -1;
	static Word cur_word = null;
	static String result = "@";
	
	Fragment f = this;
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
			return reloadWords(inflater,  container,  savedInstanceState);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;  
    }
	public View reloadWords(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) throws InterruptedException, IOException, ExecutionException{
		offline_words = getSystemWords();
		final LinearLayout ll = new LinearLayout(inflater.getContext());
	    ll.setOrientation(LinearLayout.VERTICAL); 

	    
	    // for Animation
	    AnimationSet animationSet = new AnimationSet(true);
	    AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
	    alphaAnimation.setDuration(1000);
 	    animationSet.addAnimation(alphaAnimation);
		
        /*get screen size*/
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int displayheight = displaymetrics.heightPixels;
        
        
        int rowpixels = (int)((displayheight - MainActivity.sentence.getHeight() - MainActivity.bottommenu_height))/5;
        
        begin:for(int i = 0 ; i<= 5  ;i++){
        	LinearLayout subLayout = new LinearLayout(inflater.getContext());
        	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 115);
        	subLayout.setLayoutParams(lp);
        	subLayout.setOrientation(LinearLayout.HORIZONTAL);
        	for(int j = 0 ; j< 3 ; j++){
	        	Button newbtn = new Button(inflater.getContext());
	        	
	        	
	        	/* set button words */
	        	//newbtn.setText(Integer.toString(i*3+j));
	        	
	        	
	        	if (i*3+j < 15 && i*3+j < offline_words.size()){
	        		newbtn.setText(offline_words.get(i*3+j).getWord());
	        		newbtn.setTextColor(Color.WHITE);
	        		newbtn.setTextSize(20);
	        	}
	        	else 
	        		newbtn.setText("哈哈");
	        	
	        	LinearLayout.LayoutParams btnlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT , 1);
	        	if(5 == i){				/*to fit imagebutton to each devices*/
	        		ImageButton imgButton = new ImageButton(subLayout.getContext()); 
	        		if(j == 1){
		        		imgButton.setBackgroundResource(R.drawable.left);
		        		subLayout.addView(imgButton , btnlp);
		        		TextView t = new TextView(subLayout.getContext());
		        		t.setText("Page 1");
		        		t.setTextSize(20);
		        		t.setGravity(Gravity.CENTER);
		        		subLayout.addView(t , btnlp);
	        		}
	        		else if(j == 2){
	        			imgButton.setBackgroundResource(R.drawable.right);
		        		subLayout.addView(imgButton , btnlp);
	        		}
		       
	        	}
	        	else{
	        		int margin_pixel = get_dp_To_pixel(5);
	        		btnlp.setMargins(margin_pixel, margin_pixel, margin_pixel, margin_pixel);
		        	subLayout.addView(newbtn , btnlp);
		        	newbtn.setBackgroundResource(R.drawable.textbutton);
		        	newbtn.setOnClickListener(new View.OnClickListener() {
		    			@Override
		    			public void onClick(View v) {
		    				MainActivity.stopIconAnimation();
		    				ll.removeAllViews();
		    				Button b = (Button)v;
		    				String input = b.getText().toString();
		    				//System.out.println("~~~:"+input);
		    				MainActivity.addtoSentence(input);
		    				
		    				//to-do savewords
		    				for (Word w : offline_words) {
		    					if (w.getWord().equals(input)) {
		    						loc = w.getLoc();
		    						cur_word = w; break;
		    					}
		    				}
		    				Log.v("Press button : ",cur_word.getWord());
		    				
		    				MainActivity.sentenceStructure.addWord(cur_word, MainActivity.sentence.getSelectionEnd());
		    				
		    				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		    		        ft.remove(f).commit();

		    				getFragmentManager().beginTransaction().replace(R.id.fragment1, new scroll_words()).commit();
		    		        	
		    			}
		    		});
	        	}
        	}
        	LinearLayout.LayoutParams layoutlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, rowpixels , 1);
        	subLayout.startAnimation(animationSet);
        	ll.addView(subLayout , layoutlp);
        }
        return ll;
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
private List<Word> getSystemWords() {
		
		if (wordIndex == null) {
			try {
				InputStream is = this.getResources().openRawResource(R.raw.wordindex);
				//FileInputStream fis = new FileInputStream("sentence.trie");
				ObjectInputStream ois = new ObjectInputStream(is);
				wordIndex = (Hashtable<String, ArrayList<String>>) ois.readObject();
				ois.close();
			} catch (Exception e) {
				System.out.println("Error : file not found");
				e.printStackTrace();
			}
		}
		
		List<Word> tmp = new ArrayList<Word>();
		if(MainActivity.sentence.getText().toString().equals("你想說什麼?") || MainActivity.sentence.getText().toString().equals("")){
			level = 1;
			
			Cursor cursor = MainActivity.db.rawQuery("select * from wordLevel1", null);
		    int rows_num = cursor.getCount();//取得資料表列數
		    if(rows_num != 0) {
			    cursor.moveToFirst();//將指標移至第一筆資料
			    for(int i=0; i<rows_num; i++) {
			    	System.out.println(cursor.getColumnCount());
				    Word w = new Word(cursor.getString(0), cursor.getString(1), cursor.getInt(2), i+1);
				    tmp.add(w);
				    cursor.moveToNext();//將指標移至下一筆資料
			    }
		    }
		    cursor.close(); //關閉Cursor
		}
		else if (level < 4) {
			String sql = "select * from wordLevel" + (level+1) 
					+ " where rowid <= " + (loc*30) + " and rowid > " + ((loc-1)*30);
			Log.v("query", sql); level++;
			Cursor cursor = MainActivity.db.rawQuery(sql, null);
		    int rows_num = cursor.getCount();//取得資料表列數
		    if(rows_num != 0) {
			    cursor.moveToFirst();//將指標移至第一筆資料
			    for(int i=0; i<rows_num; i++) {
				    Word w = new Word(cursor.getString(0), cursor.getString(1), cursor.getInt(2), ((loc-1)*30)+i+1);
				    tmp.add(w);
				    cursor.moveToNext();//將指標移至下一筆資料
			    }
		    }
		    cursor.close(); //關閉Cursor
		}
		else {
			String pattern = cur_word.getWord() + "(" + cur_word.getPos() + ")";
			if (wordIndex.containsKey(pattern)) {
				
				/************************* 記得改!!!!!!!!!!!!!!!!!!!!!!!!!!! *************************/
				
				String position = wordIndex.get(pattern).get(0);
				
				/************************* 記得改!!!!!!!!!!!!!!!!!!!!!!!!!!! *************************/
				
				int cur_level = Integer.valueOf(position.split("_")[0]);
				int cur_loc = Integer.valueOf(position.split("_")[1]);
				
				String sql = "select * from wordLevel" + (cur_level+1) 
						+ " where rowid <= " + (cur_loc*30) + " and rowid > " + ((cur_loc-1)*30);
				Log.v("query", sql); level = cur_level+1;
				Cursor cursor = MainActivity.db.rawQuery(sql, null);
			    int rows_num = cursor.getCount();//取得資料表列數
			    if(rows_num != 0) {
				    cursor.moveToFirst();//將指標移至第一筆資料
				    for(int i=0; i<rows_num; i++) {
					    Word w = new Word(cursor.getString(0), cursor.getString(1), cursor.getInt(2), ((cur_loc-1)*30)+i+1);
					    tmp.add(w);
					    cursor.moveToNext();//將指標移至下一筆資料
				    }
			    }
			    cursor.close(); //關閉Cursor
			}
			else {
				Log.v("QQ", "fail");
			}
		}
		
		return tmp;
		
	}
	
	private class ComparatorWords implements Comparator<Word> {
		
		@Override
		public int compare(Word w1, Word w2) {
			return w1.getScore().compareTo(w2.getScore());
		}
	}
	
	

}
