package cn.itcast.core.message;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.service.CmsService;
import cn.itcast.core.service.staticpage.StaticPageService;

/**
 * 监听器 可以获取管道中的信息
 * 
 * @author LL
 *
 */
public class CustomMessageListener implements MessageListener {

	@Autowired
	private StaticPageService staticPageService;
	@Autowired
	private CmsService cmsService;

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		ActiveMQTextMessage am = (ActiveMQTextMessage) message;
		
		try {
			String id = am.getText();
			Map<String, Object> root = new HashMap<>();
			Product product = cmsService.selectProductById(Long.parseLong(id));
			List<Sku> skus = cmsService.selectSkuListByProductId(Long.parseLong(id));
			Set<Color> colors = new HashSet<>();
			for (Sku sku : skus) {
				colors.add(sku.getColor());
			}
			root.put("skus", skus);
			root.put("colors", colors);
			root.put("product", product);

			staticPageService.productStaticPage(root, id);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
