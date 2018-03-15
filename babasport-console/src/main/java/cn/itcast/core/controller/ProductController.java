package cn.itcast.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.service.product.BrandService;
import cn.itcast.core.service.product.ProductService;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private BrandService brandService;

	//商品遍历
	@RequestMapping(value = "/product/list.do")
	public String list(String name, Integer pageNo, Long brandId, Boolean isShow, Model model) {
		List<Brand> brands = brandService.selectAll(1);
		model.addAttribute("brands", brands);

		Pagination pagination = productService.selectPaginationByProductQuery(name, brandId, pageNo, isShow);
		model.addAttribute("pagination", pagination);
		model.addAttribute("name", name);
		model.addAttribute("brandId", brandId);
		if (null != isShow) {
			model.addAttribute("isShow", isShow);
		} else {
			model.addAttribute("isShow", false);
		}
		return "/product/list";
	}
   //去添加页面
	@RequestMapping(value = "/product/toAdd.do")
	public String toAdd(Model model) {
		List<Brand> brands = brandService.selectAll(1);
		model.addAttribute("brands", brands);

		List<Color> colors = productService.selectColor();
		model.addAttribute("colors", colors);

		return "/product/add";
	}

	//商品添加
	@RequestMapping(value = "/product/add.do")
	public String add( Product product) {
		productService.insertProduct(product);

		return "redirect:/product/list.do";

	}
	//商品上架
		@RequestMapping(value = "/product/isShow.do")
		public String isShow(Long[] ids){
			productService.isShow(ids);
			return "forward:/product/list.do";
		}
}
