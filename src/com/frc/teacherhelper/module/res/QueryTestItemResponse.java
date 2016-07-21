package com.frc.teacherhelper.module.res;

import java.util.List;

import com.frc.teacherhelper.entity.Testitem;
import com.frc.teacherhelper.module.TestitemBean;

public class QueryTestItemResponse extends BaseResponse {
	protected List<TestitemBean> testitems;

	public List<TestitemBean> getTestitems() {
		return testitems;
	}
	public void setTestitems(List<TestitemBean> testitems) {
		this.testitems = testitems;
	}
}
