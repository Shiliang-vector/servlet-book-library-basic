package com.test.servlet;

import com.test.dao.AdminDAO;
import com.test.dao.ReaderDAO;
import com.test.pojo.User;
import com.test.util.RequestUtil;
import com.test.util.SystemConstants;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录接口，负责验证管理员或读者身份并将用户信息保存到会话。
 * 通过区分 type 参数来判断是管理员还是读者登录。
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private AdminDAO adminDAO = new AdminDAO();
    private ReaderDAO readerDAO = new ReaderDAO();

    /**
     * 处理登录请求：
     *   校验用户名和密码非空
     *   根据 type 从不同表中查询用户
     *   校验密码后将用户信息写入 Session
     *   登录失败则返回登录页并展示错误信息
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String username = RequestUtil.getString(request, "username");
        if (username == null) {
            error("用户名不能为空", request, response);
            return;
        }
        String password = RequestUtil.getString(request, "password");
        if (password == null) {
            error("密码不能为空", request, response);
            return;
        }
        String type = RequestUtil.getString(request, "type");
        User user = null;
        if (SystemConstants.USER_ADMIN.equals(type)) {
            user = adminDAO.getUserByUsername(username);
        } else {
            user = readerDAO.getUserByUsername(username);
        }
        if (user == null || !user.getPassword().equals(password)) {
            error("用户名或密码错误", request, response);
            return;
        }
        HttpSession session = request.getSession();
        user.setPassword(null);
        user.setType(type);
        session.setAttribute(SystemConstants.SESSION_USER, user);
        // 返回首页地址即可
        response.sendRedirect(request.getContextPath() + "/page?act=index");
    }

    /**
     * 封装错误跳转逻辑，便于在多处复用。
     */
    public void error(String message, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        request.setAttribute("message", message);
        request.getRequestDispatcher(SystemConstants.PAGE_PATH + "login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request, response);
    }

}
