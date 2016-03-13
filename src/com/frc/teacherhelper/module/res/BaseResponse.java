package com.frc.teacherhelper.module.res;

import java.util.ArrayList;
import java.util.List;

import com.frc.teacherhelper.module.MsgItem;

public class BaseResponse {
	private String responseStatus;
	private List<MsgItem> errorMessages;
	public String getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
	public List<MsgItem> getErrorMessages() {
		if (errorMessages == null) {
			errorMessages = new ArrayList<MsgItem>();
		}
		return errorMessages;
	}
	public void setErrorMessages(List<MsgItem> errorMessages) {
		this.errorMessages = errorMessages;
	}
	public void addMsgItem(MsgItem msgItem) {
		getErrorMessages().add(msgItem);
	}
}
