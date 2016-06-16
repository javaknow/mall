package com.igomall.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

public class DateUtils {
	
	public enum Type{
		year,
		month,
		day,
	}
  	
  	//当天0点
  	public static Date getDateBeginToday() {
  		Calendar cal = Calendar.getInstance();
  		cal.set(Calendar.HOUR_OF_DAY, 0);
  		cal.set(Calendar.SECOND, 0);
  		cal.set(Calendar.MINUTE, 0);
  		cal.set(Calendar.MILLISECOND, 0);
  		return cal.getTime();
  	}
  	
  	//当天24点
  	public static Date getDateEndToday() {
  		Calendar cal = Calendar.getInstance();
  		cal.set(Calendar.HOUR_OF_DAY, 24);
  		cal.set(Calendar.SECOND, 0);
  		cal.set(Calendar.MINUTE, 0);
  		cal.set(Calendar.MILLISECOND, 0);
  		return cal.getTime();
  	}
  	
  //当前时间的差index天的0点
  	public static Date getDateBeginToday(Integer index) {
  		DateTime now = new DateTime();
		now = now.withYear(now.getYear());
		now = now.withMonthOfYear(now.getMonthOfYear());
		now = now.withDayOfMonth(now.getDayOfMonth());
		now = now.withTime(0, 0, 0, 0);
		now = now.plusDays(index);
		return ChangeDate.dateTimeToDate(now);
  	}
  	
  	//当前时间的差index天的24点
  	public static Date getDateEndToday(Integer index) {
  		DateTime now = new DateTime();
		now = now.withYear(now.getYear());
		now = now.withMonthOfYear(now.getMonthOfYear());
		now = now.withDayOfMonth(now.getDayOfMonth());
		now = now.withTime(23, 59, 59, 0);
		now = now.plusDays(index);
		return ChangeDate.dateTimeToDate(now);
  	}
  	
  	//计算2个时间的插值（秒）
  	public static Long getIntervalSecond(Date startDate,Date endDate){
  		Long intervalSecond=0L;
  		Long millisecond = (endDate.getTime()-startDate.getTime());
 		intervalSecond = millisecond/1000;
  		return intervalSecond;
  	}
  	
  	//计算2个时间的插值（分）
  	public static Long getIntervalMin(Date startDate,Date endDate){
  		Long intervalMin=0L;

  		Long millisecond = (endDate.getTime()-startDate.getTime());
  		intervalMin = millisecond/1000/60;
  		return intervalMin;
  	}
  	
  	//计算2个时间的插值（时）
  	public static Long getIntervalHour(Date startDate,Date endDate){
  		Long intervalHour=0L;
  		Long millisecond = (endDate.getTime()-startDate.getTime());
  		intervalHour = millisecond/1000/60/60;
  		return intervalHour;
  	}
  	
  //计算2个时间的插值（天）
  	public static Long getIntervalDay(Date startDate,Date endDate){
  		Long intervalDay=0L;
  		Long millisecond = (endDate.getTime()-startDate.getTime());
  		intervalDay = millisecond/1000/60/60/24;
  		return intervalDay;
  	}
  	
  	//根据时间段获取所有天数
  	public static List<Date> findDates(Date dBegin, Date dEnd) {  
        List<Date> lDate = new ArrayList<Date>();  
        lDate.add(dBegin);  
        Calendar calBegin = Calendar.getInstance();  
        // 使用给定的 Date 设置此 Calendar 的时间    
        calBegin.setTime(dBegin);  
        Calendar calEnd = Calendar.getInstance();  
        // 使用给定的 Date 设置此 Calendar 的时间    
        calEnd.setTime(dEnd);  
        // 测试此日期是否在指定日期之后    
        while (dEnd.after(calBegin.getTime())) {  
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量    
            calBegin.add(Calendar.DAY_OF_MONTH, 1);  
            lDate.add(calBegin.getTime());  
        }  
        return lDate;  
    }
  	
  	/**
  	 * 获取指定时间的24点
  	 * @param date
  	 * @return
  	 */
  	public static Date getEnd(Date date){
  		DateTime now = ChangeDate.dateToDateTime(date);
		now = now.withYear(now.getYear());
		now = now.withMonthOfYear(now.getMonthOfYear());
		now = now.withDayOfMonth(now.getDayOfMonth());
		now = now.withTime(23, 59, 59, 0);
		return ChangeDate.dateTimeToDate(now);
  	}
  	
