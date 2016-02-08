package com.finalproject.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.finalproject.dao.CameraDao;
import com.finalproject.model.Camera;

@Component
public class CameraServicesImpl implements CameraServices{
	@Autowired
	CameraDao cameraDao;
	
	@Override
	public boolean addCamera(Camera camera) throws Exception {
		return this.cameraDao.addCamera(camera);
	}

	@Override
	public Camera getCameraById(long id) throws Exception {
		return this.cameraDao.getCameraById(id);
		}

	@Override
	public List<Camera> getCameraList() throws Exception {
		return this.cameraDao.getCameraList();
	}

	@Override
	public boolean deleteCamera(long id) throws Exception {
		return this.cameraDao.deleteCamera(id);
	}

}
