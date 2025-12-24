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
<body class="container-fluid"
      style="background: url('<%=request.getContextPath()%>/static/bj.jpg') no-repeat;background-size: cover">
<div class="row" style="padding-top:220px;">
    <div class="col-lg-4 col-lg-offset-4"
         style="padding-top: 30px;height: 330px;background-color: rgba(254,255,251,0.5);border-radius: 15px">
        <div style="text-align: center;padding-bottom: 20px"><h2>图书借阅管理系统</h2></div>
        <form method="post" action="<%=request.getContextPath()%>/login" class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-4 control-label">用户名：</label>
                <div class="col-sm-5">
                    <input class="form-control" name="username" value="${param.username}" autocomplete="off">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label">密&nbsp;&nbsp;&nbsp;&nbsp;码：</label>
                <div class="col-sm-5"><input class="form-control" name="password" type="password"></div>
            </div>
            <div class="form-group">
                <div class="col-sm-5 col-sm-offset-4">
                    <input type="radio" name="type" value="reader" checked>读者
                    <input type="radio" name="type" value="admin">管理员
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-4 col-sm-4">
                    <button type="submit" class="btn btn-success">登录</button>
                    <a class="btn btn-info"
                       href="<%=request.getContextPath()%>/register">注册</a>
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
