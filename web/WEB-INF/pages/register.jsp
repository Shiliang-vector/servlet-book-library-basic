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
<body class="container-fluid" style="background: url('<%=request.getContextPath()%>/static/bj.jpg') no-repeat;background-size: cover">
<div class="row" style="padding-top:200px;">
    <div class="col-lg-4 col-lg-offset-4" style="padding-top: 30px;height: 500px;background-color: rgba(254,255,251,0.5);border-radius: 15px">
        <div style="text-align: center;padding-bottom: 20px"><h2>厦门理工学院图书借阅管理系统</h2></div>
        <form method="post" action="<%=request.getContextPath()%>/register" class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-4 control-label">用户名：</label>
                <div class="col-sm-5">
                    <input class="form-control" name="username" value="${param.username}" autocomplete="off">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label">姓名：</label>
                <div class="col-sm-5">
                    <input class="form-control" name="name" value="${param.name}">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label">密码：</label>
                <div class="col-sm-5"><input class="form-control" name="password" type="password"></div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label">确认密码：</label>
                <div class="col-sm-5"><input class="form-control" name="repwd" type="password"></div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label">手机号：</label>
                <div class="col-sm-5">
                    <input class="form-control" name="mobile" value="${param.mobile}">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label">身份证：</label>
                <div class="col-sm-5">
                    <input class="form-control" name="idCard" value="${param.idCard}">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-4 col-sm-4">
                    <button type="submit" class="btn btn-success">注册</button>
                    <a class="btn btn-info"
                       href="<%=request.getContextPath()%>/login">登录</a>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-4 col-sm-4">
                    <span style="color: red">${message}</span>
                </div>
            </div>
        </form>
    </div>
</div>

</body>
</html>
