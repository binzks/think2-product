<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title><sitemesh:write property='title'/></title>
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="/libs/assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="/libs/assets/css/font-awesome.css"/>
    <!-- page specific plugin styles -->
    <!-- text fonts -->
    <link rel="stylesheet" href="/libs/assets/css/ace-fonts.css"/>
    <!-- ace styles -->
    <link rel="stylesheet" href="/libs/assets/css/ace.css" class="ace-main-stylesheet" id="main-ace-style"/>
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="/libs/assets/css/ace-part2.css" class="ace-main-stylesheet"/>
    <![endif]-->
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="/libs/assets/css/ace-ie.css"/>
    <![endif]-->
    <!-- inline styles related to this page -->
    <!-- ace settings handler -->
    <script src="/libs/assets/js/ace-extra.js"></script>
    <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->
    <!--[if lte IE 8]>
    <script src="/libs/assets/js/html5shiv.js"></script>
    <script src="/libs/assets/js/respond.js"></script>
    <![endif]-->

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="/libs/assets/css/bootstrap.css" />
    <link rel="stylesheet" href="/libs/assets/css/font-awesome.css" />

    <!-- page specific plugin styles -->
    <link rel="stylesheet" href="/libs/assets/css/jquery-ui.custom.css" />
    <link rel="stylesheet" href="/libs/assets/css/chosen.css" />
    <link rel="stylesheet" href="/libs/assets/css/datepicker.css" />
    <link rel="stylesheet" href="/libs/assets/css/bootstrap-timepicker.css" />
    <link rel="stylesheet" href="/libs/assets/css/daterangepicker.css" />
    <link rel="stylesheet" href="/libs/assets/css/bootstrap-datetimepicker.css" />
    <link rel="stylesheet" href="/libs/assets/css/colorpicker.css" />

    <!-- text fonts -->
    <link rel="stylesheet" href="/libs/assets/css/ace-fonts.css" />

    <!-- ace styles -->
    <link rel="stylesheet" href="/libs/assets/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="/libs/assets/css/ace-part2.css" class="ace-main-stylesheet" />
    <![endif]-->

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="/libs/assets/css/ace-ie.css" />
    <![endif]-->

    <!-- inline styles related to this page -->

    <!-- ace settings handler -->
    <script src="/libs/assets/js/ace-extra.js"></script>

    <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

    <!--[if lte IE 8]>
    <script src="/libs/assets/js/html5shiv.js"></script>
    <script src="/libs/assets/js/respond.js"></script>
    <![endif]-->
</head>

<body class="no-skin">
<!-- #section:basics/navbar.layout -->
<div id="navbar" class="navbar navbar-default">
    <script type="text/javascript">
        try {
            ace.settings.check('navbar', 'fixed')
        } catch (e) {
        }
    </script>

    <div class="navbar-container" id="navbar-container">
        <!-- #section:basics/sidebar.mobile.toggle -->
        <button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler" data-target="#sidebar">
            <span class="sr-only">Toggle sidebar</span>

            <span class="icon-bar"></span>

            <span class="icon-bar"></span>

            <span class="icon-bar"></span>
        </button>

        <!-- /section:basics/sidebar.mobile.toggle -->
        <div class="navbar-header pull-left">
            <!-- #section:basics/navbar.layout.brand -->
            <a href="#" class="navbar-brand">
                <small>
                    <i class="fa fa-leaf"></i>
                    Ace Admin
                </small>
            </a>

            <!-- /section:basics/navbar.layout.brand -->

            <!-- #section:basics/navbar.toggle -->

            <!-- /section:basics/navbar.toggle -->
        </div>

        <!-- #section:basics/navbar.dropdown -->
        <div class="navbar-buttons navbar-header pull-right" role="navigation">
            <ul class="nav ace-nav">
                <!-- #section:basics/navbar.user_menu -->
                <li class="light-blue">
                    <a data-toggle="dropdown" href="#" class="dropdown-toggle">
                        <img class="nav-user-photo" src="/libs/assets/avatars/user.jpg" alt="Jason's Photo"/>
                        <span class="user-info">
									<small>${sessionScope.code} </small> ${sessionScope.name}
								</span>

                        <i class="ace-icon fa fa-caret-down"></i>
                    </a>
                    <ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
                        <li>
                            <a href="#">
                                <i class="ace-icon fa fa-cog"></i>
                                修改密码
                            </a>
                        </li>

                        <li class="divider"></li>

                        <li>
                            <a href="/think2/admin/logout.do">
                                <i class="ace-icon fa fa-power-off"></i>
                                注销
                            </a>
                        </li>
                    </ul>
                </li>

                <!-- /section:basics/navbar.user_menu -->
            </ul>
        </div>

        <!-- /section:basics/navbar.dropdown -->
    </div><!-- /.navbar-container -->
</div>