  	/**
  	 * 获取指定时间的0点
  	 * @param date
  	 * @return
  	 */
  	public static Date getStart(Date date){
  		DateTime now = ChangeDate.dateToDateTime(date);
		now = now.withYear(now.getYear());
		now = now.withMonthOfYear(now.getMonthOfYear());
		now = now.withDayOfMonth(now.getDayOfMonth());
		now = now.withTime(0, 0, 0, 0);
		return ChangeDate.dateTimeToDate(now);
  	}
  	
  	public static String formatDateToString(Date date,String pattern){
  		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
  		if(date==null){
  			return null;
  		}
  		return sdf.format(date);
  	}

	public static Date getDate(Date beginDate, Integer i) {
		DateTime now = ChangeDate.dateToDateTime(beginDate);
		return ChangeDate.dateTimeToDate(ChangeDate.getNextDay(now,i));
	}

	public static Date getStart(Date beginDate, Type type) {
		DateTime now = ChangeDate.dateToDateTime(beginDate);
		now = now.withYear(now.getYear());
		
		if(type==Type.month){
			now = now.withMonthOfYear(now.getMonthOfYear());
			now = now.plusMonths(1);
			now = now.withDayOfMonth(1);
		}else if(type==Type.year){
			now = now.withMonthOfYear(12);
			now = now.withDayOfMonth(31);
		}else if(type==Type.day){
			now = now.withMonthOfYear(now.getMonthOfYear());
			now = now.withDayOfMonth(now.getDayOfMonth());
		}
		now = now.withTime(0, 0, 0, 0);
		return ChangeDate.dateTimeToDate(now);
	}
	
	/**
	 * 获取指定时间的24点。
	 * @param beginDate 指定时间
	 * @param type 如果是day  就是指定时间的24点。如果是month就是指定时间的月份的最后一天的24点。如果是year就是指定时间的年的最有一天的24点
	 * @return
	 */
	public static Date getEnd(Date beginDate, Type type) {
		DateTime now = ChangeDate.dateToDateTime(beginDate);
		now = now.withYear(now.getYear());
		
		if(type==Type.month){
			now = now.withMonthOfYear(now.getMonthOfYear());
			Calendar   cDay1   =   Calendar.getInstance();  
	        cDay1.setTime(beginDate);  
	        Integer lastDay   =   cDay1.getActualMaximum(Calendar.DAY_OF_MONTH); 
	        now = now.withDayOfMonth(lastDay);
			now = now.withTime(23, 59, 59, 0);
		}else if(type==Type.year){
			now = now.withMonthOfYear(12);
			now = now.withDayOfMonth(31);
			now = now.withTime(23, 59, 59, 0);
		}else if(type==Type.day){
			now = now.withMonthOfYear(now.getMonthOfYear());
			now = now.withDayOfMonth(now.getDayOfMonth());
			now = now.withTime(23, 59, 59, 0);
		}
		
		return ChangeDate.dateTimeToDate(now);
	}

	/**
	 * 获取指定时间的间隔index月份
	 * @param beginDate 指定时间
	 * @param index 如果是负数表面是当前时间的前几月
	 * @return
	 */
	public static Date getNexMonth(Date beginDate,Integer index) {
		DateTime now = new DateTime(beginDate);
		now = now.withYear(now.getYear());
		now = now.withMonthOfYear(now.getMonthOfYear());
		now = now.plusMonths(index);
		return ChangeDate.dateTimeToDate(now);
	}

	/**
	 * 获取当前时间的第index年
	 * @param beginDate 当前时间
	 * @param index 如果是负数表面是当前时间的前几年
	 * @return
	 */
	public static Date getNexYear(Date beginDate, Integer index) {
		DateTime now = new DateTime(beginDate);
		now = now.withYear(now.getYear());
		now = now.plusYears(index);
		return ChangeDate.dateTimeToDate(now);
	}

	public static Date getNexDay(Date beginDate, Integer index) {
		DateTime now = new DateTime(beginDate);
		now = now.withYear(now.getYear());
		now = now.withMonthOfYear(now.getMonthOfYear());
		now = now.withDayOfMonth(now.getDayOfMonth());
		now = now.plusDays(index);
		return ChangeDate.dateTimeToDate(now);
	}

	public static Integer getIntervalMonth(Date beginDate, Date endDate) {
		int result = 0;

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(beginDate);
		c2.setTime(endDate);

		result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);

