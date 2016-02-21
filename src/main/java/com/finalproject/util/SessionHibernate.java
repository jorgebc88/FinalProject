package com.finalproject.util;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class SessionHibernate {

	static SessionFactory sessionFactory;
	private static Session session;
	static final Logger LOGGER = Logger.getLogger(SessionHibernate.class);

	public SessionHibernate(SessionFactory sessionFactory) {
		SessionHibernate.sessionFactory = sessionFactory;
		session = SessionHibernate.sessionFactory.openSession();
	}


	public static Session getInstance() {
		LOGGER.info("Instancia actual: " + session);
		return session;
	}

	public static void closeSession() {
		session.close();
	}

}
