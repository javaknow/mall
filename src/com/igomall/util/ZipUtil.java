package com.igomall.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

/**
 * YouAreStupid 集合网上几个比较好的例子，使用org.apache.tools.zip包下的类解决zip压缩中的 中文文件名问题
 */
public class ZipUtil {

	public static String unZip(String path) {
		String halfPath = path.substring(path.lastIndexOf("/")-6,path.length()-4);//    201504/3a38fe1a-7f71-4b2a-96c8-3d20af99d629
		String savepath = "/upload/file/"+halfPath;
		int count = -1;
		File file = null;
		InputStream is = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			ZipFile zipFile = new ZipFile(savepath+".zip", "GB2312");
			Enumeration<?> entries = zipFile.getEntries();
			while (entries.hasMoreElements()) {
				byte buf[] = new byte[2048];
				ZipEntry entry = (ZipEntry) entries.nextElement();

				String filename = entry.getName();
				filename = savepath+"/" + filename;
				file = new File(filename);

				if (file.getParentFile().exists()) {

				} else {
					file.getParentFile().mkdirs();
				}

				if (file.isDirectory()) {
					continue;
				}

				is = zipFile.getInputStream(entry);
				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos, 2048);

				while ((count = is.read(buf)) > -1) {
					bos.write(buf, 0, count);
				}

				fos.close();

				is.close();
			}

			zipFile.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return savepath;
	}

	public static boolean isPics(String filename) {
		boolean flag = false;

		if (filename.endsWith(".jpg") || filename.endsWith(".gif")
				|| filename.endsWith(".bmp") || filename.endsWith(".png"))
			flag = true;

		return flag;
	}

	public static void main(String[] args) throws Exception {
		unZip("http://localhost:8080/igomall/upload/file/201504/0001.zip");
	}
}