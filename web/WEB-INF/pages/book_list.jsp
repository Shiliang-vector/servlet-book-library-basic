<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>图书借阅管理系统</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/bootstrap.min.css"></link>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/bootstrap-datetimepicker.css"></link>
    <script src="<%=request.getContextPath()%>/static/js/jquery-1.10.2.min.js"></script>
    <script src="<%=request.getContextPath()%>/static/js/bootstrap.min.js"></script>
    <script src="<%=request.getContextPath()%>/static/js/bootstrap-datetimepicker.min.js" charset="UTF-8"></script>
    <script src="<%=request.getContextPath()%>/static/js/bootstrap-datepicker.zh-CN.js" charset="UTF-8"></script>
</head>
<body class="container-fluid">
<jsp:include page="common.jsp">
    <jsp:param name="active" value="4"/>
</jsp:include>
<div class="row" style="padding-top:20px">
    <div class="col-lg-8">
        <form action="<%=request.getContextPath()%>/book?act=list" method="post" class="form-inline"
              id="searchForm">
            <%-- pageNum 通过隐藏域与 changePage 方法联动，维持分页查询条件 --%>
            <input type="hidden" name="pageNum" id="pageNum">
            <div class="form-group">
                <label>分类</label>
                <select class="form-control" name="categoryId">
                    <option value="">请选择</option>
                    <c:forEach items="${categories}" var="category">
                        <option value="${category.id}" ${param.categoryId eq category.id?'selected':''}>${category.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>书名</label>
                <input type="text" class="form-control" name="title" value="${param.title}">
            </div>
            <div class="form-group">
                <label>状态</label>
                <select class="form-control" name="status">
                    <option value="">请选择</option>
                    <option value="上架" ${param.status eq '上架'?'selected':''}>上架</option>
                    <option value="下架" ${param.status eq '下架'?'selected':''}>下架</option>
                </select>
            </div>
            <div class="form-group">
                <button type="submit" class="btn">查询</button>
                <a class="btn btn-success" href="<%=request.getContextPath()%>/book?act=go_edit">+添加
                </a>
                <span style="color: red">${message}</span>
            </div>
        </form>
        <table class="table table-striped table-bordered" style="margin-top:20px">
            <tr>
                <td>ID</td>
                <td>书名</td>
                <td>封面</td>
                <td>作者</td>
                <td>分类</td>
                <td>出版社</td>
                <td>出版日期</td>
                <td style="width: 200px">描述</td>
                <td>库存</td>
                <td>状态</td>
                <td>操作</td>
            </tr>
            <%-- 列表逐行渲染，包含封面展示与下载入口 --%>
            <c:forEach items="${bookList}" var="c">
                <tr>
                    <td>${c.id}</td>
                    <td>${c.title}</td>
                    <td>
                        <%-- 有封面路径时才展示图片与下载链接，避免空地址导致的 404 --%>
                        <c:if test="${not empty c.coverUrl}">
                            <img src="<%=request.getContextPath()%>/${c.coverUrl}" alt="封面"
                                 style="max-width: 80px; max-height: 80px; display: block;">
                            <a href="<%=request.getContextPath()%>/${c.coverUrl}" download>下载封面</a>
                        </c:if>
                    </td>
                    <td>${c.author}</td>
                    <td>${c.categoryName}</td>
                    <td>${c.publisher}</td>
                    <td>${c.publishDate}</td>
                    <td>${c.description}</td>
                    <td>${c.stock}</td>
                    <td>${c.status}</td>
                    <td>
                        <a href="<%=request.getContextPath()%>/book?act=go_edit&id=${c.id}">修改</a>
                        <a href="<%=request.getContextPath()%>/book?act=delete&id=${c.id}">旧书淘汰</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <%-- 分页条：页码切换通过 JS 设置隐藏域后提交表单，保持筛选条件 --%>
        <ul class="pagination">
            <li><a href="#" onclick="changePage(1)">首页</a></li>
            <c:choose>
                <c:when test="${pageInfo.pageNum>1}">
                    <li><a href="#" onclick="changePage(${pageInfo.pageNum-1})">上一页</a></li>
                </c:when>
                <c:otherwise>
                    <li class="disabled"><a href="#">上一页</a></li>
                </c:otherwise>
            </c:choose>
            <c:forEach begin="${pageInfo.startPage}" end="${pageInfo.endPage}" var="num" step="1">
                <li class="${num eq pageInfo.pageNum?'active':''}"><a href="#" onclick="changePage(${num})">${num}</a>
                </li>
            </c:forEach>
            <c:choose>
                <c:when test="${pageInfo.hasNextPage}">
                    <li>
                        <a href="#" onclick="changePage(${pageInfo.pageNum+1})">下一页</a>
                    </li>
                    <li>
                        <a href="#" onclick="changePage(${pageInfo.totalPage})">末页</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="disabled">
                        <a href="#">下一页</a>
                    </li>
                    <li class="disabled">
                        <a href="#">末页</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</div>
<script>
    // 处理页码跳转：限制上下界后回填到隐藏域再提交表单
    function changePage(pn) {
        if (pn < 1) {
            pn = 1
        } else if (pn > '${requestScope.pageInfo.totalPage}') {
            pn = '${requestScope.pageInfo.totalPage}'
        }
        $("#pageNum").val(pn);
        $("#searchForm").submit()
    }
</script>
</body>
</html>
