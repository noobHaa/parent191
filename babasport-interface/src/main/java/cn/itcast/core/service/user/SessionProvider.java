package cn.itcast.core.service.user;

/**
 * 面向接口编程
 * 用户名的存取
 * 验证码的存取
 * @author LL
 *
 */
public interface SessionProvider {
	// 往redis中存放用户名
	public void setAttributeForUsername(String name, String value);

	// 从redis中取出用户名
	public String getAttributeForUsername(String name);

}
