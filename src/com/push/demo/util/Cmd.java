package com.push.demo.util;

import java.util.LinkedList;

import com.push.demo.db.Db;

/**
 * Executed commands
 *
 */
public class Cmd {

	/**
	 * Remote open door
	 * @return
	 */
	public static String openDoor(){
		return "CONTROL DEVICE 01010106";
	}
	/**
	 * return background verification's value 
	 * @param data
	 * @return
	 */
	public static String retrunBGVerifyData(String data)
	{
		String ret = "AUTH=SUCCESS\r\n" + data + "\r\nCONTROL DEVICE 1 1 1 15\r\n";//after succeeded verify,open door for 15 seconds
//		String ret = "AUTH=FAILED\r\n" + data + "\r\n\r\n";//verify
		return ret;
	}
	
	public static void addDevCmd(String sn ,String cmd)
	{
		cmd = "C:"+Db.getDbId()+":"+cmd;
		
		LinkedList<String> cmdList;
		if(Db.cmdListMap.get(sn) == null)
		{
			cmdList=  new LinkedList<String>();
		}
		else
		{
			cmdList = Db.cmdListMap.get(sn);
		}
		cmdList.add(cmd);
		Db.cmdListMap.put(sn, cmdList);
		Integer cmdId = Integer.parseInt(cmd.split(":")[1]);
		String[] cmdArr = new String[4];//存放命令及命令执行结果save command and its result
		cmdArr[0] = cmd;
		cmdArr[1] = "";
		cmdArr[2] = sn;
		cmdArr[3] = "";
		Db.cmdMap.put(cmdId, cmdArr);
	}
}
