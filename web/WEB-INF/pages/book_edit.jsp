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
        <form method="post" action="<%=request.getContextPath()%>/book?act=edit" class="form-horizontal"
              enctype="multipart/form-data">
            <input type="hidden" name="id" value="${book.id}">
            <div class="form-group">
                <label class="col-sm-2 control-label">分类：</label>
                <div class="col-sm-10">
                    <select class="form-control" name="categoryId">
                        <option value="">请选择</option>
                        <c:forEach items="${categories}" var="category">
                            <option value="${category.id}" ${book.categoryId eq category.id?'selected':''}>${category.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">书名：</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" name="title" value="${book.title}">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">出版社：</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" name="publisher" value="${book.publisher}">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">作者：</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" name="author" value="${book.author}">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">出版日期：</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" name="publishDate" value="${book.publishDate}">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">库存：</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" name="stock" value="${book.stock}">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">描述：</label>
                <div class="col-sm-10">
                    <textarea class="form-control" name="description">${book.description}</textarea>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">封面路径：</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="coverUrlInput" name="coverUrl" value="${book.coverUrl}"
                           placeholder="可直接填写已有图片地址">
                    <%-- 支持直接粘贴完整 URL 或已有的相对路径，便于复用已有图片 --%>
                    <p class="help-block">支持填写可访问的图片 URL 或选择文件上传。</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">上传封面：</label>
                <div class="col-sm-10">
                    <input type="file" class="form-control" id="coverFileInput" name="coverFile" accept="image/*">
                    <%-- 上传新文件时将覆盖上方路径，保持两种输入方式互斥 --%>
                    <p class="help-block">如选择文件，将上传并覆盖上方路径。</p>
                    <div style="margin-top: 10px;">
                        <img id="coverPreview" src="" alt="封面预览" style="max-width: 200px; max-height: 200px; display: none;">
                        <a id="coverDownload" href="#" download style="display: none;">下载当前封面</a>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">状态：</label>
                <div class="col-sm-10">
                    <label class="control-label">
                        <input type="radio" name="status" value="上架" ${book.status eq "上架"?"checked":""}>上架
                    </label>
                    <label class="control-label">
                        <input type="radio" name="status" value="下架" ${book.status eq "下架"?"checked":""}>下架
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
<script>
    (function () {
        var contextPath = '<%=request.getContextPath()%>';
        var coverUrlInput = $('#coverUrlInput');
        var coverFileInput = $('#coverFileInput');
        var coverPreview = $('#coverPreview');
        var coverDownload = $('#coverDownload');

        // 解析相对路径或绝对 URL，确保预览时能拼出可访问的地址
        function resolveUrl(url) {
            if (!url) {
                return null;
            }
            var trimmed = url.replace(/^\s+|\s+$/g, '');
            if (trimmed.indexOf('http://') === 0 || trimmed.indexOf('https://') === 0 || trimmed.indexOf('data:') === 0) {
                return trimmed;
            }
            return contextPath + '/' + trimmed.replace(/^\//, '');
        }

        // 根据用户输入或上传结果实时展示预览和下载链接
        function showPreview(url) {
            var finalUrl = resolveUrl(url);
            if (finalUrl) {
                coverPreview.attr('src', finalUrl).show();
                coverDownload.attr('href', finalUrl).show();
            } else {
                coverPreview.hide();
                coverDownload.hide();
            }
        }

        showPreview('${book.coverUrl}');

        coverUrlInput.on('input', function () {
            showPreview($(this).val());
        });

        coverFileInput.on('change', function () {
            var file = this.files && this.files[0];
            if (file) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    coverPreview.attr('src', e.target.result).show();
                    coverDownload.hide();
                };
                reader.readAsDataURL(file);
            } else {
                showPreview(coverUrlInput.val());
            }
        });
    })();
</script>
</body>
</html>
