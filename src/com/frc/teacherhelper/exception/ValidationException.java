package com.frc.teacherhelper.exception;

import com.frc.teacherhelper.error.MessageItem;

@SuppressWarnings("serial")
public class ValidationException extends FRCCommonException {
	public ValidationException(MessageItem msgItem) {
		super();
		this.msgItem = msgItem;
	}
	
	public ValidationException(String errorCode) {
		super();
		this.msgItem = new MessageItem(errorCode);
	}
	
	public ValidationException(String errorCode, String errorType, String errorMessage) {
		super();
		this.msgItem = new MessageItem(errorCode, errorType, errorMessage);
	}
	
	public String getMessage() {
		return msgItem.getErrorMessage();
	}
}
