package com.frc.test;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

public class SpringNHibernate {
	@Test
	public void test() {
		SessionFactory sessionFactory = new org.springframework.orm.hibernate4.LocalSessionFactoryBean().getObject();
		System.out.println(sessionFactory);
	}
}
