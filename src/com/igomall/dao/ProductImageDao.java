package com.igomall.dao;

import java.util.List;

import com.igomall.entity.ProductImage;

public interface ProductImageDao extends BaseDao<ProductImage, Long> {

	List<ProductImage> getProductImage(Long productImage);

}
