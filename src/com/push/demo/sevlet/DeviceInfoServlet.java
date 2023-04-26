package com.push.demo.sevlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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

public class DeviceInfoServlet extends HttpServlet {

	/**
	 * query information about devices
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONArray data = new JSONArray();
		JSONObject ret = new JSONObject();
		String desc = "ok";
		String type = request.getParameter("type");
		try{
			if(type != null && !type.trim().equals(""))
			{
				if(type.equals("1"))//query device
				{
					Map<String,Map<String,Object>> devMap = Db.devMap;
					Set<String> set = devMap.keySet();
					for(String sn : set)
					{
						Map<String,Object> dev = devMap.get(sn);
						Map<String,Object> options = (Map<String,Object>)dev.get("options");
						JSONObject obj = new JSONObject();
						obj.put("sn", sn);
						obj.put("LockCount", options.get("LockCount"));//lock count
						obj.put("FirmVer", options.get("FirmVer"));//firmware version
						obj.put("registrycode", dev.get("registrycode"));//register code
						obj.put("DeviceName", options.get("~DeviceName"));//device name
						data.add(obj);
					}
				}
				else if(type.equals("2"))//synchronize device's data
				{
					String sn = request.getParameter("sn");
					StringBuilder sb  = new StringBuilder();
					//delete access
						Cmd.addDevCmd(sn, "DATA DELETE userauthorize *");
					//delete person
						Cmd.addDevCmd(sn, "DATA DELETE user *");
					
					//delete time
						Cmd.addDevCmd(sn, "DATA DELETE timezone *");
						
					//query time door and person about this device
						
						List<Map<String, String>> list =  new ArrayList<Map<String, String>>();
						Map<String, String> info;
						
						Map<String, Map<String, String>> groups = Db.authGroupMap;
						Set<String> groupCodeSet = groups.keySet();
						for(String code : groupCodeSet)
						{
							Map<String, String> group = groups.get(code);
							String doors = group.get("doors");//sn_doorNo|sn_doorNo
							Map<String,String> snDoorMap = getdevSnAndDoor(doors,sn);//<sn,1><sn,3>
							if(snDoorMap!=null && snDoorMap.get(sn)!=null && Db.empAuthMap.get(code)!=null)//门不空，且人不空时
							{
								info = new HashMap<String, String>();
								info.put("doors", snDoorMap.get(sn));
								info.put("emps", Db.empAuthMap.get(code));
								info.put("time", group.get("time"));
								list.add(info);
							}
						}
					// send time command to device
						for(int i = 0; i < list.size(); i++)
						{
							info = list.get(i);
							String timeId = info.get("time");
							String doors = info.get("doors");
							String emps = info.get("emps");
							
							String cmd = getTimeCmd(timeId);
							
							Cmd.addDevCmd(sn, cmd);
							
							
					// send person and access level command to device
							emps = emps.substring(0, emps.length()-1);
							String[] subArr = emps.split("\\|");
							StringBuilder empInfo =  new StringBuilder();
							StringBuilder empAuth =  new StringBuilder();
							for(int j=0; j<subArr.length; j++)
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
									empAuth.append("DATA UPDATE userauthorize Pin="+tPin+"\tAuthorizeTimezoneId="+timeId+"\tAuthorizeDoorId="+doors+"\r\n");
								}
								else
								{
									empAuth.append("Pin="+tPin+"\tAuthorizeTimezoneId="+timeId+"\tAuthorizeDoorId="+doors+"\r\n");
								}
							}
							if(empInfo.length()>0)
							{
								Cmd.addDevCmd(sn, empInfo.toString());
								Cmd.addDevCmd(sn, empAuth.toString());
								
								empInfo.setLength(0);
								empAuth.setLength(0);
							}
							
						}
				}
			}
		}catch (Exception e) {
			desc = "error";
			e.printStackTrace();
		}finally{
			ret.put("desc", desc);
			ret.put("data", data);
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.write(ret.toString());
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
	
	/**
	 * Create time segment command
	 * @param timeSeg
	 * @return
	 * @throws Exception
	 */
	public String getTimeCmd(String timeId) throws Exception
	{
		Set<Map<String, Object>> timeSegSet = new HashSet<Map<String,Object>>();
		Map<String, Object> timeSegMap =  new HashMap<String, Object>();
		
		
		Map<String, String> timeMap = Db.timeMap.get(timeId);
		
		
		
		timeSegMap.put("timeSegId", timeId);

		timeSegMap.put("MonTime1", formatTime(timeMap.get("mon_start_1"), timeMap.get("mon_end_1")));
		timeSegMap.put("MonTime2", formatTime(timeMap.get("mon_start_2"), timeMap.get("mon_end_2")));
		timeSegMap.put("MonTime3", formatTime(timeMap.get("mon_start_3"), timeMap.get("mon_end_3")));
		
		timeSegMap.put("TueTime1", formatTime(timeMap.get("tue_start_1"), timeMap.get("tue_end_1")));
		timeSegMap.put("TueTime2", formatTime(timeMap.get("tue_start_1"), timeMap.get("tue_end_1")));
		timeSegMap.put("TueTime3", formatTime(timeMap.get("tue_start_1"), timeMap.get("tue_end_1")));
		
		timeSegMap.put("WedTime1", formatTime(timeMap.get("wed_start_1"), timeMap.get("wed_end_1")));
		timeSegMap.put("WedTime2", formatTime(timeMap.get("wed_start_1"), timeMap.get("wed_end_1")));
		timeSegMap.put("WedTime3", formatTime(timeMap.get("wed_start_1"), timeMap.get("wed_end_1")));
		
		timeSegMap.put("ThuTime1", formatTime(timeMap.get("thu_start_1"), timeMap.get("thu_end_1")));
		timeSegMap.put("ThuTime2", formatTime(timeMap.get("thu_start_1"), timeMap.get("thu_end_1")));
		timeSegMap.put("ThuTime3", formatTime(timeMap.get("thu_start_1"), timeMap.get("thu_end_1")));
		
		timeSegMap.put("FriTime1", formatTime(timeMap.get("fri_start_1"), timeMap.get("fri_end_1")));
		timeSegMap.put("FriTime2", formatTime(timeMap.get("fri_start_1"), timeMap.get("fri_end_1")));
		timeSegMap.put("FriTime3", formatTime(timeMap.get("fri_start_1"), timeMap.get("fri_end_1")));
		
		timeSegMap.put("SunTime1", formatTime(timeMap.get("sun_start_1"), timeMap.get("sun_end_1")));
		timeSegMap.put("SunTime2", formatTime(timeMap.get("sun_start_1"), timeMap.get("sun_end_1")));
		timeSegMap.put("SunTime3", formatTime(timeMap.get("sun_start_1"), timeMap.get("sun_end_1")));
		
		timeSegMap.put("SatTime1", formatTime(timeMap.get("sat_start_1"), timeMap.get("sat_end_1")));
		timeSegMap.put("SatTime2", formatTime(timeMap.get("sat_start_1"), timeMap.get("sat_end_1")));
		timeSegMap.put("SatTime3",	formatTime(timeMap.get("sat_start_1"), timeMap.get("sat_end_1")));
		
		timeSegMap.put("Hol1Time1", formatTime(timeMap.get("hol1_start_1"), timeMap.get("hol1_end_1")));
		timeSegMap.put("Hol1Time2", formatTime(timeMap.get("hol1_start_1"), timeMap.get("hol1_end_1")));
		timeSegMap.put("Hol1Time3", formatTime(timeMap.get("hol1_start_1"), timeMap.get("hol1_end_1")));
		
		timeSegMap.put("Hol2Time1", formatTime(timeMap.get("hol2_start_1"), timeMap.get("hol2_end_1")));
		timeSegMap.put("Hol2Time2", formatTime(timeMap.get("hol2_start_1"), timeMap.get("hol2_end_1")));
		timeSegMap.put("Hol2Time3", formatTime(timeMap.get("hol2_start_1"), timeMap.get("hol2_end_1")));
		
		timeSegMap.put("Hol3Time1", formatTime(timeMap.get("hol3_start_1"), timeMap.get("hol3_end_1")));
		timeSegMap.put("Hol3Time2", formatTime(timeMap.get("hol3_start_1"), timeMap.get("hol3_end_1")));
		timeSegMap.put("Hol3Time3", formatTime(timeMap.get("hol3_start_1"), timeMap.get("hol3_end_1")));
		timeSegSet.add(timeSegMap);
		return decodeTimeSeg(timeSegSet);
	}
	/**
	 * Create time segment command
	 * @param timeSegOptColl
	 * @return
	 * @throws Exception
	 */
	public String decodeTimeSeg(Collection<Map<String, Object>> timeSegOptColl)
	{
		StringBuffer cmdStrBuf = new StringBuffer();
		StringBuffer tempBuffer = new StringBuffer();
		for(Map<String, Object> opt : timeSegOptColl)
		{
			tempBuffer = new StringBuffer();
			tempBuffer.append(String.format("TimezoneId=%d\t", Integer.parseInt(opt.get("timeSegId")+"")));
			tempBuffer.append(String.format("SunTime1=%d\t", Integer.parseInt(opt.get("SunTime1").toString())));
			tempBuffer.append(String.format("SunTime2=%d\t", Integer.parseInt(opt.get("SunTime2").toString())));
			tempBuffer.append(String.format("SunTime3=%d\t", Integer.parseInt(opt.get("SunTime3").toString())));
			tempBuffer.append(String.format("MonTime1=%d\t", Integer.parseInt(opt.get("MonTime1").toString())));
			tempBuffer.append(String.format("MonTime2=%d\t", Integer.parseInt(opt.get("MonTime2").toString())));
			tempBuffer.append(String.format("MonTime3=%d\t", Integer.parseInt(opt.get("MonTime3").toString())));
			tempBuffer.append(String.format("TueTime1=%d\t", Integer.parseInt(opt.get("TueTime1").toString())));
			tempBuffer.append(String.format("TueTime2=%d\t", Integer.parseInt(opt.get("TueTime2").toString())));
			tempBuffer.append(String.format("TueTime3=%d\t", Integer.parseInt(opt.get("TueTime3").toString())));
			tempBuffer.append(String.format("WedTime1=%d\t", Integer.parseInt(opt.get("WedTime1").toString())));
			tempBuffer.append(String.format("WedTime2=%d\t", Integer.parseInt(opt.get("WedTime2").toString())));
			tempBuffer.append(String.format("WedTime3=%d\t", Integer.parseInt(opt.get("WedTime3").toString())));
			tempBuffer.append(String.format("ThuTime1=%d\t", Integer.parseInt(opt.get("ThuTime1").toString())));
			tempBuffer.append(String.format("ThuTime2=%d\t", Integer.parseInt(opt.get("ThuTime2").toString())));
			tempBuffer.append(String.format("ThuTime3=%d\t", Integer.parseInt(opt.get("ThuTime3").toString())));
			tempBuffer.append(String.format("FriTime1=%d\t", Integer.parseInt(opt.get("FriTime1").toString())));
			tempBuffer.append(String.format("FriTime2=%d\t", Integer.parseInt(opt.get("FriTime2").toString())));
			tempBuffer.append(String.format("FriTime3=%d\t", Integer.parseInt(opt.get("FriTime3").toString())));
			tempBuffer.append(String.format("SatTime1=%d\t", Integer.parseInt(opt.get("SatTime1").toString())));
			tempBuffer.append(String.format("SatTime2=%d\t", Integer.parseInt(opt.get("SatTime2").toString())));
			tempBuffer.append(String.format("SatTime3=%d\t", Integer.parseInt(opt.get("SatTime3").toString())));
			tempBuffer.append(String.format("Hol1Time1=%d\t", Integer.parseInt(opt.get("Hol1Time1").toString())));
			tempBuffer.append(String.format("Hol1Time2=%d\t", Integer.parseInt(opt.get("Hol1Time2").toString())));
			tempBuffer.append(String.format("Hol1Time3=%d\t", Integer.parseInt(opt.get("Hol1Time3").toString())));
			tempBuffer.append(String.format("Hol2Time1=%d\t", Integer.parseInt(opt.get("Hol2Time1").toString())));
			tempBuffer.append(String.format("Hol2Time2=%d\t", Integer.parseInt(opt.get("Hol2Time2").toString())));
			tempBuffer.append(String.format("Hol2Time3=%d\t", Integer.parseInt(opt.get("Hol2Time3").toString())));
			tempBuffer.append(String.format("Hol3Time1=%d\t", Integer.parseInt(opt.get("Hol3Time1").toString())));
			tempBuffer.append(String.format("Hol3Time2=%d\t", Integer.parseInt(opt.get("Hol3Time2").toString())));
			tempBuffer.append(String.format("Hol3Time3=%d", Integer.parseInt(opt.get("Hol3Time3").toString())));
			cmdStrBuf.append(tempBuffer.toString() +"\r\n");
		}
		String cmd = String.format("DATA UPDATE timezone %s", cmdStrBuf.toString());
		return cmd;
	}
	
	/**
	 * Time format switch
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public int formatTime(String startTime, String endTime)
	{

		int start = Integer.parseInt(startTime.split(":")[0]) * 100 + Integer.parseInt(startTime.split(":")[1]);
		int end = Integer.parseInt(endTime.split(":")[0]) * 100 + Integer.parseInt(endTime.split(":")[1]);
		return (start << 16) + (end & (0xFFFF));
		
	}

}
