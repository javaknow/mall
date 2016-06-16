package com.igomall.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.ServletContextAware;

import com.csvreader.CsvReader;
import com.igomall.Setting;
import com.igomall.entity.Product;
import com.igomall.entity.ProductImage;
import com.igomall.plugin.StoragePlugin;
import com.igomall.service.ParseCSVService;
import com.igomall.util.FreemarkerUtils;
import com.igomall.util.ImageUtils;
import com.igomall.util.SettingUtils;
import com.igomall.util.ZipUtil;

@Service("parseCSVServiceImpl")
public class ParseCSVServiceImpl implements ParseCSVService, ServletContextAware {

	private static final String DEST_EXTENSION = "jpg";
	private static final String DEST_CONTENT_TYPE = "image/jpeg";

	private ServletContext servletContext;

	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	@Resource
	private List<StoragePlugin> storagePlugins;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public List<Product> parseTaobao(String path) {
		// String resultPath = ZipUtil.unZip(path);

		List<Product> list = new ArrayList<Product>();
		try {
			URL url = new URL(path);
			Reader reader = new InputStreamReader(new BOMInputStream(url.openStream()), "GB2312");
			CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
			int i = 0;
			try {
				for (final CSVRecord record : parser) {
					if (i < 1) {
						i++;
					} else {
						Product product = new Product();
						product.setName("".equals(record.get("title")) ? "  " : record.get("title").length() > 200 ? record.get("title").substring(0, 200) : record.get("title"));
						product.setIntroduction(record.get("description"));
						product.setCost(new BigDecimal("".equals(record.get("price")) ? "0" : record.get("price")));
						product.setStock(Integer.valueOf("".equals(record.get("num")) ? "0" : record.get("num")));
						product.setStockMemo(record.get("outer_id"));
						product.setWeight(Double.valueOf("".equals(record.get("item_weight")) ? "0" : record.get("item_weight")));
						product.setSkuProps(record.get("skuProps"));
						product.setCateProps(record.get("cateProps"));
						product.setInputValues(record.get("inputValues"));
						
						setProductImages(record.get("picture"), product);
						list.add(product);
					}

				}
			} finally {
				parser.close();
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public static void setProductImages(String pictureStr, Product product) {
		String[] pics = pictureStr.split(";");

		for (String string : pics) {
			String[] picss = string.split("http://");

			if (picss.length == 2) {
				String pic = "http://" + picss[1];
				ProductImage image = new ProductImage();
				image.setLarge(pic);
				image.setMedium(pic);
				image.setSource(pic);
				image.setThumbnail(pic);
				image.setTitle(product.getName());
				product.getProductImages().add(image);
			}
		}
	}

	public List<Product> parseAliBaBa(String path) {
		String resultPath = ZipUtil.unZip(path);
		List<Product> list = new ArrayList<Product>();
		try {
			CsvReader reader = new CsvReader(resultPath + "/products.csv", ',', Charset.forName("GB2312")); // 一般用这编码读就可以了
			reader.readHeaders(); // 跳过表头 如果需要表头的话，不要写这句。
			int i=0;
			while (reader.readRecord()) { // 逐行读入除表头的数据
				if(i==0){
					i++;
				}else{
					Product product = new Product();
					product.setName("".equals(reader.get("title")) ? "  " : reader.get("title").length() > 200 ? reader.get("title").substring(0, 200) : reader.get("title"));
					product.setIntroduction(reader.get("description"));
					product.setCost(new BigDecimal("".equals(reader.get("price")) ? "0" : reader.get("price")));
					
					product.setStock(Integer.valueOf("".equals(reader.get("num")) ? "0" : reader.get("num")));
					if(reader.get("outer_id")!=null){
						if(reader.get("outer_id").length()>200){
							product.setStockMemo(reader.get("outer_id").substring(0,200));
						}else{
							product.setStockMemo(reader.get("outer_id"));
						}
					}
					
					product.setWeight(Double.valueOf("".equals(reader.get("item_weight")) ? "0" : reader.get("item_weight")));
					
					product.setSkuProps(reader.get("skuProps"));
					product.setCateProps(reader.get("cateProps"));
					product.setInputValues(reader.get("inputValues"));
					
					setProductImages1(reader.get("picture"), product, resultPath + "/products");

					list.add(product);
				}
				
			}

		} catch (NumberFormatException e) {

			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}

	public void setProductImages1(String pictureStr, Product product, String path) {
		String[] pics = pictureStr.split(";");

		for (Integer i = 0; i < pics.length; i++) {
			if (pics[i].indexOf(":") > 0) {
				pics[i] = path + "/" + pics[i].substring(0, pics[i].indexOf(":")) + ".jpg";

				ProductImage image = new ProductImage();
				image.setTitle(product.getName());
				image.setOrder(i + 1);
				image.setSource(pics[i]);
				build(image);
				product.getProductImages().add(image);
			}

		}
	}

	private void addTask(final String sourcePath, final String largePath, final String mediumPath, final String thumbnailPath, final File tempFile, final String contentType) {
		try {
			taskExecutor.execute(new Runnable() {
				public void run() {
					Collections.sort(storagePlugins);
					for (StoragePlugin storagePlugin : storagePlugins) {
						if (storagePlugin.getIsEnabled()) {
							Setting setting = SettingUtils.get();
							String tempPath = System.getProperty("java.io.tmpdir");
							File watermarkFile = new File(servletContext.getRealPath(setting.getWatermarkImage()));
							File largeTempFile = new File(tempPath + "/upload_" + UUID.randomUUID() + "." + DEST_EXTENSION);
							File mediumTempFile = new File(tempPath + "/upload_" + UUID.randomUUID() + "." + DEST_EXTENSION);
							File thumbnailTempFile = new File(tempPath + "/upload_" + UUID.randomUUID() + "." + DEST_EXTENSION);
							try {
								ImageUtils.zoom(tempFile, largeTempFile, setting.getLargeProductImageWidth(), setting.getLargeProductImageHeight());
								ImageUtils.addWatermark(largeTempFile, largeTempFile, watermarkFile, setting.getWatermarkPosition(), setting.getWatermarkAlpha());
								ImageUtils.zoom(tempFile, mediumTempFile, setting.getMediumProductImageWidth(), setting.getMediumProductImageHeight());
								ImageUtils.addWatermark(mediumTempFile, mediumTempFile, watermarkFile, setting.getWatermarkPosition(), setting.getWatermarkAlpha());
								ImageUtils.zoom(tempFile, thumbnailTempFile, setting.getThumbnailProductImageWidth(), setting.getThumbnailProductImageHeight());
								storagePlugin.upload(sourcePath, tempFile, contentType);
								storagePlugin.upload(largePath, largeTempFile, DEST_CONTENT_TYPE);
								storagePlugin.upload(mediumPath, mediumTempFile, DEST_CONTENT_TYPE);
								storagePlugin.upload(thumbnailPath, thumbnailTempFile, DEST_CONTENT_TYPE);
							} finally {
								FileUtils.deleteQuietly(tempFile);
								FileUtils.deleteQuietly(largeTempFile);
								FileUtils.deleteQuietly(mediumTempFile);
								FileUtils.deleteQuietly(thumbnailTempFile);
							}
							break;
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void build(ProductImage productImage) {
		File file = new File(productImage.getSource());
		if (file != null) {
			try {
				Setting setting = SettingUtils.get();
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("uuid", UUID.randomUUID().toString());
				String uploadPath = FreemarkerUtils.process(setting.getImageUploadPath(), model);
				String uuid = UUID.randomUUID().toString();
				String sourcePath = uploadPath + uuid + "-source." + FilenameUtils.getExtension(file.getName());
				String largePath = uploadPath + uuid + "-large." + DEST_EXTENSION;
				String mediumPath = uploadPath + uuid + "-medium." + DEST_EXTENSION;
				String thumbnailPath = uploadPath + uuid + "-thumbnail." + DEST_EXTENSION;

				Collections.sort(storagePlugins);
				for (StoragePlugin storagePlugin : storagePlugins) {
					if (storagePlugin.getIsEnabled()) {
						
						File tempFile = new File(System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp");
						if (!tempFile.getParentFile().exists()) {
							tempFile.getParentFile().mkdirs();
						}
						
						try {
							FileCopyUtils.copy(file, tempFile);
							addTask(sourcePath, largePath, mediumPath, thumbnailPath, tempFile, null);
							productImage.setSource(storagePlugin.getUrl(sourcePath));
							productImage.setLarge(storagePlugin.getUrl(largePath));
							productImage.setMedium(storagePlugin.getUrl(mediumPath));
							productImage.setThumbnail(storagePlugin.getUrl(thumbnailPath));
						} catch (Exception e) {
						}
						
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}