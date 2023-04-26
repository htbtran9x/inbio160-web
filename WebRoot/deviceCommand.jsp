<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>My JSP 'index.jsp' starting page</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<link rel="stylesheet" type="text/css" href="./css/base.css">
<script type="text/javascript" src="./js/jquery.js"></script>
<script type="text/javascript" src="./js/jquery.easyui.min.js"></script>
<link rel="stylesheet" type="text/css" href="./css/themes/default/easyui.css">
</head>
<body>
    <div class="text">Command Example</div><br>
    <table>
        <tr>
    		<td>
    			<div align="left" class="form_text" style="float: left">Device</div>
    		</td>
    		<td>
			    <select id="group_option" style="width:200px">
			    </select>
    		</td>
    	</tr>
    	<tr>
    		<td colspan="2">1.DATA CRUD
    		<br/>
    			<select id="personOpt" onchange="personOpt(this[selectedIndex].value);"><option>Person Operation</option>
    				<option value="0">Add/Update Personnel</option>
    				<option value="1">Delete Personnel</option>
    				<option value="2">Query All Personnel</option>
    				<option value="3">Count Personnel</option>
    			
    			</select>
    			<select id="timezone" onchange="timezoneOpt(this[selectedIndex].value);"><option>Time Segment Operation</option>
    				<option value="0">Add/Update Time Zone</option>
    				<option value="1">Delete Time Zone</option>
    				<option value="2">Query Time Zone</option>
    				<option value="3">Count Time Zone</option>
    			
    			</select>
    			<select id="accesslevel" onchange="levelOpt(this[selectedIndex].value);"><option>Access Level Operation</option>
    				<option value="0">Add/Update Access Level</option>
    				<option value="1">Delete Access Level</option>
    				<option value="2">Query Access Level</option>
    				<option value="3">Count Access Level</option>
    			
    			</select>

    			<select id="transaction" onchange="transactionOpt(this[selectedIndex].value);"><option>Transaction Operation</option>
    				<option value="0">Add/Update Transaction</option>
    				<option value="1">Delete Transaction</option>
    				<option value="2">Query Transaction</option>
    				<option value="3">Count Transaction</option>
    			
    			</select>
    		 </td>
    	</tr>
    	<tr>
    		<td colspan="2">2.Configuration<br/>
    		<input id="webserverIp" type="button" value="Set WebServerIP And Port" onclick="setServerPara();"> 
    		<input id="driverTime" type="button" value="Set DoorDriverTime" onclick="setDoorDriverTime();"> 
    		</td>
    	</tr>
    	<tr>
    		<td colspan="2">3.Device Controll<br/>
    		<input type="button" value="Remote Control Door 1" onclick="openFirstDoor();"> 
    		<input type="button" value="Cancle All Alarm" onclick="cancleAlarm();"/>
    		<input type="button" value="Reboot" onclick="reboot();"> 
    		</td>
    	</tr>
    	<tr>
    		<td></td><td align="left"><font color="red" size="2">The Command will be saved in a text file，and the character “\t” must be replaced by key “Tab”</font></td>
    	</tr>
    	<tr>
    		<td>
    			<div align="left" class="form_text">Command</div>
    		</td>
    		<td>
    			<textarea rows="" cols="" id="cmd_id" class="form_textfield" style="height: 60px;width: 950px"></textarea>
    		</td>
    	</tr>
    </table>
    <input type="submit" value="Send Command" class="button" style="height:28px;width: 140px " onclick="javascript:submitCmd();">
    
    <br/><br/>
    <div class="text">Returned Information of the Lastest Executed Command</div><br>
    <textarea rows="" cols="" id="last_cmd_data" class="form_textfield" style="height: 80px;width: 1000px"></textarea><br/>
    <div class="text">Command And Returned Value </div><br>
    <table  border="1px"  cellpadding="0" cellspacing="0"  id="cmd_table" width="1000px" class="list_table">
    	<tr  align="left" style="border-bottom:  thick dotted #ff0000;" bgcolor="grey" height="30px">
    		<th width="50px" align="center">Command ID</th>
    		<th width="auto">Command Content</th>
    		<th width="380px">Returned Value</th>
    	</tr>
    </table>
    <br/><br/>
