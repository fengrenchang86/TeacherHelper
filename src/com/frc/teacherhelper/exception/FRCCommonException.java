package com.frc.teacherhelper.exception;

import com.frc.teacherhelper.error.IErrorCodes;
import com.frc.teacherhelper.error.MessageItem;

public class FRCCommonException extends Exception {
	protected MessageItem msgItem = null;
	public FRCCommonException() {
		this.msgItem = new MessageItem(IErrorCodes.COMMON_ERROR, IErrorCodes.TYPE_ERROR, "Unknown exception");
	}
	public FRCCommonException(MessageItem msgItem) {
		super();
		this.msgItem = msgItem;
	}
	
	public FRCCommonException(String errorCode) {
		super();
		this.msgItem = new MessageItem(errorCode);
	}
	
	public FRCCommonException(String errorCode, String errorType, String errorMessage) {
		super();
		this.msgItem = new MessageItem(errorCode, errorType, errorMessage);
	}
	
	public String getMessage() {
		return msgItem.getErrorMessage();
	}
	public String getErrorCode() {
		return msgItem.getErrorCode();
	}
}
