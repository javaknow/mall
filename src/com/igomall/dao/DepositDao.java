
package com.igomall.dao;

import java.util.List;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Deposit;
import com.igomall.entity.Deposit.Type;
import com.igomall.entity.Member;

public interface DepositDao extends BaseDao<Deposit, Long> {

	Page<Deposit> findPage(Member member, Pageable pageable);

	List<Deposit> findReChageList(Member member);

	Page<Deposit> findRechargePage(Member member, Pageable pageable);

	List<Deposit> findList(Member member, Type type);

}