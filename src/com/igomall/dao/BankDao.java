
package com.igomall.dao;

import java.util.List;

import com.igomall.entity.Bank;

public interface BankDao extends BaseDao<Bank, Long> {

	List<Bank> findAll(Boolean isEnabled);

}