package com.test.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.dao.BookDAO;
import com.test.dao.CategoryDAO;
import com.test.pojo.BookCriteria;
import com.test.pojo.Category;
import com.test.util.RequestUtil;
import com.test.util.SystemConstants;

/**
 * 图书分类管理入口 Servlet。
 * 负责展示分类列表、跳转至编辑页、执行新增/修改以及删除分类的相关校验。
 */
@WebServlet("/category")
public class CategoryServlet extends HttpServlet {
    private CategoryDAO categoryDAO = new CategoryDAO();
    private BookDAO bookDAO = new BookDAO();

    /**
     * 根据 act 参数分发到具体操作，默认展示列表。
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
            case "edit":// 执行添加或修改操作
                edit(request, response);
                break;
            case "delete":// 删除
                delete(request, response);
                break;
            default:
                list(request, response);
        }
    }

    /**
     * 查询全部分类并跳转到列表页。
     */
    protected void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> categories = categoryDAO.selectList();
        request.setAttribute("catList", categories);

        request.getRequestDispatcher(SystemConstants.PAGE_PATH + "category_list.jsp").forward(request, response);
    }

    /**
     * 跳转至编辑页面，存在 id 时回填原有分类信息。
     */
    protected void goEdit(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        Integer id = RequestUtil.getInteger(request, "id");
        if (id != null) {
            Category category = categoryDAO.selectById(id);
            request.setAttribute("category", category);
        }
        request.getRequestDispatcher(SystemConstants.PAGE_PATH + "category_edit.jsp").forward(request, response);
    }

    /**
     * 保存分类：校验名称后根据是否有 id 选择新增或更新。
     */
    protected void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = RequestUtil.getInteger(request, "id");
        String name = RequestUtil.getString(request, "name");
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        request.setAttribute("category", category);
        if (name == null) {
            error("名称不能为空", request, response);
            return;
        }
        if (id == null) {
            categoryDAO.add(category);
        } else {
            categoryDAO.update(category);
        }
        response.sendRedirect(request.getContextPath() + "/category?act=list");
    }

    /**
     * 统一的错误处理：带错误信息回到编辑页。
     */
    protected void error(String message, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        request.setAttribute("message", message);
        request.getRequestDispatcher(SystemConstants.PAGE_PATH + "category_edit.jsp").forward(request, response);
    }

    /**
     * 删除分类前校验是否仍有图书引用，避免删除仍在使用的分类。
     */
    protected void delete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        Integer id = RequestUtil.getInteger(request, "id");
        BookCriteria criteria = new BookCriteria();
        criteria.setCategoryId(id);
        int count = bookDAO.getCount(criteria);
        if (count > 0) {
            request.setAttribute("message", "该分类下有图书，不能删除");
            list(request, response);
            return;
        } else {
            categoryDAO.delete(id);
        }
        response.sendRedirect(request.getContextPath() + "/category?act=list");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        this.doGet(request, response);
    }
}
