package com.push.demo.db;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Db {
	private static int dbId=0;
	
	
/*	create ID */
	public static int getDbId(){
		return ++dbId;
	}
	
	
/*	device's whole information */
	public static Map<String,Map<String,Object>> devMap= new HashMap<String, Map<String,Object>>();
	
	
/*  whole commands that server send*/
	public static Map<Integer,String[]> cmdMap = new HashMap<Integer, String[]>();
	
	
/*	unexecuted commands */
	public static Map<String, LinkedList<String>> cmdListMap = new HashMap<String, LinkedList<String>>();
	
	
/*  event Records	 */
	public static Map<String, LinkedList<String>> rtLogMap = new HashMap<String, LinkedList<String>>();
	
	
/*	save person's basic information*/	
	public static Map<String, Map<String, String>> empMap = new HashMap<String, Map<String, String>>();
	
	
/*	save time segment information*/	
	public static Map<String, Map<String, String>> timeMap = new HashMap<String, Map<String, String>>();
	
/*	Access level*/	
	public static Map<String, String> empAuthMap = new HashMap<String, String>();  
	
	
/*	Access level group*/	
	public static Map<String, Map<String, String>> authGroupMap = new HashMap<String, Map<String, String>>();
	
	
/*	real time event list*/	
	public static List<Map<String,String>> realEventList = new LinkedList<Map<String,String>>();
	
/*	initialize time segment data*/	
	static{
		Map<String,String> time = new HashMap<String, String>();
		
		//Monday
		time.put("mon_start_1", "00:00");    time.put("mon_start_2", "00:00");   time.put("mon_start_3", "00:00");	
		time.put("mon_end_1", "23:59");      time.put("mon_end_2", "00:00");     time.put("mon_end_3", "00:00");	
		
		//Tuesday
		time.put("tue_start_1", "00:00");    time.put("tue_start_2", "00:00");   time.put("tue_start_3", "00:00");	
		time.put("tue_end_1", "23:59");      time.put("tue_end_2", "00:00");     time.put("tue_end_3", "00:00");	
		
		//Wednesday
		time.put("wed_start_1", "00:00");    time.put("wed_start_2", "00:00");   time.put("wed_start_3", "00:00");	
		time.put("wed_end_1", "23:59");      time.put("wed_end_2", "00:00");     time.put("wed_end_3", "00:00");	
		
		//Thursday
		time.put("thu_start_1", "00:00");    time.put("thu_start_2", "00:00");   time.put("thu_start_3", "00:00");	
		time.put("thu_end_1", "23:59");      time.put("thu_end_2", "00:00");     time.put("thu_end_3", "00:00");	
		
		//Friday
		time.put("fri_start_1", "00:00");    time.put("fri_start_2", "00:00");   time.put("fri_start_3", "00:00");	
		time.put("fri_end_1", "23:59");      time.put("fri_end_2", "00:00");     time.put("fri_end_3", "00:00");	
		
		//Saturday
		time.put("sat_start_1", "00:00");    time.put("sat_start_2", "00:00");   time.put("sat_start_3", "00:00");	
		time.put("sat_end_1", "23:59");      time.put("sat_end_2", "00:00");     time.put("sat_end_3", "00:00");	
		
		//Sunday
		time.put("sun_start_1", "00:00");    time.put("sun_start_2", "00:00");   time.put("sun_start_3", "00:00");	
		time.put("sun_end_1", "23:59");      time.put("sun_end_2", "00:00");     time.put("sun_end_3", "00:00");	
		
		//Holiday 1  
		time.put("hol1_start_1", "00:00");    time.put("hol1_start_2", "00:00");   time.put("hol1_start_3", "00:00");	
		time.put("hol1_end_1", "00:00");      time.put("hol1_end_2", "00:00");     time.put("hol1_end_3", "00:00");	
		
		//Holiday 2
		time.put("hol2_start_1", "00:00");    time.put("hol2_start_2", "00:00");   time.put("hol2_start_3", "00:00");	
		time.put("hol2_end_1", "00:00");      time.put("hol2_end_2", "00:00");     time.put("hol2_end_3", "00:00");	
		
		//Holiday 3
		time.put("hol3_start_1", "00:00");    time.put("hol3_start_2", "00:00");   time.put("hol3_start_3", "00:00");	
		time.put("hol3_end_1", "00:00");      time.put("hol3_end_2", "00:00");     time.put("hol3_end_3", "00:00");	
		
		time.put("desc", "24-Hour Accessible ");
		timeMap.put("1", time);
	}
	
	public static final Map<Integer, String> VERIFY_MODE = new HashMap<Integer, String>();
	
	public static final int VERIFY_MODE_CARDORFPORPWD = 0;// card , fingerprint or password
	public static final int VERIFY_MODE_ONLYFP = 1;// fingerprint only
	public static final int VERIFY_MODE_ONLYPIN = 2;// pin only
	public static final int VERIFY_MODE_ONLYPWD = 3;//  password only
	public static final int VERIFY_MODE_ONLYCARD = 4;//  card only
	public static final int VERIFY_MODE_FPORPWD = 5;// fingerprint or password
	public static final int VERIFY_MODE_CARDORFP = 6;// card or fingerprint
	public static final int VERIFY_MODE_CARDORPWD = 7;// card or password
	public static final int VERIFY_MODE_PINANDFP = 8;// pin and fingerprint
	public static final int VERIFY_MODE_FPANDPWD = 9;// fingerprint and password
	public static final int VERIFY_MODE_CARDANDFP = 10;// card and fingerprint
	public static final int VERIFY_MODE_CARDANDPWD = 11;//card and password
	public static final int VERIFY_MODE_FPANDCARDANDPWD = 12;// fingerprint and password and card
	public static final int VERIFY_MODE_PINANDFPANDPWD = 13;// fingerprint and password and pin
	public static final int VERIFY_MODE_PINANDFPORCARDANDFP = 14;// pin+fingerprint or card+fingerpring
	public static final int VERIFY_MODE_PINORCARDANDPWD = 15; // pin or card+password
	public static final int VERIFY_MODE_OTHER = 200;//  others
	static
	{

		
		
		VERIFY_MODE.put(Db.VERIFY_MODE_CARDORFPORPWD, "card or fingerprint or password");// card or fingerprint or password
		VERIFY_MODE.put(Db.VERIFY_MODE_ONLYFP, "fingerprint only");//  fingerprint only
		VERIFY_MODE.put(Db.VERIFY_MODE_ONLYPIN, "pin only");//  pin only
		VERIFY_MODE.put(Db.VERIFY_MODE_ONLYPWD, " password only");//  password only
		VERIFY_MODE.put(Db.VERIFY_MODE_ONLYCARD, "card only");//  card only
		VERIFY_MODE.put(Db.VERIFY_MODE_FPORPWD, "fingerprint or password");// fingerprint or password
		VERIFY_MODE.put(Db.VERIFY_MODE_CARDORFP, "card or fingerprint");//  card or fingerprint
		VERIFY_MODE.put(Db.VERIFY_MODE_CARDORPWD, "card or password");//  card or password
		VERIFY_MODE.put(Db.VERIFY_MODE_PINANDFP, "pin and fingerprint");//  pin and fingerprint
		VERIFY_MODE.put(Db.VERIFY_MODE_FPANDPWD, "fingerprint and password");//  fingerprint and password
		VERIFY_MODE.put(Db.VERIFY_MODE_CARDANDFP, "card and fingerprint");//  card and fingerprint
		VERIFY_MODE.put(Db.VERIFY_MODE_CARDANDPWD, "card and password");// card and password
		VERIFY_MODE.put(Db.VERIFY_MODE_FPANDCARDANDPWD, "fingerprint and password and card");//  fingerprint and password and card
		VERIFY_MODE.put(Db.VERIFY_MODE_PINANDFPANDPWD, "pin and fingerprint and password");//  pin and fingerprint and password
		VERIFY_MODE.put(Db.VERIFY_MODE_PINANDFPORCARDANDFP, "pin+fingerprint or card+fingerprint");// pin+fingerprint or card+fingerprint
		VERIFY_MODE.put(Db.VERIFY_MODE_PINORCARDANDPWD, "pin or card+password"); //pin or card+password
		VERIFY_MODE.put(Db.VERIFY_MODE_OTHER, " others");// others
	}
	
	
}
