package com.finalproject.model;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by Marco on 18/04/2015.
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class UserSession implements Serializable {
    /**
	 * 
	 */
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
