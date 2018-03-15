package cn.itcast.core.service.product;

import java.util.List;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Product;

public interface ProductService {
	//商品列表
	public Pagination selectPaginationByProductQuery(String name, Long brandId, Integer pageNo, Boolean isShow);

	// 遍历颜色
	public List<Color> selectColor();

	// 保存商品
	public void insertProduct(Product poduct);
	
	// 上架
	public void isShow(Long[] ids);
}
