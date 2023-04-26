package com.push.demo.sevlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.push.demo.db.Db;
import com.push.demo.util.Cmd;
import com.push.demo.util.Constants;
/**
 * Recevie all of the requestes from Devices
 * @author wkque & Keith
 * @email keith.yang@zkteco.com
 */
public class DeviceVisitServerSevlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.addHeader("Content-Type", "text/plain");
		String retValue = "OK";
		String pathInfo = request.getPathInfo();
		try{
			String sn  = request.getParameter("SN");
			if(pathInfo.equals("/cdata"))
			{
				String type = request.getParameter("table");
				if(type == null && request.getParameter("AuthType") != null){
					type = "BGV";// background verification
				}
				type = type == null ? "isConnect" : type;
				if(type.equals("isConnect"))
				{
					System.out.println("***************/cdata type=isconnect  || first step: set up connections between device and server***************");
				}
				else if(type.equals("rtstate"))
				{
					String datas = getStreamData(request);
					System.out.println("***************/cdata type=rtstate  || post device's state to server***************");
					parseDevState(datas, sn);
				}
				else if(type.equals("rtlog"))
				{
					System.out.println("***************/cdata type=rtlog  || post device's event to server***************");
					String datas = getStreamData(request);
					String eventName = "";
					if(datas!=null && datas.length()>0){
						Map<String, String> eventMap = parseStringToMap(datas, "\t");
						eventMap.put("sn", sn);
						synchronized (DeviceVisitServerSevlet.class) {
							if(Db.realEventList.size()>=5)
							{
								Db.realEventList.remove(0);
							}
							Db.realEventList.add(eventMap);
						}
						String eventNo = eventMap.get("event");
						if(eventNo !=null && eventNo.length()>0){
							eventName = Constants.allEventMap.get(eventNo);
						}
					}
					System.out.println("\t(" + eventName + ")  " + datas);
				}
				else if(type.equals("BGV"))
				{
					System.out.println("***************/cdata type=BGV  || background verification***************");
					System.out.println("◎background verification:"+ request.getParameter("AuthType"));
					String datas = getStreamData(request);
					System.out.println("\t device event data：" + datas); 
					String ret = Cmd.retrunBGVerifyData(datas);
					System.out.println("\t verify result：" + ret); 
					retValue = ret;		
				}
				else
				{
					System.out.println("***************/cdata type=unknown  || request:"+type+"***************");
				}
			}
			else if(pathInfo.equals("/registry"))
			{
				System.out.println("***************/registry  || Start to regist***************");
				retValue = registryDevice(request);
			}
			else if(pathInfo.equals("/push"))
			{
				System.out.println("***************/push  || device get parameters from server***************");
				retValue = getServiceParamete(sn);
			}
			else if(pathInfo.equals("/getrequest"))
			{
				System.out.println("device say : give me instructions");
				int size =0;
				if(Db.cmdListMap.get(sn) != null)
				{
					size = Db.cmdListMap.get(sn).size();
					if(size > 0)
					{
						System.out.println("◎get commands from server and execute");
						String cmd = Db.cmdListMap.get(sn).remove(0);
						System.out.println("\t" + cmd);
						retValue = cmd;
					}
				}
			}
			else if(pathInfo.equals("/devicecmd"))
			{
				System.out.println("***************/devicecmd  || return the result of executed command to server***************");
				String datas = getStreamData(request);
				System.out.println("◎result of executed command：" + datas);
				//save result to map
				Integer cmdId = Integer.parseInt(datas.split("&")[0].split("=")[1]);
				if(Db.cmdMap.get(cmdId)!=null)
				{
					String[] cmdArr = Db.cmdMap.get(cmdId);
					cmdArr[1] = datas;
					Db.cmdMap.put(cmdId, cmdArr);
				}
			}
			else if(pathInfo.equals("/querydata"))
			{
				System.out.println("***************/querydata  ||response the server with person data that server asked***************");
				String datas = getStreamData(request);
				String type = request.getParameter("type");
				if("tabledata".equals(type))//returned value   /iclock/querydata?SN=0566141900195&type=tabledata&type=tabledata&cmdid=1&tablename=user&count=1&packcnt=1&packidx=1
				{
					String table = request.getParameter("tablename");
					String count = request.getParameter("count");
					int actualCount = "".equals(datas) ? 0 : datas.split("\r\n").length;
					
					if(count.equals(actualCount+"") )//data without loss
					{
						retValue = table + "=" + actualCount ;  
					}
					else
					{
						// return -731
					}
				}
				else{
//					SN=0566141900195&type=count&cmdid=1&tablename=user&count=1&packcnt=1&packidx=1
//					SN=0566141900195&type=options&cmdid=1&tablename=options&count=1&packcnt=1&packidx=1
//					****above 2 request,return "ok" to device,
				}
				
				
				
				System.out.println("◎returned value：" + datas);
				//save the result
				Integer cmdId = Integer.parseInt( request.getParameter("cmdid"));
				if(Db.cmdMap.get(cmdId)!=null)
				{
					String[] cmdArr = Db.cmdMap.get(cmdId);
					cmdArr[3] = datas.replaceAll("\\t", " ");
					Db.cmdMap.put(cmdId, cmdArr);
				}
			}
			else
			{
				System.out.println("***************unknown request:"+pathInfo+"***************");
				retValue = "404";
			}
		}
		catch (Exception e) 
		{
			retValue = "404";
		}
		finally
		{
			PrintWriter out = response.getWriter();
			out.write(retValue);
			out.flush();
			out.close();
		}
	}

	@SuppressWarnings("unchecked")
	private void parseDevState(String datas, String sn) {
		Map<String, String> stateMap = parseStringToMap(datas,"\t");
		Map<String, String>  optionsMap = (Map<String, String>)Db.devMap.get(sn).get("options");
		int lockCount = Integer.parseInt(optionsMap.get("LockCount"));
		String sensor = stateMap.get("sensor");
		sensor = getBinary(sensor, lockCount, 2, false);
		String relay = stateMap.get("relay");
		relay = getBinary(relay, lockCount, 1, false);
		String alarm = stateMap.get("alarm");
		alarm = getBinary(alarm, lockCount, 8, true);
		String door =stateMap.get("door");
		door=getBinary(door, lockCount, 8, false);
		System.out.println("\tsensor:" + sensor + "  relay:" + relay + " alarm:" + alarm);
		System.out.println("\t" +
				" Door Sensor:(" +  parseDoorStateDes(sensor, Constants.sensorDesMap) + ")" +
				" Door Lock:(" +  parseDoorStateDes(relay, Constants.relayDesMap) +	")" +
				" Alarm:(" +  parseDoorStateDes(alarm, Constants.alarmDesMap) + ")"+
				" Door:(" +  parseDoorStateDes(door, Constants.alarmDesMap) + ")");
	}

	private String parseDoorStateDes(String state,	Map<String, String> stateDesMap) {
		String stateDesc = "";
		String[] stateArr = state.split("\\,");
		for(String key : stateArr){
			if(stateDesc.length()==0)
			{
				stateDesc = stateDesMap.get(key);
			}
			else
			{
				stateDesc += "," + stateDesMap.get(key);
			}
		}
		return stateDesc;
	}

	private String registryDevice(HttpServletRequest request)throws IOException {
		String retValue = "404";
		String sn = request.getParameter("SN");
		if(Db.devMap.get(sn) != null)//has registered
		{
			String registrycode = (String)Db.devMap.get(sn).get("registrycode");
			retValue = "RegistryCode=" + registrycode;
			System.out.println("\t has been registed，register code：" + registrycode);
		}
		else//not registered
		{
			//save device's informaation and part of server's information when registering
				//device information
			String randomString = getRandomString(10);
			String datas = getStreamData(request);
			Map<String,String> dataMap = parseStringToMap(datas);
				//server information
			dataMap.put("ServerVersion", "10.2");
			dataMap.put("ServerName", "myServerName");
			dataMap.put("PushVersion", "5.6");
			dataMap.put("ErrorDelay", "30");
			dataMap.put("RequestDelay", "3");
			dataMap.put("TransTimes", "00:30\t13:00");
			dataMap.put("TransInterval", "1");
			dataMap.put("TransTables", "User\tTransaction");
			dataMap.put("Realtime", "1");
			String sessionId = request.getSession().getId();
			dataMap.put("SessionID", sessionId);
			
			Map<String,Object> optionsMap = new HashMap<String, Object>();
			optionsMap.put("options", dataMap);
			optionsMap.put("registrycode", randomString);
			//save device information
			Db.devMap.put(sn, optionsMap);
			//return the register code
			retValue = "RegistryCode=" + randomString;
			System.out.println("\t not registered,go to regist，return register code：" + randomString);
		}
		return retValue;
	}
	
	/**
	 * print the request content or the parameters
	 * @param request
	 */
	public void getRequestParameters(HttpServletRequest request){
		Enumeration<String> parameters = request.getParameterNames();
		while(parameters.hasMoreElements())
		{
			String paramete = parameters.nextElement();
			System.out.println(paramete + " ::: " + request.getParameter(paramete));
		}
	}
	
	/**
	 * Create a 10 bytes of random Srting which made of mixed numbers and letters
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) { 
	    StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"); 
	    StringBuffer sb = new StringBuffer(); 
	    Random r = new Random(); 
	    int range = buffer.length(); 
	    for (int i = 0; i < length; i ++) { 
	        sb.append(buffer.charAt(r.nextInt(range))); 
	    } 
	    return sb.toString(); 
	}
	
	/**
	 * Transfer the requested stream data to String
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public String getStreamData(HttpServletRequest request) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try
		{
			request.setCharacterEncoding("utf-8");
			br = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
			String inline = "";
			while ((inline = br.readLine()) != null)
			{
				sb.append(inline + "\r\n");
			}
		}
		finally
		{
			if(br != null)
			{
				br.close();
			}
		}
		return sb.toString().trim();
	}
	
	public Map<String,String> parseStringToMap(String data)
	{
//		 see detail,rolling to the bottom of this class
		Map<String,String> ret = new HashMap<String, String>();
		String options[] = data.split("\\,");
		for(String option : options)
		{
			String opArr[] = option.split("=");
			if(opArr.length == 2)
			{
				ret.put(opArr[0], opArr[1]);
			}
		}
		return ret;
	}
	public Map<String,String> parseStringToMap(String data, String type)
	{
//		see detail,rolling to the bottom of this class
		Map<String,String> ret = new HashMap<String, String>();
		String options[] = data.split(type);
		for(String option : options)
		{
			String opArr[] = option.split("=");
			if(opArr.length == 2)
			{
				ret.put(opArr[0], opArr[1]);
			}
		}
		return ret;
	}
	
	public String getServiceParamete(String sn)
	{
//		see detail,rolling to the bottom of this class
		String[] serverParamete = new String[]{"ServerVersion","ServerName","PushVersion","ErrorDelay","RequestDelay",
				"TransTimes","TransInterval","TransTables","Realtime","SessionID"
				};
		Map<String,String> options = (Map<String,String>)Db.devMap.get(sn).get("options");
		StringBuilder sb = new StringBuilder();
		for(String param : serverParamete)
		{
			sb.append(param + "=" + options.get(param) + "\n");
		}
		
		return sb.toString();
	}
	
	
	/**
	 * 
	 * @param hexStrValue  十六进制的值
	 * @param lockCount  Door count of the access controller
	 * @param bitConvert 值占用几位二进制  how many bits that binary value needs
	 * @param reverse
	 * @return
	 */
	public String getBinary(String hexStrValue, int lockCount, int bitConvert, boolean reverse)
	{
		String setValue = "";
		long intValue = 0L;
		if (reverse)
		{
			int validLetterLen = bitConvert * lockCount / 4;
			for (int i = 0; i < validLetterLen / 2; i++)
			{
				setValue += hexStrValue.substring(validLetterLen - (i + 1) * 2, validLetterLen - i * 2);
			}
			intValue = Long.parseLong(setValue, 16);
		}
		else
		{
			intValue = Long.parseLong(hexStrValue, 16);
		}
		String ret = "";// "1,2,3,4"
		long sum = 0; // 8 doors need 8 bytes,type "int" is not enough
		for (int i = 0; i < bitConvert; i++)
		{
			sum += Math.pow(2, i); // 2^(i-1) 该和需要与状态值做位运算//Bitwise operate
		}
		for (int i = 0; i < lockCount; i++)
		{
			if ("".equals(ret))
			{
				ret += (intValue & sum);// >> 0
			}
			else
			{
				ret += "," + ((intValue >> bitConvert * i) & sum);// 先移位(右移到最低位)后与运算.移位时只需要判断多个位的最低位.
			}
		}
		return ret;
	}

	/**
	 * 	Attachment 1:	
		    DisableUserFunOn   1
			SimpleEventType   1
			LogIDFunOn   1
			~RelayStateFunOn   1
			LockCount   4
			MThreshold   55
			~Ext485ReaderFunOn   1
			~ZKFPVersion   10
			~MaxUserCount   90
			~SupAuthrizeFunOn   1
			DeviceType   acc
			~DeviceName   inBIO460
			~LossCardFunOn   1
			VerifyStyles   DB0C
			AuxInCount   4
			~ReaderCFGFunOn   1
			~CtlAllRelayFunOn   1
			~IsOnlyRFMachine   0
			MachineType   10
			~CardFormatFunOn   1
			~MaxAttLogCount   10
			~TimeAPBFunOn   1
			PushVersion   3.2.1
			CommType   ethernet
			AuxOutCount   4
			DeleteAndFunOn   1
			NetMask   255.255.255.0
			FirmVer   AC Ver 5.7.6.2020 Jul 10 2014
			~REXInputFunOn   1
			MaxPackageSize   2048000
			IPAddress   192.168.0.114
			ReaderCount   4
			DateFmtFunOn   1
			~MaxUserFingerCount   10
			GATEIPAddress   192.168.0.1
			EventTypes   FFFFFF3FF0B601000000000070030000000000000000000000F74FF006000000
			~ReaderLinkageFunOn   1
			
		Attachment 2:
			ServerVersion=3.1.1 	//服务器版本
			ServerName=ADMS 	//服务器名称
			PushVersion=3.2.0 	//push版本
			ErrorDelay=60 	 //为联网失败后重新联接服务器的间隔时间（秒）
			RequestDelay=5 	 //设备请求服务器命令的间隔时间
			TransTimes=00:00  14:00  	//为定时检查并传送新数据时间（时:分，24小时格式）
			TransInterval=1	 //为检查并传送新数据间隔时间（分钟）
			TransTables=User  Transaction 	 //需要检查新数据并上传的数据表  用\t隔开
			Realtime=1 	//是否实时传送新记录
			SessionID=d8y8o75hgn
	 */
}

