package cn.itcast.core.service;

import cn.itcast.common.page.Pagination;

public interface SearchService {
	// 全文检索
	public Pagination selectPaginationByQuery(Integer pageNo,String keyword, String price, Long brandId) throws Exception;

	
	// 保存商品到solr服务器
	public void insertProductToSolr(Long id);
}
