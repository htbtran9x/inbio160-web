package com.push.demo.sevlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.push.demo.db.Db;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TimeServlet extends HttpServlet {

	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject info = new JSONObject();
		JSONArray ja = new JSONArray();
		String ret = "ok";
		try{
			String type = request.getParameter("type");
			type = null == type ? "" : type;
			if(type.equals("0"))//Add or edit
			{
				Enumeration<String> enu = request.getParameterNames();
				String id = request.getParameter("id");
				Map<String, String> map = new HashMap<String, String>();
				while(enu.hasMoreElements())
				{
					String key = enu.nextElement();
					if(!key.equals("id"))
					{
						map.put(key, request.getParameter(key));
					}
					Db.timeMap.put(id, map);
				}
			}
			else if(type.equals("1"))//query
			{
				Map<String, Map<String, String>> times = Db.timeMap;
				Set<String> ids = times.keySet();
				int i=0;
				for(String id : ids)
				{
					if(i!=0) break;//this demo just return one time segment 
					Map<String, String> time = times.get(id);
					Set<String> keys = time.keySet();
					time.put("id", id);
					JSONObject t = new JSONObject();
					for(String key : keys)
					{
						t.put(key, time.get(key));
					}
					ja.add(t);
					i++;
				}
			}
		}catch (Exception e) {
			ret = "error";
		}finally{
			info.put("ret", ret);
			info.put("data", ja);
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(info.toString());
			out.flush();
			out.close();
		}
	}

}
