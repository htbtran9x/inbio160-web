<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>My JSP 'index.jsp' starting page</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<link rel="stylesheet" type="text/css" href="./css/base.css">
<script type="text/javascript" src="./js/jquery.js"></script>
</head>
<body style="margin-left: 20px;margin-top: 20px">
	<div class="text">Access Devices</div><br>
	<table width="1000" cellpadding="0" cellspacing="0" class="list_table" id="table">
		<tr align="center" style="border-bottom:  thick dotted #ff0000;" bgcolor="grey" height="30px">
			<th class="table_head" width="150px" >sn</th>
			<th class="table_head" width="150px">Device Name</th>
			<th class="table_head" width="150px">Register Code</th>
			<th class="table_head" width="350px">Device Version</th>
			<th class="table_head" width="150px">Operation</th>
		</tr>
	</table>
</body>
<script type="text/javascript">
	$(document).ready(function(){
		queryDevice();
	});
	
	function queryDevice(){
		$.post("/deviceServlet",{"type":"1"},function(obj){
			if(obj != '' && obj.desc == 'ok')
			{
				var data = obj.data;
				if(data.length > 0){
					var table = $("#table");
					var dataTrEle = $("#table tr:gt(1)");
					dataTrEle.remove();
					 		 
					var len = data.length;
					for(var i = 0; i < len; i++){
						table.append("<tr align=\"center\" height=\"30px\" align=\"left\"  onmouseover=\"this.style.background=&#39;#e8eaeb&#39;;\" onmouseout=\"this.style.background=&#39;#FFFFFF&#39;\">" +
						"<td >" + data[i].sn + "</td>" +
						"<td>" + data[i].DeviceName + "</td>" +
						"<td>" + data[i].registrycode + "</td>" +
						"<td>" + data[i].FirmVer + "</td>" +
						"<td>" +
							"<a href=\"javascript:executeCmd('openDoor','"+data[i].sn+"')\">Open</a>&nbsp;&nbsp;&nbsp;" +
							"<a href=\"javascript:syncData('"+data[i].sn+"')\">Synchronize</a>" +
						"</td>" +
						"</tr>");
					}	
				}
			}
		},"json");
	}
	  	//send command
  	function executeCmd(type,sn){
  		$.post("/createCmd",{"cmdType":type,"sn": sn},function(data){
  		});
  	}
	  	
	function syncData(sn)
	{
		$.post("/deviceServlet",{"type":"2","sn": sn},function(data){
  		});
	}
	  	
	  	


</script>
</html>
