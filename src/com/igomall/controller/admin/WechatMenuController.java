package com.igomall.controller.admin;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.igomall.Message;
import com.igomall.entity.WechatMenu;
import com.igomall.entity.WechatMenu.Type;
import com.igomall.service.WechatMenuService;
import com.igomall.util.HttpUtils;
import com.igomall.util.JsonUtils;
import com.igomall.util.SettingUtils;
import com.igomall.wechat.CommonWeChatAttributes;
import com.igomall.wechat.entity.Result;
import com.igomall.wechat.util.GetAccessTokenUtil;

@Controller("adminWechatMenuController")
@RequestMapping("/admin/wechatMenu")
public class WechatMenuController extends BaseController {

	@Resource(name = "wechatMenuServiceImpl")
	private WechatMenuService wechatMenuService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("wechatMenuTree", wechatMenuService.findTree(null));
		model.addAttribute("types", WechatMenu.Type.values());
		return "/admin/wechatMenu/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(WechatMenu wechatMenu, Long parentId, RedirectAttributes redirectAttributes) {
		wechatMenu.setParent(wechatMenuService.find(parentId));
		if (!isValid(wechatMenu)) {
			return ERROR_VIEW;
		}
		if(wechatMenu.getType()==Type.click){
			wechatMenu.setUrl(null);
			wechatMenu.setMedia_id(null);
		}else if(wechatMenu.getType()==Type.view){
			wechatMenu.setKey(null);
			wechatMenu.setMedia_id(null);
		}else if(wechatMenu.getType()==Type.media_id||wechatMenu.getType()==Type.view_limited){
			wechatMenu.setKey(null);
			wechatMenu.setUrl(null);
		}
		wechatMenu.setTreePath(null);
		wechatMenu.setGrade(null);
		wechatMenu.setChildren(null);
		wechatMenuService.save(wechatMenu);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:add.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		WechatMenu wechatMenu = wechatMenuService.find(id);
		model.addAttribute("wechatMenuTree", wechatMenuService.findTree(null));
		model.addAttribute("wechatMenu", wechatMenu);
		model.addAttribute("types", WechatMenu.Type.values());
		return "/admin/wechatMenu/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(WechatMenu wechatMenu, Long parentId, RedirectAttributes redirectAttributes) {
		wechatMenu.setParent(wechatMenuService.find(parentId));
		if (!isValid(wechatMenu)) {
			return ERROR_VIEW;
		}
		
		if(wechatMenu.getType()==Type.click){
			wechatMenu.setUrl(null);
			wechatMenu.setMedia_id(null);
		}else if(wechatMenu.getType()==Type.view){
			wechatMenu.setKey(null);
			wechatMenu.setMedia_id(null);
		}else if(wechatMenu.getType()==Type.media_id||wechatMenu.getType()==Type.view_limited){
			wechatMenu.setKey(null);
			wechatMenu.setUrl(null);
		}
		
