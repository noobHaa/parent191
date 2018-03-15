package cn.itcast.core.dao.product;

import cn.itcast.core.bean.product.BrandQuery;

import java.util.List;

import cn.itcast.core.bean.product.Brand;

public interface BrandDao {

	// 带条件查询
	public List<Brand> selectBrandListByQuery(BrandQuery brandQuery);

	// 查询总条数
	public Integer selectCount(BrandQuery brandQuery);

	// id查询
	public Brand selectBrandById(Long id);

	// 修改
	public void updateBrand(Brand brand);

	// 批量删除
	public void deletesBrand(Long[] ids);
}
