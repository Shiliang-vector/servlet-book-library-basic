package com.test.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.test.dao.ReaderDAO;
import com.test.pojo.Reader;
import com.test.pojo.ReaderCriteria;
import com.test.pojo.PageInfo;
import com.test.util.RequestUtil;
import com.test.util.SystemConstants;

/**
 * 读者管理接口：包含列表查询、跳转编辑、保存、删除等操作。
 * 该 Servlet 同时被列表页与编辑页调用，通过 act 参数区分具体动作。
 */
@WebServlet("/reader")
public class ReaderServlet extends HttpServlet {
    private ReaderDAO readerDAO = new ReaderDAO();

    /**
     * 根据 act 参数分发到不同的处理方法，默认展示列表。
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
            case "delete":
            case "batch_delete":
                delete(request, response);
                break;
            default:
                list(request, response);
        }
    }

    /**
     * 列出读者信息并按照条件分页。
     * 这里将查询条件封装到 ReaderCriteria，便于与 DAO 交互。
     */
    protected void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        moveSessionMessageToRequest(request);
        // 封装查询条件
        ReaderCriteria criteria = new ReaderCriteria();
        // RequestUtil.get类型(request, "参数名")
        criteria.setIdCard(RequestUtil.getString(request, "idCard"));
        criteria.setMobile(RequestUtil.getString(request, "mobile"));
        criteria.setStatus(RequestUtil.getString(request, "status"));
        Integer pageNum = RequestUtil.getInteger(request, "pageNum");
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        Integer pageSize = 2;
        criteria.setLimit((pageNum - 1) * pageSize);
        criteria.setPageSize(pageSize);

        Integer totalCount = readerDAO.getCount(criteria);
        PageInfo pageInfo = new PageInfo(pageNum, pageSize, totalCount);
        request.setAttribute("pageInfo", pageInfo);
        if (totalCount > 0) {
            List<Reader> readerList = readerDAO.selectList(criteria);
            request.setAttribute("readerList", readerList);
        }
        request.getRequestDispatcher(SystemConstants.PAGE_PATH + "reader_list.jsp").forward(request, response);
    }

    /**
     * 跳转到编辑页。如果带有 id 则回填原有数据，实现编辑与新增共用一张表单。
     */
    protected void goEdit(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        Integer id = RequestUtil.getInteger(request, "id");
        if (id != null) {
            Reader reader = readerDAO.selectById(id);
            request.setAttribute("reader", reader);
        }
        request.getRequestDispatcher(SystemConstants.PAGE_PATH + "reader_edit.jsp").forward(request, response);
    }

    /**
     * 新增或修改读者。使用必填校验防止空数据提交。
     */
    protected void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Reader reader = new Reader();
        reader.setId(RequestUtil.getInteger(request, "id"));
        reader.setMobile(RequestUtil.getString(request, "mobile"));
        reader.setStatus(RequestUtil.getString(request, "status"));
        request.setAttribute("reader", reader);
        if (reader.getMobile() == null) {
            error("手机号不能为空", request, response);
            return;
        }
        if (reader.getStatus() == null) {
            error("状态不能为空", request, response);
            return;
        }
        readerDAO.update(reader);
        response.sendRedirect(request.getContextPath() + "/reader?act=list");
    }

    /**
     * 将错误消息放入 request 并返回编辑页，便于表单回显。
     */
    protected void error(String message, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        request.setAttribute("message", message);
        request.getRequestDispatcher(SystemConstants.PAGE_PATH + "reader_edit.jsp").forward(request, response);
    }

    /**
     * 解析页面传入的单个或批量 ID 参数，返回统一的结果对象。
     */
    private IdParseResult parseIds(HttpServletRequest request) {
        List<Integer> ids = new ArrayList<>();
        String[] idParams = request.getParameterValues("ids");
        if (idParams != null) {
            for (String idParam : idParams) {
                if (idParam == null) {
                    continue;
                }
                String[] splitIds = idParam.split(",");
                for (String splitId : splitIds) {
                    if (splitId == null || splitId.trim().isEmpty()) {
                        continue;
                    }
                    try {
                        ids.add(Integer.parseInt(splitId.trim()));
                    } catch (NumberFormatException e) {
                        return new IdParseResult(ids, "存在非法的ID参数");
                    }
                }
            }
        }
        Integer id = RequestUtil.getInteger(request, "id");
        if (id != null) {
            ids.add(id);
        }
        return new IdParseResult(ids, null);
    }

    /**
     * 支持单条或批量删除读者，并通过 Session 携带操作结果给列表页显示。
     */
    protected void delete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        HttpSession session = request.getSession();
        IdParseResult parseResult = parseIds(request);
        if (parseResult.getErrorMessage() != null) {
            session.setAttribute("message", parseResult.getErrorMessage());
            response.sendRedirect(request.getContextPath() + "/reader?act=list");
            return;
        }
        List<Integer> ids = parseResult.getIds();
        if (ids.isEmpty()) {
            session.setAttribute("message", "请选择要删除的用户");
            response.sendRedirect(request.getContextPath() + "/reader?act=list");
            return;
        }
        int success = readerDAO.deleteByIds(ids);
        if (success != ids.size()) {
            session.setAttribute("message", "部分用户删除失败，请稍后再试");
        } else {
            session.setAttribute("message", "删除成功");
        }
        response.sendRedirect(request.getContextPath() + "/reader?act=list");
    }

    /**
     * 将存放在 Session 中的提示消息转移到 request，防止刷新重复提示。
     */
    private void moveSessionMessageToRequest(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        Object message = session.getAttribute("message");
        if (message != null) {
            request.setAttribute("message", message);
            session.removeAttribute("message");
        }
    }

    /**
     * 简单的结果封装，包含解析出的 ID 集合和错误信息。
     */
    private static class IdParseResult {
        private List<Integer> ids;
        private String errorMessage;

        public IdParseResult(List<Integer> ids, String errorMessage) {
            this.ids = ids;
            this.errorMessage = errorMessage;
        }

        public List<Integer> getIds() {
            return ids;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        this.doGet(request, response);
    }
}
