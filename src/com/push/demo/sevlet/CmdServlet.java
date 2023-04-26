package com.push.demo.sevlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.push.demo.db.Db;

public class CmdServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		Map<Integer,String[]> cmdMap = Db.cmdMap;
		Set<Integer> set = cmdMap.keySet();
		JSONArray jsonArray = new JSONArray();
		JSONObject retJson = new JSONObject();
		String value = "";
		JSONObject obj;
		for(Integer cmdId : set){
			obj = new JSONObject();
			obj.put("cmdId", cmdId);
			obj.put("cmd", cmdMap.get(cmdId)[0]);
			obj.put("cmdRet", cmdMap.get(cmdId)[1]);
			obj.put("sn", cmdMap.get(cmdId)[2]);
			//obj.put("cmdData", cmdMap.get(cmdId)[3]);
			jsonArray.add(obj);
			value = cmdMap.get(cmdId)[3];
		}
		retJson.put("cmdArray", jsonArray);
		retJson.put("cmdData", value);
		PrintWriter out = response.getWriter();
		out.write(retJson.toString());
		out.flush();
		out.close();
	}

}
