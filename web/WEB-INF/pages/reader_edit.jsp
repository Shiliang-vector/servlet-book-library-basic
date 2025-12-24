<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <div class="col-lg-4">
        <form method="post" action="<%=request.getContextPath()%>/reader?act=edit" class="form-horizontal">
            <input type="hidden" name="id" value="${reader.id}">
            <div class="form-group">
                <label class="col-sm-2 control-label">用户名：</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" name="username" value="${reader.username}" readonly>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">姓名：</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" name="name" value="${reader.name}" readonly>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">身份证：</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" name="idCard" value="${reader.idCard}" readonly>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">手机号：</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" name="mobile" value="${reader.mobile}">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">状态：</label>
                <div class="col-sm-10">
                    <label class="control-label">
                        <input type="radio" name="status" value="正常" ${reader.status eq "正常"?"checked":""}>正常
                    </label>
                    <label class="control-label">
                        <input type="radio" name="status" value="禁用" ${reader.status eq "禁用"?"checked":""}>禁用
                    </label>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-2">
                    <button type="submit" id="btn" class="btn btn-success">提交</button>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-4">
                    <span style="color: red">${message}</span>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>
