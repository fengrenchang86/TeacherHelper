package com.frc.teacherhelper.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.frc.teacherhelper.common.IConstants;
import com.frc.teacherhelper.error.IErrorCodes;
import com.frc.teacherhelper.exception.LogonException;
import com.frc.teacherhelper.exception.ValidationException;
import com.frc.teacherhelper.module.MsgItem;
import com.frc.teacherhelper.module.UserInfo;
import com.frc.teacherhelper.module.req.LogonRequest;
import com.frc.teacherhelper.module.res.LogonResponse;
import com.frc.teacherhelper.service.UserService;
/*
@Controller
@RequestMapping("/User")*/
public class UserController {
	
	@Resource(name="UserService")
	private UserService userService;

	@RequestMapping(value="/test", method = RequestMethod.POST)
	public @ResponseBody String test(@RequestBody String request) {
		System.out.println(request);
		return "_" + request + "_";
	}
	
	@RequestMapping(value="/reg", method=RequestMethod.POST)
	public @ResponseBody String doRegister(@RequestBody UserInfo userinfo) {
		System.out.println(userinfo.getUsername());
		System.out.println(userinfo.getPassword());
		System.out.println(userinfo.getEmail());
		String rs = "";
		try {
			rs = userService.createUser(
					userinfo.getUsername(), 
					userinfo.getPassword(), 
					userinfo.getEmail(), 
					userinfo.getNickname(), 
					userinfo.getType());
		} catch (ValidationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	@RequestMapping(value="/logon", method=RequestMethod.POST)
	public @ResponseBody LogonResponse doLogon(@RequestBody LogonRequest request) {
		UserInfo userinfo = request.getUserInfo();
		
		System.out.println(userinfo.getUsername());
		System.out.println(userinfo.getPassword());
		System.out.println(userinfo.getEmail());
		
		LogonResponse response = new LogonResponse();
		UserInfo rs = null;
		try {
			rs = userService.logon(userinfo);
			response.setUserInfo(rs);
			response.setResponseStatus(IConstants.RES_STATUS_SUCCESS);
		} catch (LogonException e) {
			response.getErrorMessages().add(new MsgItem(e.getErrorCode()));
			response.setResponseStatus(IConstants.RES_STATUS_FAIL);
			e.printStackTrace();
		} catch (Exception e) {
			response.getErrorMessages().add(new MsgItem(IErrorCodes.COMMON_ERROR));
			response.setResponseStatus(IConstants.RES_STATUS_FAIL);
			e.printStackTrace();
		}
		return response;
	}
}
