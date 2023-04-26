<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>just designed one time segment for this demo</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<script type="text/javascript" src="./js/jquery.js"></script>
<script type="text/javascript" src="./js/jquery.easyui.min.js"></script>
<link rel="stylesheet" type="text/css" href="./css/base.css">
<link rel="stylesheet" type="text/css" href="./css/themes/default/easyui.css">
</head>
<body style="margin-left: 20px;margin-top: 20px;">
	<div   class="text">Time Zone Setting</div><br/><br/><br/>
	<div>
		Time Zone Name:
		<input style="border:1px solid #c3c2c2;" id="desc">
		<input type="hidden" id="id">
	</div>
	<table>
		<tr>
			<th>&nbsp;</th><th style="width:110px;">Time Segment 1 Start</th><th style="width:110px;">Time Segment 1 End</th><th style="width:110px;">Time Segment 2 Start</th><th style="width:110px;">Time Segment 2 End</th><th style="width:110px;">Time Segment 3 Start</th><th style="width:110px;">Time Segment 3 End</th>
		</tr>
		<tr align="center">
			<td>Monday</td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="mon_start_1"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="mon_end_1"/></td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="mon_start_2"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="mon_end_2"/></td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="mon_start_3"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="mon_end_3"/></td>
		</tr>
		<tr align="center">
			<td>Tuesday</td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="tue_start_1"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="tue_end_1"/></td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="tue_start_2"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="tue_end_2"/></td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="tue_start_3"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="tue_end_3"/></td>
		</tr>
		<tr align="center">
			<td>Wednesday</td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="wed_start_1"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="wed_end_1"/></td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="wed_start_2"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="wed_end_2"/></td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="wed_start_3"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="wed_end_3"/></td>
		</tr>
		<tr align="center">
			<td>Thursday</td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="thu_start_1"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="thu_end_1"/></td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="thu_start_2"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="thu_end_2"/></td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="thu_start_3"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="thu_end_3"/></td>
		</tr>
		<tr align="center">
			<td>Friday</td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="fri_start_1"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="fri_end_1"/></td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="fri_start_2"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="fri_end_2"/></td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="fri_start_3"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="fri_end_3"/></td>
		</tr>
		<tr align="center">
			<td>Saturday</td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="sat_start_1"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="sat_end_1"/></td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="sat_start_2"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="sat_end_2"/></td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="sat_start_3"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="sat_end_3"/></td>
		</tr>
		<tr align="center">
			<td>Sunday</td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="sun_start_1"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="sun_end_1"/></td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="sun_start_2"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="sun_end_2"/></td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="sun_start_3"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="sun_end_3"/></td>
		</tr>
		<tr align="center">
			<td>Holiday Type1</td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="hol1_start_1"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="hol1_end_1"/></td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="hol1_start_2"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="hol1_end_2"/></td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="hol1_start_3"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="hol1_end_3"/></td>
		</tr>
		<tr align="center">
			<td>Holiday Type2</td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="hol2_start_1"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="hol2_end_1"/></td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="hol2_start_2"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="hol2_end_2"/></td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="hol2_start_3"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="hol2_end_3"/></td>
		</tr>
		<tr align="center">
			<td>Holiday Type3</td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="hol3_start_1"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="hol3_end_1"/></td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="hol3_start_2"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="hol3_end_2"/></td>
				<td><input style="width:80px;"  class="easyui-timespinner"  id="hol3_start_3"/></td><td><input style="width:80px;"  class="easyui-timespinner"  id="hol3_end_3"/></td>
		</tr>
	</table>
	<input type="submit" value="Submit" class="button" style="height:28px;width: 100px " onclick="mySubmit()">
	
