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
    <jsp:param name="active" value="5"/>
</jsp:include>
<div class="row" style="padding-top:20px">
    <div class="col-lg-6">
        <a class="btn btn-success" href="<%=request.getContextPath()%>/category?act=go_edit">+添加</a> <span style="color: red">${message}</span>
        <table class="table table-striped table-bordered" style="margin-top:20px">
            <tr>
                <td>ID</td>
                <td>名称</td>
                <td>操作</td>
            </tr>
            <c:forEach items="${catList}" var="c">
                <tr>
                    <td>${c.id}</td>
                    <td>${c.name}</td>
                    <td>
                        <a href="<%=request.getContextPath()%>/category?act=go_edit&id=${c.id}">修改</a>
                        <a href="<%=request.getContextPath()%>/category?act=delete&id=${c.id}">删除</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
</body>
</html>
