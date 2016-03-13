package com.frc.teacherhelper.exception;

import com.frc.teacherhelper.error.MessageItem;

public class LogonException extends FRCCommonException {
	public LogonException(MessageItem msgItem) {
		super();
		this.msgItem = msgItem;
	}
	
	public LogonException(String errorCode) {
		super();
		this.msgItem = new MessageItem(errorCode);
	}
	
	public LogonException(String errorCode, String errorType, String errorMessage) {
		super();
		this.msgItem = new MessageItem(errorCode, errorType, errorMessage);
	}
	
	public String getMessage() {
		return msgItem.getErrorMessage();
	}
}
