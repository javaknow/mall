
package com.igomall.dao;

import com.igomall.entity.Member;
import com.igomall.entity.WeChatMember;

public interface WeChatMemberDao extends BaseDao<WeChatMember, Long> {

	boolean openIdExists(String openId);
	
	WeChatMember findByOpenId(String openId);

	WeChatMember getWeChatMember(Member member);

}