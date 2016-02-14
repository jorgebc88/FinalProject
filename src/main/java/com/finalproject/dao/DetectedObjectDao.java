package com.finalproject.dao;

import com.finalproject.model.DetectedObject;

import java.util.Date;
import java.util.List;

public interface DetectedObjectDao {
	public boolean addDetectedObject(DetectedObject detectedObject) throws Exception;

	public List<DetectedObject> getDetectedObjectList() throws Exception;

	public List<DetectedObject> getDetectedObjectListByCameraId(long cameraId);

	boolean deleteDetectedObjectBeforeDateByCameraId(Date date, long cameraId) throws Exception;

	public List<DetectedObject> findByDateAndCameraId(Date date, long cameraId) throws Exception;

	public List<DetectedObject> findByMonthAndCameraId(int month, long cameraId) throws Exception;

	public List<DetectedObject> findByYearAndCameraId(int Year, long cameraId) throws Exception;

	public List<DetectedObject> findByDatesBetweenAndCameraId(Date startDate, Date endDate, long cameraId) throws Exception;
}
