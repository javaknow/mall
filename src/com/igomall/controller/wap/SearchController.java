package com.igomall.controller.wap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("wapSearchController")
@RequestMapping("/wap/search")
public class SearchController extends BaseController {

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) throws Exception {
		return "/wap/tmpl/search";
	}
}
