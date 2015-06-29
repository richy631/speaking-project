package com.yangyu.myslidingmenudemo02;

public class Word {
	private String word;
	private String pos;
	private int loc;
	private Integer score;
	private String table_name = null;
	
	public Word(String s, String p){
		this.word = s;
		this.pos = p;
		this.score = 0;
	}
	
	public Word(String s, String p, Integer score, int loc){
		this.word = s;
		this.pos = p;
		this.score = score;
		this.loc = loc;
	}
	
	public Word(String s, String p, Integer score){
		this.word = s;
		this.pos = p;
		this.score = score;
	}
	
	public Word(String s, String p, Integer score, String table_name){
		this.word = s;
		this.pos = p;
		this.score = score;
		this.table_name = table_name;
	}
	
	public String getWord(){
		return this.word;
	}
	
	public String getPos(){
		return this.pos;
	}
	
	public int getLoc(){
		return this.loc;
	}
	
	public Integer getScore() {
		return this.score;
	}
	
	public String getTableName() {
		return this.table_name;
	}
	
	public void setWord(String w){
		this.word = w;
	}
	
	public void setPos(String p){
		this.pos = p;
	}
	
	public void setPos(int l){
		this.loc = l;
	}
	
	public void setScore(Integer score) {
		this.score = score;
	}
	
	public void setTableName(String table_name) {
		this.table_name = table_name;
	}
	
}
