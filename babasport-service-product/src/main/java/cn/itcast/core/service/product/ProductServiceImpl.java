package cn.itcast.core.service.product;

import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.ColorQuery;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.bean.product.ProductQuery.Criteria;
import cn.itcast.core.bean.product.Sku;

import cn.itcast.core.dao.product.ColorDao;
import cn.itcast.core.dao.product.ProductDao;
import cn.itcast.core.dao.product.SkuDao;
import redis.clients.jedis.Jedis;

@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ColorDao colorDao;

	@Autowired
	private SkuDao skuDao;

	// 商品遍历
	public Pagination selectPaginationByProductQuery(String name, Long brandId, Integer pageNo, Boolean isShow) {

		ProductQuery productQuery = new ProductQuery();
		productQuery.setPageNo(Pagination.cpn(pageNo));
		// 排序
		productQuery.setOrderByClause("id desc");
		// productQuery.s
		Criteria createCriteria = productQuery.createCriteria();
		StringBuilder params = new StringBuilder();
		if (null != name) {
			createCriteria.andNameLike("%" + name + "%");
			params.append("name=").append(name);
		}
		if (null != brandId) {
			createCriteria.andBrandIdEqualTo(brandId);
			params.append("&brandId=").append(brandId);
		}
		if (null != isShow) {
			createCriteria.andIsShowEqualTo(isShow);
			params.append("&isShow=").append(isShow);
		} else {
			createCriteria.andIsShowEqualTo(false);
			params.append("&isShow=").append(false);
		}

		Pagination pagination = new Pagination(productQuery.getPageNo(), productQuery.getPageSize(),
				productDao.countByExample(productQuery), productDao.selectByExample(productQuery));
		String url = "/product/list.do";

		pagination.pageView(url, params.toString());

		return pagination;
	}

	// 遍历颜色
	public List<Color> selectColor() {
		ColorQuery colorQuery = new ColorQuery();
		colorQuery.createCriteria().andParentIdNotEqualTo(0L);
		List<Color> colors = colorDao.selectByExample(colorQuery);

		return colors;
	}

	@Autowired
	private Jedis jedis;

	// 商品保存
	public void insertProduct(Product product) {
		Long id = jedis.incr("pno");
		product.setId(id);
		// 是否删除后台程序写
		product.setIsDel(true);
		// 是否上架后台写
		product.setIsShow(false);
        
		// 保存商品表
		productDao.insert(product);

		// 保存库存表 sku
		Sku sku = new Sku();
		String[] colors = product.getColors().split(",");
		String[] sizes = product.getSizes().split(",");
		for (String color : colors) {
			for (String size : sizes) {
				// 颜色
				sku.setColorId(Long.parseLong(color));
				// 尺码
				sku.setSize(size);
				// productId
				sku.setProductId(product.getId());
				// 限制
				sku.setUpperLimit(200);
				// 市场价
				sku.setMarketPrice(1000f);
				// 价格
				sku.setPrice(999f);
				// 运费
				sku.setDeliveFee(8f);
				// 创建时间
				sku.setCreateTime(new Date());
				// 库存
				sku.setStock(0);

				skuDao.insert(sku);

			}
		}

	}

	@Autowired
	private JmsTemplate jmsTemplate;

	// 上架
	public void isShow(Long[] ids) {
		//上架
		Product product = new Product();
		product.setIsShow(true);

		for (final Long id : ids) {
			product.setId(id);
			// 商品状态改变
			productDao.updateByPrimaryKeySelective(product);

			// 发送消息到ActiveMQ
			/*
			 * 指定目标管道 jmsTemplate.send(destination, messageCreator);
			 */
			jmsTemplate.send(new MessageCreator() {

				@Override
				public Message createMessage(Session session) throws JMSException {

					return session.createTextMessage(String.valueOf(id));
				}
			});
		}
		//静态化  todo
	}
}
