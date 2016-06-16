package com.igomall.util;  

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parse1688Product {  
    
	public static String getKeyWords(String msg) {
		Integer keywordsIndex = msg.indexOf("name=\"keywords\"");
		msg = msg.substring(keywordsIndex);
		
		keywordsIndex = msg.indexOf("content=");
		msg = msg.substring(keywordsIndex+8);
		
		keywordsIndex = msg.indexOf("/>");
		msg = msg.substring(0,keywordsIndex);
		msg = msg.replace("\"", "");
		return msg;
	}
	
	public static String getDescription(String msg) {
		Integer keywordsIndex = msg.indexOf("name=\"description\"");
		msg = msg.substring(keywordsIndex);
		
		keywordsIndex = msg.indexOf("content=");
		msg = msg.substring(keywordsIndex+8);
		
		keywordsIndex = msg.indexOf("/>");
		msg = msg.substring(0,keywordsIndex);
		msg = msg.replace("\"", "");
		return msg;
	}
	
	public static String getPrice(String msg) {
		Integer keywordsIndex = msg.indexOf("property=\"og:product:orgprice\"");
		msg = msg.substring(keywordsIndex);
		
		keywordsIndex = msg.indexOf("content=");
		msg = msg.substring(keywordsIndex+8);
		
		keywordsIndex = msg.indexOf("/>");
		msg = msg.substring(0,keywordsIndex);
		msg = msg.replace("\"", "");
		return msg;
	}
	
	public static String getTitle(String msg) {
		Integer keywordsIndex = msg.indexOf("<h1 class=\"d-title\">");
		msg = msg.substring(keywordsIndex+20);
		
		keywordsIndex = msg.indexOf("</h1");
		msg = msg.substring(0,keywordsIndex);
		return msg;
	}
	
	public static List<String> getPic(String msg) {
		List<String> picList = new ArrayList<String>();
		
		String msg1 = "";
		Integer keywordsIndex = msg.indexOf("<div id=\"dt-tab\"");
		msg = msg.substring(keywordsIndex);
		
		keywordsIndex = msg.indexOf("<div class=\"obj-fav\">");
		msg = msg.substring(0,keywordsIndex);
		
		
		Boolean flag = true;
		
		while(flag){
			keywordsIndex = msg.indexOf("original");
			if(keywordsIndex>0){
				msg = msg.substring(keywordsIndex);
				keywordsIndex = msg.indexOf("}'>");
				msg1 = msg.substring(10,keywordsIndex).replace("\"", "");
				msg = msg.substring(keywordsIndex);
				picList.add(msg1);
			}else{
				flag = false;
			}
			
		}
		
		return picList;
	}
	
	
	public static String getContent(String msg) {
		Integer keywordsIndex = msg.indexOf("data-tfs-url");
		msg = msg.substring(keywordsIndex+14);
		
		keywordsIndex = msg.indexOf(">");
		msg = msg.substring(0,keywordsIndex-20);
		msg = HttpUtils.getRequest(msg,"gbk");
		msg = msg.substring(10,msg.length()-2);
		return msg;
	}
	
	public static Map<String, List<String>> parseSpecification(String msg) {
		Map<String,List<String>> map = new HashMap<String,List<String>>();
		String[] msgArray = msg.split(";");
		for (String string : msgArray) {
			String [] result = string.split(":");
			if(result.length>=2){
				//取最后两个 最后一个属性值 倒数第二个是属性名
				Integer length = result.length;
				String specification = result[length-2];
				String specificationValue = result[length-1];				
				if(map.containsKey(specification)){
					
				}else{
					map.put(specification, new ArrayList<String>());
				}
				
				if(map.get(specification).contains(specificationValue)){
					
				}else{
					map.get(specification).add(specificationValue);
				}
				
			}
			
			
		}
		
		return map;
	}
}