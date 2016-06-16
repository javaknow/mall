
package com.igomall.wechat.controller;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.entity.Brand;
import com.igomall.service.BrandService;

@Controller("wechatBrandController")
@RequestMapping("/wechat/brand")
public class BrandController extends BaseController {

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		Map<String,List<Brand>> map = new HashMap<String,List<Brand>>();
		/*for (Brand brand : brands) {
			brand.setPinyin(Pinyin.toPinyinShort(brand.getName()));
			if(brand.getPinyin()==null||"".equals(brand.getPinyin())){
				brand.setPinyin(brand.getName().toUpperCase());
			}
			brandService.update(brand);
		}*/
		
		
		map = analyse();
		
		model.addAttribute("map", map);
		return "/wechat/brand/list";
	}

	public Map<String, List<Brand>> analyse() {
		Map<String, List<Brand>> map = new TreeMap<String, List<Brand>>(new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.hashCode() - s2.hashCode();
			}
		});
		for (char c='A';c<='Z';c++) {
			List<Brand> brands = brandService.findByPinYin(c);
			map.put(c+"", brands);
		}
		System.out.println(map);
		return map;
	}
}