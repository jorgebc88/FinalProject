package com.finalproject.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;



/**
 * Created by Marco on 18/04/2015.
 */
@Entity
@Table(name="camera")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Camera implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 2607403988611110166L;

	@Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "ubication")
    private String ubication;
 
    public Camera(){
    }
    public Camera(String ubication) {
    	super();
    	this.ubication = ubication;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUbication(String ubication) {
        this.ubication = ubication;
    }

    public Long getId() {
        return id;
    }

    public String getUbication() {
        return ubication;
    }
}
