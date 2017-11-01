<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>错误</title>
</head>
<body>
<div class="error-container">
    <div class="well">
        <h1 class="grey lighter smaller">
				<span class="blue bigger-125"> <i class="icon-sitemap"></i>
					${msg }
				</span>
        </h1>
    </div>
    <div class="clearfix form-actions center">
        <div class="col-md-12">
            <button class="btn btn-sm" type="reset"
                    onclick="location.href='javascript:history.go(-1);'">
                <i class="icon-undo bigger-110"></i> 返回
            </button>
        </div>
    </div>
</div>
</body>
</html>