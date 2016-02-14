package com.finalproject.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "detected_object")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DetectedObject implements Serializable {

	private static final long serialVersionUID = 2607403988611110166L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "direction")
	private String direction;

	@Column(name = "objectType")
	private String ObjectType;

	@Column(name = "date")
	private Date date;

	@Column(name = "camera_id")
	private long camera_id;

	public DetectedObject() {
	}

	public DetectedObject(String direction, String objectType, Date date) {
		super();
		this.direction = direction;
		this.ObjectType = objectType;
		this.date = date;
		this.camera_id = 1;
	}

	public DetectedObject(long id, String direction, String objectType, Date date, long camera_id) {
		super();
		this.id = id;
		this.direction = direction;
		this.ObjectType = objectType;
		this.date = date;
		this.camera_id = camera_id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public void setObjectType(String objectType) {
		ObjectType = objectType;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public String getDirection() {
		return direction;
	}

	public String getObjectType() {
		return ObjectType;
	}

	public Date getDate() {
		return date;
	}

	public long getCamera_id() {
		return camera_id;
	}

	public void setCamera_id(int camera_id) {
		this.camera_id = camera_id;
	}

	@Override
	public String toString() {
		return "DetectedObject [id=" + id + ", direction=" + direction + ", ObjectType=" + ObjectType + ", date=" + date
				+ ", camera_id=" + camera_id + "]";
	}
}
