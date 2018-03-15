package cn.itcast.core.service.staticpage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 静态化管理
 * 
 * @author LL
 *
 */
public class StaticPageServiceImpl implements StaticPageService, ServletContextAware {

	private Configuration conf;

	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
		this.conf = freeMarkerConfigurer.getConfiguration();
	}

	// 静态化商品详情页面
	public void productStaticPage(Map<String, Object> root, String id) {
		// 输出路径的全路径
		String path = getPath("/html/product/" + id + ".html");
		File f = new File(path);
		// 获取目录文件
		File parentFile = f.getParentFile();
		// 判断是否存在
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		Writer out = null;
		try {
			// 加载模板 读文件 语言转换注意
			Template template = conf.getTemplate("product.html");

			// 输出流 最终成文件 写文件
			out = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");

			template.process(root, out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getPath(String path) {

		return servletContext.getRealPath(path);
	}

	// 申明对象
	private ServletContext servletContext;

	@Override
	public void setServletContext(ServletContext servletContext) {
		// TODO Auto-generated method stub
		this.servletContext = servletContext;

	}

}