		return result;
	}

	public static Integer getIntervalYear(Date beginDate, Date endDate) {
		int result = 0;

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(beginDate);
		c2.setTime(endDate);

		result = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);

		return result;
	}

	public static Date formatStringToDate(String timeStr, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
  		if(timeStr==null){
  			return null;
  		}
  		try {
			return sdf.parse(timeStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**  
     * 将String型格式化,比如想要将2011-11-11格式化成2011年11月11日,就StringPattern("2011-11-11","yyyy-MM-dd","yyyy年MM月dd日"). 
     * @param date String 想要格式化的日期 
     * @param oldPattern String 想要格式化的日期的现有格式 
     * @param newPattern String 想要格式化成什么格式 
     * @return String  
     */ 
	public static String stringPattern(String date, String oldPattern, String newPattern) {   
        if (date == null || oldPattern == null || newPattern == null)   
            return "";   
        SimpleDateFormat sdf1 = new SimpleDateFormat(oldPattern) ;        // 实例化模板对象    
        SimpleDateFormat sdf2 = new SimpleDateFormat(newPattern) ;        // 实例化模板对象    
        Date d = null ;    
        try{    
            d = sdf1.parse(date) ;   // 将给定的字符串中的日期提取出来    
        }catch(Exception e){            // 如果提供的字符串格式有错误，则进行异常处理    
            e.printStackTrace() ;       // 打印异常信息    
        }    
        return sdf2.format(d);  
    } 
	
	
	public static List<String> getTimeSegment(int year, int month, int day){
		Calendar cal = Calendar.getInstance();
	    cal.set(year, month-1, day, 0, 0, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    long startTime = cal.getTimeInMillis();
	    cal.set(year, month-1, day, 23, 59, 59);
	    long endTime = cal.getTimeInMillis();
	    final int seg = 60*60*1000;//1小时
	    ArrayList<String> result = new ArrayList<String>((int)((endTime-startTime)/seg+1));
	    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH");
	    for(long time = startTime;time<=endTime;time+=seg){
	        result.add(fmt.format(new Date(time)).toString()+":00:00"+"&"+fmt.format(new Date(time)).toString()+":59:59");
	    }
	    return result;
	}
	
	public static String lastWeek(Date date){
		int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
		int month = Integer.parseInt(new SimpleDateFormat("MM").format(date));
		int day = Integer.parseInt(new SimpleDateFormat("dd").format(date)) - 6;

		if (day < 1) {
			month -= 1;
			if (month == 0) {
				year -= 1;
				month = 12;
			}
			if (month == 4 || month == 6 || month == 9 || month == 11) {
				day = 30 + day;
			} else if (month == 1 || month == 3 || month == 5 || month == 7
					|| month == 8 || month == 10 || month == 12) {
				day = 31 + day;
			} else if (month == 2) {
				if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)){
					day = 29 + day;
				}else{
					day = 28 + day;
				}
			}
		}
		String y = year + "";
		String m = "";
		String d = "";
		if (month < 10){
			m = "0" + month;
		}else{
			m = month + "";
		}
		if (day < 10){
			d = "0" + day;
		}else{
			d = day + "";
		}
		return y+"-"+m+"-"+d;
	}
	
	/**
	 * 计算置顶日期的前面或后面几天
	 * @param now 制定的时间
	 * @param index 前面多少天或者后面多少天。如果是前面多少天index是负数。
	 * @param flag 是否包含当天。true 包含 false 不包含
	 * @return
	 */
	public static List<Date> getDays(Date now,Integer index,Boolean flag){
		DateTime dateTime = ChangeDate.dateToDateTime(now);
		List<Date> list = new ArrayList<Date>();
		if(index>0){
			if(flag){//包含当天
				for (int i = index-1; i >= 0; i--) {
					list.add(ChangeDate.dateTimeToDate(dateTime.plusDays(i)));
				}
			}else{
				for (int i = index; i > 0; i--) {
					list.add(ChangeDate.dateTimeToDate(dateTime.plusDays(i)));
				}
			}
		}else if(index<0){
			if(flag){//包含当天
				for (int i = index+1; i <= 0; i++) {
					list.add(ChangeDate.dateTimeToDate(dateTime.plusDays(i)));
				}
			}else{
				for (int i = index; i < 0; i++) {
					list.add(ChangeDate.dateTimeToDate(dateTime.plusDays(i)));
				}
			}
		}
		
		
		return list;
	}
	
	
	public static void main(String[] args) {
		System.out.println(getDays(new Date(),-7,false));
	}
}
