package cn.itcast.core.service.staticpage;

import java.util.Map;

public interface StaticPageService {

	// 静态化商品详情页面
	public void productStaticPage(Map<String, Object> root, String id);

}
