
package com.igomall.service;

import java.util.List;

import com.igomall.entity.ProductImage;

public interface ProductImageService extends BaseService<ProductImage, Long>{

	void build(ProductImage productImage);
	
	List<ProductImage> getProductImage(Long productImage);

}