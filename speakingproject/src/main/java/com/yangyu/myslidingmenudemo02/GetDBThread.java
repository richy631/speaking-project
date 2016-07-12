package com.yangyu.myslidingmenudemo02;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;

import android.database.Cursor;

public class GetDBThread implements Callable<PriorityQueue<Word>>{
	
	private String tablename;
	private String flag;
	private PriorityQueue<Word> tmpRank;
	
	public GetDBThread(String tablename, String flag){
		this.tablename = tablename;
		this.flag = flag;
		ComparatorWords comparator = new ComparatorWords();
		this.tmpRank = new PriorityQueue<Word>(scroll_words.total_words_num, comparator);;
	}
	
	public PriorityQueue<Word> call(){
		
		Cursor size = MainActivity.db.rawQuery("select * from " + this.tablename, null);
		int count = size.getCount() / 2;
		
		Cursor cursor = MainActivity.db.rawQuery("select * from " + this.tablename + " where rowid " + this.flag + count , null);
	    int rows_num = cursor.getCount();//取得資料表列數
	    if(rows_num != 0) {
		    cursor.moveToFirst();//將指標移至第一筆資料
		    for(int i=0; i<rows_num; i++) {
			    String word = cursor.getString(0);
			    String pos = cursor.getString(1);
			    int pick = cursor.getInt(2);
			    
			    Word w = new Word(word, pos, pick, this.tablename);
			    //Log.v("size", "" + rank.size());
			    if (this.tmpRank.size() < scroll_words.total_words_num) {
			    	this.tmpRank.add(w);
			    }
			    else {
			    	if (this.tmpRank.peek().getScore() < w.getScore()) {
			    		this.tmpRank.poll(); this.tmpRank.add(w);
			    	}
			    }
			    
			    //Log.v("word :", word + " / " + pos + " / " + " / " + pick);
			    cursor.moveToNext();//將指標移至下一筆資料
		    }
	    }
	    cursor.close(); //關閉Cursor
		return this.tmpRank;
	}
	
	private class ComparatorWords implements Comparator<Word> {
		
		@Override
		public int compare(Word w1, Word w2) {
			return w1.getScore().compareTo(w2.getScore());
		}
	}
}
