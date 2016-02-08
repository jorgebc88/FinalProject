package com.finalproject.services;

import java.util.List;

import com.finalproject.model.Camera;

public interface CameraServices {
	public boolean addCamera(Camera camera) throws Exception;
	public Camera getCameraById(long id) throws Exception;
	public List<Camera> getCameraList() throws Exception;
	public boolean deleteCamera(long id) throws Exception;

}
