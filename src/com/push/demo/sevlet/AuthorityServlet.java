package com.push.demo.sevlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.push.demo.db.Db;
import com.push.demo.util.Cmd;

public class AuthorityServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject info = new JSONObject();
		JSONArray dev = new JSONArray();
		JSONArray emp = new JSONArray();
		JSONArray time = new JSONArray();
		JSONArray data = new JSONArray();
		String ret = "ok";
		try{
			String type = request.getParameter("type");
			type = null == type ? "" : type;
			if(type.equals("1"))//query
			{
				
				//devices
				Map<String, Map<String, Object>> devs = Db.devMap;
				for(String sn : devs.keySet())
				{
					Map<String,Object> options = (Map<String,Object>)devs.get(sn).get("options");
					JSONObject d = new JSONObject();
					d.put("sn",sn);
					d.put("lockCount",options.get("LockCount"));
					dev.add(d);
				}
				//persons
				Map<String, Map<String, String>> emps = Db.empMap;
				String groutCode = request.getParameter("authGroup");
				groutCode = (groutCode==null || groutCode.equals("null"))? "" : groutCode;
				String hasEmp = Db.empAuthMap.get(groutCode)== null ? "" : Db.empAuthMap.get(groutCode);
				for(String pin : emps.keySet())
				{
					JSONObject e = new JSONObject();
					e.put("empName", emps.get(pin).get("empName"));
					e.put("pin", pin);
					if(Arrays.asList(hasEmp.split("\\|")).contains(pin))
					{
						e.put("has_selected", "selected");
					}
					else
					{
						e.put("has_selected", "");
					}
					emp.add(e);
				}
				//time
				Map<String, Map<String, String>> times = Db.timeMap;
				for(String id : times.keySet())
				{
					JSONObject e = new JSONObject();
					e.put("desc", times.get(id).get("desc"));
					e.put("id", id);
					time.add(e);
				}
			}
			else if(type.equals("2"))//Add Access Level 
			{
				String code = request.getParameter("code");
				String desc = request.getParameter("desc");
				String timeId = request.getParameter("time");
				String doors = request.getParameter("doors");
				
				
				if(code!=null && !code.equals("") && timeId!=null && !timeId.equals(""))
				{
					Map<String, String> map  = new HashMap<String, String>();
					map.put("desc", desc);
					map.put("time", timeId);
					map.put("doors", doors);
					Db.authGroupMap.put(code, map);
				}
				else
				{
					ret = "error";
				}
			}
			else if(type.equals("3"))
			{
				Map<String, Map<String,String>> authGroup = Db.authGroupMap;
				Set<String> set = authGroup.keySet();
				for(String code : set)
				{
					JSONObject temp =  new JSONObject();
					temp.put("desc", authGroup.get(code).get("desc"));
					temp.put("code", code);
					temp.put("timeDesc", Db.timeMap.get(authGroup.get(code).get("time")).get("desc"));
					data.add(temp);
				}
			}
			else if(type.equals("4"))//Access Level Setting
			{
				String groupCode = request.getParameter("groupCode");
				groupCode = (groupCode== null || groupCode.equals("null"))? "" : groupCode;
				String del = request.getParameter("del");//Deleted person
				String add = request.getParameter("add");//Saved person
				
				if( !groupCode.trim().equals("") && (!del.trim().equals("") || !add.trim().equals(""))){
					
					
					Map<String,  String> map = new HashMap<String, String>();
					String saveEmp  = Db.empAuthMap.get(groupCode)==null ? "" : Db.empAuthMap.get(groupCode);
					String[] subArr = null;
					String[] tempArr = null;
					//Delete
					if(!del.trim().equals(""))
					{
						del = del.endsWith("|") ? del.substring(0,del.length()-1) : del;
						tempArr= saveEmp.split("\\|");
						subArr = del.split("\\|");
						for(int i=0; i<subArr.length; i++)
						{
							if(Arrays.asList(tempArr).contains(subArr[i]))
							{
								saveEmp = saveEmp.replaceAll(subArr[i]+"|", "");
							}
						}
					}
					//save
					if(!add.trim().equals(""))
					{
						add = add.endsWith("|") ? add.substring(0,add.length()-1) : add;
						tempArr= saveEmp.split("\\|");
						subArr = add.split("\\|");
						for(int i=0; i<subArr.length; i++)
						{
							if(!Arrays.asList(tempArr).contains(subArr[i]))
							{
								saveEmp += subArr[i]+"|";
							}
						}
						
					}
					
					
					Db.empAuthMap.put(groupCode, saveEmp);
					
					
					
					//+++++++++++++++++++++ Synchronize Access Level To Device  ++++++++++++++++++++++++++++
					Map<String, Map<String, String>> authGroup = Db.authGroupMap;
						//present access Level group
					Map<String, String> groupInfo = authGroup.get(groupCode);
						//present access Level group's devices and parameters
					String doors = groupInfo.get("doors");//sn_doorNo1|sn_doorNo2
					String groupTime =  groupInfo.get("time");
					Map<String,String> snAndDoor = getdevSnAndDoor(doors, null);
					Set<String> snSet = snAndDoor.keySet();
					
						//1.delete all when delete ,then send all
						if(!del.trim().equals(""))
						{
							//get all devices in this access level group
							String[] delEmps = del.split("\\|");
							StringBuilder sb = new StringBuilder();
							
							//send command to remove person from access level
								for(String sn : snSet)
								{
									List<String> hasEmp = new ArrayList<String>();
									sb.append("DATA DELETE userauthorize ");
//									"DATA DELETE userauthorize Pin=1001\r\nPin=20150186\r\n";
									for(String pin : delEmps)
									{
										sb.append("Pin="+pin+"\r\n");
									}
									Cmd.addDevCmd(sn, sb.toString());
									sb.setLength(0);
									Set<String> authSet = authGroup.keySet();
									for(String authCode : authSet)
									{
										
										Map<String, String> group = authGroup.get(authCode);
										String emps = Db.empAuthMap.get(authCode);
										String door = group.get("doors");
										String timeId =  group.get("time");
										
										for(String pin : delEmps)
										{
											//DATA UPDATE userauthorize Pin=116\tAuthorizeTimezoneId=1\tAuthorizeDoorId=3\r\nPin=96\tAuthorizeTimezoneId=1\tAuthorizeDoorId=3\r\n 
											if(door!=null && door.contains(sn+"_") && emps!=null && emps.contains(pin)){
												hasEmp.add(pin);
												Map<String, String> authMap = getdevSnAndDoor(door, sn);
												if(sb.length()==0)
												{
													sb.append("DATA UPDATE userauthorize Pin="+pin+"\tAuthorizeTimezoneId="+timeId+"\tAuthorizeDoorId="+authMap.get(sn)+"\r\n");
												}
												else
												{
													sb.append("Pin="+pin+"\tAuthorizeTimezoneId="+timeId+"\tAuthorizeDoorId="+authMap.get(sn)+"\r\n");
												}
											}
										}
										if(sb.length()>0)
										{
											Cmd.addDevCmd(sn, sb.toString());
											sb.setLength(0);
										}
										
									}
									
									//delete person informations which who has no access
									for(String pin : delEmps)
									{
										if(!hasEmp.contains(pin))
										{
											if(sb.length()==0)
											{
												sb.append("DATA DELETE user Pin="+pin+"\r\n");
											}
											else
											{
												sb.append("pin="+pin+"\r\n");
											}
										}
									}
									if(sb.length()>0)
									{
										Cmd.addDevCmd(sn, sb.toString());
										sb.setLength(0);
									}
								}
						}
							
						if(!add.trim().equals(""))
						{
							//DATA UPDATE user CardNo=2\tPin=2\tPassword=281\tGroup=0\tStartTime=0\tEndTime=0\tName=郑2\tSuperAuthorize=0\tDisable=0\tCardNo=2812364\r\n
							//DATA UPDATE userauthorize Pin=116\tAuthorizeTimezoneId=1\tAuthorizeDoorId=3\r\n
							subArr = add.split("\\|");
							StringBuilder empInfo =  new StringBuilder();
							StringBuilder empAuth =  new StringBuilder();
							for(String sn : snSet)
							{
								for(int i=0; i<subArr.length; i++)
								{
									String tPin = subArr[i];
									Map<String,String> empMap = Db.empMap.get(tPin);
									if(empInfo.length()==0)
									{
										empInfo.append("DATA UPDATE user " +
												"CardNo="+empMap.get("empCard")+"\t" +
												"Pin="+empMap.get("empPin")+"\t" +
												"Password="+empMap.get("empPwd")+"\t" +
												"Group=0\t" +
												"StartTime=0\t" +
												"EndTime=0\t" +
												"Name="+empMap.get("empName")+"\t" +
												"SuperAuthorize="+empMap.get("empSuper")+"\t" +
												"Disable="+empMap.get("empDisable")+"\r\n");
									}
									else
									{
										empInfo.append("CardNo="+empMap.get("empCard")+"\t" +
												"Pin="+empMap.get("empPin")+"\t" +
												"Password="+empMap.get("empPwd")+"\t" +
												"Group=0\t" +
												"StartTime=0\t" +
												"EndTime=0\t" +
												"Name="+empMap.get("empName")+"\t" +
												"SuperAuthorize="+empMap.get("empSuper")+"\t" +
												"Disable="+empMap.get("empDisable")+"\r\n");
									}
									if(empAuth.length()==0)
									{
										empAuth.append("DATA UPDATE userauthorize Pin="+tPin+"\tAuthorizeTimezoneId="+groupTime+"\tAuthorizeDoorId="+snAndDoor.get(sn)+"\r\n");
									}
									else
									{
										empAuth.append("Pin="+tPin+"\tAuthorizeTimezoneId="+groupTime+"\tAuthorizeDoorId="+snAndDoor.get(sn)+"\r\n");
									}
								}
								if(empInfo.length()>0)
								{
									Cmd.addDevCmd(sn, empInfo.toString());
									Cmd.addDevCmd(sn, empAuth.toString());
									Cmd.addDevCmd(sn, "DATA UPDATE timezone TimezoneId="+groupTime+timezoneCmd(Db.timeMap.get(groupTime)));
									empInfo.setLength(0);
									empAuth.setLength(0);
								}
							}
						}
					
					
				}
				else
				{
					ret = "error";
				}
				
				
			}
		}catch (Exception e) {
			ret = "error";
		}finally{
			info.put("ret", ret);
			info.put("dev", dev);
			info.put("emp", emp);
			info.put("time", time);
			info.put("data", data);
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(info.toString());
			out.flush();
			out.close();
		}
	}

	private Map<String, String> getdevSnAndDoor(String doors,String sn) {
		Map<String, String> ret = new HashMap<String, String>();
		Map<String, String> temp = new HashMap<String, String>();//<'SN','1,2,4'>
		if(doors!=null && doors.length()>0)
		{
			String[] doorArr = doors.split("\\|");
			for(String info : doorArr )
			{
				if(!info.equals(""))
				{
					String[] sn_door = info.split("_");
					if(sn!=null && !sn_door[0].equals(sn))
					{
						break;
					}
					if(temp.get(sn_door[0])==null)
					{
						temp.put(sn_door[0], sn_door[1]);
					}
					else
					{
						temp.put(sn_door[0], temp.get(sn_door[0])+","+sn_door[1]);
					}
				}
			}
		}
		Set<String>set = temp.keySet();
		for(String devSn : set)
		{
			ret.put(devSn, getAuthDoorId(temp.get(devSn)));
		}
		return ret;
	}
	
	private String getAuthDoorId(String doorNos)
	{
		String[] doorNoArray = doorNos.split(",");
		int authSum = 0;
		for (String doorNo : doorNoArray)
		{
			authSum = authSum + (int) Math.pow(2, (Integer.parseInt(doorNo) - 1));
		}
		return String.valueOf(authSum);
	}
	
	private String timezoneCmd (Map<String, String> map){
		String paraString =
				"\tSunTime1="+calculateTime(map.get("sun_start_1"), map.get("sun_end_1"))+
				"\tSunTime2="+calculateTime(map.get("sun_start_2"), map.get("sun_end_2"))+
				"\tSunTime3="+calculateTime(map.get("sun_start_3"), map.get("sun_end_3"))+
				"\tMonTime1="+calculateTime(map.get("mon_start_1"), map.get("mon_end_1"))+
				"\tMonTime2="+calculateTime(map.get("mon_start_2"), map.get("mon_end_2"))+
				"\tMonTime3="+calculateTime(map.get("mon_start_3"), map.get("mon_end_3"))+
				"\tTueTime1="+calculateTime(map.get("tue_start_1"), map.get("tue_end_1"))+
				"\tTueTime2="+calculateTime(map.get("tue_start_2"), map.get("tue_end_2"))+
				"\tTueTime3="+calculateTime(map.get("tue_start_3"), map.get("tue_end_3"))+
				"\tWedTime1="+calculateTime(map.get("wed_start_1"), map.get("wed_end_1"))+
				"\tWedTime2="+calculateTime(map.get("wed_start_2"), map.get("wed_end_2"))+
				"\tWedTime3="+calculateTime(map.get("wed_start_3"), map.get("wed_end_3"))+
				"\tThuTime1="+calculateTime(map.get("thu_start_1"), map.get("thu_end_1"))+
				"\tThuTime2="+calculateTime(map.get("thu_start_2"), map.get("thu_end_2"))+
				"\tThuTime3="+calculateTime(map.get("thu_start_3"), map.get("thu_end_3"))+
				"\tFriTime1="+calculateTime(map.get("fri_start_1"), map.get("fri_end_1"))+
				"\tFriTime2="+calculateTime(map.get("fri_start_2"), map.get("fri_end_2"))+
				"\tFriTime3="+calculateTime(map.get("fri_start_3"), map.get("fri_end_3"))+
				"\tSatTime1="+calculateTime(map.get("sat_start_1"), map.get("sat_end_1"))+
				"\tSatTime2="+calculateTime(map.get("sat_start_2"), map.get("sat_end_2"))+
				"\tSatTime3="+calculateTime(map.get("sat_start_3"), map.get("sat_end_3"))+
				"\tHol1Time1="+calculateTime(map.get("hol1_start_1"), map.get("hol1_end_1"))+
				"\tHol1Time2="+calculateTime(map.get("hol1_start_2"), map.get("hol1_end_2"))+
				"\tHol1Time3="+calculateTime(map.get("hol1_start_3"), map.get("hol1_end_3"))+
				"\tHol2Time1="+calculateTime(map.get("hol2_start_1"), map.get("hol2_end_1"))+
				"\tHol2Time2="+calculateTime(map.get("hol2_start_2"), map.get("hol2_end_2"))+
				"\tHol2Time3="+calculateTime(map.get("hol2_start_3"), map.get("hol2_end_3"))+
				"\tHol3Time1="+calculateTime(map.get("hol3_start_1"), map.get("hol3_end_1"))+
				"\tHol3Time2="+calculateTime(map.get("hol3_start_2"), map.get("hol3_end_2"))+
				"\tHol3Time3="+calculateTime(map.get("hol3_start_3"), map.get("hol3_end_3"))+
				"\r\n";
		return paraString;
	}

	private int calculateTime(String start ,String end){
		int startHour = Integer.valueOf(start.split(":")[0]);
		int startMin = Integer.valueOf(start.split(":")[1]);
		int endHour = Integer.valueOf(end.split(":")[0]);
		int endMin = Integer.valueOf(end.split(":")[1]);
		int value = ((startHour*100+startMin) << 16 ) + endHour*100 + endMin ;
		return value;
	}

}
