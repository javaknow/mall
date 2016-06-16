package com.igomall.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class BackUpMySQL {
	public static void backup(String cmd,String ip,String username,String password, String databaseName,String fileName,String charset) {      
		cmd = cmd+"mysqldump -h"+ip+" -u"+username+" -p"+password+" "+databaseName;
		//"d:/Program Files/MySQL/MySQL Server 5.5/bin/mysqldump -uroot -proot  qmgj"
		try {      
            Runtime rt = Runtime.getRuntime();      
           
            // 调用 mysql 的 cmd:      
            Process child = rt.exec(cmd);// 设置导出编码为utf8。这里必须是utf8      
                 
            // 把进程执行中的控制台输出信息写入.sql文件，即生成了备份文件。注：如果不对控制台信息进行读出，则会导致进程堵塞无法运行      
            InputStream in = child.getInputStream();// 控制台的输出信息作为输入流      
                             
            InputStreamReader xx = new InputStreamReader(in, charset);// 设置输出流编码为utf8。这里必须是utf8，否则从流中读入的是乱码      
                 
            String inStr;      
            StringBuffer sb = new StringBuffer("");      
            String outStr;      
            // 组合控制台输出信息字符串      
            BufferedReader br = new BufferedReader(xx);      
            while ((inStr = br.readLine()) != null) {
System.out.println(inStr);
                sb.append(inStr + "\r\n");      
            }      
            outStr = sb.toString();                     
            // 要用来做导入用的sql目标文件：      
            FileOutputStream fout = new FileOutputStream(fileName);      
            OutputStreamWriter writer = new OutputStreamWriter(fout, "utf8");      
            writer.write(outStr);      
            // 注：这里如果用缓冲方式写入文件的话，会导致中文乱码，用flush()方法则可以避免      
            writer.flush();      
     
            // 别忘记关闭输入输出流      
            in.close();      
            xx.close();      
            br.close();      
            writer.close();      
            fout.close();      
     
     
        } catch (Exception e) {      
            e.printStackTrace();      
        }      
     
    }
	
	public static void load(String cmd,String ip,String username,String password, String databaseName,String fileName,String charset) {      
		cmd = cmd+"mysql.exe -h"+ip+" -u"+username+" -p"+password+" "+databaseName;
		//"d:/Program Files/MySQL/MySQL Server 5.5/bin/mysql.exe -uroot -proot test111 "
		try {      
	           Runtime rt = Runtime.getRuntime();     
	           
	           // 调用 mysql 的 cmd:      
	           Process child = rt.exec(cmd);      
	           OutputStream out = child.getOutputStream();//控制台的输入信息作为输出流      
	           String inStr;      
	           StringBuffer sb = new StringBuffer("");      
	           String outStr;      
	           BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charset));      
	           while ((inStr = br.readLine()) != null) {      
	               sb.append(inStr + "\r\n");      
	           }      
	           outStr = sb.toString();          
	           OutputStreamWriter writer = new OutputStreamWriter(out, charset);      
	           writer.write(outStr);      
	           // 注：这里如果用缓冲方式写入文件的话，会导致中文乱码，用flush()方法则可以避免      
	           writer.flush();      
	           // 别忘记关闭输入输出流      
	           out.close();      
	           br.close();      
	           writer.close();      
	    
	       } catch (Exception e) {      
	           e.printStackTrace();      
	       }      
	    
	   }    
	  
	
	public static void main(String[] args) {
System.out.println("kaishi");
		backup("d:/Program Files/MySQL/MySQL Server 5.5/bin/","114.55.99.173","root","chenYUwei123","mmm","d:/xytx123456781.sql","utf8");
		//load("d:/Program Files/MySQL/MySQL Server 5.5/bin/","localhost","root","root","test111","d:/xytx12345678.sql","utf8");
System.out.println("jieshu");
	}
}