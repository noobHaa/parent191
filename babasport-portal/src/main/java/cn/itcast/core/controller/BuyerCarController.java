package cn.itcast.core.controller;

import java.io.StringWriter;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.itcast.common.utils.RequestUtils;
import cn.itcast.common.web.Constants;
import cn.itcast.core.bean.BuyerCart;
import cn.itcast.core.bean.BuyerItem;
import cn.itcast.core.bean.order.Order;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.service.product.SkuService;
import cn.itcast.core.service.user.BuyerService;
import cn.itcast.core.service.user.SessionProvider;

/**
 * 购物车
 * 
 * @author LL
 *
 */
@Controller
public class BuyerCarController {
	@Autowired
	private SessionProvider sessionProvider;

	// 购物车添加
	@RequestMapping(value = "/buyerCart")
	public String addCart(Long skuId, Integer amount, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		BuyerCart buyerCart = null;

		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(Include.NON_NULL);
		// 1从request中取出cookies
		Cookie[] cookies = request.getCookies();
		// 2遍历cookie
		if (null != cookies && cookies.length > 0) {
			// 遍历cookie
			for (Cookie cookie : cookies) {
				// 3判断cookie中是否存在购物车
				if (cookie.getName().equals(Constants.BUYER_CAR)) {
					buyerCart = om.readValue(cookie.getValue(), BuyerCart.class);
					break;
				}
			}
		}
		// 4没有就创建
		if (null == buyerCart) {
			buyerCart = new BuyerCart();
		}
		// 创建对象
		Sku sku = new Sku();
		sku.setId(skuId);
		BuyerItem buyerItem = new BuyerItem();
		buyerItem.setAmount(amount);
		buyerItem.setSku(sku);
		// 追加商品到购物车 是否存在相同款
		buyerCart.addBuyerItem(buyerItem);

		// 判断用户是否登录
		String username = sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(request, response));
		if (null != username) {
			buyerService.insertBuyerCartToRedis(buyerCart, username);
			// 清理cookie
			Cookie cookie = new Cookie(Constants.BUYER_CAR, null);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
		} else {
			// 非登录
			// 5将新购物车放回cookie
			StringWriter w = new StringWriter();
			om.writeValue(w, buyerCart);
			Cookie cookie = new Cookie(Constants.BUYER_CAR, w.toString());
			// 设置时间
			cookie.setMaxAge(60 * 60 * 24);
			// 设置路径
			cookie.setPath("/");
			// 设置域 上线后考虑
			// 写回浏览器
			response.addCookie(cookie);
		}
		return "redirect:/toCart";
	}

	@Autowired
	private SkuService skuService;
	@Autowired
	private BuyerService buyerService;

	// 去购物车页面
	@RequestMapping(value = "/toCart")
	public String toCart(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		BuyerCart buyerCart = null;
		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(Include.NON_NULL);
		// 获取cookie 遍历cookie
		Cookie[] cookies = request.getCookies();
		if (null != cookies && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				// 判断是否存在
				if (cookie.getName().equals(Constants.BUYER_CAR)) {
					buyerCart = om.readValue(cookie.getValue(), BuyerCart.class);
					break;
				}
			}
		}
		// 判断是否登录
		String username = sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(request, response));
		if (null != username) {
			// 取出cookie中的购物车 存入redis
			if (null != buyerCart) {
				buyerService.insertBuyerCartToRedis(buyerCart, username);
				// 清理cookie
				Cookie cookie = new Cookie(Constants.BUYER_CAR, null);
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
			// 获取全部购物车
			buyerCart = buyerService.selectCartFromRedisByCartName(username);
		}
		// 判断有无
		if (null != buyerCart) {
			List<BuyerItem> items = buyerCart.getItems();
			// 有 购物车装满 因为cookie中存放的只是id
			for (BuyerItem buyerItem : items) {
				buyerItem.setSku(skuService.selectSkuById(buyerItem.getSku().getId()));
			}
		}
		// 存入
		model.addAttribute("buyerCart", buyerCart);
		return "cart";
	}

	// 结算商品 要求用户必须登录
	@RequestMapping(value = "/buyer/trueBuy")
	public String trueBuy(Long[] skuIds, Model model, HttpServletRequest request, HttpServletResponse response) {
		// 购物车必须有货
		String username = sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(request, response));
		// 从redis中取出购物车
		BuyerCart buyerCart = buyerService.selectCartFromRedisByCartName(username);
		List<BuyerItem> items = buyerCart.getItems();

		// 标识 是否存在无货
		Boolean flog = false;
		// 判断购物车是否有商品
		if (items.size() > 0) {
			// 把购物车装满
			for (BuyerItem buyerItem : items) {
				buyerItem.setSku(skuService.selectSkuById(buyerItem.getSku().getId()));
				// 购买的数量大于库存
				if (buyerItem.getAmount() > buyerItem.getSku().getStock()) {
					// 商品必须有库存
					buyerItem.setIsHave(false);
					flog = true;
				}
			}
			// 如果存在无货就刷新购物车页面
			if (flog) {
				model.addAttribute("buyerCart", buyerCart);
				return "cart";
			}
		} else {
			return "redirect:/toCart";
		}
		// 都有货进入下个页面
		return "order";

	}

	// 结算商品 要求用户必须登录
	@RequestMapping(value = "/buyer/submitOrder")
	public String submitOrder(Order order, Long[] skuIds, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		String username = sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(request, response));
		buyerService.insertOrder(order, username);
		return "success";

	}
}
