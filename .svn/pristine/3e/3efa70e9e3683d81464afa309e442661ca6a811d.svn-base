package cn.itcast.core.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 因为是不同的tomcat服务器之间进行通信  传递的诗句需要实现序列化
 * @author LL
 *
 */
public class TestTb implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private Date birthday;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Override
	public String toString() {
		return "TestTb [id=" + id + ", name=" + name + ", birthday=" + birthday + "]";
	}

}
