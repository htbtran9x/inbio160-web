package com.push.demo.sevlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.push.demo.db.Db;
import com.push.demo.util.Cmd;

/**
 * Create commands that can be read by devices
 * 
 * @author wkque
 * 
 */
public class CreateCmdServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String ret = "{\"ret\":\"ok\"}";
		try {
			String cmd = "";
			String sn = request.getParameter("sn");
			String cmdType = request.getParameter("cmdType");
			if (null != cmdType && cmdType.length() > 0) {
				if (cmdType.equals("openDoor")) {// ◎command that has already been defined
					cmd = Cmd.openDoor();
				} else if (cmdType.equals("userDefined")) {
					String originalCmd = request.getParameter("originalCmd");// original command
					if (originalCmd != null && !originalCmd.trim().equals("")) {
						cmd = originalCmd;
					}
				}
			}
			if (cmd != null && cmd.length() > 0) {
				Cmd.addDevCmd(sn, cmd);
			}
		} catch (Exception e) {
			ret = "{\"ret\":\"error\"}";
		}
		PrintWriter out = response.getWriter();
		out.write(ret);
		out.flush();
		out.close();

	}

}
