
package com.igomall.dao.impl;

import java.util.List;

import javax.persistence.FlushModeType;

import com.igomall.dao.ShopAttributeDao;
import com.igomall.entity.Shop;
import com.igomall.entity.ShopAttribute;
import com.igomall.entity.ShopAttribute.Type;

import org.springframework.stereotype.Repository;

@Repository("shopAttributeDaoImpl")
public class ShopAttributeDaoImpl extends BaseDaoImpl<ShopAttribute, Long> implements ShopAttributeDao {

	public Integer findUnusedPropertyIndex() {
		for (int i = 0; i < Shop.ATTRIBUTE_VALUE_PROPERTY_COUNT; i++) {
			String jpql = "select count(*) from ShopAttribute shopAttribute where shopAttribute.propertyIndex = :propertyIndex";
			Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("propertyIndex", i).getSingleResult();
			if (count == 0) {
				return i;
			}
		}
		return null;
	}

	public List<ShopAttribute> findList() {
		String jpql = "select shopAttribute from ShopAttribute shopAttribute where shopAttribute.isEnabled = true order by shopAttribute.order asc";
		return entityManager.createQuery(jpql, ShopAttribute.class).setFlushMode(FlushModeType.COMMIT).getResultList();
	}

	@Override
	public void remove(ShopAttribute shopAttribute) {
		if (shopAttribute != null && (shopAttribute.getType() == Type.text || shopAttribute.getType() == Type.select || shopAttribute.getType() == Type.checkbox)) {
			String propertyName = Shop.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + shopAttribute.getPropertyIndex();
			String jpql = "update Shop shops set shops." + propertyName + " = null";
			entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).executeUpdate();
			super.remove(shopAttribute);
		}
	}

}