package cn.itcast.core.service.user;

import cn.itcast.core.bean.BuyerCart;
import cn.itcast.core.bean.order.Order;
import cn.itcast.core.bean.user.Buyer;

public interface BuyerService {
	// 通过用户名查询
	public Buyer selectBuyerByUsername(String username);

	// 存购物车到redis中 保存商品到redis
	public void insertBuyerCartToRedis(BuyerCart buyerCart, String username);

	// 从redis中取出购物车
	public BuyerCart selectCartFromRedisByCartName(String username);

	// 存放订单
	public void insertOrder(Order order, String name);

}
