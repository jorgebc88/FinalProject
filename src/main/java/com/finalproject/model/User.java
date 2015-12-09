package com.finalproject.model;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

@Entity
@Table(name = "USER")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements Serializable{

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "Id")
    private Long userId;

    @Column(name = "userName", unique = true)
    private String userName;

    @Column(name = "password")
    private String password;
    
    @Column(name = "level")
    private int level;
    
    
    public User(){
    	super();
    }
    
    public User(String userName, String password, int level) {
        super();
        this.userName = userName;
        this.password = password;
        this.level = level; 
    }

    
    public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValidPassword(String password) {
        return (this.password.equals(password));
    }

}
