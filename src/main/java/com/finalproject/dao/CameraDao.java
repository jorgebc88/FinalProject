package com.finalproject.dao;

import com.finalproject.model.Camera;

import java.util.List;

public interface CameraDao {
	public boolean addCamera(Camera camera) throws Exception;

	public Camera getCameraById(long id) throws Exception;

	public List<Camera> getCameraList() throws Exception;

	public boolean deleteCamera(long id) throws Exception;

}
