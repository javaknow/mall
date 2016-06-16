
package com.igomall.wechat.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.entity.Article;
import com.igomall.service.ArticleService;

@Controller("wapArticleController")
@RequestMapping("/wechat/article")
public class ArticleController extends BaseController {
	
	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;
	
	@RequestMapping(value="content",method = RequestMethod.GET)
	public String index(ModelMap model,Long articleId) {
		Article article = articleService.find(articleId);
		model.addAttribute("article",article);
		return "wap/article/content";
	}
}