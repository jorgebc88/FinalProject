package com.finalproject.model;

import java.io.Serializable;

public class UserSession implements Serializable {
	private static final long serialVersionUID = 2419616269450372897L;
	public User user;

	public UserSession() {
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
