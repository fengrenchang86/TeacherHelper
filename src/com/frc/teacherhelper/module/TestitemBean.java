package com.frc.teacherhelper.module;

import com.frc.teacherhelper.entity.Testitem;

public class TestitemBean extends Testitem {
	protected String contentL1;
	protected String contentL2;
	protected String contentL3;
	protected String subjectName;
	
	public TestitemBean() {
		
	}
	public TestitemBean(Testitem item) {
		this.setContent(item.getContent());
		this.setCreatetime(item.getCreatetime());
		this.setCreator(item.getCreator());
		this.setGrade(item.getGrade());
		this.setId(item.getId());
		this.setLevel(item.getLevel());
		this.setParentId(item.getParentId());
		this.setSeq(item.getSeq());
		this.setSubjectid(item.getSubjectid());
		this.setUnit(item.getUnit());
		this.contentL1 = "";
		this.contentL2 = "";
		this.contentL3 = "";
	}
	
	public String getContentL1() {
		return contentL1;
	}
	public void setContentL1(String contentL1) {
		this.contentL1 = contentL1;
	}
	public String getContentL2() {
		return contentL2;
	}
	public void setContentL2(String contentL2) {
		this.contentL2 = contentL2;
	}
	public String getContentL3() {
		return contentL3;
	}
	public void setContentL3(String contentL3) {
		this.contentL3 = contentL3;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
}
