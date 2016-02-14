package com.finalproject.services;

import com.finalproject.model.DetectedObject;

import java.util.Date;
import java.util.List;

public interface DetectedObjectServices {
	public boolean addDetectedObject(DetectedObject detectedObject) throws Exception;

	public List<DetectedObject> getDetectedObjectList() throws Exception;

	public List<DetectedObject> getDetectedObjectListByCameraId(long cameraId) throws Exception;

	public boolean deleteDetectedObjectBeforeDateByCameraId(Date date, long cameraId) throws Exception;

	public List<DetectedObject> findByDateAndCameraId(Date date, long cameraId) throws Exception;

	public List<DetectedObject> findByMonthAndCameraId(int Month, long cameraId) throws Exception;

	public List<DetectedObject> findByYearAndCameraId(int Year, long cameraId) throws Exception;

	public List<DetectedObject> findByDatesBetweenAndCameraId(Date startDate, Date endDate, long cameraId) throws Exception;

}
