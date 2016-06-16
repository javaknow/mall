
package com.igomall.service;

import com.igomall.entity.Member;
import com.igomall.entity.WeChatMember;

public interface WeChatMemberService extends BaseService<WeChatMember, Long> {

	boolean openIdExists(String openId);

	WeChatMember findByOpenId(String openId);

	/**
	 * 创建微信用户
	 * @param fromUserName 用户的openId
	 * @param parent 推荐人
	 * @return
	 * @author 夏黎
	 */
	WeChatMember create(String fromUserName, Member parent);

	/**
	 * 跟新用户
	 */
	WeChatMember update(WeChatMember weChatMember);

	WeChatMember getWeChatMember(Member member);
	
	WeChatMember getCurrent();
	
	String getCurrentOpenid();

}