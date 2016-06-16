package com.igomall.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.igomall.wechat.CommonWeChatAttributes;
import com.igomall.wechat.WeChatSign;

public class SHA1 {
	
	public static boolean checkSignature(String token,String signature,String timestamp,String nonce){
		if(token==null) token = "";
		if(signature==null) signature = "";
		if(timestamp==null) timestamp = "";
		if(nonce==null) nonce = "";
		String[] arr = new String[]{token,timestamp,nonce};
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for(int i=0;i<arr.length;i++){
			content.append(arr[i]);
		}
		MessageDigest md = null;
		String tmpStr = null;
		
        try {
			md = MessageDigest.getInstance("SHA-1");
	        byte[] digest = md.digest(content.toString().getBytes());
	        tmpStr = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		content = null;
		return tmpStr!=null?tmpStr.equals(signature.toUpperCase()):false;
	}
	
    // 将字节转换为十六进制字符串
    private static String byteToHexStr(byte ib) {
        char[] Digit = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
                'D', 'E', 'F'
            };
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];

        String s = new String(ob);
        return s;
    }
	
    // 将字节数组转换为十六进制字符串
    private static String byteToStr(byte[] bytearray) {
        String strDigest = "";
        for (int i = 0; i < bytearray.length; i++) {
            strDigest += byteToHexStr(bytearray[i]);
        }
        return strDigest;
    }
    public static String hash(String str){
		StringBuilder content = new StringBuilder();
		MessageDigest md = null;
		String tmpStr = "";
		
        try {
			md = MessageDigest.getInstance("SHA-1");
	        byte[] digest = md.digest(content.toString().getBytes());
	        tmpStr = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        
		content = null;
		return tmpStr;
	}

	public static boolean checkSignature(WeChatSign weChatSign) {
		String[] arr = new String[]{CommonWeChatAttributes.TOKEN,weChatSign.getTimestamp(),weChatSign.getNonce()};
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for(int i=0;i<arr.length;i++){
			content.append(arr[i]);
		}
		MessageDigest md = null;
		String tmpStr = null;
		
        try {
			md = MessageDigest.getInstance("SHA-1");
	        byte[] digest = md.digest(content.toString().getBytes());
	        tmpStr = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		content = null;
		return tmpStr!=null?tmpStr.equals(weChatSign.getSignature().toUpperCase()):false;
	}
}
