package com.finalproject.dao;

import java.util.List;

import javax.management.RuntimeErrorException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import com.finalproject.model.User;


/**
 * Created by Marco on 18/04/2015.
 */

public class UserDaoImpl implements UserDao{

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction tx = null;

	@SuppressWarnings("unchecked")
	@Override
	public boolean addUser(User user) throws Exception {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		List<User> userList = session.createQuery("from User where userName = :name ")
				.setParameter("name", user.getUserName())
				.list();
		tx.commit();
		if(userList.isEmpty()){
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
		}else{
			session.close();
			throw new RuntimeException("Usuario Existente!");
		}
		session.close();
		return true;
	}

	@Override
	public User getUserById(long id) throws Exception {
		session = sessionFactory.openSession();
		User user = (User) session.load(User.class,
				new Long(id));
		tx = session.getTransaction();
		session.beginTransaction();
		tx.commit();
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserList() throws Exception {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		List<User> userList = session.createCriteria(User.class)
				.list();
		tx.commit();
		session.close();
		return userList;
	}
	
	@Override
	public boolean deleteUser(long id)
			throws Exception {
		session = sessionFactory.openSession();
		Object o = session.load(User.class, id);
		tx = session.getTransaction();
		session.beginTransaction();
		session.delete(o);
		tx.commit();
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public User login(String name, String password) throws Exception{
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		List<User> userList = session.createQuery("from User where userName = :name ")
				.setParameter("name", name)
				.list();
		tx.commit();
		session.close();
		for(User user : userList){
			if(user.getPassword().equals(password)){
				return user;
			}
		}
		return null;
	}
	
}
