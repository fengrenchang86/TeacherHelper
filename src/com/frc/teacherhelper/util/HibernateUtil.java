package com.frc.teacherhelper.util;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Test;

import com.frc.teacherhelper.entity.Profile;


@SuppressWarnings("deprecation")
public class HibernateUtil {
	private static SessionFactory sessionFactory;

	static {
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

	public static void main(String[] args) throws Exception {
		List list = HibernateUtil.listUsers();
	}
	
	public static void save(Profile profile) throws Exception {
		Transaction tr = null;
		Session session = sessionFactory.openSession();

		try {
			tr = session.beginTransaction();
			// tr.begin();
			session.save(profile);
			tr.commit();
		} catch (Exception e) {
			if (null != tr) {
				tr.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public static List listUsers() throws Exception {
		List list = null;
		Transaction tr = null;
		Session session = sessionFactory.openSession();

		try {
			tr = session.beginTransaction();
			// tr.begin();
			String sql = "from Profile";
			Query query = session.createQuery(sql);
			list = query.list();

			tr.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return list;
	}
	
	@Test
	public void testListUser() {
		try {
			List<Profile> list = HibernateUtil.listUsers();
			for (Profile profile : list) {
				System.out.println(String.format("[%s]%s", profile.getUsername(), profile));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
