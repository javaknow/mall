
package com.igomall.controller.admin;

import javax.annotation.Resource;

import com.igomall.Message;
import com.igomall.Pageable;
import com.igomall.entity.Ad;
import com.igomall.entity.AdPosition;
import com.igomall.entity.Ad.Category;
import com.igomall.entity.Ad.Type;
import com.igomall.service.AdPositionService;
import com.igomall.service.AdService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminAdController")
@RequestMapping("/admin/ad")
public class AdController extends BaseController {

	@Resource(name = "adServiceImpl")
	private AdService adService;
	@Resource(name = "adPositionServiceImpl")
	private AdPositionService adPositionService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model,Category category) {
		model.addAttribute("types", Type.values());
		if(category==Category.pc){
			model.addAttribute("adPositions", adPositionService.findList(AdPosition.Type.pc));
		}else if(category==Category.phone){
			model.addAttribute("adPositions", adPositionService.findList(AdPosition.Type.phone));
		}else{
			model.addAttribute("adPositions", adPositionService.findList(AdPosition.Type.pc));
		}
		model.addAttribute("category", category);
		return "/admin/ad/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Ad ad, Long adPositionId, RedirectAttributes redirectAttributes) {
		ad.setAdPosition(adPositionService.find(adPositionId));
		if (!isValid(ad)) {
			return ERROR_VIEW;
		}
		if (ad.getBeginDate() != null && ad.getEndDate() != null && ad.getBeginDate().after(ad.getEndDate())) {
			return ERROR_VIEW;
		}
		if (ad.getType() == Type.text) {
			ad.setPath(null);
		} else {
			ad.setContent(null);
		}
		adService.save(ad);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("types", Type.values());
		model.addAttribute("ad", adService.find(id));
		model.addAttribute("adPositions", adPositionService.findList(AdPosition.Type.pc));
		return "/admin/ad/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Ad ad, Long adPositionId, RedirectAttributes redirectAttributes) {
		ad.setAdPosition(adPositionService.find(adPositionId));
		if (!isValid(ad)) {
			return ERROR_VIEW;
		}
		if (ad.getBeginDate() != null && ad.getEndDate() != null && ad.getBeginDate().after(ad.getEndDate())) {
			return ERROR_VIEW;
		}
		if (ad.getType() == Type.text) {
			ad.setPath(null);
		} else {
			ad.setContent(null);
		}
		adService.update(ad);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model,Category category) {
		if(category==null){
			category = Category.pc;
		}
		model.addAttribute("page", adService.findPage(pageable,category));
		model.addAttribute("category", category);
		return "/admin/ad/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		adService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}