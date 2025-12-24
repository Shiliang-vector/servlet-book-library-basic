<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>图书借阅管理系统</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/bootstrap.min.css"></link>
    <script src="<%=request.getContextPath()%>/static/js/jquery-1.10.2.min.js"></script>
    <script src="<%=request.getContextPath()%>/static/js/bootstrap.min.js"></script>
</head>
<body class="container-fluid">
<jsp:include page="common.jsp">
    <jsp:param name="active" value="3"/>
</jsp:include>
<div class="row" style="padding-top:20px">
    <div class="col-lg-4">
        <form method="post" action="<%=request.getContextPath()%>/user" class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-2 control-label">昵称：</label>
                <div class="col-sm-4"><input class="form-control" name="nickname" type="text"></div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-4">
                    <button type="submit" class="btn btn-success">提交</button>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-4">
                    <span style="color: red" >${message}</span>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>
