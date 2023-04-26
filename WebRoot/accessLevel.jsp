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
<body style="margin-left: 20px;margin-top: 20px">

	<div class="text">Access Level</div><br>
		<table>
			<tr>
				<td>Code：</td><td><input style="width:150px" id="code_group"></td>
			</tr>
			<tr>
				<td>Access Level Name:</td><td><input style="width:150px" id="desc_group"></td>
			</tr>
			<tr>
				<td>Time Zone:</td>
				<td>
					<select style="width:150px" id="time">
					</select>
				</td>
			</tr>
			<tr>
				<td>Door:</td>
				<td>
					<fieldset style="width: 600px" id="dev">
					</fieldset>
				</td>
			</tr>
		</table>
	<br/>
	<input type="submit" value="Submit" class="button" style="height:28px;width: 100px " onclick="submitGroup()">
	
	<br/><br/><br/><br/>
	
	<div class="text">Personnel Access Level Setting</div><br>
	<table>
		<tr>
			<td>Access Level：</td>
			<td>
				<select style="width:100px;text-align:center" id="group_option" onchange="javascript:getBaseData()"></select>
			</td>
		</tr>
		<tr>
			<td>Select Personnel:</td>
			<td>
				<fieldset  id="emp"  style="width: 600px ;margin: 0px;padding: 0px" >
					<table width="100%">
						<tr  align="center" style="border-bottom:  thick dotted #ff0000;" bgcolor="grey" height="30px">
							<th>Check To Add</th><th>Check To Remove</th>
						</tr>
						<tr>
							<td height="100px" id="emp_unselected" width="50%" bgcolor="#DBDBDB">
							</td>
							<td height="100px" id="emp_selected" bgcolor="#CDCDB4">
							</td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
	</table>
	<br/>
	<input type="submit" value="Submit" class="button" style="height:28px;width: 100px " onclick="submitAuth()">
	
	<br/><br/><br/><br/>
	<div class="text">Access Level List</div><br>
		<table width="500" cellpadding="0" cellspacing="0" class="list_table" id="table">
		<tr align="center" style="border-bottom:  thick dotted #ff0000;" bgcolor="grey" height="30px">
			<th class="table_head">Code</th>
			<th class="table_head">Description</th>
			<th class="table_head">Time Slot</th>
		</tr>
	</table>
</body>
<script type="text/javascript">

$(document).ready(function(){
	refreshGroup();
});

function submitAuth()
{
	var emp_unselected = '';
	var emp_selected = '';
	var groupCode=$("#group_option").val();
	var unselectedObj = $("input[type='checkbox'][name='emp_nuselected']:checked");
	var selectedObj = $("input[type='checkbox'][name='emp_selected']:checked");
	unselectedObj.each(function(){
	   emp_unselected += $(this).val()+"|";
	 });
	selectedObj.each(function(){
	   emp_selected += $(this).val()+"|";
	 });
	
	
	$.post("/authorityServlet",{"type":"4","groupCode":groupCode,"add":emp_unselected,"del":emp_selected},function(obj){
		if(obj!='' && obj.ret=='ok')
		{
			getBaseData();
		}
		else
		{
			$.messager.alert('Result','Access set failed,please check input','error');
		}
	},"json");
	
	
}
//Refresh  Access group
function refreshGroup()
{
	$.post("/authorityServlet",{"type":"3"},function(obj){
		if(obj!='' && obj.ret == 'ok')
		{
		
			var table = $("#table");
			var dataTrEle = $("#table tr:gt(0)");
			dataTrEle.remove();
			
			$("#group_option").empty();
			
			var data = obj.data;		 
			var len = data.length;
			for(var i = 0; i < len; i++){
				
				//refresh list
				table.append("<tr height=\"30px\" align=\"center\"  onmouseover=\"this.style.background=&#39;#e8eaeb&#39;;\" onmouseout=\"this.style.background=&#39;#FFFFFF&#39;\">" +
				"<td>" + data[i].code + "</td>" +
				"<td>" + data[i].desc + "</td>" +
				"<td>" + data[i].timeDesc + "</td>" +
				//"<td>" + "<a href=\"javascript:executeCmd('openDoor','"+data[i].sn+"')\">开门</a>" + "</td>" +
				"</tr>");
				
				//refresh element select
				$("#group_option").append("<option value='"+data[i].code+"'>"+data[i].desc+"</option>");
			}
			getBaseData();
		}
	},"json");
}

