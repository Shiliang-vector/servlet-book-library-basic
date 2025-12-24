package com.test.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.test.dao.CategoryDAO;
import com.test.dao.BookDAO;
import com.test.dao.RecordDAO;
import com.test.pojo.*;
import com.test.util.RequestUtil;
import com.test.util.SystemConstants;

/**
 * 书籍管理：提供列表查询、编辑、新增、删除以及封面上传校验等功能。
 * 该 Servlet 覆盖了后台与前台展示所需的大部分业务逻辑。
 */
@WebServlet("/book")
@MultipartConfig(maxFileSize = 2 * 1024 * 1024, maxRequestSize = 4 * 1024 * 1024)
public class BookServlet extends HttpServlet {
    private CategoryDAO categoryDAO = new CategoryDAO();
    private BookDAO bookDAO = new BookDAO();
    private RecordDAO recordDAO = new RecordDAO();

    private static final long MAX_COVER_SIZE = 2 * 1024 * 1024;
    private static final Set<String> ALLOWED_CONTENT_TYPES = new HashSet<>(
        Arrays.asList("image/jpeg", "image/png", "image/gif"));
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(
        Arrays.asList("jpg", "jpeg", "png", "gif"));

    /**
     * 根据 act 参数分发到不同动作；默认为列表展示。
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
     * 按条件分页查询图书，并将分类数据一起返回页面。
     */
    protected void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 封装查询条件
        BookCriteria criteria = new BookCriteria();
        // RequestUtil.get类型(request, "参数名")
        criteria.setTitle(RequestUtil.getString(request, "title"));
        criteria.setCategoryId(RequestUtil.getInteger(request, "categoryId"));
        criteria.setStatus(RequestUtil.getString(request, "status"));
        Integer pageNum = RequestUtil.getInteger(request, "pageNum");
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        Integer pageSize = 2;
        criteria.setLimit((pageNum - 1) * pageSize);
        criteria.setPageSize(pageSize);
        // 总条数
        Integer totalCount = bookDAO.getCount(criteria);
        PageInfo pageInfo = new PageInfo(pageNum, pageSize, totalCount);
        request.setAttribute("pageInfo", pageInfo);
        if (totalCount > 0) {
            List<Book> bookList = bookDAO.selectList(criteria);
            request.setAttribute("bookList", bookList);
        }
        // 分类列表
        List<Category> categories = categoryDAO.selectList();
        request.setAttribute("categories", categories);

