package com.yangyu.myslidingmenudemo02;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class Speaker implements
TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener{
	public static TextToSpeech mTts;
	private Context thisContext;
	// for display if a TTS instance is finished
	private boolean TTSfinished = false;
	private Handler handler = new Handler(); // used for timer
	
	private int MY_DATA_CHECK_CODE = 0;

	//Parameters Setting
	private float  fPitch          = 1.0f;
    private float  fSpeed          = 1.0f;
    private int    intSpeaker      = 1;           //1:man, 2:woman, 3:girl
    private String TTSLanguage     = "Mandarin";  //or "English"
	//private String SynthesizedFile = "/sdcard/tts_syn_sound.wav";
	private String LicenseFile     = "/sdcard/uTTS4NTHUCS/License.dat";
	public Speaker(Context c){
		WriteLicenseFile();
		thisContext = c;
		mTts = new TextToSpeech(c, this, "tw.nthucs.tts");
	}
	
	@Override
	public void onInit(int status) {
		mTts.setLanguage(Locale.CHINESE);
		mTts.setOnUtteranceCompletedListener(this);
		//String DefaultEngine = mTts.getDefaultEngine();
		/*if(!DefaultEngine.equals("tw.nthucs.tts"))
		{
			Toast.makeText(this, R.string.ttsVerify, Toast.LENGTH_SHORT).show();
		}*/
		//Toast.makeText(getApplicationContext(),"Start TTS: " + DefaultEngine, Toast.LENGTH_SHORT).show();
		Log.d("tts init status", "");
	}

	/*@Override
	public void onDestroy() {
    	handler.removeCallbacks(TimerRunning);         
		// Shutdown mTts!
		if (mTts != null) {
			mTts.stop();
			mTts.shutdown();
		}
		super.onDestroy();
	}*/

	@Override
	public void onUtteranceCompleted(String uttId) {
		if (uttId.compareTo("TTS_Finished_Message_ID") == 0) {
			TTSfinished = true;
		}
	}
	
	// write license file
		private void WriteLicenseFile() {
			// Create a valid license for the usage of uTTS engine (TextToSpeech)
			byte[] bytesBuffer = { 0x07, 0x1A, 0x1C, 0x07, 0x0F, 0x1A, 0x0D, 0x1A,
					0x1A, 0x1D, 0x18, 0x7F, 0x00, 0x1A, 0x06, 0x1B, 0x0D, 0x1D,
					0x4E };

			File file = new File(LicenseFile);
			try {
				FileOutputStream file_out = new FileOutputStream(file);
				DataOutputStream data_out = new DataOutputStream(file_out);
				data_out.write(bytesBuffer, 0, bytesBuffer.length);
				data_out.close();
				file_out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	   
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
					Toast.makeText(thisContext, "TTS Finished!", Toast.LENGTH_SHORT).show();
					TTSfinished = false;
				}
				handler.postDelayed(this, 100);
			}
		};
		
		public void speaking(String text){
			Log.d("speech text", text);
			ttsSpeak(text);
		}

}
