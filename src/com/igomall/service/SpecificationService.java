
package com.igomall.service;

import com.igomall.entity.Specification;

public interface SpecificationService extends BaseService<Specification, Long> {

	Specification findByTaoBaoId(Long taobaoId);

	Specification createSpecificationTaoBaoId(Long taobaoId);

}