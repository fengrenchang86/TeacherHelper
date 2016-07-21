package com.frc.teacherhelper.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.frc.teacherhelper.entity.Testitem;
import com.frc.teacherhelper.module.TestitemBean;
import com.frc.teacherhelper.module.req.CreateMultiTestitemRequest;
import com.frc.teacherhelper.module.req.CreateTestItemRequest;
import com.frc.teacherhelper.module.req.QueryTestItemRequest;
import com.frc.teacherhelper.module.res.CreateTestItemResponse;
import com.frc.teacherhelper.module.res.QueryTestItemResponse;
import com.frc.teacherhelper.service.ImportTestItemService;
import com.frc.teacherhelper.service.TestItemService;
import com.frc.teacherhelper.util.DateUtil;

@Controller
@RequestMapping("/testitem")
public class TestItemController {
	private TestItemService testitemService;

	@RequestMapping(value = "/hello")
	public @ResponseBody String sayHello() {
		return "OK";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody CreateTestItemResponse createTestitem(@RequestBody CreateTestItemRequest request) {
		System.out.println("TestItemController::CreateTestItemResponse()");
		CreateTestItemResponse response = null;

		Testitem item = null;
		int unit = Integer.parseInt(request.getUnit());
		item = testitemService.addTestitem(request.getGrade(), request.getSubject(), unit,
				request.getParentContent(), request.getContent());

		System.out.println("Content:" + request.getContent());
		
		return response;
	}
	
	@RequestMapping(value = "/createmulti", method = RequestMethod.POST)
	public @ResponseBody CreateTestItemResponse createTestitem(@RequestBody CreateMultiTestitemRequest request) {
		System.out.println("TestItemController::CreateTestItemResponse()");
		CreateTestItemResponse response = null;

		List<TestitemBean> list = request.getList();
		System.out.println(list.size());
		testitemService.addTestitem(list);
		
		return response;
	}

	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public @ResponseBody QueryTestItemResponse queryTestitem(@RequestBody QueryTestItemRequest request) {
		QueryTestItemResponse response = new QueryTestItemResponse();
		List<TestitemBean> list = testitemService.queryTestitemsByGradeNSubject(request.getGrade(), request.getSubject());
		response.setTestitems(list);
		return response;
	}

	@Autowired
	public void setTestitemService(TestItemService testitemService) {
		this.testitemService = testitemService;
	}

}
