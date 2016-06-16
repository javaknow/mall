package com.igomall.util;

import java.util.Random;

public class SMSCodeUtils {

	public static String getCode(Integer length) {
		Random random = new Random();
		String code = "";
		while (code.length()<length){
			code = code + random.nextInt(10);
		}
		
		return code;
	}
}
