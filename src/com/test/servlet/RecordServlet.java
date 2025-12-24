package com.test.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.test.dao.BookDAO;
import com.test.dao.CategoryDAO;
import com.test.dao.RecordDAO;
import com.test.pojo.*;
import com.test.util.RequestUtil;
import com.test.util.SystemConstants;

/**
 * 借阅记录相关操作入口。
 * 提供借阅记录列表、归还逻辑以及罚金计算等能力。
 */
@WebServlet("/record")
public class RecordServlet extends HttpServlet {
    private RecordDAO recordDAO = new RecordDAO();
    private BookDAO bookDAO = new BookDAO();

    /**
     * 按 act 参数分发到列表、归还或罚金计算页面，默认进入列表。
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String act = request.getParameter("act");
        if (act == null) {
            act = "list";// 默认展示列表页
        }
        switch (act) {
            case "go_edit":// 跳转到编辑页，如果是修改，需要回填数据
                goEdit(request, response);
                break;
            case "back":// 执行添加或修改操作
                back(request, response);
                break;
            default:
                list(request, response);
        }
    }

    /**
     * 按条件查询借阅记录并分页，读者仅能看到自己的记录，管理员可查看全部。
     */
    protected void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RecordCriteria criteria = new RecordCriteria();
        criteria.setStatus(RequestUtil.getString(request, "status"));
        criteria.setIdCard(RequestUtil.getString(request,"idCard"));
        criteria.setTitle(RequestUtil.getString(request,"title"));
        HttpSession session = request.getSession();
        User sessionUser = (User)session.getAttribute(SystemConstants.SESSION_USER);
        if (SystemConstants.USER_READER.equals(sessionUser.getType())) {
            criteria.setUserId(sessionUser.getId());
        }
        Integer pageNum = RequestUtil.getInteger(request, "pageNum");
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        Integer pageSize = 2;
        criteria.setLimit((pageNum - 1) * pageSize);
        criteria.setPageSize(pageSize);
        Integer totalCount = recordDAO.getCount(criteria);
        PageInfo pageInfo = new PageInfo(pageNum, pageSize, totalCount);
        request.setAttribute("pageInfo", pageInfo);
        if (totalCount > 0) {
            List<Record> recordList = recordDAO.selectList(criteria);
            request.setAttribute("recordList", recordList);
        }
        request.getRequestDispatcher(SystemConstants.PAGE_PATH + "record_list.jsp").forward(request, response);
    }

    /**
     * 归还操作：读者归还需校验超期，管理员归还可附带罚金；归还后同步恢复库存。
     */
    protected void back(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = RequestUtil.getInteger(request, "id");
        Double price = RequestUtil.getDouble(request, "price");
        HttpSession session = request.getSession();
        User sessionUser = (User)session.getAttribute(SystemConstants.SESSION_USER);
        Record record = recordDAO.selectById(id);
        // 超时的书籍必须联系管理员交罚款
        if (SystemConstants.USER_READER.equals(sessionUser.getType())) {
            if (new Date().after(record.getExpireTime())) {
                request.setAttribute("message", "该书借阅已超时，请联系管理员归还");
                list(request, response);
                return;
            }
        }
        record.setReturnTime(new Date());
        record.setStatus(SystemConstants.RECORD_RETURN);
        record.setPrice(price);
        recordDAO.update(record);
        // 恢复库存
        Book book = bookDAO.selectById(record.getBookId());
        book.setStock(book.getStock() + 1);
        bookDAO.update(book);
        list(request, response);
    }

    /**
     * 跳转到罚金编辑页，同时根据超期天数预计算罚金（30天免罚，其后每日 1 元）。
     */
    protected void goEdit(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        Integer id = RequestUtil.getInteger(request, "id");
        Record record = recordDAO.selectById(id);
        long msNum = System.currentTimeMillis() - record.getExpireTime().getTime();// 时间戳相差的毫秒数
        long dayNum = msNum / (24 * 60 * 60 * 1000);// 除以一天的毫秒数，得到相差天数
        if (dayNum > 30) {// 超出30天部分一天1元罚款
            record.setPrice(dayNum - 30.0);
        }
        request.setAttribute("record", record);
        request.getRequestDispatcher(SystemConstants.PAGE_PATH + "record_edit.jsp").forward(request, response);
    }

    /**
     * 通用错误提示：附带消息返回罚金编辑页面。
     */
    protected void error(String message, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        request.setAttribute("message", message);
        request.getRequestDispatcher(SystemConstants.PAGE_PATH + "record_edit.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        this.doGet(request, response);
    }
}
