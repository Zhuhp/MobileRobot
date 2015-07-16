package com.example.robot.tuling;

import android.R.integer;

public class WordsMeanParser {
	public final static int NO_MEAN = 0;
	public final static int HELLO = 1;
	public final static int SEARCH = 2;
	public final static int DANCE = 3;
	public static int parserWordsMean(String words){
		if(words.contains("你好")){
			return HELLO;
		}
		
		if(words.contains("百度搜索")){
			return SEARCH;
		}
		if(words.contains("跳舞")){
			return DANCE;
		}
		return NO_MEAN;
	}
}
