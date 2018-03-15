package cn.itcast.core.service.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.BrandQuery;
import cn.itcast.core.dao.product.BrandDao;
import redis.clients.jedis.Jedis;

@Service("brandService")
@Transactional
public class BrandServiceImpl implements BrandService {
	@Autowired
	private BrandDao brandDao;

	// 查询分页对象
	public Pagination selectPaginationByBrandQuery(String name, Integer isDisplay, Integer pageNo) {
		BrandQuery brandQuery = new BrandQuery();
		// 当前页
		brandQuery.setPageNo(Pagination.cpn(pageNo));// cpn是静态方法，如果pageNo＜=0自动赋值为1
		// 每页数
		brandQuery.setPageSize(1);

		StringBuilder params = new StringBuilder();

		// 条件
		if (null != name) {
			brandQuery.setName(name);
			params.append("name=").append(name);
		}
		if (null != isDisplay) {
			brandQuery.setIsDisplay(isDisplay);
			params.append("&isDisplay=").append(isDisplay);
		} else {
			brandQuery.setIsDisplay(1);
			params.append("&isDisplay=").append(1);
		}

		Pagination pagination = new Pagination(brandQuery.getPageNo(), brandQuery.getPageSize(),
				brandDao.selectCount(brandQuery));
		// 设置结果集
		pagination.setList(brandDao.selectBrandListByQuery(brandQuery));
		// 分页展示
		String url = "/brand/list.do";

		pagination.pageView(url, params.toString());

		return pagination;

	}

	// 通过id查询
	public Brand selectBrandById(Long id) {
		return brandDao.selectBrandById(id);
	}

	@Autowired
	private Jedis jedis;

	// 修改
	public void updateBrand(Brand brand) {
		// 品牌修改保存时添加到redis中 hashset类似于map
		jedis.hset("brand", String.valueOf(brand.getId()), brand.getName());
		brandDao.updateBrand(brand);
	}

	// 删除
	public void deletesBrand(Long[] ids) {
		brandDao.deletesBrand(ids);
	}

	// 查询所有品牌
	public List<Brand> selectAll(Integer isDisplay) {
		BrandQuery brandQuery = new BrandQuery();
		brandQuery.setIsDisplay(isDisplay);
		List<Brand> brands = brandDao.selectBrandListByQuery(brandQuery);
		return brands;
	}

	// 从redis中查询品牌
	public List<Brand> selectBrandFromRedis() {
		List<Brand> brands = new ArrayList<Brand>();
		// 获取结果集
		Map<String, String> hgetAll = jedis.hgetAll("brand");
		Set<Entry<String, String>> entrySet = hgetAll.entrySet();
		for (Entry<String, String> entry : entrySet) {
			Brand brand = new Brand();
			brand.setId(Long.parseLong(entry.getKey()));
			brand.setName(entry.getValue());

			brands.add(brand);
		}
		return brands;
	}
}
