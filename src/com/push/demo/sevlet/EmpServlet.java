package com.push.demo.sevlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.push.demo.db.Db;

public class EmpServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject info = new JSONObject();
		JSONArray ja = new JSONArray();
		String ret = "ok";
		try{
			String type = request.getParameter("type");
			type = type == null ? "" : type;
			if(type.equals("0"))//Add
			{
				String empPin = request.getParameter("empPin");
				if(null != empPin && empPin.trim().length()>0)
				{
					String empCard = request.getParameter("empCard");
					String empName = request.getParameter("empName");
					String empPwd = request.getParameter("empPwd");
					String empStartTime = request.getParameter("empStartTime");
					String empEndTime = request.getParameter("empEndTime");
					String empGroup = request.getParameter("empGroup");
					String empSuper = request.getParameter("empSuper");
					String empDisable = request.getParameter("empDisable");
					Map<String,String> emp = new HashMap<String, String>();
					emp.put("empPin", empPin);
					emp.put("empCard", empCard);
					emp.put("empName", empName);
					emp.put("empPwd", empPwd);
					emp.put("empStartTime", empStartTime);
					emp.put("empEndTime", empEndTime);
					emp.put("empGroup", empGroup);
					emp.put("empSuper", empSuper);
					emp.put("empDisable", empDisable);
					Db.empMap.put(empPin, emp);
				}
			}
			else if(type.equals("1"))
			{
				Map<String,Map<String, String>> empMap = Db.empMap;
				Set<String>  sets = empMap.keySet();
				for(String pin : sets)
				{
					Map<String, String> emp = empMap.get(pin);
					Set<String> keySet = emp.keySet();
					JSONObject t = new JSONObject();
					for(String key : keySet )
					{
						t.put(key, emp.get(key)+"");
					}
					t.put("id", pin);
					ja.add(t);
				}
				
			}
		}catch (Exception e) {
			e.printStackTrace();
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
