package com.igomall.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class LotteryUtils {
    
    public static String getDate(String url) throws IOException{
System.out.println(url);
        URL u=new URL(url);
        InputStream in=u.openStream();
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        try {
            byte buf[]=new byte[1024];
            int read = 0;
            while ((read = in.read(buf)) > 0) {
                out.write(buf, 0, read);
            }
        }  finally {
            if (in != null) {
                in.close();
            }
        }
        byte b[]=out.toByteArray( );
        return new String(b,"utf-8");
    }
    
    
    public static void main(String[] args) throws IOException {
        String url = "http://apis.haoservice.com/lifeservice/HighLottery/query?key=9d58383273a9442582c25ab93cb7e72d&spell=cqssc&date=";
        System.out.println(getDate(url));
    }
}
