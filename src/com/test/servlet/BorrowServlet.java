package com.test.servlet;

import java.io.IOException;
import java.util.Calendar;
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
import com.test.dao.ReaderDAO;
import com.test.dao.RecordDAO;
import com.test.pojo.*;
import com.test.util.RequestUtil;
import com.test.util.SystemConstants;

/**
 * 借书流程相关接口，提供可借图书列表展示以及借阅操作。
 * 针对已登录读者，控制借阅数量、账号状态和库存的校验。
 */
@WebServlet("/borrow")
public class BorrowServlet extends HttpServlet {
    private CategoryDAO categoryDAO = new CategoryDAO();
    private BookDAO bookDAO = new BookDAO();
    private ReaderDAO readerDAO = new ReaderDAO();
    private RecordDAO recordDAO = new RecordDAO();

    /**
     * 根据 act 参数分派到列表或借阅动作。
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String act = request.getParameter("act");
        if (act == null) {
            act = "list";// 默认展示列表页
        }
        switch (act) {
            case "borrow":
                borrow(request, response);
                break;
            default:
                list(request, response);
        }
    }

    /**
     * 图书列表
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 封装查询条件
        BookCriteria criteria = new BookCriteria();
        // RequestUtil.get类型(request, "参数名")
        criteria.setTitle(RequestUtil.getString(request, "title"));
        criteria.setCategoryId(RequestUtil.getInteger(request, "categoryId"));
        // 借书时只能看到上架的书籍
        criteria.setStatus(SystemConstants.BOOK_ON);
        Integer pageNum = RequestUtil.getInteger(request, "pageNum");
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        Integer pageSize = 2;
        criteria.setLimit((pageNum - 1) * pageSize);
        criteria.setPageSize(pageSize);

        Integer totalCount = bookDAO.getCount(criteria);
        PageInfo pageInfo = new PageInfo(pageNum, pageSize, totalCount);
        request.setAttribute("pageInfo", pageInfo);
        if (totalCount > 0) {
            List<Book> bookList = bookDAO.selectList(criteria);
            request.setAttribute("bookList", bookList);
        }

        List<Category> categories = categoryDAO.selectList();
        request.setAttribute("categories", categories);

        request.getRequestDispatcher(SystemConstants.PAGE_PATH + "borrow_list.jsp").forward(request, response);
    }

    /**
     * 借阅操作核心逻辑：
     *   校验读者状态与借阅数量限制
     *   更新图书库存
     *   创建借阅记录并设置到期时间
     */
    protected void borrow(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        Integer id = RequestUtil.getInteger(request, "id");
        HttpSession session = request.getSession();
        User sessionUser = (User)session.getAttribute(SystemConstants.SESSION_USER);
        Reader user = readerDAO.selectById(sessionUser.getId());
        if (SystemConstants.STATUS_DISABLE.equals(user.getStatus())) {
            request.setAttribute("message", "您的账号已被冻结，请联系管理员");
            list(request, response);
            return;
        }
        RecordCriteria criteria = new RecordCriteria();
        criteria.setStatus(SystemConstants.RECORD_BORROW);
        criteria.setUserId(user.getId());
        int count = recordDAO.getCount(criteria);
        if (count >= 3) {
            request.setAttribute("message", "最多只能借三本书，请先归还已借书籍");
            list(request, response);
            return;
        }
        // 减库存
        Book book = bookDAO.selectById(id);
        book.setStock(book.getStock() - 1);
        bookDAO.update(book);
        // 添加借阅记录
        Record record = new Record();
        record.setBookId(id);
        record.setTitle(book.getTitle());
        record.setUserId(sessionUser.getId());
        record.setBorrowTime(new Date());
        // 最多借30天
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 30);
        record.setExpireTime(calendar.getTime());
        record.setStatus(SystemConstants.RECORD_BORROW);
        record.setPrice(0.0);
        recordDAO.add(record);
        response.sendRedirect(request.getContextPath() + "/record?act=list");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        this.doGet(request, response);
    }
}
