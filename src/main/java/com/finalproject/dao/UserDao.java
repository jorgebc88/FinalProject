package com.finalproject.dao;

import com.finalproject.model.User;

import java.util.List;


public interface UserDao {
	boolean addUser(User user) throws Exception;

	User getUserById(long id) throws Exception;

	List<User> getUserList() throws Exception;

	boolean deleteUser(long id) throws Exception;

	User login(String name, String password) throws Exception;
}
