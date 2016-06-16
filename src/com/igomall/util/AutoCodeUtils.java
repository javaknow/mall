package com.igomall.util;

public class AutoCodeUtils {

	public static String setNumber(String memberNumberPrefix, Integer memberNumberLength) {
		String number = "";
		String prefix = memberNumberPrefix;
		if(prefix!=null){
			number = prefix;
		}
		
		//1446883637170
		String current = System.currentTimeMillis()+"";
		number = number+current.substring((current.length()-memberNumberLength)>0?(current.length()-memberNumberLength):0, current.length());
		
		return number;
	}
}
