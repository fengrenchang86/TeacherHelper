package com.frc.teacherhelper.service;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class InitServer implements ServletContextListener {
	private static WebApplicationContext springContext;

	public void contextDestroyed(ServletContextEvent arg0) {

	}

	public void contextInitialized(ServletContextEvent event) {
		springContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());

		System.out.println("contextInitialized");
	}
	
	public static ApplicationContext getApplicationContext() {
        return springContext;
    }

}
