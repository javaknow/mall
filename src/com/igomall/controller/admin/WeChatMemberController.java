
package com.igomall.controller.admin;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.igomall.Pageable;
import com.igomall.Setting;
import com.igomall.entity.Member;
import com.igomall.entity.WeChatMember;
import com.igomall.entity.Member.Gender;
import com.igomall.service.MemberRankService;
import com.igomall.service.MemberService;
import com.igomall.service.WeChatMemberService;
import com.igomall.util.SettingUtils;
import com.igomall.wechat.entity.TotalMember;
import com.igomall.wechat.util.GetAccessTokenUtil;

@Controller("adminWeChatMemberController")
@RequestMapping("/admin/weChatMember")
public class WeChatMemberController extends BaseController {

	@Resource(name = "weChatMemberServiceImpl")
	private WeChatMemberService weChatMemberService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		
		model.addAttribute("page", weChatMemberService.findPage(pageable));
		
		return "/admin/weChatMember/list";
	}
	
	@RequestMapping(value = "/syscInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> syscInfo(){
		
		TotalMember totalMember = GetAccessTokenUtil.getMemberAll();
		
		for (String openId : totalMember.getData().getOpenid()) {
			WeChatMember weChatMember = null;
			
			//数据库中存在
			weChatMember = weChatMemberService.findByOpenId(openId);
			Member member = null;
			if(weChatMember!=null){
				member = weChatMember.getMember();
			}
			
			//根据openid获取用户信息
			com.igomall.wechat.entity.WeChatMember weChatMember1 = GetAccessTokenUtil.getMemberInfo(openId);
			
			if(member==null){
				member = memberService.findByOpenId(weChatMember1.getOpenid());
			}
			
			if("0".equals(weChatMember1.getErrcode())){
				if(weChatMember==null){
					weChatMember = new WeChatMember();
				}
				weChatMember.copyProperties(weChatMember1);
				
				weChatMember.setMember(member);

				if(weChatMember.getId()==null){
					
					try {
						weChatMemberService.save(weChatMember);
					} catch (Exception e) {
						weChatMember.setNickname("");
						//weChatMemberService.update(weChatMember);
						e.printStackTrace();
					}
				}else{
					try {
						weChatMemberService.update(weChatMember);
					} catch (Exception e) {
						weChatMember.setNickname("");
						//weChatMemberService.update(weChatMember);
						e.printStackTrace();
					}
				}
				
				if(member==null){
					member = new Member();
					Setting setting = SettingUtils.get();
					member.setOpenId(weChatMember.getOpenid());
					member.setNumber(System.currentTimeMillis()+"");
					member.setPassword(DigestUtils.md5Hex(setting.getDefaultUserPassword()));
					member.setMemberRank(memberRankService.findDefault());
					member.setUsername(member.getNumber());
					member.setEmail("igmall@qq.com");
					member.setType(2);
					member.setParent(memberService.find(337L));
					member.setGender(Gender.values()[weChatMember.getSex()]);
					member.setWeChatMember(weChatMember);
					memberService.save(member);
					weChatMember.setMember(member);
					try {
						weChatMemberService.update(weChatMember);
					} catch (Exception e) {
						weChatMember.setNickname("");
						//weChatMemberService.update(weChatMember);
						e.printStackTrace();
					}
				}
			}
		}
		Map<String,Object> data = new HashMap<String,Object>();
		/*List<Member> list = memberService.findAll();
		for (Member member : list) {
			if(member.getOpenId()!=null&&!"".equals(member.getOpenId())){
				System.out.println(member.getOpenId());
				WeChatMember weChatMember = null;
				weChatMember = weChatMemberService.findByOpenId(member.getOpenId());
				if(weChatMember==null){
					weChatMember = new WeChatMember();
					com.lx.wechat.entity.WeChatMember weChatMember1 = GetAccessTokenUtil.getMemberInfo(member.getOpenId());
					weChatMember.copyProperties(weChatMember1);
					weChatMember.setMember(member);
					member.setWeChatMember(weChatMember);
					weChatMemberService.save(weChatMember);
					//memberService.update(member);
				}
			}
		}*/
		data.put("message", SUCCESS_MESSAGE);
		data.put("msg","同步数据成功！");
		return data;
	}

}