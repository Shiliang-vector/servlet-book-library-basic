package com.test.servlet;

import com.test.dao.AdminDAO;
import com.test.dao.ReaderDAO;
import com.test.pojo.Reader;
import com.test.pojo.User;
import com.test.util.RequestUtil;
import com.test.util.SystemConstants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 修改密码入口。
 * 校验原密码、新密码一致性后，根据用户类型分别更新管理员或读者表。
 */
@WebServlet("/password")
public class PasswordServlet extends HttpServlet {
    private AdminDAO adminDAO = new AdminDAO();
    private ReaderDAO readerDAO = new ReaderDAO();

    /**
     * 处理密码修改请求，包含必填校验、原密码核对及持久化更新。
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String password = RequestUtil.getString(request, "password");
        if (password == null) {
            error("原始密码不能为空", request, response);
            return;
        }
        String newPassword = RequestUtil.getString(request, "newpwd");
        if (newPassword == null) {
            error("新密码不能为空", request, response);
            return;
        }
        String repeatPassword = RequestUtil.getString(request, "repwd");
        if (repeatPassword == null) {
            error("确认密码不能为空", request, response);
            return;
        }
        if (!repeatPassword.equals(newPassword)) {
            error("新密码和确认密码不一致", request, response);
            return;
        }
        HttpSession session = request.getSession();
        User sessionUser = (User)session.getAttribute(SystemConstants.SESSION_USER);

        int result =0;
        // 查询出数据库中的密码,进行密码校验
        if (SystemConstants.USER_ADMIN.equals(sessionUser.getType())) {
            User user = adminDAO.getUserById(sessionUser.getId());
            if (!user.getPassword().equals(password)) {
                error("原始密码错误", request, response);
                return;
            }
            result=adminDAO.updatePassword(user.getId(), newPassword);
        } else {
            Reader user = readerDAO.selectById(sessionUser.getId());
            if (!user.getPassword().equals(password)) {
                error("原始密码错误", request, response);
                return;
            }
            result=readerDAO.updatePassword(sessionUser.getId(),newPassword);
        }
        if (result == 1) {
            // 修改成功后回到登录页
            request.getRequestDispatcher(SystemConstants.PAGE_PATH + "login.jsp").forward(request, response);
        } else {
            error("修改失败，请稍后再试", request, response);
        }
    }

    /**
     * 统一的错误处理逻辑：附带提示信息转发回修改密码页面。
     */
    public void error(String message, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        request.setAttribute("message", message);
        request.getRequestDispatcher(SystemConstants.PAGE_PATH + "password.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request, response);
    }

}
