package com.igomall.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


public class Pinyin {

	public static String capitalize(String s) {
		char ch[];
		ch = s.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		String newString = new String(ch);
		return newString;
	}

	public static String getFirstPinyin(String s) {
		char ch[] = s.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
			return String.valueOf(ch[0]);
		}
		return "";
	}

	public static String toPinyin(String str) {
		String pinyin = "";
		char[] chars = str.toCharArray();
		try {
			for (int i = 0; i < chars.length; i++) {
				String[] result = PinyinHelper.toHanyuPinyinStringArray(
						chars[i], getDefaultOutputFormat());
				if (result != null)
					pinyin += capitalize(result[0]);
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return pinyin;
	}

	public static String toPinyinShort(String str) {
		String pinyin = "";
		char[] chars = str.toCharArray();
		try {
			for (int i = 0; i < chars.length; i++) {
				String[] result = PinyinHelper.toHanyuPinyinStringArray(
						chars[i], getDefaultOutputFormat());
				if (result != null)
					pinyin += getFirstPinyin(result[0]);
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return pinyin;
	}

	public static HanyuPinyinOutputFormat getDefaultOutputFormat() {
		HanyuPinyinOutputFormat hpof = new HanyuPinyinOutputFormat();
		hpof.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		hpof.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		hpof.setVCharType(HanyuPinyinVCharType.WITH_V);
		return hpof;
	}

}
