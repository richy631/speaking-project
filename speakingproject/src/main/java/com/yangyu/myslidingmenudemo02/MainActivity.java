package com.yangyu.myslidingmenudemo02;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends FragmentActivity {
	public static final String PREFS_NAME = "MyPrefsFile";
	private SlidingMenu menu;
	public static ImageView setting;
	
	private ImageView voice;
	private ImageView keypad;
	private ImageView back;
	private ImageView menu_icon;
	
	private boolean keyboard = false;
	public static Sentence sentenceStructure;
	static EditText sentence;
	public static int bottommenu_height;
	public static Speaker speaker;
	public static SQLiteDatabase db;
	static LinearLayout tmplayout ;
	private static String category1;
	private static String category2;
	
	ListView lv; /*for slidingmenu*/
	
	
	/*Setting var*/
	public static int pitch_pos;
	public static int speed_pos;
	public static int speaker_pos;
	public static int speaker_style_pos;
	
	
	/*Testing sliding menu sentences*/
	ArrayList<Map<String,Object>> list;
	int pos_Sentence;
	String target_content;
	TextView description;
	public static String[] menu_sentences = new String[6];
	private SimpleAdapter adapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DBManager dbHelper;
		dbHelper = new DBManager(this);
		dbHelper.openDatabase();
		dbHelper.closeDatabase();

		speaker = new Speaker(this);

		db = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);
	    /*  if null -> new  */
		sentenceStructure = new Sentence();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.content_frame);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		sentence = (EditText) findViewById(R.id.editText);
		sentence.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent m) {
				Layout layout = ((EditText) v).getLayout();
				float x = m.getX() + sentence.getScrollX();
				int offset = layout.getOffsetForHorizontal(0, x);
				if (offset > 0) {
					if (x > layout.getLineMax(0))
						sentence.setSelection(offset);
					else
						sentence.setSelection(offset - 1);
				}
				return true;
			}
		});
		sentence.setSelection(sentence.length());

		setting = (ImageView)findViewById(R.id.setting);
		voice = (ImageView)findViewById(R.id.voice);
		keypad = (ImageView)findViewById(R.id.keypad);
		back = (ImageView)findViewById(R.id.back);
		tmplayout = (LinearLayout) findViewById(R.id.fragment1);
		menu_icon = (ImageView)findViewById(R.id.menu_icon);
		setting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				stopIconAnimation();
				intent.setClass(MainActivity.this, Setting.class);
				startActivity(intent);
			}
		});
		voice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				speaker.speaking(sentence.getText().toString());
			}
		});
		keypad.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if(!keyboard){
					imm.showSoftInput(sentence, InputMethodManager.SHOW_IMPLICIT);
					keyboard = true;
				}
				else{
					imm.hideSoftInputFromWindow(sentence.getWindowToken(), 0);
					keyboard = false;
				}
			}
		});
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				deleteonSentence();
			}
		});
		initmenuSentences();
		initSlidingMenu();
		initScroll_words();
		initLayerHeight();
		initIconAnimation();
		initSetting();

		menu_icon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				menu.showMenu(true);
			}
		});
	}
	
	@Override
    protected void onStop(){
       super.onStop();

      SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
      SharedPreferences.Editor editor = settings.edit();
      editor.putInt("pitch_pos", pitch_pos);
      editor.putInt("speed_pos", speed_pos);
      editor.putInt("speaker_pos", speaker_pos);
      editor.putInt("speaker_style_pos", speaker_style_pos);

      // Commit the edits!
      editor.commit();
    }
	
	@Override
	protected void onResumeFragments(){
		super.onResumeFragments();
		initScroll_words();	     
	}
	/*@Override
	protected void onResume(){
		super.onResume();
		initScroll_words();	
	}*/


	private void initmenuSentences() {
		/*menu_sentences{"救命","我想喝水","我想上廁所","我肚子餓了","你今天過得怎樣","你好嗎"}*/
    	SharedPreferences menu_settings = getSharedPreferences("menu_sentences", 0);
    	menu_sentences[0] = menu_settings.getString("s0", "救命");
    	menu_sentences[1] = menu_settings.getString("s1", "我想喝水");
    	menu_sentences[2] = menu_settings.getString("s2", "我想上廁所");
    	menu_sentences[3] = menu_settings.getString("s3", "我肚子餓了");
    	menu_sentences[4] = menu_settings.getString("s4", "你今天過得怎樣");
    	menu_sentences[5] = menu_settings.getString("s5", "你好嗎");
	}

	private void initSetting() {
		// TODO Auto-generated method stub
		 // load setting
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    boolean is_Setted = settings.getBoolean("UserSetting", false);
	    //if(is_Setted == true){
	    	pitch_pos = settings.getInt("pitch_pos", 2);
	    	speed_pos =  settings.getInt("speed_pos", 0);
	    	speaker_pos = settings.getInt("speaker_pos", 0);
	    	speaker_style_pos = settings.getInt("speaker_style_pos", 0);
	    	float fPitch = 1.0f;
			switch(pitch_pos)
        	{
        		case 0:	fPitch=0.5f;break;
        		case 1:	fPitch=0.75f;break;
        		case 2:	fPitch=1.0f;break;
        		case 3: fPitch=1.5f;	break;
        		case 4: fPitch=2.0f;	break;
        	}
			
	    	float fSpeed = 0.5f;
	    	switch(speed_pos)
        	{
        	case 0:	fSpeed=0.5f;break;
        	case 1:	fSpeed=0.75f;break;
        	case 2:	fSpeed=1.0f;break;
        	case 3: fSpeed=1.5f;	break;
        	case 4: fSpeed=2.0f;	break;
        	}
	    	Speaker.mTts.setPitch((float)(speaker_pos+3));
	    	Speaker.mTts.speak("", TextToSpeech.QUEUE_FLUSH, null);
	    	Speaker.mTts.setPitch(fPitch);
	    	Speaker.mTts.speak("", TextToSpeech.QUEUE_FLUSH, null);
	    	Speaker.mTts.setSpeechRate(fSpeed);
	    	Speaker.mTts.speak("", TextToSpeech.QUEUE_FLUSH, null);
	    	Speaker.mTts.setSpeechRate((float)(speaker_style_pos+3));
	    	Speaker.mTts.speak("", TextToSpeech.QUEUE_FLUSH, null);    	
	   // }
	}

	public static void stopIconAnimation(){
		setting.setAnimation(null);
	}
	
	private void initIconAnimation() {
	    float viewleft = setting.getLeft();
	    float viewtop = setting.getRight();
	    final TranslateAnimation alphaAnimation2 = new TranslateAnimation(viewleft-10, viewleft+10, viewtop, viewtop);  
	    alphaAnimation2.setDuration(100);  
	    alphaAnimation2.setRepeatMode(Animation.REVERSE);  

	    final TranslateAnimation alphaAnimation = new TranslateAnimation(viewleft, viewleft, viewtop, viewtop);  
	    alphaAnimation.setDuration(1500);
	    
	    alphaAnimation2.setAnimationListener(new AnimationListener() {
	        @Override
	        public void onAnimationEnd(Animation animation) {
	        	setting.startAnimation(alphaAnimation);

	        }

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
	    });
	    
	    alphaAnimation.setAnimationListener(new AnimationListener() {
	        @Override
	        public void onAnimationEnd(Animation animation) {
	        	setting.startAnimation(alphaAnimation2);

	        }

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
	    });
	    
	    setting.setAnimation(alphaAnimation);
	    alphaAnimation.start();
		
	}

	private void initLayerHeight() {
		// TODO Auto-generated method stub
		 DisplayMetrics displaymetrics = new DisplayMetrics();
	     getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	     int displayheight = displaymetrics.heightPixels;

	     bottommenu_height = displayheight/10;
	     LinearLayout menu = (LinearLayout)findViewById(R.id.bottom_menu);
	     RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
	    		    RelativeLayout.LayoutParams.WRAP_CONTENT, 
	    		    bottommenu_height);
	     lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	     menu.setLayoutParams(lay);

	}

	public void initScroll_words() {
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, new scroll_words()).commit();
	}
	
	public static void removeFragmentViews(){
		tmplayout.removeAllViews();
	}


	private void initSlidingMenu() {
		
		menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.menu_frame);	
		
		description = (TextView) menu.findViewById(R.id.description);
		
		list = new ArrayList<Map<String,Object>>();
		for(int i=0; i<menu_sentences.length; i++){
			 Map<String,Object> item = new HashMap<String,Object>();
			 item.put("Sentence", menu_sentences[i]);
			 item.put("Voice", R.drawable.voice);
			 list.add( item );
		}
		
		
		//新增SimpleAdapter
		 adapter = new SimpleAdapter( 
		 this, 
		 list,
		 R.layout.setences_in_menu,
		 new String[] { "Voice","Sentence" },
		 new int[] { R.id.imageView1, R.id.textView1} );
		 
		 final OnItemClickListener menuSentences_Listener = new OnItemClickListener(){
		       @Override
		       public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		    	   
		          Toast.makeText(getApplicationContext(),    "你選擇的是"+menu_sentences[position], Toast.LENGTH_SHORT).show();
		       }
		 };
		
		 lv = (ListView) menu.findViewById(R.id.listView);
		 lv.setAdapter(adapter);
		 lv.setOnItemClickListener(menuSentences_Listener);
		 final OnItemClickListener ModyfiyListener = new OnItemClickListener(){
		       @Override
		       public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		    	   pos_Sentence = position;
		    	   target_content = menu_sentences[position];
		    	   Toast.makeText(getApplicationContext(),    "你選擇的是"+menu_sentences[position], Toast.LENGTH_SHORT).show();
		    	   speaker.speaking(menu_sentences[position].toString());
		       }
		 };
		final Button modifybutton = (Button)menu.findViewById(R.id.modifyButton);
		final Button modifycancel = (Button)menu.findViewById(R.id.modify_cancel);
		final Button modifyconfirm = (Button)menu.findViewById(R.id.modify_confirm);
		final Button save_cancel = (Button)menu.findViewById(R.id.save_cancel);
		final Button save_confirm = (Button)menu.findViewById(R.id.save_confirm);
		final Button port_in = (Button)menu.findViewById(R.id.por_tin);
		final EditText target  = (EditText)menu.findViewById(R.id.modify_target);
		final LinearLayout modifylayer = (LinearLayout) menu.findViewById(R.id.modify_layer);
		final RelativeLayout rl = (RelativeLayout) menu.findViewById(R.id.modify_page);
		modifybutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				lv.setAdapter(new ArrayAdapter<String>(menu.getContext(),
		                R.layout.adapter, menu_sentences));
				lv.setOnItemClickListener(ModyfiyListener);
				lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				modifybutton.setVisibility(View.GONE);
				modifylayer.setVisibility(View.VISIBLE);
				FadeInAnimation(modifylayer);
				FadeInAnimation(lv);
				description.setText("選擇修改的語句 ：");
			}
		});
		modifycancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 lv = (ListView) menu.findViewById(R.id.listView);
				 lv.setAdapter(adapter);
				 lv.setOnItemClickListener(menuSentences_Listener);
				 description.setText("便利語句 ：");
				 modifylayer.setVisibility(View.GONE);
				 modifybutton.setVisibility(View.VISIBLE);
				 FadeInAnimation(modifybutton);
				 FadeInAnimation(lv);
				 
			}
		});
		modifyconfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				lv.setVisibility(View.GONE);
				modifylayer.setVisibility(View.GONE);
				rl.setVisibility(View.VISIBLE);
				FadeInAnimation(rl);
				description.setText("更改內容 ：");
				target.setText(target_content);
				target.requestFocus();
			}
		});
		save_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				rl.setVisibility(View.GONE);
				lv.setVisibility(View.VISIBLE);
				FadeInAnimation(lv);
				 lv = (ListView) menu.findViewById(R.id.listView);
				 lv.setAdapter(adapter);
				 lv.setOnItemClickListener(menuSentences_Listener);
				 description.setText("便利語句 ：");
				 modifylayer.setVisibility(View.GONE);
				 modifybutton.setVisibility(View.VISIBLE);
				 FadeInAnimation(modifybutton);
			}
		});
		save_confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 menu_sentences[pos_Sentence] = target.getText().toString(); 
				 Toast.makeText(getApplicationContext(),    "儲存成功!", Toast.LENGTH_SHORT).show();
				 savemenu_sentences();
				 list.remove(pos_Sentence);
				 Map<String,Object> item = new HashMap<String,Object>();
				 item.put("Sentence", target.getText().toString());
				 item.put("Voice", R.drawable.voice);
				 list.add(pos_Sentence, item);
				 
				 lv.setVisibility(View.VISIBLE);
				 rl.setVisibility(View.GONE);
				 FadeInAnimation(lv);
				 lv = (ListView) menu.findViewById(R.id.listView);
				 lv.setAdapter(adapter);
				 lv.setOnItemClickListener(menuSentences_Listener);
				 description.setText("便利語句 ：");	 
				 modifylayer.setVisibility(View.GONE);
				 modifybutton.setVisibility(View.VISIBLE);
				 FadeInAnimation(modifybutton);
			}
		});
		port_in.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				target.setText(sentence.getText().toString());
				Toast.makeText(getApplicationContext(),    "已將主畫面的句子導入!", Toast.LENGTH_SHORT).show();
			}
		});
		
		
		
		
	}
	
	public static void addtoSentence(String word){
		if(sentence.getText().toString().equals("你想說什麼?")){
			sentence.setText("");
		}
		int start = sentence.getSelectionStart();
		sentence.setText(sentence.getText().insert(start, word));
		sentence.setSelection(start + word.length());
	}
	public static void deleteonSentence(){
		if(sentence.getText().toString().equals("你想說什麼?") || sentence.getText().toString().equals("")){
			sentence.setText("");
		}
		else{
			int start = sentence.getSelectionStart();
			if(start > 0) {
				String newSentence = sentence.getText().delete(start - 1, start).toString();
				sentence.setText(newSentence);
				sentence.setSelection(start - 1);
			}
		}
	}
	public static void saveCategory(String cate01 , String cate02){
		category1 = cate01;
		category2 = cate02;
	}
	private void FadeInAnimation(View view){
		AnimationSet animationSet = new AnimationSet(true);
	    AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
	    alphaAnimation.setDuration(600);
		animationSet.addAnimation(alphaAnimation);
		view.startAnimation(animationSet);
	}
	private void savemenu_sentences(){
		  SharedPreferences settings = getSharedPreferences("menu_sentences", 0);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putString("s0", menu_sentences[0]);
	      editor.putString("s1", menu_sentences[1]);
	      editor.putString("s2", menu_sentences[2]);
	      editor.putString("s3", menu_sentences[3]);
	      editor.putString("s4", menu_sentences[4]);
	      editor.putString("s5", menu_sentences[5]);
	      // Commit the edits!
	      editor.commit();
	}


}
