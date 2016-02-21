package com.finalproject.dao;

import com.finalproject.model.User;
import com.sun.org.apache.bcel.internal.generic.LOOKUPSWITCH;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This is the DAO that manages the transaction to the database for the Users
 */
@Component
public class UserDaoImpl implements UserDao {

	@Autowired
	SessionFactory sessionFactory;

	static final Logger LOGGER = Logger.getLogger(UserDaoImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public boolean addUser(User user) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<User> userList = session.createQuery("from User where userName = :name ")
				.setParameter("name", user.getUserName()).list();
		tx.commit();
		if (userList.isEmpty()) {
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
		} else {
			session.close();
			throw new RuntimeException("The user already exist!");
		}
		LOGGER.info("The user: " + user.toString() + " was created successfully");
		session.close();
		return true;
	}

	@Override
	public User getUserById(long id) throws Exception {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		User user = (User) session.get(User.class, id);
		session.flush();
		session.close();
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserList() throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<User> userList = session.createCriteria(User.class).list();
		tx.commit();
		session.close();
		return userList;
	}

	@Override
	public boolean deleteUser(long id) throws Exception {
		Session session = sessionFactory.openSession();
		User user = (User) session.load(User.class, id);
		session.delete(user);
		session.flush();
		session.close();
		LOGGER.info("The user: " + user + " was deleted successfully");
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public User login(String name, String password) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<User> userList = session.createQuery("from User where userName = :name ").setParameter("name", name)
				.list();
		tx.commit();
		session.close();
		for (User user : userList) {
			if (user.getPassword().equals(password)) {
				return user;
			}
		}
		return null;
	}

}
