<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${title}</title>
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
        ${searchHead}
        ${searchBody}
    </div>
</form>
<table class="table table-striped table-bordered table-hover dataTable">
    ${tableHead}
    ${tableBody}
</table>
${page}
<!-- /section:custom/widget-box -->
</body>
</html>