		wechatMenuService.update(wechatMenu);
		
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		model.addAttribute("wechatMenuTree",wechatMenuService.findTree(null));
		return "/admin/wechatMenu/list";
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long id) {
		WechatMenu wechatMenu = wechatMenuService.find(id);
		if (wechatMenu == null) {
			return ERROR_MESSAGE;
		}
		Set<WechatMenu> children = wechatMenu.getChildren();
		if (children != null && !children.isEmpty()) {
			return Message.error("admin.wechatMenu.deleteExistChildrenNotAllowed");
		}
		wechatMenuService.delete(id);
		return SUCCESS_MESSAGE;
	}
	
	/**
	 * 微信菜单的重新生成
	 * @return
	 */
	@RequestMapping(value="/createWechatMenu",method = RequestMethod.POST)
	public @ResponseBody Message createWechatMenu(HttpServletRequest request,HttpServletResponse response){
		String jsonStr = toJson();
System.out.println(jsonStr);
		String msg = GetAccessTokenUtil.httpRequest(CommonWeChatAttributes.MENU_CREATE_URL.replace("ACCESS_TOKEN", SettingUtils.get().getAccess_token()),"POST",jsonStr);
		Result result = JsonUtils.toObject(msg, Result.class);
System.out.println(result);
		if("0".equals(result.getErrcode())){
			return SUCCESS_MESSAGE;
		}else{
			return Message.error(result.getErrcode()+"<br/>"+result.getErrmsg());
		}
		
	}
	
	/**
	 * @return
	 */
	public String toJson() {
		StringBuffer jsonStr = new StringBuffer();
		jsonStr.append("{\"button\":[");
		List<WechatMenu> rootList = wechatMenuService.findRoots(true);
		if (rootList != null && rootList.size() > 0) {
			String str = "";
			for (WechatMenu wechatMenu : rootList) {
				Set<WechatMenu> children = wechatMenu.getChildrens();
				if(children!=null&&children.size()>0){//有二级菜单
					jsonStr.append("{\"name\":\""+wechatMenu.getName()+"\",");
					jsonStr.append("\"sub_button\":[");
					
					for (WechatMenu wechatMenu2 : children) {
						if(wechatMenu2.getType()==Type.click){
							jsonStr.append("{\"type\":\"click\", \"name\":\""+wechatMenu2.getName()+"\",\"key\":\""+wechatMenu2.getKey()+"\"}");
						}else if(wechatMenu2.getType()==Type.view){
							wechatMenu2.setUrl(HttpUtils.urlEncode(wechatMenu2.getUrl()));
							jsonStr.append("{\"type\":\"view\", \"name\":\""+wechatMenu2.getName()+"\",\"url\":\""+CommonWeChatAttributes.AUTHURL.replace("REDIRECT_URI", wechatMenu2.getUrl()).replace("APPID", CommonWeChatAttributes.APPID)+"\"}");
						}else if(wechatMenu2.getType()==Type.media_id||wechatMenu2.getType()==Type.view_limited){
							jsonStr.append("{\"type\":\"media_id\", \"name\":\""+wechatMenu2.getName()+"\",\"media_id\":\""+wechatMenu2.getMedia_id()+"\"}");
						}else{
							jsonStr.append("{\"type\":\""+wechatMenu2.getType()+"\", \"name\":\""+wechatMenu2.getName()+"\",\"key\":\""+wechatMenu2.getKey()+"\"}");
						}
						jsonStr.append(",");
					}
					str = jsonStr.toString();
					str = str.substring(0,str.lastIndexOf(","));
					jsonStr = new StringBuffer(str);
					str="";
					jsonStr.append("]");
					jsonStr.append("}");
				}else{//没有二级菜单
					if(wechatMenu.getType()==Type.click){
						jsonStr.append("{\"type\":\"click\", \"name\":\""+wechatMenu.getName()+"\",\"key\":\""+wechatMenu.getKey()+"\"}");
					}else if(wechatMenu.getType()==Type.view){
						wechatMenu.setUrl(HttpUtils.urlEncode(wechatMenu.getUrl()));
						jsonStr.append("{\"type\":\"view\", \"name\":\""+wechatMenu.getName()+"\",\"url\":\""+CommonWeChatAttributes.AUTHURL.replace("REDIRECT_URI", wechatMenu.getUrl()).replace("", CommonWeChatAttributes.APPID)+"\"}");
					}else if(wechatMenu.getType()==Type.media_id||wechatMenu.getType()==Type.view_limited){
						jsonStr.append("{\"type\":\"media_id\", \"name\":\""+wechatMenu.getName()+"\",\"media_id\":\""+wechatMenu.getMedia_id()+"\"}");
					}else{
						jsonStr.append("{\"type\":\""+wechatMenu.getType()+"\", \"name\":\""+wechatMenu.getName()+"\",\"key\":\""+wechatMenu.getKey()+"\"}");
					}
				}
				jsonStr.append(",");
			}
			str = jsonStr.toString();
			str = str.substring(0,str.lastIndexOf(","));
			jsonStr = new StringBuffer(str);
			str="";
		}
		jsonStr.append("]}");
		return jsonStr.toString();
	}
	
}