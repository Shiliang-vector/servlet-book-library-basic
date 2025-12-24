package com.test.servlet;

import com.test.util.RequestUtil;
import com.test.util.SystemConstants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 浏览器访问 /page?act=userList
 *             ↓
 * Servlet 获取 act="userList"
 *             ↓
 * 拼接成  "/WEB-INF/page/userList.jsp"
 *             ↓
 * forward 转发到对应 JSP
 */


@WebServlet("/page")
public class PageServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String page = RequestUtil.getString(request, "act");
        if (page == null) {
            // 默认首页
            page = "login";
        }
        request.getRequestDispatcher(SystemConstants.PAGE_PATH + page + ".jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request, response);
    }

}
