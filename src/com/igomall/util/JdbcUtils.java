package com.igomall.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 使用apache 的 commons-dbutils-1.6 来连接数据库
 * @author blackboy
 *
 */
public class JdbcUtils {
    
    private static ComboPooledDataSource ds = null;
    
    //使用ThreadLocal存储当前线程中的Connection对象
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();
    
    //在静态代码块中创建数据库连接池
    static{
        try{
        	 //读取db.properties文件中的数据库连接信息
            InputStream in = JdbcUtils.class.getClassLoader().getResourceAsStream("lx2014.properties");
            Properties prop = new Properties();
            prop.load(in);
            //通过代码创建C3P0数据库连接池
            ds = new ComboPooledDataSource();
            ds.setDataSourceName("igomall");
            ds.setDriverClass(prop.getProperty("jdbc.driver"));
            ds.setJdbcUrl(prop.getProperty("jdbc.url"));
            ds.setUser(prop.getProperty("jdbc.username"));
            ds.setPassword(prop.getProperty("jdbc.password"));
            ds.setInitialPoolSize(new Integer(prop.getProperty("connection_pools.initial_pool_size")));
            ds.setMinPoolSize(new Integer(prop.getProperty("connection_pools.min_pool_size")));
            ds.setMaxPoolSize(new Integer(prop.getProperty("connection_pools.max_pool_size")));
        }catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    /**
     * 从数据源处获取数据库连接
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException{
        //从当前线程中获取Connection
        Connection conn = threadLocal.get();
        if(conn==null){
            //从数据源中获取数据库连接
            conn = getDataSource().getConnection();
            //将conn绑定到当前线程
            threadLocal.set(conn);
        }
        return conn;
    }
    
   /**
    * 开启事务
    */
    public static void startTransaction(){
        try{
            Connection conn =  threadLocal.get();
            if(conn==null){
                conn = getConnection();
                 //把 conn绑定到当前线程上
                threadLocal.set(conn);
            }
            //开启事务
            conn.setAutoCommit(false);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 事务的回滚
     */
    public static void rollback(){
        try{
            //从当前线程中获取Connection
            Connection conn = threadLocal.get();
            if(conn!=null){
                //回滚事务
                conn.rollback();
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 事务的提交
     */
    public static void commit(){
        try{
            //从当前线程中获取Connection
            Connection conn = threadLocal.get();
            if(conn!=null){
                //提交事务
                conn.commit();
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 关闭连接
     */
    public static void close(){
        try{
            //从当前线程中获取Connection
            Connection conn = threadLocal.get();
            if(conn!=null){
                conn.close();
                 //解除当前线程上绑定conn
                threadLocal.remove();
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 获取数据源
     * @return
     */
    public static DataSource getDataSource(){
        return ds;
    }
    
    /**
	 * 数据的插入
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static Integer save(String sql, Object... params) {
		QueryRunner qr = new QueryRunner(getDataSource());
		try {
			return qr.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close();
		}
		
		return 0;
	}
	
	
	 /**
		 * 数据的删除
		 * 
		 * @param sql
		 * @param params
		 * @return
		 */
		public static Integer delete(String sql, Object... params) {
			QueryRunner qr = new QueryRunner(getDataSource());
			try {
				return qr.update(sql, params);
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				close();
			}
			
			return 0;
		}

	/**
	 * 数据的更新
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static Integer update(String sql, Object... params) {
		QueryRunner qr = new QueryRunner(getDataSource());
		try {
			return qr.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close();
		}
		
		return 0;
	}
}
