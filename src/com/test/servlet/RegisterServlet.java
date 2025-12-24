package com.test.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.dao.ReaderDAO;
import com.test.pojo.Reader;
import com.test.pojo.ReaderCriteria;
import com.test.pojo.User;
import com.test.util.RequestUtil;
import com.test.util.SystemConstants;

/**
 * 读者注册入口。
 * 包含基础信息的非空校验、重复账号/身份证校验以及持久化创建。
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private ReaderDAO readerDAO = new ReaderDAO();

    /**
     * 处理注册请求，校验必填项并确保用户名、身份证号唯一。
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String username = RequestUtil.getString(request, "username");
        if (username == null) {
            error("用户名不能为空", request, response);
            return;
        }
        String name = RequestUtil.getString(request, "name");
        if (name == null) {
            error("姓名不能为空", request, response);
            return;
        }
        String password = RequestUtil.getString(request, "password");
        if (password == null) {
            error("密码不能为空", request, response);
            return;
        }
        String repeatPassword = RequestUtil.getString(request, "repwd");
        if (repeatPassword == null) {
            error("确认密码不能为空", request, response);
            return;
        }
        if (!repeatPassword.equals(password)) {
            error("两次密码不一致", request, response);
            return;
        }
        String mobile = RequestUtil.getString(request, "mobile");
        if (mobile == null) {
            error("手机号不能为空", request, response);
            return;
        }
        String idCard = RequestUtil.getString(request, "idCard");
        if (idCard == null) {
            error("身份证号不能为空", request, response);
            return;
        }
        // 查询出数据库中用户
        User user = readerDAO.getUserByUsername(username);

        if (user != null) {
            error("用户名已存在", request, response);
            return;
        }
        ReaderCriteria criteria = new ReaderCriteria();
        criteria.setIdCard(idCard);
        int count = readerDAO.getCount(criteria);
        if (count > 0) {
            error("身份证号已注册", request, response);
            return;
        }

        Reader reader = new Reader();
        reader.setStatus(SystemConstants.STATUS_COMMON);
        reader.setIdCard(idCard);
        reader.setMobile(mobile);
        reader.setPassword(password);
        reader.setUsername(username);
        reader.setName(name);
        int result = readerDAO.add(reader);
        if (result == 1) {
            // 成功后回到登录页
            request.getRequestDispatcher(SystemConstants.PAGE_PATH + "login.jsp").forward(request, response);
        } else {
            error("注册失败，请稍后再试", request, response);
        }

    }

    /**
     * 统一的错误处理：附带提示信息返回注册页。
     */
    public void error(String message, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        request.setAttribute("message", message);
        request.getRequestDispatcher(SystemConstants.PAGE_PATH + "register.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request, response);
    }

}
