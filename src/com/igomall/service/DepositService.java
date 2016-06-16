
package com.igomall.service;

import java.util.List;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Deposit;
import com.igomall.entity.Deposit.Type;
import com.igomall.entity.Member;

public interface DepositService extends BaseService<Deposit, Long> {

	Page<Deposit> findPage(Member member, Pageable pageable);

	List<Deposit> findReChageList(Member member);

	Page<Deposit> findRechargePage(Member member, Pageable pageable);

	List<Deposit> findList(Member member, Type rechargecard);

}