<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>

    <!-- #section:basics/sidebar -->
    <div id="sidebar" class="sidebar responsive">
        <script type="text/javascript">
            try {
                ace.settings.check('sidebar', 'fixed')
            } catch (e) {
            }
        </script>
        ${menus}
        <!-- #section:basics/sidebar.layout.minimize -->
        <div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
            <i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left"
               data-icon2="ace-icon fa fa-angle-double-right"></i>
        </div>

        <!-- /section:basics/sidebar.layout.minimize -->
        <script type="text/javascript">
            try {
                ace.settings.check('sidebar', 'collapsed')
            } catch (e) {
            }
        </script>
    </div>

    <!-- /section:basics/sidebar -->
    <div class="main-content">
        <div class="main-content-inner">
            <div class="page-content">
                <div class="row">
                    <div class="col-xs-12">
                        <!-- PAGE CONTENT BEGINS -->
                        <sitemesh:write property='body'/>
                        <!-- PAGE CONTENT ENDS -->
                    </div><!-- /.col -->
                </div><!-- /.row -->
            </div><!-- /.page-content -->
        </div>
    </div><!-- /.main-content -->

    <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
        <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
    </a>
</div><!-- /.main-container -->

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
<script src="/libs/assets/js/bootstrap.js"></script>

<!-- page specific plugin scripts -->

<!-- ace scripts -->
<script src="/libs/assets/js/ace/elements.scroller.js"></script>
<script src="/libs/assets/js/ace/elements.colorpicker.js"></script>
<script src="/libs/assets/js/ace/elements.fileinput.js"></script>
<script src="/libs/assets/js/ace/elements.typeahead.js"></script>
<script src="/libs/assets/js/ace/elements.wysiwyg.js"></script>
<script src="/libs/assets/js/ace/elements.spinner.js"></script>
<script src="/libs/assets/js/ace/elements.treeview.js"></script>
<script src="/libs/assets/js/ace/elements.wizard.js"></script>
<script src="/libs/assets/js/ace/elements.aside.js"></script>
<script src="/libs/assets/js/ace/ace.js"></script>
<script src="/libs/assets/js/ace/ace.ajax-content.js"></script>
<script src="/libs/assets/js/ace/ace.touch-drag.js"></script>
<script src="/libs/assets/js/ace/ace.sidebar.js"></script>
<script src="/libs/assets/js/ace/ace.sidebar-scroll-1.js"></script>
<script src="/libs/assets/js/ace/ace.submenu-hover.js"></script>
<script src="/libs/assets/js/ace/ace.widget-box.js"></script>
<script src="/libs/assets/js/ace/ace.settings.js"></script>
<script src="/libs/assets/js/ace/ace.settings-rtl.js"></script>
<script src="/libs/assets/js/ace/ace.settings-skin.js"></script>
<script src="/libs/assets/js/ace/ace.widget-on-reload.js"></script>
<script src="/libs/assets/js/ace/ace.searchbox-autocomplete.js"></script>

<!-- inline scripts related to this page -->

<!-- the following scripts are used in demo only for onpage help and you don't need them -->
<link rel="stylesheet" href="/libs/assets/css/ace.onpage-help.css"/>
<link rel="stylesheet" href="/libs/assets/js/themes/sunburst.css"/>

<script type="text/javascript"> ace.vars['base'] = '..'; </script>
<script src="/libs/assets/js/ace/elements.onpage-help.js"></script>
<script src="/libs/assets/js/ace/ace.onpage-help.js"></script>
<script src="/libs/assets/js/rainbow.js"></script>
<script src="/libs/assets/js/language/generic.js"></script>
<script src="/libs/assets/js/language/html.js"></script>
<script src="/libs/assets/js/language/css.js"></script>
<script src="/libs/assets/js/language/javascript.js"></script>
<script type="text/javascript">
    doSel();

    function doSel() {
        var url = window.location.href;
        url = url.substring(url.indexOf(window.location.host)
            + window.location.host.length, url.length);
        var i = url.indexOf(".");
        if (i > 0) {
            url = url.substring(0, i);
        }
        i = url.indexOf("-");
        if (i > 0) {
            url = url.substring(0, i);
        }
        var liObj = document.getElementById("menu_" + url);
        if (null == liObj) {
            url = document.referrer;
            url = url.substring(url.indexOf(window.location.host)
                + window.location.host.length, url.length);
            var i = url.indexOf(".");
            if (i > 0) {
                url = url.substring(0, i);
            }
            i = url.indexOf("-");
            if (i > 0) {
                url = url.substring(0, i);
            }
            liObj = document.getElementById("menu_" + url);
        }
        liObj.setAttribute("class", "active");
        liObj.parentNode.parentNode.setAttribute("class", "active open");
        var parent = liObj.parentNode.parentNode.parentNode.parentNode;
        if (parent.tagName == "LI") {
            parent.setAttribute("class", "active open");
        }
    }
</script>
</body>
</html>