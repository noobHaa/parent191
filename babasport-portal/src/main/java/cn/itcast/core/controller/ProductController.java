package cn.itcast.core.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.service.CmsService;
import cn.itcast.core.service.SearchService;
import cn.itcast.core.service.product.BrandService;

@Controller
public class ProductController {

	// 去首页 入口
	@RequestMapping(value = "/")
	public String index() {
		return "index";
	}

	@Autowired
	private SearchService searchService;
	@Autowired
	private BrandService brandService;

	// 搜索商品
	@RequestMapping(value = "/search")
	public String search(Model model, String price, Long brandId, Integer pageNo, String keyword) throws Exception {

		List<Brand> brands = brandService.selectBrandFromRedis();
		model.addAttribute("brands", brands);
		// 已选条件容器
		Map<String, String> map = new HashMap<String, String>();
		if (null != brandId) {
			for (Brand brand : brands) {
				if (brand.getId() == brandId) {
					map.put("品牌", brand.getName());
					break;
				}
			}
		}
		if (null != price) {
			if (price.contains("-")) {
				map.put("价格", price);
			} else {
				map.put("价格", price + "以上");
			}
		}
		model.addAttribute("map", map);
		Pagination pagination = searchService.selectPaginationByQuery(pageNo, keyword, price, brandId);
		model.addAttribute("pagination", pagination);
		return "search";
	}

	@Autowired
	private CmsService cmsService;

	@RequestMapping(value = "/product/detail")
	public String detail(Long id, Model model) {
		Product product = cmsService.selectProductById(id);
		List<Sku> skus = cmsService.selectSkuListByProductId(id);
		Set<Color> colors = new HashSet<>();
		for (Sku sku : skus) {
			colors.add(sku.getColor());
		}
		model.addAttribute("skus", skus);
		model.addAttribute("colors", colors);
		model.addAttribute("product", product);

		return "product";
	}

}
