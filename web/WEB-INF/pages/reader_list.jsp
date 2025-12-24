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
    <jsp:param name="active" value="6"/>
</jsp:include>
<div class="row" style="padding-top:20px">
    <div class="col-lg-8">
        <form action="<%=request.getContextPath()%>/reader?act=list" method="post" class="form-inline"
              id="searchForm">
            <input type="hidden" name="pageNum" id="pageNum">
            <div class="form-group">
                <label>手机号</label>
                <input type="text" class="form-control" name="mobile" value="${param.mobile}">
            </div>
            <div class="form-group">
                <label>身份证</label>
                <input type="text" class="form-control" name="idCard" value="${param.idCard}">
            </div>
            <div class="form-group">
                <label>状态</label>
                <select class="form-control" name="status">
                    <option value="">请选择</option>
                    <option value="正常" ${param.status eq '正常'?'selected':''}>正常</option>
                    <option value="禁用" ${param.status eq '禁用'?'selected':''}>禁用</option>
                </select>
            </div>
            <div class="form-group">
                <button type="submit" class="btn">查询</button>
                <button type="button" class="btn btn-danger" id="batchDeleteBtn">批量删除</button>
                <button type="button" class="btn btn-default" id="invertSelectionBtn">反选</button>
                <span style="color: red">${message}</span>
            </div>
        </form>
        <form id="batchDeleteForm" method="post" action="<%=request.getContextPath()%>/reader?act=batch_delete">
            <input type="hidden" name="ids" id="batchDeleteIds">
        </form>
        <table class="table table-striped table-bordered" style="margin-top:20px">
            <tr>
                <td style="width: 60px"><input type="checkbox" id="selectAll"> 全选</td>
                <td>ID</td>
                <td>用户名</td>
                <td>姓名</td>
                <td>手机号</td>
                <td>身份证</td>
                <td>状态</td>
                <td>操作</td>
            </tr>
            <c:forEach items="${readerList}" var="c">
                <tr>
                    <td><input type="checkbox" class="user-checkbox" value="${c.id}"></td>
                    <td>${c.id}</td>
                    <td>${c.username}</td>
                    <td>${c.name}</td>
                    <td>${c.mobile}</td>
                    <td>${c.idCard}</td>
                    <td>${c.status}</td>
                    <td>
                        <a href="<%=request.getContextPath()%>/reader?act=go_edit&id=${c.id}">修改</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
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
    function changePage(pn) {
        if (pn < 1) {
            pn = 1
        } else if (pn > '${requestScope.pageInfo.totalPage}') {
            pn = '${requestScope.pageInfo.totalPage}'
        }
        $("#pageNum").val(pn);
        $("#searchForm").submit()
    }

    function updateSelectAllState() {
        var total = $(".user-checkbox").length;
        var checked = $(".user-checkbox:checked").length;
        $("#selectAll").prop("checked", total > 0 && total === checked);
    }

    function collectSelectedIds() {
        return $(".user-checkbox:checked").map(function () {
            return $(this).val();
        }).get();
    }

    $(function () {
        $("#selectAll").on("change", function () {
            var isChecked = $(this).is(":checked");
            $(".user-checkbox").prop("checked", isChecked);
            updateSelectAllState();
        });

        $(document).on("change", ".user-checkbox", function () {
            updateSelectAllState();
        });

        $("#invertSelectionBtn").on("click", function () {
            $(".user-checkbox").each(function () {
                $(this).prop("checked", !$(this).prop("checked"));
            });
            updateSelectAllState();
        });

        $("#batchDeleteBtn").on("click", function () {
            var ids = collectSelectedIds();

            if (ids.length === 0) {
                alert("请选择要删除的用户");
                return;
            }

            if (confirm("确认删除选中的用户吗？")) {
                $("#batchDeleteIds").val(ids.join(","));
                $("#batchDeleteForm").submit();
            }
        });

        updateSelectAllState();
    });
</script>
</body>
</html>
