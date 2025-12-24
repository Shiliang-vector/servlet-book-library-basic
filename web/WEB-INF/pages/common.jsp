<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- 左侧导航栏复用片段，通过传入 active 参数控制当前页面高亮 --%>
<div class="row" style="padding-bottom: 10px">
    <div class="col-md-10" style="background-color: #337ab7">
        <h2 style="color: white">图书借阅管理系统</h2>
    </div>
    <div class="col-md-2" style="background-color: #337ab7;line-height: 63px;height: 63px;color: white">
        <span>你好:${sessionScope.SESSION_USER.nickname}</span>
    </div>
</div>
<div class="col-md-2">
    <ul class="nav nav-pills  nav-stacked">
        <%-- 通过请求参数 active 控制选中态，避免每个页面重复维护样式 --%>
        <li class="${param.active eq '1'?'active':''}"><a
                href="<%=request.getContextPath()%>/page?act=index">首页</a></li>
        <li class="${param.active eq '2'?'active':''}"><a
                href="<%=request.getContextPath()%>/page?act=password">修改密码</a></li>
        <%--如果是读者则显示--%>
        <c:if test="${sessionScope.SESSION_USER.type eq 'reader'}">
            <%-- 读者专属入口，管理员不展示，避免误点进入借阅列表 --%>
            <li class="${param.active eq '7'?'active':''}"><a
                    href="<%=request.getContextPath()%>/borrow?act=list">我要借书</a></li>
        </c:if>
        <li class="${param.active eq '3'?'active':''}"><a
                href="<%=request.getContextPath()%>/record?act=list">借阅记录</a></li>
        <%--如果是管理员则显示--%>
        <c:if test="${sessionScope.SESSION_USER.type eq 'admin'}">
            <%-- 管理员独有的管理入口，按业务模块拆分 --%>
            <li class="${param.active eq '4'?'active':''}"><a
                    href="<%=request.getContextPath()%>/book?act=list">图书管理</a></li>
            <li class="${param.active eq '5'?'active':''}"><a
                    href="<%=request.getContextPath()%>/category?act=list">分类管理</a></li>
            <li class="${param.active eq '6'?'active':''}"><a
                    href="<%=request.getContextPath()%>/reader?act=list">读者管理</a></li>
        </c:if>
        <li><a href="<%=request.getContextPath()%>/logout">退出登录</a></li>
    </ul>
</div>