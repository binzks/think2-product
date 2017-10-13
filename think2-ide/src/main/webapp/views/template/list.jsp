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
    ${search}
</form>
<table class="table table-striped table-bordered table-hover dataTable">
    ${tableHead}
    ${tableBody}
</table>
<div class="row">
    <div class="col-xs-6">
        <div class="dataTables_info" id="sample-table-2_info" role="status" aria-live="polite">
            <span>&nbsp;&nbsp;&nbsp;&nbsp; 共10页/92记录</span>
        </div>
    </div>
    <div class="col-xs-6">
        <div class="dataTables_paginate paging_simple_numbers" id="sample-table-2_paginate">
            <ul class="pagination">

                <li class="prev disabled"><a href="javascript:;"><i class="ace-icon fa fa-angle-double-left"></i></a>
                </li>


                <li class="active"><a href="javascript:;">1</a></li>

                <li><a href='javascript:page("/tpl/list2-2.idea");'>2</a></li>


                <li><a href='javascript:page("/tpl/list2-3.idea");'>3</a></li>


                <li><a href='javascript:page("/tpl/list2-4.idea");'>4</a></li>


                <li><a href='javascript:page("/tpl/list2-5.idea");'>5</a></li>


                <li><a href='javascript:page("/tpl/list2-6.idea");'>6</a></li>


                <li class="next"><a href='javascript:page("/tpl/list2-7.idea");'><i
                        class="ace-icon fa fa-angle-right"></i></a></li>


                <li class="next"><a href='javascript:page("/tpl/list2-10.idea");'><i
                        class="ace-icon fa fa-angle-double-right"></i></a></li>

            </ul>
        </div>
    </div>
</div>
<!-- /section:custom/widget-box -->
</body>
</html>
