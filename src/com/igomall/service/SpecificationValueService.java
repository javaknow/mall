
package com.igomall.service;

import com.igomall.entity.Specification;
import com.igomall.entity.SpecificationValue;

public interface SpecificationValueService extends BaseService<SpecificationValue, Long> {

	SpecificationValue findByTaoBaoId(Specification specification, Long taobaoId);

}