</body>
<script type="text/javascript">
	 $(document).ready(function(){
		queryCmd();
		queryDevice();
		setInterval(queryCmd,1000);
	});
	 
	 /*query the returned result from execulted commands*/
  	function queryCmd(){
  		$.post("/cmdServlet",{},function(retJson){
  			var data = retJson.cmdArray;
  			var lastCmdData = retJson.cmdData;
  			if(data.length > 0){
  				var table = $("#cmd_table");
			 		 var dataTrEle = $("#cmd_table tr:gt(0)");
			 		 var eqEle = $("#cmd_table tr:eq(0)");
			 		 dataTrEle.remove();
			 		 
			 		 var len = data.length;
			 		 for(var i = 0; i < len; i++){
			 			eqEle.after("<tr align=\"left\"  height=\"30px\" align=\"left\"  onmouseover=\"this.style.background=&#39;#e8eaeb&#39;;\" onmouseout=\"this.style.background=&#39;#FFFFFF&#39;\">" +
			 			"<td align=\"center\">" + data[i].cmdId + "</td>" +
// 			 			"<td >" + data[i].sn + "</td>" +
			 			"<td >" + data[i].cmd + "</td>" +
			 			"<td >" + data[i].cmdRet + "</td>" +
			 			"</tr>");
			 		 }	
			 	
			 	$("#last_cmd_data").val(lastCmdData);
			}
  		},"json");
  	}
	function queryDevice()
	{
	 $.post("/deviceServlet",{"type":"1"},function(obj){
			if(obj != '' && obj.desc == 'ok')
			{
				$("#group_option").empty();
				var data = obj.data;
				if(data.length > 0)
				{
					var len = data.length;
					for(var i = 0; i < len; i++)
					{
						$("#group_option").append("<option value='"+data[i].sn +"'>"+data[i].sn +"</option>");
					}	
				}
			}
		},"json");
	}
  	
  	
  	function submitCmd()
  	{
  		var sn = $("#group_option").val();
  		var cmd = $("#cmd_id").val();
  		cmd = ltrim(cmd);
  		if(sn== null || cmd=='')
		{
		
  			$.messager.alert('Alert','You must select the device and input the commands first.','error');
		}
  		else
		{
  			$.post("/createCmd",{
  				"cmdType":"userDefined",
  				"sn":sn,
  				"originalCmd":cmd
  			},function(data){
  			if(data.length > 0){
  			
			}
  		},"json");
 			
		}
  		
  	}
  	
  	function ltrim(str){ //delete space on the left
　　     return str.replace(/(^\s*)/g,"");  
　　 }
  	
  	/*
  		remote open door
  	*/
  	function openFirstDoor(){
  		$("#cmd_id").val("CONTROL DEVICE 01010105");//open door for five seconds
  	}
  	
  	/**
  	*cancle alarm operation
  	*/
  	function cancleAlarm(){
  		$("#cmd_id").val("CONTROL DEVICE 02000000");
  	}
  	
  	
  	
  	/**
  	*	set device's sebserverIP and its port
  	*/
  	function setServerPara(){
  		if($("#webserverIp").val()=="Set WebServerIP And Port"){
  			$("#cmd_id").val("SET OPTIONS WebServerIP=192.168.216.24,WebServerPort=8080");
  			$("#webserverIp").val("Get WebServerIP And Port");
  		}else{
  			$("#cmd_id").val("GET OPTIONS WebServerIP,WebServerPort");
  			$("#webserverIp").val("Set WebServerIP And Port");
  		}
  	}
  	
  	/**
  	*	set door driver time
  	*/
  	function setDoorDriverTime(){
  		if($("#driverTime").val()=="Set DoorDriverTime"){
  			$("#cmd_id").val("SET OPTIONS Door1Drivertime=5");
  			$("#driverTime").val("Get DoorDriverTime");
  		}else{
  			$("#cmd_id").val("GET OPTIONS Door1Drivertime");
  			$("#driverTime").val("Set DoorDriverTime");
  		}
  	}
  	
  	
  	/*
  		device reboot
  	*/
  	function reboot(){
  		$("#cmd_id").val("CONTROL DEVICE 03000000");
  	}

  	
  	/**
  	*person CRUD Option
  	*/
    function personOpt(value){
  		if(value=="0"){
  			$("#cmd_id").val("DATA UPDATE user cardno=20002	pin=1000	password=	starttime=0	endtime=0	name=Keith	superauthorize=0	disable=0");
  		}else if(value=="1"){
  			$("#cmd_id").val("DATA DELETE user pin=1000");
  		}else if(value=="2"){
  			$("#cmd_id").val("DATA QUERY tablename=user,fielddesc=*,filter =*");
  		}else if(value=="3"){
  			$("#cmd_id").val("DATA COUNT user");
  		}else{
  			$("#cmd_id").val("");
  		}
  	}
  	
    /**
  	*time  segment CRUD Option
  	*/
    function timezoneOpt(value){
  		if(value=="0"){
  			$("#cmd_id").val("DATA UPDATE timezone timezoneid=3");
  		}else if(value=="1"){
  			$("#cmd_id").val("DATA DELETE timezone timezoneid=3");
  		}else if(value=="2"){
  			$("#cmd_id").val("DATA QUERY tablename=timezone,fielddesc=*,filter =*");
  		}else if(value=="3"){
  			$("#cmd_id").val("DATA COUNT timezone");
  		}else{
  			$("#cmd_id").val("");
  		}
  	}
  	
    /**
  	*access level CRUD Option
  	*/
    function levelOpt(value){
  		if(value=="0"){
  			$("#cmd_id").val("DATA UPDATE userauthorize pin=1000	authorizetimezoneid=1	authorizedoorid=1");
  		}else if(value=="1"){
  			$("#cmd_id").val("DATA DELETE userauthorize pin=1000	authorizedoorid=1");
  		}else if(value=="2"){
  			$("#cmd_id").val("DATA QUERY tablename=userauthorize,fielddesc=*,filter =*");
  		}else if(value=="3"){
  			$("#cmd_id").val("DATA COUNT userauthorize");
  		}else{
  			$("#cmd_id").val("");
  		}
  	}
    
    
    /**
  	*transaction query Option
  	*/
    function transactionOpt(value){
  		if(value=="0"){
  			$("#cmd_id").val("");alert("Note : event record can not be added or updated manually !");
  		}else if(value=="1"){
  			$("#cmd_id").val("DATA DELETE transaction *");
  		}else if(value=="2"){
  			$("#cmd_id").val("DATA QUERY tablename=transaction,fielddesc=*,filter =*");
  		}else if(value=="3"){
  			$("#cmd_id").val("DATA COUNT transaction");
  		}else{
  			$("#cmd_id").val("");
  		}
  	}
  	
  	
</script>
</html>
