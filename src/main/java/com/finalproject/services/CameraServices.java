package com.finalproject.services;

import com.finalproject.model.Camera;

import java.util.List;

public interface CameraServices {
	boolean addCamera(Camera camera) throws Exception;

	Camera getCameraById(long id) throws Exception;

	List<Camera> getCameraList() throws Exception;

	boolean deleteCamera(long id) throws Exception;

	boolean modifyCamera(long id, boolean active) throws Exception;
}
