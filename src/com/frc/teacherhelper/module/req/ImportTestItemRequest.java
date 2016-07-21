package com.frc.teacherhelper.module.req;

public class ImportTestItemRequest extends BaseRequest {
	protected String subjectName;
	protected String grade;
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
}
