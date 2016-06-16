package com.igomall.wechat.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class QRCodeUtils {

	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF; 
	
	/**
	 *  最终调用该方法生成二维码图片
	 * @param url 要生成二维码的url
	 * @param imgPath 二维码生成的绝对路径
	 * @param logoPath 二维码中间logo绝对地址
	 * @throws Exception
	 */
	public static String get2CodeImage(String content,String imgPath,String logoPath,Integer width,Integer height) throws Exception{
		String format = "png";
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>(); 
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
		File qrcodeFile = new File(imgPath);  
		writeToFile(bitMatrix, format, qrcodeFile, logoPath); 
		
		return imgPath;
	}
	
	/**
	 * 
	 * @param matrix 二维码矩阵相关
	 * @param format 二维码图片格式
	 * @param file 二维码图片文件
	 * @param logoPath logo路径
	 * @throws IOException
	 */
	public static void writeToFile(BitMatrix matrix,String format,File file,String logoPath) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		Graphics2D gs = image.createGraphics();
		
		//载入logo
		Image logo = ImageIO.read(new File(logoPath));
		
		/**
		 * 设置logo的大小,本人设置为二维码图片的20%,因为过大会盖掉二维码
		 */
		int widthLogo = logo.getWidth(null) > image.getWidth() * 2 / 10 ? (image
				.getWidth() * 2 / 10) : logo.getWidth(null), heightLogo = logo
				.getHeight(null) > image.getHeight() * 2 / 10 ? (image
				.getHeight() * 2 / 10) : logo.getWidth(null);
		
		// 计算图片放置位置
		/**
		 * logo放在中心
		 */
		int x = (image.getWidth() - widthLogo) / 2;
		int y = (image.getHeight() - heightLogo) / 2;
		
		gs.drawImage(logo, x, y, null);
		gs.dispose();
		logo.flush();
		if(!ImageIO.write(image, format, file)){
			throw new IOException("Could not write an image of format " + format + " to " + file);  
		}
	}
	
	public static BufferedImage toBufferedImage(BitMatrix matrix){
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;	
	}

	
	public static void main(String[] args) {
		try {
			System.out.println(get2CodeImage("www.baidu.com","d:\\testImage\\logocode1.jpg","d:\\testImage\\logo.png",960,960));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}