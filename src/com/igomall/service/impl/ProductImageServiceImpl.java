
package com.igomall.service.impl;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import com.igomall.Setting;
import com.igomall.dao.ProductImageDao;
import com.igomall.entity.ProductImage;
import com.igomall.plugin.StoragePlugin;
import com.igomall.service.ProductImageService;
import com.igomall.util.FreemarkerUtils;
import com.igomall.util.ImageUtils;
import com.igomall.util.SettingUtils;

@Service("productImageServiceImpl")
public class ProductImageServiceImpl extends BaseServiceImpl<ProductImage,Long> implements ProductImageService, ServletContextAware {

	private static final String DEST_EXTENSION = "jpg";
	private static final String DEST_CONTENT_TYPE = "image/jpeg";

	private ServletContext servletContext;

	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	@Resource(name = "productImageDaoImpl")
	public ProductImageDao productImageDao;
	
	
	
	@Resource
	private List<StoragePlugin> storagePlugins;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	@Resource(name = "productImageDaoImpl")
	public void setBaseDao(ProductImageDao productImageDao) {
		super.setBaseDao(productImageDao);
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
		MultipartFile multipartFile = productImage.getFile();
		if (multipartFile != null && !multipartFile.isEmpty()) {
			try {
				Setting setting = SettingUtils.get();
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("uuid", UUID.randomUUID().toString());
				String uploadPath = FreemarkerUtils.process(setting.getImageUploadPath(), model);
				String uuid = UUID.randomUUID().toString();
				String sourcePath = uploadPath + uuid + "-source." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
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
						
						multipartFile.transferTo(tempFile);
						addTask(sourcePath, largePath, mediumPath, thumbnailPath, tempFile, multipartFile.getContentType());
						productImage.setSource(storagePlugin.getUrl(sourcePath));
						productImage.setLarge(storagePlugin.getUrl(largePath));
						productImage.setMedium(storagePlugin.getUrl(mediumPath));
						productImage.setThumbnail(storagePlugin.getUrl(thumbnailPath));
					}
				}
			} catch (Exception e) {
				
			}
		}
	}

	@Override
	public List<ProductImage> getProductImage(Long productImage) {
		return productImageDao.getProductImage(productImage);
	}
}