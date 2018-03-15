package cn.itcast.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.product.SkuQuery;
import cn.itcast.core.dao.product.ColorDao;
import cn.itcast.core.dao.product.ProductDao;
import cn.itcast.core.dao.product.SkuDao;

@Service("cmsService")
public class CmsServiceImpl implements CmsService {

	@Autowired
	private ProductDao productDao;
	@Autowired
	private SkuDao skuDao;
	@Autowired
	private ColorDao colorDao;

	// 返回Product对象
	public Product selectProductById(Long productId) {
		return productDao.selectByPrimaryKey(productId);

	}

	// 遍历sku结果集
	public List<Sku> selectSkuListByProductId(Long id) {

		SkuQuery skuQuery = new SkuQuery();
		// 筛选商品id为productId的sku对象 库存大于0
		skuQuery.createCriteria().andProductIdEqualTo(id).andStockGreaterThan(0);
		List<Sku> skus = skuDao.selectByExample(skuQuery);
		for (Sku sku : skus) {
			sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		}

		return skus;

	}
}
