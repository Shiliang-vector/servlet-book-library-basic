package com.test.filter;

import com.test.util.SystemConstants;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录过滤器：保证除登录/注册与静态资源外的请求都需要登录态。
 * 用户请求进入应用
 *       ↓
 * 是否访问静态资源？是 -> 放行；否 -> 继续
 * 是否访问 /login 或 /register？是 -> 放行；否 -> 继续
 * Session 中是否存在用户？否 -> 转发到登录页；是 -> 放行
 */


@WebFilter(value = "/*", filterName = "filter02")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        //把父类ServletRequest和ServletResponse转化为子类HttpServletRequest和HttpServletResponse
        //用于拿到session中登录的信息
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String uri = request.getServletPath();
        if (uri.startsWith("/static/")) {
            filterChain.doFilter(request, response);
            return;
            //静态资源放行
        }
        if ("/login".equals(uri) || "/register".equals(uri)) {
            filterChain.doFilter(request, response);
            return;
            //登录注册放行
        }
        HttpSession session = request.getSession();
        if (session.getAttribute(SystemConstants.SESSION_USER) == null) {
            request.getRequestDispatcher(SystemConstants.PAGE_PATH + "login.jsp").forward(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
        //判断 session 是否有用户
    }

    @Override
    public void destroy() {

    }
}