function submitGroup()
{
	var doors = '';
	var devDoors = $("input[type='checkbox'][id^='dev_'][id$='-']:checked");
	devDoors.each(function(){
	   doors += $(this).val()+"|";
	 });
	if(doors.length > 0){
		doors = doors.substring(0,doors.length -1);
	}
	
	
	var code  = $('#code_group').val();
	var desc  = $('#desc_group').val();
	var time = $('#time').val();
	$.post("/authorityServlet",{"type":"2","code":code,"desc":desc,"time":time,"doors":doors},function(data){
		if(data!='' && data.ret == 'ok')
		{
			refreshGroup();
			$.messager.alert('Result','Success');
		}
		else
		{
			$.messager.alert('Result','failed，please check the input','error');
		}
	},"json");
}


function getBaseData()
{
	$.post("/authorityServlet",{"type":"1","authGroup":$("#group_option").val()},function(obj_value){
		if( obj_value.ret = 'ok'){
			var devs = obj_value.dev;
			if(devs!='')
			{
				var devHtml='';
				var len = devs.length;
				$('#dev').empty();
				for(var i = 0; i < len; i++)
				{
					var dev=devs[i];
					devHtml += '<input type="checkbox" id="dev_'+dev.sn+'_">'+dev.sn+'&nbsp;&nbsp;&nbsp;';
					var lockCount = dev.lockCount;
					for(var j = 1; j <= lockCount; j++)
					{
						devHtml += '<input type="checkbox" id="dev_'+dev.sn+'_'+j+'-" value="'+dev.sn+'_'+j+'">Door &nbsp;'+(j);
					}
					devHtml +='<br/>';
					$('#dev').append(devHtml);
				}
			}
			var emps = obj_value.emp;
			if(emps != '')
			{
				var nuempHtml='';
				var empHtml='';
				var un =0;
				var has =0;
				var len = emps.length;
				$('#emp_unselected').empty();
				$('#emp_selected').empty();
				for(var i = 0; i < len; i++)
				{
					var emp=emps[i];
					if(emp.has_selected=="selected")
					{
						empHtml += '<input type="checkbox" name="emp_selected" value="'+emp.pin+'">'+emp.empName+'&nbsp;&nbsp;';
						has++;
					}
					else
					{
						nuempHtml += '<input type="checkbox" name="emp_nuselected" value="'+emp.pin+'">'+emp.empName+'&nbsp;&nbsp;';
						un++;									   
					}
					
					if((has) % 4 == 0 && has != 0)		empHtml +='<br/>';
					if((un) % 4 == 0 && un!=0)		nuempHtml +='<br/>';
				}
				$('#emp_unselected').append(nuempHtml);
				$('#emp_selected').append(empHtml);
			}
			var times = obj_value.time;
			if(times != '')
			{
				var timeHtml='';
				var len = times.length;
				$('#time').empty();
				for(var i = 0; i < times.length; i++)
				{
					var time=times[i];
					timeHtml += '<option value="'+time.id+'">'+time.desc+'</option>';
					if((i+1) % 7 == 0)
					{
						timeHtml +='<br/>';
					}
				}
				$('#time').append(timeHtml);
				
				
			}
			bindEvent();
		}
	},"json");
}

function bindEvent()
{
	var devs = $("input[type='checkbox'][id^='dev_'][id$='_']");
	devs.bind("click", function () {
		$("input[type='checkbox'][id^='dev_']").attr("checked",this.checked);
	});
}


</script>



</html>
