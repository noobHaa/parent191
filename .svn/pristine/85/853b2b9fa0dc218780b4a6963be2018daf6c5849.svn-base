package cn.itcast.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/control")
public class CenterController {

	@RequestMapping(value = "/index.do")
	public String index(Model model) {

		return "index";
	}

	// 头
	@RequestMapping(value = "/top")
	public String top(Model model) {

		return "top";
	}

	// 身体
	@RequestMapping(value = "/main")
	public String main(Model model) {

		return "main";
	}

	// 身体右
	@RequestMapping(value = "/right")
	public String right(Model model) {

		return "right";
	}

	// 身体左
	@RequestMapping(value = "/left")
	public String left(Model model) {

		return "left";
	}
	// 商品身体
	@RequestMapping(value = "/frame/product_main")
	public String product_main(Model model) {
		
		return "/frame/product_main";
	}
	// 商品身体左
	@RequestMapping(value = "/frame/product_left")
	public String product_left(Model model) {
		
		return "/frame/product_left";
	}
}
