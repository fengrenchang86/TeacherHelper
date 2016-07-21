package com.frc.teacherhelper.module.req;

import java.util.List;

import com.frc.teacherhelper.module.TestitemBean;

public class CreateMultiTestitemRequest extends BaseRequest {
	protected List<TestitemBean> list = null;

	public List<TestitemBean> getList() {
		return list;
	}
	public void setList(List<TestitemBean> list) {
		this.list = list;
	} 
}
