package com.push.demo.util;

import java.util.HashMap;
import java.util.Map;

public class Constants {
//	Door sensor state description
	public static Map<String,String> sensorDesMap = new HashMap<String, String>();
	static{
		sensorDesMap.put("0", "no sensor");
		sensorDesMap.put("1", "closed");
		sensorDesMap.put("2", "open");
	}
	
//	Door lock state description
	public static Map<String,String> relayDesMap = new HashMap<String, String>();
	static{
		relayDesMap.put("0", "closed");
		relayDesMap.put("1", "open");
	}
	
//	Alarm state description
	public static Map<String,String> alarmDesMap = new HashMap<String, String>();
	static{
		alarmDesMap.put("0", "no");
		alarmDesMap.put("1", "Opened Forcefully");
		alarmDesMap.put("2", "Tamper alarm");
		alarmDesMap.put("4", "Duress password Alarm");
		alarmDesMap.put("8", "Duress fingerprint Alarm");
		alarmDesMap.put("16", "Door sensor alarm");
		alarmDesMap.put("32", "reserved 32");
		alarmDesMap.put("64", "reserved 64");
		alarmDesMap.put("128", "reserved 128");
	}
	
//	事件描述
	public static Map<String,String>  allEventMap = new HashMap<String, String>();
	public static Map<String,String>  baseEventMap = new HashMap<String, String>();
	static{
		baseEventMap.put("0","Normal Verify Open");
		baseEventMap.put("1","Verify During Passage Mode Time Zone");
		baseEventMap.put("2","First-Personnel Open");
		baseEventMap.put("3","Multi-Personnel Open");
		baseEventMap.put("4","Emergency Password Open");
		baseEventMap.put("5","Open during Passage Mode Time Zone");
		baseEventMap.put("6","Linkage Event Triggered");
		baseEventMap.put("7","Cancel Alarm");
		baseEventMap.put("8","Remote Opening");
		baseEventMap.put("9","Remote Closing");
		baseEventMap.put("10","Disable Normal Open");
		baseEventMap.put("11","Enable Normal Open");
		baseEventMap.put("12","Auxiliary Output Remotely Open");
		baseEventMap.put("13","Auxiliary Output Remotely Close");
		baseEventMap.put("20","Operate Interval too Short");
		baseEventMap.put("21","Door Inactive Time Zone Verify Open");
		baseEventMap.put("22","Illegal Time Zone");
		baseEventMap.put("23","Access Denied");
		baseEventMap.put("24","Anti-Passback");
		baseEventMap.put("25","Interlock");
		baseEventMap.put("26","Multi-Personnel Authentication Wait");
		baseEventMap.put("27","Unregistered Personnel");
		baseEventMap.put("28","Open Door Timeout");
		baseEventMap.put("29","Personnel Expired");
		baseEventMap.put("36","Door Inactive Time Zone(Press Exit Button)");
		baseEventMap.put("37","Failed to Close during Passage Mode Time Zone");
		baseEventMap.put("38","Card Reported Lost");
		baseEventMap.put("39","Blacklisted");
		baseEventMap.put("41","Verify Mode Error");
		baseEventMap.put("42","Wiegand Format Error");
		baseEventMap.put("44","Background Verify Failed");
		baseEventMap.put("45","Background Verify Timed Out");
		baseEventMap.put("48","Multi-Personnel Verify Failed");
		baseEventMap.put("100","Tamper alarm");
		baseEventMap.put("101","Duress Open Alarm");
		baseEventMap.put("102","Opened Forcefully");
		baseEventMap.put("105","Can not connect to server");
		baseEventMap.put("200","Door Opened Correctly");
		baseEventMap.put("201","Door Closed Correctly");
		baseEventMap.put("202","Exit Button Open");
		baseEventMap.put("204","Passage Mode Time Zone Over");
		baseEventMap.put("205","Remote Normal Opening");
		baseEventMap.put("206","Device Started");
		baseEventMap.put("208","Superuser Open Doors");
		baseEventMap.put("209","Exit Button triggered(Without Unlock)");
		baseEventMap.put("214","Connected to the server");
		baseEventMap.put("220","Auxiliary Input Disconnected");
		baseEventMap.put("221","Auxiliary Input Shorted");
		baseEventMap.put("222","Background Verification Success");
		baseEventMap.put("223","Background Verification");
		baseEventMap.put("700","Lost Connect");
	}
	static{
		allEventMap.putAll(baseEventMap);
	}
	
	
	//-731 get wrong returned value from server

}
