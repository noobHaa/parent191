package cn.itcast.core.service.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.common.web.Constants;
import cn.itcast.core.bean.BuyerCart;
import cn.itcast.core.bean.BuyerItem;
import cn.itcast.core.bean.order.Detail;
import cn.itcast.core.bean.order.Order;
import cn.itcast.core.bean.user.Buyer;
import cn.itcast.core.bean.user.BuyerQuery;
import cn.itcast.core.dao.order.DetailDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.product.SkuDao;
import cn.itcast.core.dao.user.BuyerDao;
import cn.itcast.core.service.product.SkuService;
import redis.clients.jedis.Jedis;

/**
 * 用户管理
 * 
 * @author lx
 *
 */
@Service("buyerService")
public class BuyerServiceImpl implements BuyerService {

	@Autowired
	private BuyerDao buyerDao;
	@Autowired
	private SkuDao skuDao;

	// 通过用户名查询用户对象
	public Buyer selectBuyerByUsername(String username) {
		BuyerQuery buyerQuery = new BuyerQuery();
		buyerQuery.createCriteria().andUsernameEqualTo(username);

		List<Buyer> buyers = buyerDao.selectByExample(buyerQuery);
		if (null != buyers && buyers.size() > 0) {
			return buyers.get(0);
		}
		return null;
	}

	@Autowired
	private Jedis jedis;

	// 存购物车到redis中 保存商品到redis
	public void insertBuyerCartToRedis(BuyerCart buyerCart, String username) {
		List<BuyerItem> items = buyerCart.getItems();
		if (items.size() > 0) {
			for (BuyerItem item : items) {
				// 判断是否存在相同款
				if (jedis.hexists(Constants.BUYER_CAR + username, String.valueOf(item.getSku().getId()))) {
					jedis.hincrBy(Constants.BUYER_CAR + username, String.valueOf(item.getSku().getId()),
							item.getAmount());
				} else {
					jedis.hset(Constants.BUYER_CAR + username, String.valueOf(item.getSku().getId()),
							String.valueOf(item.getAmount()));
				}
			}
		}
	}

	// 从redis中取出购物车
	public BuyerCart selectCartFromRedisByCartName(String username) {
		BuyerCart buyerCart = new BuyerCart();
		List<BuyerItem> items = new ArrayList<BuyerItem>();
		Map<String, String> hgetAll = jedis.hgetAll(Constants.BUYER_CAR + username);
		// 判断是否存在购物车
		if (hgetAll.size() > 0) {
			Set<Entry<String, String>> entrySet = hgetAll.entrySet();
			for (Entry<String, String> entry : entrySet) {
				// 注意
				BuyerItem item = new BuyerItem();
				item.setSku(skuDao.selectByPrimaryKey(Long.parseLong(entry.getKey())));
				item.setAmount(Integer.valueOf(entry.getValue()));
				items.add(item);
			}
			buyerCart.setItems(items);
		}
		return buyerCart;
	}

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private SkuService skuService;
	@Autowired
	private DetailDao detailDao;

	// 存放订单
	public void insertOrder(Order order, String name) {
		// 全国唯一 订单id
		Long orderId = jedis.incr("orId");

		// 取出购物车
		BuyerCart buyerCart = selectCartFromRedisByCartName(name);
		// 装满购物车
		// 把购物车装满
		List<BuyerItem> items = buyerCart.getItems();
		for (BuyerItem buyerItem : items) {
			buyerItem.setSku(skuService.selectSkuById(buyerItem.getSku().getId()));
			// 先写detail
			Detail detail = new Detail();
			// 数量
			detail.setAmount(buyerItem.getAmount());
			// 颜色
			detail.setColor(buyerItem.getSku().getColor().getName());
			// 价格
			detail.setPrice(buyerItem.getSku().getPrice());
			// 编号
			detail.setProductId(buyerItem.getSku().getProductId());
			// 尺码
			detail.setSize(buyerItem.getSku().getSize());
			// 订单id
			detail.setOrderId(orderId);
			// 商品名称
			detail.setProductName(buyerItem.getSku().getProduct().getName());
			// 保存详情单
			detailDao.insert(detail);
		}
		// 获取用户id
		order.setBuyerId(Long.parseLong(jedis.get(name)));
		// 创建时间
		order.setCreateDate(new Date());
		// 运费
		order.setDeliverFee(buyerCart.getFee());
		// 价格
		order.setOrderPrice(buyerCart.getPrice());
		// 总金额
		order.setTotalPrice(buyerCart.getTotalPrice());
		// 支付状态：0到付1待付款,2已付款,3待退款,4退款成功,5退款失败
		if (order.getPaymentWay() == 1) {
			// 货到付款
			order.setIsPaiy(0);
		} else {
			order.setIsPaiy(1);
		}
		// 订单状态：0:提交订单 1:仓库配货 2:商品出库 3:等待收货 4:完成 5待退货 6已退货
		order.setOrderState(1);
		// 保存订单
		orderDao.insertSelective(order);

		// 清空购物车
		jedis.del(Constants.BUYER_CAR+":fbb2016");
	}
}
