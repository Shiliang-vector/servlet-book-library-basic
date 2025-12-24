package com.test.servlet;

import com.test.dao.ReaderDAO;
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
 * 用户个人信息维护入口。
 * 当前仅支持修改昵称，可扩展为更多个人资料配置。
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private ReaderDAO readerDAO = new ReaderDAO();

    /**
     * 处理昵称修改请求，将新昵称写入 Session 中的用户对象，后续可拓展为持久化更新。
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String nickname = RequestUtil.getString(request, "nickname");
        if (nickname == null) {
            error("昵称不能为空", request, response);
            return;
        }
        HttpSession session = request.getSession();
        User sessionUser = (User)session.getAttribute(SystemConstants.SESSION_USER);
        sessionUser.setNickname(nickname);
        // 如需同步数据库可参考下方注释代码调用 DAO
//        int result = readerDAO.update(sessionUser);
//        if (result == 1) {
//            request.getRequestDispatcher(SystemConstants.PAGE_PATH + "index.jsp").forward(request, response);
//        } else {
//            error("修改失败，请稍后再试", request, response);
//        }
    }

    /**
     * 通用错误处理：携带提示信息返回个人信息页。
     */
    public void error(String message, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        request.setAttribute("message", message);
        request.getRequestDispatcher(SystemConstants.PAGE_PATH + "user.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        this.doGet(request, response);
    }

}
