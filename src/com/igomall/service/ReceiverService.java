
package com.igomall.service;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Member;
import com.igomall.entity.Receiver;

public interface ReceiverService extends BaseService<Receiver, Long> {

	Receiver findDefault(Member member);

	Page<Receiver> findPage(Member member, Pageable pageable);

}