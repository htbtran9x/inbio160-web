<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>ZKTeco-AC PUSH Demo</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
<frameset rows="*,1" cols="276,*,1" frameborder="no" border="0" framespacing="0">
	<frameset rows="60,*" cols="*" framespacing="0" frameborder="no" border="0">
		<frame src="./logo.jsp" name="topFrame1" scrolling="No" noresize="noresize" id="topFrame1" title="topFrame1">
		<frame src="./menu.jsp" name="leftFrame" scrolling="No" noresize="noresize" id="leftFrame" title="leftFrame">
	</frameset>
	<frameset rows="60,*" cols="*" framespacing="0" frameborder="no" border="0">
		<frame src="head.jsp" name="leftFrame1" scrolling="No" noresize="noresize" id="leftFrame1" title="leftFrame1">
		<frame src="device.jsp" name="mainFrame" id="mainFrame" title="mainFrame">
	</frameset>
</frameset>
<noframes></noframes>
</html>
