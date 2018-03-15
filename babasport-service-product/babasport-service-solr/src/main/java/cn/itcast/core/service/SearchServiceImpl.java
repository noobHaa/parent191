package cn.itcast.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.product.SkuQuery;
import cn.itcast.core.dao.product.ProductDao;
import cn.itcast.core.dao.product.SkuDao;

/**
 * 全文检索用的SOlr
 * 
 * @author lx
 *
 */
@Service("searchService")
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SolrServer solrServer;

	// 全文检索
	public Pagination selectPaginationByQuery(Integer pageNo, String keyword, String price, Long brandId)
			throws Exception {
		// 创建包装类
		ProductQuery productQuery = new ProductQuery();
		// 当前页
		productQuery.setPageNo(Pagination.cpn(pageNo));
		// 每页显示 12条
		productQuery.setPageSize(12);

		// 拼接条件
		StringBuilder params = new StringBuilder();

		List<Product> products = new ArrayList<Product>();
		SolrQuery solrQuery = new SolrQuery();
		// 关键词
		solrQuery.set("q", "name_ik:" + keyword);
		params.append("keyword=").append(keyword);
		// 过滤条件
		if (null != brandId) {
			solrQuery.addFilterQuery("brandId:" + brandId);
		}
		if (null != price) {
			String[] split = price.split("-");
			if (split.length == 2) {
				solrQuery.addFilterQuery("price:[" + split[0] + " TO " + split[1] + "]");
			} else {
				solrQuery.addFilterQuery("price:[" + split[0] + " TO *]");
			}
		}

		// 高亮
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("name_ik");
		// 样式<span style='color:red'>2016</span>
		solrQuery.setHighlightSimplePre("<span style='color:red'>");
		solrQuery.setHighlightSimplePost("</span>");
		// 排序
		solrQuery.addSort("price", ORDER.asc);
		// 分页 limit 开始行，每页显示条数
		solrQuery.setStart(productQuery.getStartRow());
		solrQuery.setRows(productQuery.getPageSize());
		// 执行查询
		QueryResponse response = solrServer.query(solrQuery);

		// 取高亮
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		// Map K : V 442 : Map
		// Map K : V name_ik : List<String>
		// List<String> list 2016年最新款191期卖的瑜伽服最新限量纯手工制作细心打造商户经典买一送一清list.get(0);
		// 结果集
		SolrDocumentList docs = response.getResults();
		// 发现的条数（总条件）构建分页时用到
		long numFound = docs.getNumFound();
		for (SolrDocument doc : docs) {
			// 创建商品对象
			Product product = new Product();
			// 商品ID
			String id = (String) doc.get("id");
			product.setId(Long.parseLong(id));
			// 商品名称ik
			Map<String, List<String>> map = highlighting.get(id);
			List<String> list = map.get("name_ik");
			product.setName(list.get(0));
			/*
			 * String name = (String) doc.get("name_ik"); product.setName(name);
			 */
			// 图片
			String url = (String) doc.get("url");
			product.setImgUrl(url);// img,img2,img3
			// 价格售价 select price from bbs_sku where product_id =442 order by
			// price asc limit 0,1
			product.setPrice((Float) doc.get("price"));
			// 品牌ID Long
			product.setBrandId(Long.parseLong(String.valueOf((Integer) doc.get("brandId"))));

			products.add(product);
		}
		// 构建分页对象
		Pagination pagination = new Pagination(productQuery.getPageNo(), productQuery.getPageSize(), (int) numFound,
				products);
		// 页面展示
		String url = "/search";
		pagination.pageView(url, params.toString());

		return pagination;
	}

	@Autowired
	private ProductDao productDao;
	@Autowired
	private SkuDao skuDao;

	// 保存商品到solr服务器
	public void insertProductToSolr(Long id) {
		// 保存商品到solr服务器
		SolrInputDocument doc = new SolrInputDocument();
		// 商品id
		doc.addField("id", id);
		// 商品name
		Product p = productDao.selectByPrimaryKey(id);
		doc.addField("name_ik", p.getName());
		// 商品url
		doc.addField("url", p.getImages()[0]);
		// brandId
		doc.addField("brandId", p.getBrandId());

		// 商品price 查询最低 select price from bbs where id =? order by price asc
		// limit 1
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.createCriteria().andProductIdEqualTo(id);

		skuQuery.setOrderByClause("price asc");
		// 设置查询限制 只查询一条
		skuQuery.setPageNo(1);
		skuQuery.setPageSize(1);

		skuQuery.setFields("price");// 指定查询字段
		List<Sku> skus = skuDao.selectByExample(skuQuery);
		doc.addField("price", skus.get(0).getPrice());

		try {
			solrServer.add(doc);
			solrServer.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
