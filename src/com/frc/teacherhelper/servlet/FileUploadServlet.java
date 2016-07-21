package com.frc.teacherhelper.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.naming.ldap.InitialLdapContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;

import com.frc.teacherhelper.module.req.ImportTestItemRequest;
import com.frc.teacherhelper.module.res.ImportTestItemResponse;
import com.frc.teacherhelper.service.IFileUploadService;
import com.frc.teacherhelper.service.ImportTestItemService;
import com.frc.teacherhelper.service.InitServer;
import com.frc.teacherhelper.util.DateUtil;
import com.frc.teacherhelper.util.StringUtil;

public class FileUploadServlet extends WFServlet {
	
	/**
	 * Constructor of the object.
	 */
	public FileUploadServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		request.setCharacterEncoding("ISO-8859-1");
		System.out.println("request.getCharacterEncoding(): " + request.getCharacterEncoding());
		request.setCharacterEncoding("UTF-8");
		
		String command = request.getParameter("command");
	
		
		if (StringUtil.isEmpty(command) || "import".equals(command)) {
			ImportTestItemRequest req = new ImportTestItemRequest();
			req.setGrade(request.getParameter("grade"));
			req.setSubjectName(request.getParameter("subject"));
			ImportTestItemResponse rsp = new ImportTestItemResponse();
			ImportTestItemService importTestItemService = (ImportTestItemService) InitServer.getApplicationContext()
					.getBean("ImportTestItemService");
			processFileUpload(request, importTestItemService, req);
			rsp.setResponseStatus("OK");
			
			dispatchHTMLResponse(request, response, rsp);
		}
		
		System.out.println(command);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	
	private Object processFileUpload(HttpServletRequest request, IFileUploadService service, Object obj) {
		String fileName = "", fullpath = "";
		Map<String, String> map = new HashMap<String, String>();
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("utf-8");
			Iterator items = null;
			try {
				items = upload.parseRequest(request).iterator();
				while (items.hasNext()) {
					FileItem item = (FileItem) items.next();
					if (!item.isFormField()) {
						
						long size = item.getSize();
						String name = item.getName();
						if (size == 0 || StringUtil.isEmpty(name)) {
							continue;
						}
//						name = new String(name.getBytes("ISO-8859-1"), "utf8");
						fileName = name.substring(
								name.lastIndexOf('\\') + 1, name.length());
						fileName = String.format("%s.%s", fileName, DateUtil.getDateTime("yyyyMMddHHmmss"));
						
						String path = "testitem";
						
						String folderPath = String.format("%s%c%s", 
								String.format("files%cupload", File.separatorChar), 
								File.separatorChar, 
								path);
						System.out.println("folderPath: " +folderPath);
						
						fullpath =String.format("%s%c%s", folderPath, File.separatorChar, fileName);
						System.out.println("fullpath: " +fullpath);
						
						File uploadedFile = new File(folderPath);
						uploadedFile.mkdirs();
						
						fullpath = String.format("%s%c%s", folderPath, File.separatorChar, fileName);
						uploadedFile = new File(fullpath);
						
						item.write(uploadedFile);
						System.out.println("Abs path:" + uploadedFile.getAbsolutePath());

					} else {
						String name = item.getFieldName();
						String value =item.getString("GBK");
						map.put(name, value);
						System.out.println("[WF_Not_FormField]" + name + ":" + value);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (service != null) {
			service.process(fullpath, map);
		}
		return null;
	}
	
}
