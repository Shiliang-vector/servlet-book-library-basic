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
<body>
<div class="container-fluid">
    <jsp:include page="common.jsp">
        <jsp:param name="active" value="1"/>
    </jsp:include>
    <h1>欢迎:${sessionScope.SESSION_USER.nickname}</h1>
</div>
</body>
</html>
