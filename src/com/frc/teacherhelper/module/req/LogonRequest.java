package com.frc.teacherhelper.module.req;

import com.frc.teacherhelper.module.UserInfo;

public class LogonRequest extends BaseRequest {
	private UserInfo userInfo;

	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
}
