package com.frc.teacherhelper.error;

public interface IErrorCodes {
	public static final String TYPE_ERROR = "E";
	public static final String TYPE_INFORMATION = "I";
	
	public static final String COMMON_ERROR = "COM000";
	public static final String USERNAME_EXISTS = "TH1001";
	public static final String EMAIL_EXISTS = "TH1002";
	public static final String INVALID_TYPE = "TH1003";
	public static final String EMPTY_USERNAME = "TH1004";
	public static final String EMPTY_PASSWORD = "TH1005";
	public static final String EMPTY_EMAIL = "TH1006";
	public static final String EMPTY_TYPE = "TH1007";
	
	public static final String INVALID_USER = "TH2001";
	public static final String WRONG_USERNAME_PSW = "TH2002";
}
