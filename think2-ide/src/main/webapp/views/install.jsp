<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>IDE系统安装</title>

    <meta name="description" content="User login page"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="/libs/assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="/libs/assets/css/font-awesome.css"/>

    <!-- text fonts -->
    <link rel="stylesheet" href="/libs/assets/css/ace-fonts.css"/>

    <!-- ace styles -->
    <link rel="stylesheet" href="/libs/assets/css/ace.css"/>

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="/libs/assets/css/ace-part2.css"/>
    <![endif]-->
    <link rel="stylesheet" href="/libs/assets/css/ace-rtl.css"/>

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="/libs/assets/css/ace-ie.css"/>
    <![endif]-->

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

    <!--[if lt IE 9]>
    <script src="/libs/assets/js/html5shiv.js"></script>
    <script src="/libs/assets/js/respond.js"></script>
    <![endif]-->
</head>

<body class="login-layout light-login">
<div class="main-container">
    <div class="main-content">
        <div class="row">
            <a href="/ide/install/install.api">安装</a>
        </div><!-- /.row -->
    </div><!-- /.main-content -->
</div><!-- /.main-container -->
<div id="log-container" style="height: 450px; overflow-y: scroll; background: #333; color: #aaa; padding: 10px;">
    <div>
    </div>
</div>
<script>
    $(document).ready(function() {
        // 指定websocket路径
        var websocket = new WebSocket('ws://localhost:8080/log');
        websocket.onmessage = function(event) {
            // 接收服务端的实时日志并添加到HTML页面中
            $("#log-container div").append(event.data);
            // 滚动条滚动到最低部
            $("#log-container").scrollTop($("#log-container div").height() - $("#log-container").height());
        };
    });
</script>
<!-- basic scripts -->

<!--[if !IE]> -->
<script type="text/javascript">
    window.jQuery || document.write("<script src='/libs/assets/js/jquery.js'>" + "<" + "/script>");
</script>

<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
    window.jQuery || document.write("<script src='/libs/assets/js/jquery1x.js'>" + "<" + "/script>");
</script>
<![endif]-->
<script type="text/javascript">
    if ('ontouchstart' in document.documentElement) document.write("<script src='/libs/assets/js/jquery.mobile.custom.js'>" + "<" + "/script>");
</script>

</body>
</html>
