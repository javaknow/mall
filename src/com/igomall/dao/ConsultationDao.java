
package com.igomall.dao;

import java.util.List;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Consultation;
import com.igomall.entity.Member;
import com.igomall.entity.Product;

public interface ConsultationDao extends BaseDao<Consultation, Long> {

	List<Consultation> findList(Member member, Product product, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders);

	Page<Consultation> findPage(Member member, Product product, Boolean isShow, Pageable pageable);

	Long count(Member member, Product product, Boolean isShow);

}