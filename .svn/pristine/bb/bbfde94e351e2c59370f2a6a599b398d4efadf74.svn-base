package cn.itcast;

import java.io.StringWriter;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.itcast.core.bean.user.Buyer;

public class TestJson {

	/**
	 * 对象和json互转
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRedis() throws Exception {

		Buyer buyer = new Buyer();
		buyer.setRealName("范冰冰");

		ObjectMapper om = new ObjectMapper();

		// 使用含有缓冲的流
		StringWriter w = new StringWriter();
		// 不包含null的字节
		om.setSerializationInclusion(Include.NON_NULL);
		// 将对象写成流
		om.writeValue(w, buyer);
		System.out.println(w.toString());

		// json转对象
		Buyer r = om.readValue(w.toString(), Buyer.class);
		System.out.println(r);
	}
}