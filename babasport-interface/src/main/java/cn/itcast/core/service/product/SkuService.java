package cn.itcast.core.service.product;

import java.util.List;

import cn.itcast.core.bean.product.Sku;

public interface SkuService {
	// 遍历商品
	public List<Sku> selectSkuListByProductId(Long productId);

	// 保存修改
	public void updateSkuById(Sku sku);

	// 根据skuId查询sku对象
	public Sku selectSkuById(Long skuId);

}
