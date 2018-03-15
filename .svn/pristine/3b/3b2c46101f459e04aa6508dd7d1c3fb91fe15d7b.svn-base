package cn.itcast.core.service.product;

import java.util.List;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;

public interface BrandService {

	public Pagination selectPaginationByBrandQuery(String name, Integer isDisplay, Integer pageNo);

	public Brand selectBrandById(Long id);

	public void updateBrand(Brand brand);

	public void deletesBrand(Long[] ids);

	public List<Brand> selectAll(Integer isDisplay);

	// 从redis中查询品牌
	public List<Brand> selectBrandFromRedis();
}
