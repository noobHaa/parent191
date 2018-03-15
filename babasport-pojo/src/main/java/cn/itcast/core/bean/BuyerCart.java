package cn.itcast.core.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 购物车对象
 * 
 * @author LL
 *
 */
public class BuyerCart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 购物项结果集 list<BuyerItem>
	private List<BuyerItem> items = new ArrayList<BuyerItem>();

	// 小计 价格
	// 商品数量
	@JsonIgnore
	public Long getAmount() {
		Long result = 0l;
		if (null != items && items.size() > 0) {
			for (BuyerItem item : items) {
				result += item.getAmount();
			}
		}
		return result;
	}

	// 商品小计
	@JsonIgnore
	public Float getPrice() {
		Float result = 0f;
		if (null != items && items.size() > 0) {
			for (BuyerItem item : items) {
				result += item.getAmount() * item.getSku().getPrice();
			}
		}
		return result;
	}

	// 邮费
	@JsonIgnore
	public Float getFee() {
		Float result = 0f;
		if (getPrice() < 90) {
			result = 10f;
		}
		return result;
	}

	// 总计
	@JsonIgnore
	public Float getTotalPrice() {
		return getFee() + getPrice();
	}

	// 添加商品
	public void addBuyerItem(BuyerItem buyerItem) {
		// 判断是否是相同款
		if (items.contains(buyerItem)) {
			for (BuyerItem it : items) {
				if (it.equals(buyerItem)) {
					it.setAmount(it.getAmount() + buyerItem.getAmount());
				}
			}
		} else {
			items.add(buyerItem);
		}
	}

	public List<BuyerItem> getItems() {
		return items;
	}

	public void setItems(List<BuyerItem> items) {
		this.items = items;
	}

}
