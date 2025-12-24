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
    <jsp:param name="active" value="5"/>
</jsp:include>
<div class="row" style="padding-top:20px">
    <div class="col-lg-4">
        <form method="post" action="<%=request.getContextPath()%>/record?act=back" class="form-horizontal">
            <input type="hidden" name="id" value="${record.id}">
            <div class="form-group">
                <label class="col-sm-2 control-label">书名：</label>
                <div class="col-sm-10">
                    <input class="form-control" value="${record.title}" readonly/>
                </div>
            </div>
            <%-- 借阅时间、超时截止日期仅供查看，归还时不可修改 --%>
            <div class="form-group">
                <label class="col-sm-2 control-label">借阅日期：</label>
                <div class="col-sm-10">
                    <input class="form-control" value="<fmt:formatDate value="${record.borrowTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" readonly/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">超时日期：</label>
                <div class="col-sm-10">
                    <input class="form-control" value="<fmt:formatDate value="${record.expireTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" readonly/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">缴费：</label>
                <div class="col-sm-10">
                    <%-- 管理员可调整赔偿金额；不限制读写时会影响结算金额 --%>
                    <input type="text" class="form-control" name="price" value="${record.price}">
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
