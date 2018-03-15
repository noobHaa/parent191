package cn.itcast.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.service.product.BrandService;

@Controller
public class BrandController {

	@Autowired
	private BrandService brandService;

	@RequestMapping(value = "/brand/list.do")
	public String index(String name, Integer isDisplay, Integer pageNo, Model model) {
		Pagination pagination = brandService.selectPaginationByBrandQuery(name, isDisplay, pageNo);
		model.addAttribute("pagination", pagination);
		// 回显
		model.addAttribute("name", name);
		if (null != isDisplay) {
			model.addAttribute("isDisplay", isDisplay);
		} else {
			model.addAttribute("isDisplay", 1);
		}

		return "brand/list";
	}

	@RequestMapping(value = "/brand/toEdit.do")
	public String toEdit(Long id, Model model) {
		Brand brand = brandService.selectBrandById(id);
		model.addAttribute("brand", brand);
		return "/brand/edit";
	}

	@RequestMapping(value = "/brand/edit.do")
	public String edit(Brand brand) {

		brandService.updateBrand(brand);

		return "redirect:/brand/list.do";
	}

	@RequestMapping(value = "/brand/deletes.do")
	public String deletes(Long[] ids) {
		brandService.deletesBrand(ids);
		return "forward:/brand/list.do";
	}
}
