package com.finalproject.services;

import com.finalproject.dao.DetectedObjectDao;
import com.finalproject.model.DetectedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class DetectedObjectServicesImpl implements DetectedObjectServices {
	@Autowired
	DetectedObjectDao detectedObjectDao;

	@Override
	public boolean addDetectedObject(DetectedObject detectedObject)
			throws Exception {
		return this.detectedObjectDao.addDetectedObject(detectedObject);
	}

	@Override
	public List<DetectedObject> getDetectedObjectList() throws Exception {
		return this.detectedObjectDao.getDetectedObjectList();
	}

	@Override
	public List<DetectedObject> getDetectedObjectListByCameraId(long cameraId) throws Exception {
		return this.detectedObjectDao.getDetectedObjectListByCameraId(cameraId);
	}

	@Override
	public boolean deleteDetectedObjectBeforeDateByCameraId(Date date, long cameraId) throws Exception {
		return this.detectedObjectDao.deleteDetectedObjectBeforeDateByCameraId(date, cameraId);
	}

	@Override
	public List<DetectedObject> findByDateAndCameraId(Date date, long cameraId) throws Exception {
		return this.detectedObjectDao.findByDateAndCameraId(date, cameraId);
	}

	@Override
	public List<DetectedObject> findByMonthAndCameraId(int Month, long cameraId) throws Exception {
		return this.detectedObjectDao.findByMonthAndCameraId(Month, cameraId);
	}

	@Override
	public List<DetectedObject> findByYearAndCameraId(int Year, long cameraId) throws Exception {
		return this.detectedObjectDao.findByYearAndCameraId(Year, cameraId);
	}

	@Override
	public List<DetectedObject> findByDatesBetweenAndCameraId(Date startDate, Date endDate, long cameraId)
			throws Exception {
		return this.detectedObjectDao.findByDatesBetweenAndCameraId(startDate, endDate, cameraId);
	}
}
