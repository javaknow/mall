
package com.igomall.service;

import java.util.List;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Integrate;
import com.igomall.entity.Member;

public interface IntegrateService extends BaseService<Integrate, Long> {

	Page<Integrate> findPage(Member member, Pageable pageable);

	List<Integrate> findAll(Member member);

}