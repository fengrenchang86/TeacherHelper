package com.frc.teacherhelper.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.frc.teacherhelper.dao.ISubjectDao;
import com.frc.teacherhelper.dao.ITestitemDao;
import com.frc.teacherhelper.entity.Subject;
import com.frc.teacherhelper.entity.Testitem;
import com.frc.teacherhelper.module.req.ImportTestItemRequest;
import com.frc.teacherhelper.util.DateUtil;
import com.frc.teacherhelper.util.IOUtil;

@Component("ImportTestItemService")
public class ImportTestItemService implements IFileUploadService {
	private ITestitemDao testitemDao = null;
	private ISubjectDao subjectDao = null;
	
	@Override
	public void process(String fileName, Object input) {
		Map<String, String> inputMap = (Map)input;
		ImportTestItemRequest request = new ImportTestItemRequest();
		request.setGrade(inputMap.get("grade"));
		request.setSubjectName(inputMap.get("subject"));
		System.out.println("ImportTestItemService");
		String fileContent = "";
		try {
			byte[] data = IOUtil.readBytesFromFile(fileName, false);
			fileContent = new String(data);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		Subject subject = subjectDao.querySubject(request.getSubjectName());
		if (subject == null) {
			System.out.println("fail to query subject : " + request.getSubjectName());
			return;
		}
		
		String dateTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss");
		List<Testitem> list = new ArrayList<Testitem>();
		
		String lines[] = fileContent.split("\r\n");
		Map<String, Testitem> map = new HashMap<>();

		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			String items[] = line.split(",", -1);
			if (items.length > 4 || items.length < 2) {
				System.out.println("Incorrect format");
				return;
			}
			for (int j = 1; j < items.length; j++) {
				if (items[j] != null && !"".equals(items[j]) && !map.containsKey(items[j])) {
					Testitem ti = new Testitem();
					ti.setCreator("SYS");
					ti.setCreatetime(dateTime);
					ti.setSubjectid(subject.getSubjectid());
					ti.setLevel(j);
					ti.setUnit(Integer.parseInt(items[0]));
					ti.setContent(items[j]);
					ti.setGrade(request.getGrade());
					map.put(items[j], ti);
					if (j == 1) {
						Testitem rs = testitemDao.queryTestitemsByContent(items[1]);
						if (rs == null) {
							ti.setParentId(0);
							String seq = generateSEQ(request.getGrade(), subject.getSubjectid(), j, 0);
							ti.setSeq(seq);
							testitemDao.addTestitem(ti);
						}
					} else {
						Testitem rs = testitemDao.queryTestitemsByContent(items[j]);
						if (rs == null) {
							Testitem pre = testitemDao.queryTestitemsByContent(items[j-1]);
							ti.setParentId(pre.getId());
							String seq = generateSEQ(request.getGrade(), subject.getSubjectid(), j, pre.getId());
							ti.setSeq(seq);
							testitemDao.addTestitem(ti);
						}
					}
				}				
			}
		}
	}
	
	public static String generateSEQ(String grade, int subject, int level, int parentId) {
		long d = new Date().getTime();
		String str = String.format("%s%02d%02d%04d%06d", grade, subject, level, parentId, d);
		return str;
	}
	
	@Autowired
	public void setTestitemDao(ITestitemDao testitemDao) {
		this.testitemDao = testitemDao;
	}
	@Autowired
	public void setSubjectDao(ISubjectDao subjectDao) {
		this.subjectDao = subjectDao;
	}
	
	

}
