package com.yangyu.myslidingmenudemo02;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class Sentence {
	
	private List<Word> sentence;
	private List<Integer> loc;
	
	public Sentence(){
		this.sentence = new ArrayList<Word>();
		this.loc = new ArrayList<Integer>();
	}
	
	public List<Word> getSentence(){
		return this.sentence;
	}
	
	public List<Integer> getLoc(){
		return this.loc;
	}
	
	public void addWord(Word w, int len){
		
		Log.v("len : ", ""+len);
		
		if (this.loc.size() == 0) {
			this.sentence.add(w);
			this.loc.add(len);
		}
		else if(len-w.getWord().length() == this.loc.get(this.loc.size()-1)){ //插在最後面
			this.sentence.add(w);
			this.loc.add(len);
		}
		/*else if(len < this.loc.get(this.loc.size()-1)){ // 插中間
			for(int i = 0; i < this.loc.size(); i++){
				if(this.loc.get(i) == len){ // 剛好
					this.sentence.add(i + 1, w);
					this.loc.add(i + 1, w.getWord().length() + len);
					for(int j = i + 2; j < this.loc.size(); j++){
						this.loc.set(j, this.loc.get(j) + w.getWord().length());
					}
				}
				else if(this.loc.get(i) > len){ // 不剛好
					
				}
			}
			
		}*/
	}
	
	public Word getPreviouWord(int len) {
		for(int i = 0; i < this.loc.size(); i++){
			if (this.loc.get(i) == len) {
				return this.sentence.get(i);
			}
		}
		return null;
	}
	
	public String getPreviouSpeeches(int len) {
		if (len == this.loc.get(this.loc.size()-1)) {
			int i = 1; String str = "";
			for (Word w : this.sentence) {
				if (i != this.sentence.size())
					str += "speech_" + (i++) + "= '" + w.getPos() + "' and ";
				else
					str += "speech_" + (i++) + "= '" + w.getPos() + "'";
			}
			return str;
		}
		return null;
	}
	
	public int getCurrentSize(int len) {
		if (len == this.loc.get(this.loc.size()-1)) {
			return this.sentence.size();
		}
		return -1;
	}

}
