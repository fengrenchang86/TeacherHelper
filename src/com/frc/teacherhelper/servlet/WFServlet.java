package com.frc.teacherhelper.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class WFServlet extends HttpServlet {
	protected void dispatchJsonResponse(HttpServletRequest request, HttpServletResponse response, Object rsp) throws IOException {
		JSONObject jsObj = new JSONObject();
		jsObj.put("response", rsp);
		
		response.setCharacterEncoding("UTF-8");  
		response.setContentType("text/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String resp = jsObj.toString();
		out.write(resp);
		out.flush();
		out.close();
	}
	
	protected void dispatchHTMLResponse(HttpServletRequest request, HttpServletResponse response, Object rsp) throws IOException {
		JSONObject jsObj = new JSONObject();
		jsObj.put("response", rsp);
		
		response.setCharacterEncoding("UTF-8");  
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String resp = jsObj.toString();
		out.write("<html><body><textarea>" + resp + "</textarea></body></html>");
		out.flush();
		out.close();
	}
	
}
