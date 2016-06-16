package com.igomall.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.igomall.dao.BrandDao;
import com.igomall.entity.Brand;
import com.igomall.entity.ProductCategory;

@Repository("brandDaoImpl")
public class BrandDaoImpl extends BaseDaoImpl<Brand, Long> implements BrandDao {

	@Override
	public List<Brand> findList(List<ProductCategory> productCategorys,Integer count) {
		List<Brand> list = new ArrayList<Brand>();
		List<Brand> list1 = new ArrayList<Brand>();
		for (ProductCategory productCategory : productCategorys) {
			list.addAll(productCategory.getBrands());
		}
		if (count != null && list.size() > count) {
			for (int i = 0; i < count; i++) {
				list1.add(list.get(i));
			}

			return list1;
		}

		return list;
	}

	@Override
	public List<Brand> findByPinYin(Character c) {
		String pinyin = c.toString();
		if (pinyin == null) {
			return null;
		}
		try {
			String jpql = "select brands from Brand brands where brands.pinyin like :pinyin";
			return entityManager.createQuery(jpql, Brand.class).setFlushMode(FlushModeType.COMMIT).setParameter("pinyin", pinyin+"%").getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

}