package com.push.demo.sevlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import com.push.demo.util.Constants;

public class RealEvent extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONArray data = new JSONArray();
		JSONObject ret = new JSONObject();
		String desc = "ok";
		try{
			Map<String, String> map = null;
			synchronized (DeviceVisitServerSevlet.class) {
				if(Db.realEventList.size()>0)
				{
					map = Db.realEventList.remove(0);
				}
			}
			if(map!=null)
			{
				JSONObject obj = new JSONObject();
				
				//time=2015-01-28 16:32:57	pin=0	cardno=505931040	eventaddr=1	event=27	inoutstatus=0	verifytype=4	index=21268
				obj.put("sn", map.get("sn"));
				obj.put("time", map.get("time"));
				String pin = map.get("pin");
				String empName = "";
				if(!pin.equals("0") && !pin.equals(""))
				{
					Map<String,String> emp = Db.empMap.get(pin);
					if(emp!=null)
					{
						empName = emp.get("empName");
					}
				}
				
				empName = (null == empName || empName.equals("") )? pin : empName+"("+pin+")";
				obj.put("pin", empName);
				
				obj.put("cardno", map.get("cardno"));
				obj.put("eventaddr", "Door "+map.get("eventaddr"));
				obj.put("event", map.get("event"));
				
				String eventNo = map.get("event");
				String eventName = null;
				if(eventNo !=null && eventNo.length()>0){
					eventName = Constants.allEventMap.get(eventNo);
				}
				eventName = eventName == null? "unknown event" : eventName;
				obj.put("event",eventName+"("+eventNo+")");
				String inOut = map.get("inoutstatus");
				inOut = inOut.equals("0") ? "in" : "out"; 
				obj.put("inoutstatus",inOut);
				
				int verifyType = Integer.parseInt(map.get("verifytype"));
				String verifyTypeName = Db.VERIFY_MODE.get(verifyType);
				verifyTypeName = (verifyTypeName==null || verifyTypeName.equals("")) ? "unknown verification("+verifyType+")" : verifyTypeName +"("+verifyType+")";
				
				obj.put("verifytype",verifyTypeName );
				obj.put("index", map.get("index"));
				data.add(obj);
			}
			
		}catch (Exception e) {
			desc = "error";
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

}
