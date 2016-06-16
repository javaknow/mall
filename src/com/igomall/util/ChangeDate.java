package com.igomall.util;

import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;

public class ChangeDate {

	// 当天
	public static DateTime getThisDay() {
		DateTime now = new DateTime();
		return now;
	}

	// 上一天
	public static DateTime getPrevDay(int year, int month, int day,int index) {
		DateTime now = new DateTime();
		now = now.withYear(year);
		now = now.withMonthOfYear(month);
		now = now.withDayOfMonth(day);
		now = now.plusDays(-1*index);
		return now;
	}

	// 下一天
	public static DateTime getNextDay(int year, int month, int day,int index) {
		DateTime now = new DateTime();
		now = now.withYear(year);
		now = now.withMonthOfYear(month);
		now = now.withDayOfMonth(day);
		now = now.plusDays(index);
		return now;
	}
	
	
	// 当前时间的前面几天
	public static DateTime getPrevDay(int index) {
		DateTime now = new DateTime();
		now = now.plusDays(-1*index);
		return now;
	}

	// 当前时间的后面几天
	public static DateTime getNextDay(int index) {
		DateTime now = new DateTime();
		now = now.plusDays(index);
		return now;
	}


	// 本月
	public static DateTime getThisMonth() {
		DateTime now = new DateTime();
		now = now.withTime(0, 0, 0, 0);
		now = now.withDayOfMonth(1);
		return now;
	}

	// 上一个月
	public static DateTime getPrevMonth(int year, int month,int index) {
		DateTime now = new DateTime();
		now = now.withYear(year);
		now = now.withMonthOfYear(month);
		now = now.plusMonths(-1*index);
		now = now.withTime(0, 0, 0, 0);
		now = now.withDayOfMonth(1);
		return now;
	}

	// 下一个月
	public static DateTime getNextMonth(int year, int month,int index) {
		DateTime now = new DateTime();
		now = now.withYear(year);
		now = now.withMonthOfYear(month);
		now = now.plusMonths(index);
		now = now.withTime(0, 0, 0, 0);
		now = now.withDayOfMonth(1);
		return now;
	}

	// 本年
	public static DateTime getThisYear() {
		DateTime now = new DateTime();
		return now;
	}

	// 上一年
	public static DateTime getPrevYear(int year,int index) {
		DateTime now = new DateTime();
		now = now.withYear(year);
		now = now.plusYears(-1*index);
		return now;
	}

	// 下一年
	public static DateTime getNextYear(int year,int index) {
		DateTime now = new DateTime();
		now = now.withYear(year);
		now = now.plusYears(1*index);
		return now;
	}
	
	
	public static Date dateTimeToDate(DateTime now){
		
		return now.toDate();
	}
	
	public static DateTime dateToDateTime(Date now){
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		DateTime dateTime = new DateTime(c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1,c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),c.get(Calendar.SECOND),c.get(Calendar.MILLISECOND));
		return dateTime;
	}
	
	public static DateTime getNextDay(DateTime now,int index) {
		now = now.withYear(now.getYear());
		now = now.withMonthOfYear(now.getMonthOfYear());
		now = now.withDayOfMonth(now.getDayOfMonth());
		now = now.plusDays(index);
		return now;
	}

	public static DateTime getPrevDay(DateTime now, int index) {
		now = now.withYear(now.getYear());
		now = now.withMonthOfYear(now.getMonthOfYear());
		now = now.withDayOfMonth(now.getDayOfMonth());
		now = now.plusDays(-1*index);
		return now;
	}
	
}
