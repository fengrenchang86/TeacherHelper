package com.frc.teacherhelper.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.frc.teacherhelper.module.req.DownloadRequest;
import com.frc.teacherhelper.util.IOUtil;

@Controller
@RequestMapping("/demo")
public class DemoController {

	@RequestMapping(value = "/createfile")
	public @ResponseBody String test(@RequestBody String request) {
		System.out.println(request);
		File file = new File(request);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(file.getAbsolutePath());

		}
		return "_" + request + "_";
	}

	@RequestMapping(value = "/download")
	public void download(@RequestBody String fileName, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		File file = new File(fileName);
		if (file.exists()) {
			System.out.println(file.getAbsolutePath());
		}

		InputStream is = new FileInputStream(file);

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		bis = new BufferedInputStream(is);
		bos = new BufferedOutputStream(response.getOutputStream());
		int r = 0;
		String contentType = request.getServletContext().getMimeType(fileName);
		response.setContentType("application/octet-stream");
		response.addHeader("Content-Disposition", "attachment; filename=ttt");
		response.setContentLength(bis.available());
		response.setDateHeader("Expires", getExpiresMills());
		response.addHeader("Cache-Control", "max-age=600");

		while ((r = bis.read()) != -1) {
			bos.write(r);
		}
		bis.close();
		bos.close();
	}

	@RequestMapping(value = "/download2",  method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<byte[]> download(@RequestParam String fileName) throws IOException {
		if (fileName == null || fileName.length() == 0) {
			fileName = "ttt";
		}
		String dfileName = new String(fileName.getBytes("UTF-8"), "UTF-8");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", dfileName);
		
		return new ResponseEntity<byte[]>(IOUtil.readBytesFromFile(fileName), headers, HttpStatus.CREATED);
	}

	protected long getExpiresMills() {
		Date date = DateUtils.addMinutes(new Date(), 30);
		long t = date.getTime();
		return t;
	}
}
