package cn.itcast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.dao.product.ProductDao;

/**
 * 测试类 junit + Spring
 * 
 * @author lx
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context.xml" })
public class TestTbProduct {

	@Autowired
	private ProductDao productDao;

	@Test
	public void testAdd() throws Exception {

		// 带条件查询
		// Product product = productDao.selectByPrimaryKey(441L);
		// productQuery.createCriteria().andBrandIdEqualTo(4L).andNameLike("%好莱坞%");

		ProductQuery productQuery = new ProductQuery();

		// 分页
		productQuery.setPageNo(1);
		productQuery.setPageSize(11);
		productQuery.setOrderByClause("id desc");

		// 字段查询
		productQuery.setFields("id,brand_id");
		// 查询总数
		int count = productDao.countByExample(productQuery);
		System.out.println(count);
	}
}
