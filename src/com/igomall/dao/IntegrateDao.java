
package com.igomall.dao;

import java.util.List;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Integrate;
import com.igomall.entity.Member;

public interface IntegrateDao extends BaseDao<Integrate, Long> {

	Page<Integrate> findPage(Member member, Pageable pageable);
	
	List<Integrate> findAll(Member member);

}