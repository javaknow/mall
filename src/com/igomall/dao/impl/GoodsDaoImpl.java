/*
 * 
 * 
 * 
 */
package com.igomall.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.dao.GoodsDao;
import com.igomall.dao.ProductDao;
import com.igomall.dao.SnDao;
import com.igomall.entity.Goods;
import com.igomall.entity.Product;
import com.igomall.entity.SpecificationValue;
import com.igomall.entity.Sn.Type;

@Repository("goodsDaoImpl")
public class GoodsDaoImpl extends BaseDaoImpl<Goods, Long> implements GoodsDao {

	@Resource(name = "productDaoImpl")
	private ProductDao productDao;
	@Resource(name = "snDaoImpl")
	private SnDao snDao;

	@Override
	public void persist(Goods goods) {
		Assert.notNull(goods);

		if (goods.getProducts() != null) {
			for (Product product : goods.getProducts()) {
				setValue(product);
			}
		}
		super.persist(goods);
	}

	@Override
	public Goods merge(Goods goods) {
		Assert.notNull(goods);

		if (goods.getProducts() != null) {
			for (Product product : goods.getProducts()) {
				if (product.getId() != null) {
					if (!product.getIsGift()) {
						String jpql = "delete from GiftItem giftItem where giftItem.gift = :product";
						entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("product", product).executeUpdate();
					}
					if (!product.getIsMarketable() || product.getIsGift()) {
						String jpql = "delete from CartItem cartItem where cartItem.product = :product";
						entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("product", product).executeUpdate();
					}
				}
				setValue(product);
			}
		}
		return super.merge(goods);
	}

	private void setValue(Product product) {
		if (product == null) {
			return;
		}
		if (StringUtils.isEmpty(product.getSn())) {
			String sn;
			do {
				sn = snDao.generate(Type.product);
			} while (productDao.snExists(sn));
			product.setSn(sn);
		}
		StringBuffer fullName = new StringBuffer(product.getName());
		if (product.getSpecificationValues() != null && !product.getSpecificationValues().isEmpty()) {
			List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>(product.getSpecificationValues());
			Collections.sort(specificationValues, new Comparator<SpecificationValue>() {
				public int compare(SpecificationValue a1, SpecificationValue a2) {
					return new CompareToBuilder().append(a1.getSpecification(), a2.getSpecification()).toComparison();
				}
			});
			fullName.append(Product.FULL_NAME_SPECIFICATION_PREFIX);
			int i = 0;
			for (Iterator<SpecificationValue> iterator = specificationValues.iterator(); iterator.hasNext(); i++) {
				if (i != 0) {
					fullName.append(Product.FULL_NAME_SPECIFICATION_SEPARATOR);
				}
				fullName.append(iterator.next().getName());
			}
			fullName.append(Product.FULL_NAME_SPECIFICATION_SUFFIX);
		}
		product.setFullName(fullName.toString());
	}

	@Override
	public List<Goods> findList(Integer count, List<Filter> filters,List<Order> orders, int index) {
		List<Goods> list = super.findList(null, null, null, null);
		Integer size = list.size();
		Integer length = 0;
		List<Goods> list1 = new ArrayList<Goods>();
		for(int i=0;i<10;i++){
			length = new Random().nextInt(size);
			list1.add(list.get(length));
		}
		return list1;
	}

}