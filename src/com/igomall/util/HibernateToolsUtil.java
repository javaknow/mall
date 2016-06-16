package com.igomall.util;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Table;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang.StringUtils;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.Filter.Operator;
import com.igomall.Order.Direction;
import com.igomall.Pageable;
import com.igomall.entity.Member;

/**
 * 通过注解javax.persistence.Table获取数据库表的具体信息 java hibernate 根据 Table 注解 获取 数据库 表名
 * 字段名 工具类 需要 注解方式为 javax.persistence.Table的注解 【备注：
 * 如果哪位大牛感觉我的代码有问题或者有待优化，请明确提出，帮助我这个小菜鸟提高下，谢谢】
 * 
 * @author www.soservers.com 晚风工作室
 * 
 */
public class HibernateToolsUtil {

	/**
	 * 获得表名
	 * 
	 * @param clazz
	 *            映射到数据库的po类
	 * @return String
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String getTableName(Class clazz) {
		Table annotation = (Table) clazz.getAnnotation(Table.class);
		if (annotation != null) {
			return annotation.name();
		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	public static String getProperty(Class clazz,String propertyName) throws NoSuchFieldException, SecurityException {
		Field field = clazz.getDeclaredField(propertyName);
		char[] chars = field.getName().toCharArray();
		StringBuffer sb = new StringBuffer();
		
		for (char c : chars) {
			if(c>=65&&c<=90){
				sb.append("_"+((char)(c+32)));
			}else{
				sb.append(c);
			}
		}
		
		return sb.toString();
	}
	
	public static String getProperty(String propertyName){
		char[] chars = propertyName.toCharArray();
		StringBuffer sb = new StringBuffer();
		
		for (char c : chars) {
			if(c>=65&&c<=90){
				sb.append("_"+((char)(c+32)));
			}else{
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	
	@SuppressWarnings("rawtypes")
	public static String getSqlQuery(Class clazz,String[] columns){
		StringBuffer sb = new StringBuffer();
		if(columns==null||columns.length==0){
			sb.append("select * from "+getTableName(clazz));
		}else{
			String entityName = clazz.getSimpleName();
			StringBuffer columnSb = new StringBuffer();
			for (String column : columns) {
				columnSb.append(entityName+"."+getProperty(column)+" as "+column+", ");
			}
			columnSb.append(entityName+".id as id, ");
			columnSb.append(entityName+".create_date as createDate, ");
			columnSb.append(entityName+".modify_date as modifyDate ");
			
			sb.append("select "+ columnSb +" from "+getTableName(clazz)+" as "+entityName);
		}
		
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String getSqlPageQuery(Pageable pageable,Class clazz,String[] columns){
		StringBuffer sb = new StringBuffer();
		String entityName = clazz.getSimpleName();
		StringBuffer columnSb = new StringBuffer();
		
		if(columns==null||columns.length==0){
			sb.append("select * from "+getTableName(clazz));
		}else{
			
			for (String column : columns) {
				columnSb.append(entityName+"."+getProperty(column)+" as "+column+", ");
			}
			columnSb.append(entityName+".id as id, ");
			columnSb.append(entityName+".create_date as createDate, ");
			columnSb.append(entityName+".modify_date as modifyDate ");
			sb.append("select "+ columnSb +" from "+getTableName(clazz)+" as "+entityName);
		}
		
		//where语句
		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();
		if(StringUtils.isNotEmpty(searchProperty)&&StringUtils.isNotEmpty(searchValue)){
			sb.append(" where "+entityName+"."+getProperty(searchProperty)+" like '%"+searchValue+"%'");
		}
		List<Filter> filters = pageable.getFilters();
		if(filters!=null&&filters.size()>0){
			for (Filter filter : filters) {
				
				if (filter == null || StringUtils.isEmpty(filter.getProperty())) {
					continue;
				}
				sb.append(" and "+entityName+"."+getProperty(filter.getProperty()));
				if (filter.getOperator() == Operator.eq && filter.getValue() != null) {//相等
					
				} else if (filter.getOperator() == Operator.ne && filter.getValue() != null) {//取反
					
				} else if (filter.getOperator() == Operator.gt && filter.getValue() != null) {//大于
				} else if (filter.getOperator() == Operator.lt && filter.getValue() != null) {//小于
				} else if (filter.getOperator() == Operator.ge && filter.getValue() != null) {//大于等于
				} else if (filter.getOperator() == Operator.le && filter.getValue() != null) {//小于等于
				} else if (filter.getOperator() == Operator.like && filter.getValue() != null && filter.getValue() instanceof String) {//like
				} else if (filter.getOperator() == Operator.in && filter.getValue() != null) {//包含
				} else if (filter.getOperator() == Operator.isNull) {
				} else if (filter.getOperator() == Operator.isNotNull) {//非空
				}
			}
		}
		
		//排序语句
		Direction orderDirection = pageable.getOrderDirection();
		String orderProperty = pageable.getOrderProperty();
		if(StringUtils.isNotEmpty(orderProperty)&&orderDirection!=null){
			sb.append(" order by  "+entityName+"."+getProperty(orderProperty)+" "+orderDirection.name());
		}
		List<Order> orders = pageable.getOrders();
		if(orders!=null&&orders.size()>0){
			for (Order order : orders) {
				sb.append(",  "+entityName+"."+getProperty(order.getProperty())+" "+order.getDirection().name());
			}
		}
		
		
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String getCountSqlQuery(Class clazz){
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(id) from "+getTableName(clazz));
		
		return sb.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked"})
	public static void main(String[] args) throws NoSuchFieldException, SecurityException {
		String sql = getSqlQuery(Member.class,new String[]{"number","username","password","amount","balance","isEnabled","loginFailureCount","lockedDate","loginDate","name","birth","photo","openId","signInDay","type"});
		
		QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
		List<Member> list = new ArrayList<Member>();
		try {
			list = (List<Member>) qr.query(sql, new BeanListHandler(Member.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (Member member : list) {
			System.out.println(member.getIsEnabled());
		}
		JdbcUtils.close();
	}
}