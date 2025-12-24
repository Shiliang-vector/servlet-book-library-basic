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
    <jsp:param name="active" value="7"/>
</jsp:include>
<div class="row" style="padding-top:20px">
    <div class="col-lg-8">
        <form action="<%=request.getContextPath()%>/borrow?act=list" method="post" class="form-inline"
              id="searchForm">
            <%-- 隐藏页码与分页条配合使用，切换页码时保持当前筛选条件 --%>
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
                <button type="submit" class="btn  btn-success">查询</button>
                <span style="color: red">${message}</span>
            </div>
        </form>
        <table class="table table-striped table-bordered" style="margin-top:20px">
            <tr>
                <td>书名</td>
                <td>封面</td>
                <td>作者</td>
                <td>分类</td>
                <td>出版社</td>
                <td>出版日期</td>
                <td style="width: 200px">描述</td>
                <td>库存</td>
                <td>操作</td>
            </tr>
            <%-- 逐本书输出，优先显示封面、库存，再决定是否提供借阅按钮 --%>
            <c:forEach items="${bookList}" var="c">
                <tr>
                    <td>${c.title}</td>
                    <td>
                        <%-- 仅当上传了封面路径时渲染预览，避免空地址请求 --%>
                        <c:if test="${not empty c.coverUrl}">
                            <img src="<%=request.getContextPath()%>/${c.coverUrl}" alt="封面" style="max-width: 80px; max-height: 80px; display: block;">
                            <a href="<%=request.getContextPath()%>/${c.coverUrl}" download>下载封面</a>
                        </c:if>
                    </td>
                    <td>${c.author}</td>
                    <td>${c.categoryName}</td>
                    <td>${c.publisher}</td>
                    <td>${c.publishDate}</td>
                    <td>${c.description}</td>
                    <td>${c.stock}</td>
                    <td>
                        <%-- 库存大于 0 才展示可点击的借阅操作 --%>
                        <c:if test="${c.stock gt 0}">
                            <a class="btn btn-success" href="<%=request.getContextPath()%>/borrow?act=borrow&id=${c.id}">借阅</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <%-- 分页组件：使用 changePage 与隐藏域协同提交查询条件 --%>
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
    // 分页跳转：限定页码范围后提交搜索表单
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
