package com.finalproject.util;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SessionHibernate {

	static SessionFactory sessionFactory;
	private static Session session;
	static final Logger LOGGER = Logger.getLogger(SessionHibernate.class);

	public SessionHibernate(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
		this.session = this.sessionFactory.openSession();;
	}
	
	
	public static Session getInstance(){
		LOGGER.info("Instancia actual: " + session);
		return session;
	}
	
	public static void closeSession(){
		session.close();
	}
	
}
