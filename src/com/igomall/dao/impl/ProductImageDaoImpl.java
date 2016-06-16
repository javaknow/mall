package com.igomall.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.igomall.dao.ProductImageDao;
import com.igomall.entity.ProductImage;

@Repository("productImageDaoImpl")
public class ProductImageDaoImpl extends BaseDaoImpl<ProductImage, Long> implements ProductImageDao {

	@Override
	public List<ProductImage> getProductImage(Long productImage) {
		
		return null;
	}

}