package com.frc.teacherhelper.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.stereotype.Service;

import com.frc.teacherhelper.entity.Profile;
import com.frc.teacherhelper.error.IErrorCodes;
import com.frc.teacherhelper.error.MessageItem;
import com.frc.teacherhelper.exception.LogonException;
import com.frc.teacherhelper.exception.ValidationException;
import com.frc.teacherhelper.module.UserInfo;
import com.frc.teacherhelper.util.HibernateUtil;

@Service("UserService")
public class UserService {
	
	@Resource(name="sessionFactory")
    private SessionFactory sessionFactory;
	
	public String createUser(String username, String password, String email, String nickname, int type) throws Exception {
		if (!validate(username, password, email, nickname, type)) {
			return "FAIL";
		}
		Profile profile = new Profile();
		profile.setMailAddress(email);
		profile.setUsername(username);
		profile.setPassword(password);
		profile.setNickname(nickname);
		profile.setType(type);
		
		Date date = new Date();
		profile.setRegisterDate(date);
		
		HibernateUtil.save(profile);
		return "";
	}
	
	public UserInfo logon(UserInfo userInfo) throws LogonException {
		UserInfo result = new UserInfo();
		
		List<Profile> list = null;
		Transaction tr = null;
		Session session = null;
		session = sessionFactory.openSession();
		try {
			tr = session.beginTransaction();
			String sql = "from Profile where username = ? and password = ?";
			Query query = session.createQuery(sql);
			query.setString(0, userInfo.getUsername());
			query.setString(1, userInfo.getPassword());
			list = query.list();
		} catch (Exception e) {
			if (null != tr) {
				tr.rollback();
			}
		} finally {
			session.close();
		}
		if (list != null && list.size() == 1) {
			Profile profile = list.get(0);
			if (profile.getStatus() % 2 == 0) {
				throw new LogonException(IErrorCodes.INVALID_USER, IErrorCodes.TYPE_ERROR, "Invalid user, error status");
			}
			result.setUsername(profile.getUsername());
			result.setNickname(profile.getNickname());
			result.setEmail(profile.getMailAddress());
			result.setType(profile.getType());
		} else {
			throw new LogonException(IErrorCodes.WRONG_USERNAME_PSW, IErrorCodes.TYPE_ERROR, "Username or password incorrect");
		}
		
		return result;
	}
	
	public boolean isUsernameExist(String username) {
		List<Profile> result = null;
		Transaction tr = null;
		Session session = null;
		session = sessionFactory.openSession();
		try {
			tr = session.beginTransaction();
			String sql = "from Profile where username = ?";
			Query query = session.createQuery(sql);
			query.setString(0, username);
			result = query.list();
		} catch (Exception e) {
			if (null != tr) {
				tr.rollback();
			}
		} finally {
			session.close();
		}
		if (result == null || result.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean isEmailAddressExist(String email) {
		List<Profile> result = null;
		Transaction tr = null;
		Session session = null;
		session = sessionFactory.openSession();
		try {
			tr = session.beginTransaction();
			String sql = "from Profile where mailAddress = ?";
			Query query = session.createQuery(sql);
			query.setString(0, email);
			result = (List<Profile>) query.list();
		} catch (Exception e) {
			if (null != tr) {
				tr.rollback();
			}
		} finally {
			session.close();
		}
		if (result == null || result.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	private boolean validate(String username, String password, String email, String nickname, int type) throws ValidationException {
		if (username == null || username.length() == 0) {
			throw new ValidationException(IErrorCodes.EMPTY_USERNAME, IErrorCodes.TYPE_ERROR, "Missing username");
		}
		if (password == null || password.length() == 0) {
			throw new ValidationException(IErrorCodes.EMPTY_PASSWORD, IErrorCodes.TYPE_ERROR, "Missing password");
		}
		if (email == null || email.length() == 0) {
			throw new ValidationException(IErrorCodes.EMPTY_EMAIL, IErrorCodes.TYPE_ERROR, "Missing email");
		}
		if (isUsernameExist(username)) {
			String errorMsg = String.format("username [%s] already exists", username);
			throw new ValidationException(IErrorCodes.USERNAME_EXISTS, IErrorCodes.TYPE_ERROR, errorMsg);
		}
		if (isEmailAddressExist(email)) {
			String errorMsg = String.format("email address [%s] already exists", email);
			throw new ValidationException(IErrorCodes.EMAIL_EXISTS, IErrorCodes.TYPE_ERROR, errorMsg);
		}
		if (type != 1 && type != 2) {
			String errorMsg = String.format("type [%d] invalid", type);
			throw new ValidationException(IErrorCodes.INVALID_TYPE, IErrorCodes.TYPE_ERROR, errorMsg);
		}
		return true;
	}
	@Test
	public void testLogon() {
		UserInfo userInfo = new UserInfo();
		userInfo.setUsername("frc001");
		userInfo.setPassword("frc001");
		try {
			UserInfo rs = logon(userInfo);
			System.out.println("Email:" + rs.getEmail());
		} catch (LogonException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testIsUsernameExist() {
		boolean b = false;
		b = isUsernameExist("wf");
		System.out.println(b);
		b = isUsernameExist("wf1");
		System.out.println(b);
		b = isUsernameExist("wfwww");
		System.out.println(b);
	}
	
	@Test
	public void testIsEmailAddressExist() {
		boolean b = false;
		b = isEmailAddressExist("wf@wf.com");
		System.out.println(b);
		b = isEmailAddressExist("wf@wf.com1");
		System.out.println(b);
		b = isEmailAddressExist("aaa");
		System.out.println(b);
	}
	
	@Test
	public void testCreateUser() {
		String username = "fengrenchang001";
		String password = "fengrenchang001";
		String email = "fengrenchang001@wf.com";
		String nickname = "fengrenchang001";
		int type = 1;
		try {
			createUser(username, password, email, nickname, type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Before
	public void before() {
		try {
			Configuration conf = new Configuration();
			conf.configure();
			ServiceRegistry sr = new ServiceRegistryBuilder().applySettings(
					conf.getProperties()).buildServiceRegistry();

			sessionFactory = conf.buildSessionFactory(sr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
