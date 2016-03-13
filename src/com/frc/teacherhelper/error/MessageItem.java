package com.frc.teacherhelper.error;

public class MessageItem {
	private String errorCode;
	private String errorType;
	private String errorMessage;
	
	public MessageItem(String errorCode) {
		super();
		this.errorCode = errorCode;
		this.errorType = IErrorCodes.TYPE_INFORMATION;
		this.errorMessage = "";
	}
	public MessageItem(String errorCode, String errorType, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorType = errorType;
		this.errorMessage = errorMessage;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorType() {
		return errorType;
	}
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
