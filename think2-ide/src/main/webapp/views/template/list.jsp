<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${title}列表</title>
</head>
<body>
<form action="${uri}" method="post" id="list_form" name="list_form">
    <script language="javascript">
        function page(action) {
            document.list_form.action = action;
            list_form.submit();
        }
    </script>
    <div class="widget-box">
        <div class="widget-header">
            <h5 class="widget-title">搜索</h5>
            <div class="widget-toolbar">
                <a href="${url}" title="查询" class="ace-icon fa fa-search orange bigger-130"></a>
                <a href="#" data-action="collapse"><i class="ace-icon fa fa-chevron-down bigger-130"></i></a>
            </div>
            ${actions}
        </div>
        <div class="widget-body">
            <div class="widget-main">
                <div class="row">
                    ${search}
                </div>
            </div>
        </div>
    </div>
</form>
<table class="table table-striped table-bordered table-hover dataTable">
    ${head}
    ${body}
</table>
${page}
<!-- /section:custom/widget-box -->
</body>
</html>
