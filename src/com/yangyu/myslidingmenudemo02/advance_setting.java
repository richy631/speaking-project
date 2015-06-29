package com.yangyu.myslidingmenudemo02;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class advance_setting extends Fragment{
	private static TextToSpeech mTts;
	View view;
	
	// for display if a TTS instance is finished
	private boolean TTSfinished = false;
	private Handler handler = new Handler(); // used for timer
	private Spinner  pitch_spin;
	private Spinner  speed_spin;
	private Spinner  speaker_spin;
	private Spinner  speaker_style;

	
	private int MY_DATA_CHECK_CODE = 0;

	//Parameters Setting
	private float  fPitch          = 1.0f;
    private float  fSpeed          = 1.0f;
    private int    intSpeaker      = 1;           //1:man, 2:woman, 3:girl

	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = (View)inflater.inflate(R.layout.advance_setting_menu, container, false);
		// start TTS engine
		mTts = MainActivity.speaker.mTts;
		initUI();
		AnimationSet animationSet = new AnimationSet(true);
	    AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
	    alphaAnimation.setDuration(600);
 	    animationSet.addAnimation(alphaAnimation);
 	    view.startAnimation(animationSet);
		return view;
    }
	
		private void initUI() {
			speaker_spin = (Spinner) view. findViewById(R.id.spinner_speaker);    	
		    pitch_spin   = (Spinner) view.  findViewById(R.id.spinner_pitch);
		    speed_spin   = (Spinner) view.  findViewById(R.id.spinner_speed);
		    speaker_style= (Spinner) view.  findViewById(R.id.spinner_style);


		    
		    ArrayAdapter<CharSequence> adapter_speaker = ArrayAdapter.createFromResource(
		    		view.getContext(), R.array.speakers, 
	                android.R.layout.simple_spinner_item);
	        adapter_speaker.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        speaker_spin.setAdapter(adapter_speaker);

	        ArrayAdapter<CharSequence> adapter_pitch = ArrayAdapter.createFromResource(
	        		view.getContext(), R.array.pitch, 
	                android.R.layout.simple_spinner_item);
	        adapter_pitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        pitch_spin.setAdapter(adapter_pitch);
	        
	        ArrayAdapter<CharSequence> adapter_speed = ArrayAdapter.createFromResource(
	        		view.getContext(), R.array.speed,
	                android.R.layout.simple_spinner_item);
	        adapter_speed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        speed_spin.setAdapter(adapter_speed);
	        ArrayAdapter<CharSequence> adapter_style = ArrayAdapter.createFromResource(
	        		view.getContext(), R.array.style,
	                android.R.layout.simple_spinner_item);
	        adapter_style.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        speaker_style.setAdapter(adapter_style);

	        //Set Listener for each UI control 
	        pitch_spin.setOnItemSelectedListener(getPitch);
	        speed_spin.setOnItemSelectedListener(getSpeed);
	        speaker_spin.setOnItemSelectedListener(getSpeaker);
	        speaker_style.setOnItemSelectedListener(getStyle);
	        
	        
	        //Initialize UI settings
	       	pitch_spin.setSelection(MainActivity.pitch_pos);
	    	speed_spin.setSelection(MainActivity.speed_pos);
	    	speaker_spin.setSelection(MainActivity.speaker_pos);
	    	speaker_style.setSelection(MainActivity.speaker_style_pos);
	
	    	
	    	
		}
		 private Spinner.OnItemSelectedListener getSpeaker = new Spinner.OnItemSelectedListener() 
		    {
		        public void onItemSelected(AdapterView parent, View v, int position, long id) 
		        {
		        	int pos = parent.getSelectedItemPosition();
		        	MainActivity.speaker_pos = pos;
		        	intSpeaker = pos+1;
		        	mTts.setPitch((float)(intSpeaker+2));
		        	mTts.speak("", TextToSpeech.QUEUE_FLUSH, null);
		        	System.out.println("speaker:" + intSpeaker);
		        }
				
		        @Override
				public void onNothingSelected(AdapterView<?> arg0) 
		        {
					// TODO Auto-generated method stub
				}
		    };  
		    private Spinner.OnItemSelectedListener getPitch = new Spinner.OnItemSelectedListener() 
		    {
		        public void onItemSelected(AdapterView parent, View v, int position, long id) 
		        {
		        	int pos = parent.getSelectedItemPosition();
		        	MainActivity.pitch_pos = pos;
		        	switch(pos)
		        	{
		        		case 0:	fPitch=0.5f;	break;
		        		case 1:	fPitch=0.75f;	break;
		        		case 2:	fPitch=1.0f;	break;
		        		case 3: fPitch=1.5f;	break;
		        		case 4: fPitch=2.0f;	break;
		        	}
		    		mTts.setPitch(fPitch);
		    		mTts.speak("", TextToSpeech.QUEUE_FLUSH, null);
		        }
		        public void onNothingSelected(AdapterView parent) 
		        {
		        }
		    };
		    private Spinner.OnItemSelectedListener getSpeed = new Spinner.OnItemSelectedListener() 
		    {
		    	public void onItemSelected(AdapterView parent, View v, int position, long id) 
		    	{
		        	int pos = parent.getSelectedItemPosition();
		        	MainActivity.speed_pos = pos;
		        	switch(pos)
		        	{
		        	case 0:	fSpeed=0.5f;	break;
		        	case 1:	fSpeed=0.75f;	break;
		        	case 2:	fSpeed=1.0f;	break;
		        	case 3: fSpeed=1.5f;	break;
		        	case 4: fSpeed=2.0f;	break;
		        	}
		    		mTts.setSpeechRate(fSpeed);  
		    		mTts.speak("", TextToSpeech.QUEUE_FLUSH, null);
		        }
		        public void onNothingSelected(AdapterView parent) 
		        {
		        }
		    };
		    private Spinner.OnItemSelectedListener getStyle = new Spinner.OnItemSelectedListener() 
		    {
		        public void onItemSelected(AdapterView parent, View v, int position, long id) 
		        {
		        	int pos = parent.getSelectedItemPosition();
		        	MainActivity.speaker_style_pos = pos;
		        	intSpeaker = pos+1;  
		        	mTts.setSpeechRate((float)(intSpeaker+2));
		        	mTts.speak("", TextToSpeech.QUEUE_FLUSH, null);
		        }
				
		        @Override
				public void onNothingSelected(AdapterView<?> arg0) 
		        {
					// TODO Auto-generated method stub
				}
		    };
		   
		    private static void ttsSpeak(String text)
		    {
				HashMap<String, String> myHashAlarm = new HashMap<String, String>();
				myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "TTS_Finished_Message_ID");
				mTts.speak(text, TextToSpeech.QUEUE_FLUSH, myHashAlarm);
		    }
		    
		 // Timer Implementation
			private Runnable TimerRunning = new Runnable() {
				public void run() {
					if (TTSfinished) {
						Toast.makeText(view.getContext(), "TTS Finished!", Toast.LENGTH_SHORT).show();
						TTSfinished = false;
					}
					handler.postDelayed(this, 100);
				}
			};
			
			public static void speaking(String text){
				Log.d("speech text", text);
				ttsSpeak(text);
			}
			
			

}
