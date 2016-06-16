
package com.igomall.service;

import java.util.List;

import com.igomall.entity.Bank;

public interface BankService extends BaseService<Bank, Long> {

	List<Bank> findAll(Boolean isEnabled);
	

}