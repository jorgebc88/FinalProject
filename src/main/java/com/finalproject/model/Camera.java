package com.finalproject.model;

import java.io.Serializable;

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

	private static final long serialVersionUID = 2607403988611110166L;

	@Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "location")
    private String location;
    
    @Column(name = "latitude")
    private String latitude;
 
    @Column(name = "longitude")
    private String longitude;
    
    public Camera(){
    }
    
    public Camera(String location) {
    	super();
    	this.location = location;
    }

    public Camera(Long id, String location, String latitude, String longitude) {
		super();
		this.id = id;
		this.location = location;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public void setId(Long id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

    @Override
	public String toString() {
		return "Camera [id=" + id + ", location=" + location + ", latitude=" + latitude + ", longitude=" + longitude
				+ "]";
	}
}
