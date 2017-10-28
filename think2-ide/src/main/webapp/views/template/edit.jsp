<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>修改${title}</title>
</head>
<body>
<form class="form-horizontal" role="form" action="/tpl/save${mid}-${id}.page" method="post"
      enctype="multipart/form-data">
    ${body}
    <div class="clearfix form-actions center">
        <div class="col-md-12">
            <button class="btn btn-info btn-sm" type="submit">
                <i class="icon-ok bigger-110"></i> 提交
            </button>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <button class="btn btn-sm" type="reset"
                    onclick="location.href='javascript:history.go(-1);'">
                <i class="icon-undo bigger-110"></i> 返回
            </button>
        </div>
    </div>
</form>
</body>
</html>
