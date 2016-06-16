
package com.igomall.dao;

import com.igomall.entity.Specification;
import com.igomall.entity.SpecificationValue;

public interface SpecificationValueDao extends BaseDao<SpecificationValue, Long> {

	SpecificationValue findByTaoBaoId(Specification specification, Long taobaoId);

}