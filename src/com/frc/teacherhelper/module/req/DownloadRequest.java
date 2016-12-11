package com.frc.teacherhelper.module.req;

public class DownloadRequest extends BaseRequest {
	protected String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
