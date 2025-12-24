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
    <jsp:param name="active" value="3"/>
</jsp:include>
<div class="row" style="padding-top:20px">
    <div class="col-lg-8">
        <form action="<%=request.getContextPath()%>/record?act=list" method="post" class="form-inline"
              id="searchForm">
            <%-- 隐藏页码字段配合底部分页条，保持条件切换时的查询状态 --%>
            <input type="hidden" name="pageNum" id="pageNum">
            <div class="form-group">
                <label>书名</label>
                <input type="text" class="form-control" name="title" value="${param.title}">
            </div>
            <c:if test="${sessionScope.SESSION_USER.type eq 'admin'}">
                <%-- 管理员可按身份证查询，读者无权限看到该字段 --%>
                <div class="form-group">
                    <label>身份证</label>
                    <input type="text" class="form-control" name="idCard" value="${param.idCard}">
                </div>
            </c:if>
            <div class="form-group">
                <label>状态</label>
                <select class="form-control" name="status">
                    <option value="">请选择</option>
                    <option value="借阅中" ${param.status eq '借阅中'?'selected':''}>借阅中</option>
                    <option value="已归还" ${param.status eq '已归还'?'selected':''}>已归还</option>
                </select>
            </div>
            <div class="form-group">
                <button type="submit" class="btn btn-success">查询</button>
                <span style="color: red">${message}</span>
            </div>
        </form>
        <table class="table table-striped table-bordered" style="margin-top:20px">
            <tr>
                <td>书名</td>
                <td>读者</td>
                <td>借阅日期</td>
                <td>超时日期</td>
                <td>归还日期</td>
                <td>缴费金额</td>
                <td>状态</td>
                <td>操作</td>
            </tr>
            <%-- 借阅记录行内根据状态与用户类型决定可操作按钮 --%>
            <c:forEach items="${recordList}" var="c">
                <tr>
                    <td>${c.title}</td>
                    <td>${c.name}[${c.idCard}]</td>
                    <td><fmt:formatDate value="${c.borrowTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td><fmt:formatDate value="${c.expireTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td><fmt:formatDate value="${c.returnTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>${c.price}</td>
                    <td>${c.status}</td>
                    <td>
                        <c:if test="${c.status eq '借阅中'}">
                            <%-- 读者只能发起归还，管理员进入还书页面录入费用与归还时间 --%>
                            <c:if test="${sessionScope.SESSION_USER.type eq 'reader'}">
                                <a class="btn btn-success"
                                   href="<%=request.getContextPath()%>/record?act=back&id=${c.id}">归还</a>
                            </c:if>
                            <c:if test="${sessionScope.SESSION_USER.type eq 'admin'}">
                                <a class="btn btn-info"
                                   href="<%=request.getContextPath()%>/record?act=go_edit&id=${c.id}">还书</a>
                            </c:if>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <%-- 分页控制条，沿用 changePage JS 保留筛选条件提交 --%>
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
    // 页码切换时校验上下界，避免超出有效页数
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
