package com.finalproject.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;


/**
 */
@Entity
@Table(name = "camera")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Camera implements Serializable {

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

	@Column(name = "ip")
	private String ip;

	@Column(name = "active")
	private boolean active;

	@Column(name = "pointing_at")
	private String pointing_at;


	public Camera() {
	}

	public Camera(String location) {
		super();
		this.location = location;
	}

	public Camera(Long id, String location, String latitude, String longitude, String ip, boolean active, String pointing_at) {
		super();
		this.id = id;
		this.location = location;
		this.latitude = latitude;
		this.longitude = longitude;
		this.ip = ip;
		this.active = active;
		this.pointing_at = pointing_at;
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getPointingAt() {
		return pointing_at;
	}

	public void setPointingAt(String pointingAt) {
		this.pointing_at = pointing_at;
	}

	@Override
	public String toString() {
		return "Camera{" +
				"id=" + id +
				", location='" + location + '\'' +
				", latitude='" + latitude + '\'' +
				", longitude='" + longitude + '\'' +
				", ip='" + ip + '\'' +
				", active=" + active +
				", pointing_at='" + pointing_at + '\'' +
				'}';
	}
}