</body>
<script type="text/javascript">
	$(document).ready(function(){
		getTime();
	});
	function getTime()
	{
		$.post("/timeServlet",{"type":"1"},function(obj){
			if(obj!='' && obj.ret== 'ok'){
				if(obj.data.length >0 )
				{
					var myData = obj.data;
					var timeData = myData[0];
					
					$("#desc").val(timeData.desc);
					$("#id").val(timeData.id);
					
					for( var mykey in timeData)
					{
						var value = timeData[mykey];
						if(mykey != 'desc' && mykey != 'id')
						{
							if($('#'+mykey).length>0)
								$('#'+mykey).timespinner('setValue', value);
						}
					}
				}
			}
		},"json");
	}
	
	function mySubmit()
	{
		$.post(
			"/timeServlet"
			,{
				'mon_start_1':$('#mon_start_1').timespinner('getValue'),'mon_end_1':$('#mon_end_1').timespinner('getValue'),
				'mon_start_2':$('#mon_start_2').timespinner('getValue'),'mon_end_2':$('#mon_end_2').timespinner('getValue'),
				'mon_start_3':$('#mon_start_3').timespinner('getValue'),'mon_end_3':$('#mon_end_3').timespinner('getValue'),
				'tue_start_1':$('#tue_start_1').timespinner('getValue'),'tue_end_1':$('#tue_end_1').timespinner('getValue'),
				'tue_start_2':$('#tue_start_2').timespinner('getValue'),'tue_end_2':$('#tue_end_2').timespinner('getValue'),
				'tue_start_3':$('#tue_start_3').timespinner('getValue'),'tue_end_3':$('#tue_end_3').timespinner('getValue'),
				'wed_start_1':$('#wed_start_1').timespinner('getValue'),'wed_end_1':$('#wed_end_1').timespinner('getValue'),
				'wed_start_2':$('#wed_start_2').timespinner('getValue'),'wed_end_2':$('#wed_end_2').timespinner('getValue'),
				'wed_start_3':$('#wed_start_3').timespinner('getValue'),'wed_end_3':$('#wed_end_3').timespinner('getValue'),
				'thu_start_1':$('#thu_start_1').timespinner('getValue'),'thu_end_1':$('#thu_end_1').timespinner('getValue'),
				'thu_start_2':$('#thu_start_2').timespinner('getValue'),'thu_end_2':$('#thu_end_2').timespinner('getValue'),
				'thu_start_3':$('#thu_start_3').timespinner('getValue'),'thu_end_3':$('#thu_end_3').timespinner('getValue'),
				'fri_start_1':$('#fri_start_1').timespinner('getValue'),'fri_end_1':$('#fri_end_1').timespinner('getValue'),
				'fri_start_2':$('#fri_start_2').timespinner('getValue'),'fri_end_2':$('#fri_end_2').timespinner('getValue'),
				'fri_start_3':$('#fri_start_3').timespinner('getValue'),'fri_end_3':$('#fri_end_3').timespinner('getValue'),
				'sat_start_1':$('#sat_start_1').timespinner('getValue'),'sat_end_1':$('#sat_end_1').timespinner('getValue'),
				'sat_start_2':$('#sat_start_2').timespinner('getValue'),'sat_end_2':$('#sat_end_2').timespinner('getValue'),
				'sat_start_3':$('#sat_start_3').timespinner('getValue'),'sat_end_3':$('#sat_end_3').timespinner('getValue'),
				'sun_start_1':$('#sun_start_1').timespinner('getValue'),'sun_end_1':$('#sun_end_1').timespinner('getValue'),
				'sun_start_2':$('#sun_start_2').timespinner('getValue'),'sun_end_2':$('#sun_end_2').timespinner('getValue'),
				'sun_start_3':$('#sun_start_3').timespinner('getValue'),'sun_end_3':$('#sun_end_3').timespinner('getValue'),
				'hol1_start_1':$('#hol1_start_1').timespinner('getValue'),'hol1_end_1':$('#hol1_end_1').timespinner('getValue'),
				'hol1_start_2':$('#hol1_start_2').timespinner('getValue'),'hol1_end_2':$('#hol1_end_2').timespinner('getValue'),
				'hol1_start_3':$('#hol1_start_3').timespinner('getValue'),'hol1_end_3':$('#hol1_end_3').timespinner('getValue'),
				'hol2_start_1':$('#hol2_start_1').timespinner('getValue'),'hol2_end_1':$('#hol2_end_1').timespinner('getValue'),
				'hol2_start_2':$('#hol2_start_2').timespinner('getValue'),'hol2_end_2':$('#hol2_end_2').timespinner('getValue'),
				'hol2_start_3':$('#hol2_start_3').timespinner('getValue'),'hol2_end_3':$('#hol2_end_3').timespinner('getValue'),
				'hol3_start_1':$('#hol3_start_1').timespinner('getValue'),'hol3_end_1':$('#hol3_end_1').timespinner('getValue'),
				'hol3_start_2':$('#hol3_start_2').timespinner('getValue'),'hol3_end_2':$('#hol3_end_2').timespinner('getValue'),
				'hol3_start_3':$('#hol3_start_3').timespinner('getValue'),'hol3_end_3':$('#hol3_end_3').timespinner('getValue'),
				'desc':$("#desc").val(),
				'id':$("#id").val(),
				'type':'0'
			 }
			,function(obj){
				if(obj!='' && obj.ret== 'ok'){
					 $.messager.alert('Result','Success');
				}else{
					 $.messager.alert('Result','Failed','error');
				}
			}
			,"json");
		
		
	}
	function centerMessage(){
        $.messager.show({
            title:'My Title',
            msg:'The message content',
            showType:'fade',
            style:{
                right:'',
                bottom:''
            }
        });
   }
</script>

</html>
