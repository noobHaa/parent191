package cn.itcast.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.itcast.common.utils.RequestUtils;
import cn.itcast.core.service.user.SessionProvider;

/**
 * 配置自定义拦截器 如果用户没有登录就跳转到登录页面 登录则放行
 * 
 * @author LL
 *
 */
public class CustomInterceptor implements HandlerInterceptor {

	@Autowired
	private SessionProvider sessionProvider;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		// 判断用户是否登录
		String username = sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(request, response));
		if (null == username) {
			//跳转到登录页面  登录完成返回至首页面
			response.sendRedirect("http://localhost:8081/login.aspx?returnUrl=http://localhost:8082");
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
