<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>My JSP 'index.jsp' starting page</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src="./js/jquery.js"></script>
<style type="text/css">
body {
	margin: 0px;
	padding: 0px;
	background-color: #474b4f;
	list-style-type: none;
}

.menu div {
	width: 280px;
	height: 60px;
	text-align: center;
	line-height: 60px;
	font-size: 16px;
	color: #FFFFFF;
	font-family: "Arial";
}

.menu img {
	width: 280px;
	height: 2px;
}

.selected {
	background-image: url(./image/menu_selected.png); 
	background-size:280px 60px; 
}
</style>
<script type="text/javascript">
	function openContent(url,obj){
		changeStyle(obj);
		parent.document.getElementById("mainFrame").src=url;
	}
	function changeStyle(obj){
		var menus = $(".menu div");
		menus.removeAttr("class");
		$(obj).attr("class","selected");
	}


</script>
</head>
<body>
	<div>
		<div class="menu">
			<div onclick="javascript:openContent('personnel.jsp',this);">Personnel</div><img src="./image/menu_split_line.png"></img>
			<div onclick="javascript:openContent('device.jsp',this);">Device</div><img src="./image/menu_split_line.png"></img>
			<div onclick="javascript:openContent('timezone.jsp',this);">Time Zone</div><img src="./image/menu_split_line.png"></img>
			<div onclick="javascript:openContent('accessLevel.jsp',this);">Access Levles</div><img src="./image/menu_split_line.png"></img>
			<div onclick="javascript:openContent('deviceCommand.jsp',this);">Device Commands</div><img src="./image/menu_split_line.png"></img>
			<div onclick="javascript:openContent('monitoring.jsp',this);">Real-Time Monitoring</div><img src="./image/menu_split_line.png"></img>
		</div>
	</div>
</body>
</html>