        request.getRequestDispatcher(SystemConstants.PAGE_PATH + "book_list.jsp").forward(request, response);
    }

    /**
     * 跳转到编辑页，若存在 id 则查询原数据用于回填。
     */
    protected void goEdit(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        Integer id = RequestUtil.getInteger(request, "id");
        if (id != null) {
            Book book = bookDAO.selectById(id);
            request.setAttribute("book", book);
        }
        List<Category> categories = categoryDAO.selectList();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher(SystemConstants.PAGE_PATH + "book_edit.jsp").forward(request, response);
    }

    /**
     * 处理新增或修改保存，包含必填校验、封面路径解析以及新增/更新分支。
     */
    protected void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Book book = new Book();
        book.setId(RequestUtil.getInteger(request, "id"));
        book.setCategoryId(RequestUtil.getInteger(request, "categoryId"));
        book.setTitle(RequestUtil.getString(request, "title"));
        book.setPublisher(RequestUtil.getString(request, "publisher"));
        book.setDescription(RequestUtil.getString(request, "description"));
        book.setAuthor(RequestUtil.getString(request, "author"));
        book.setPublishDate(RequestUtil.getString(request, "publishDate"));
        book.setStatus(RequestUtil.getString(request, "status"));
        book.setStock(RequestUtil.getInteger(request, "stock"));
        book.setCoverUrl(resolveCoverUrl(request, book.getId()));
        request.setAttribute("book", book);
        if (request.getAttribute("message") != null) {
            error((String) request.getAttribute("message"), request, response);
            return;
        }
        if (book.getCategoryId() == null) {
            error("分类不能为空", request, response);
            return;
        }
        if (book.getTitle() == null) {
            error("名称不能为空", request, response);
            return;
        }
        if (book.getStatus() == null) {
            error("状态不能为空", request, response);
            return;
        }

        if (book.getId() == null) {
            bookDAO.add(book);
        } else {
            bookDAO.update(book);
        }
        response.sendRedirect(request.getContextPath() + "/book?act=list");
    }

    /**
     * 解析封面路径或上传文件路径，优先使用新上传的文件，其次表单输入，最后沿用旧值。
     */
    private String resolveCoverUrl(HttpServletRequest request, Integer bookId) throws ServletException, IOException {
        String coverUrl = RequestUtil.getString(request, "coverUrl");
        UploadResult uploadResult = saveCoverFile(request.getPart("coverFile"), request);
        if (uploadResult != null && uploadResult.getErrorMessage() != null) {
            request.setAttribute("message", uploadResult.getErrorMessage());
            return null;
        }
        String uploadedUrl = uploadResult != null ? uploadResult.getUrl() : null;
        String finalCover = uploadedUrl != null ? uploadedUrl : coverUrl;
        if (finalCover == null && bookId != null) {
            Book exist = bookDAO.selectById(bookId);
            if (exist != null) {
                finalCover = exist.getCoverUrl();
            }
        }
        return finalCover;
    }

    /**
     * 保存上传的封面文件到服务器，并进行大小、类型、扩展名校验。
     */
    private UploadResult saveCoverFile(Part coverPart, HttpServletRequest request) throws IOException, ServletException {
        if (coverPart == null || coverPart.getSize() == 0) {
            return null;
        }

        String originalName = coverPart.getSubmittedFileName();
        if (originalName == null || "".equals(originalName.trim())) {
            return new UploadResult(null, "封面文件名不能为空");
        }

        if (coverPart.getSize() > MAX_COVER_SIZE) {
            return new UploadResult(null, "封面大小不能超过2MB");
        }

        String fileNameOnly = new File(originalName).getName();
        String extension = getExtension(fileNameOnly);
        if (extension == null || !ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            return new UploadResult(null, "仅支持上传 jpg、jpeg、png、gif 图片");
        }
        String contentType = coverPart.getContentType();
        if (contentType != null && !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            return new UploadResult(null, "文件类型不正确，请上传图片文件");
        }

        String safeFileName = System.currentTimeMillis() + "_" + fileNameOnly;
        String uploadPath = request.getServletContext().getRealPath("/static/upload");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        File savedFile = new File(uploadDir, safeFileName);
        coverPart.write(savedFile.getAbsolutePath());
        return new UploadResult("static/upload/" + safeFileName, null);
    }

    private String getExtension(String fileName) {
        int idx = fileName.lastIndexOf('.') + 1;
        if (idx <= 0 || idx >= fileName.length()) {
            return null;
        }
        return fileName.substring(idx);
    }

    private static class UploadResult {
        private final String url;
        private final String errorMessage;

        UploadResult(String url, String errorMessage) {
            this.url = url;
            this.errorMessage = errorMessage;
        }

        public String getUrl() {
            return url;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    protected void error(String message, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        request.setAttribute("message", message);
        List<Category> categories = categoryDAO.selectList();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher(SystemConstants.PAGE_PATH + "book_edit.jsp").forward(request, response);
    }

    protected void delete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        Integer id = RequestUtil.getInteger(request, "id");
        RecordCriteria criteria = new RecordCriteria();
        criteria.setBookId(id);
        criteria.setStatus(SystemConstants.RECORD_BORROW);
        int count = recordDAO.getCount(criteria);
        if (count > 0) {
            request.setAttribute("message", "还有书籍未归还，不能淘汰");
            list(request, response);
            return;
        }
        bookDAO.delete(id);
        response.sendRedirect(request.getContextPath() + "/book?act=list");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        this.doGet(request, response);
    }
}
