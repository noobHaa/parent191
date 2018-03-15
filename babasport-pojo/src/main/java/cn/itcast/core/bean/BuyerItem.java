package cn.itcast.core.bean;

import java.io.Serializable;

import cn.itcast.core.bean.product.Sku;

/**
 * 购物项
 * 
 * @author LL
 *
 */
public class BuyerItem implements Serializable {

	private static final long serialVersionUID = 1L;
	// skuid sku对象中含有自己的id
	private Sku sku;

	// 购买数量
	private Integer amount = 1;

	// 是否有货
	private Boolean isHave = true;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) // 判断地址相同
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())// 判断类相同 cn.itcast.core.bean.BuyerItem
			return false;
		BuyerItem other = (BuyerItem) obj;
		if (sku == null) {
			if (other.sku != null)
				return false;
		} else if (!sku.getId().equals(other.sku.getId()))// 判断是否是同款
			return false;
		return true;
	}

	public Integer getAmount() {
		return amount;
	}

	public Boolean getIsHave() {
		return isHave;
	}

	public Sku getSku() {
		return sku;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sku == null) ? 0 : sku.hashCode());
		return result;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public void setIsHave(Boolean isHave) {
		this.isHave = isHave;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}

	@Override
	public String toString() {
		return "BuyerItem [sku=" + sku + ", amount=" + amount + ", isHave=" + isHave + "]";
	}

}
