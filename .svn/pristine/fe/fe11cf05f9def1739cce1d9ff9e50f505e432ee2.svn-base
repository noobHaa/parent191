package cn.itcast;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.core.bean.TestTb;
import cn.itcast.core.service.TestTbService;

/**
 * 测试类 junit + Spring
 * 
 * @author lx
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context.xml" })
public class TestTbTest {

	@Autowired
	private TestTbService testTbService;

	@Test
	public void testAdd() throws Exception {
		TestTb testTb = new TestTb();
		testTb.setName("lll0");
		testTb.setBirthday(new Date());

		testTbService.insertTestTb(testTb);

	}
}
