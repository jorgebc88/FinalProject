package com.finalproject.services;

import com.finalproject.model.DetectedObject;

import java.util.Date;
import java.util.List;

public interface DetectedObjectServices {
	boolean addDetectedObject(DetectedObject detectedObject) throws Exception;

	List<DetectedObject> getDetectedObjectList() throws Exception;

	List<DetectedObject> getDetectedObjectListByCameraId(long cameraId) throws Exception;

	boolean deleteDetectedObjectBeforeDateByCameraId(Date date, long cameraId) throws Exception;

	List<DetectedObject> findByDateAndCameraId(Date date, long cameraId) throws Exception;

	List<DetectedObject> findByMonthAndCameraId(int Month, long cameraId) throws Exception;

	List<DetectedObject> findByYearAndCameraId(int Year, long cameraId) throws Exception;

	List<DetectedObject> findByDatesBetweenAndCameraId(Date startDate, Date endDate, long cameraId) throws Exception;

//------------------------------------------   RANKINGS   ------------------------------------------

	List<DetectedObject> allTimeDetectedObjectsRanking() throws Exception;

	List<DetectedObject> detectedObjectsRankingByYear(int year) throws Exception;

	List<DetectedObject> detectedObjectsRankingByYearAndMonth(int year, int month) throws Exception;

	List<DetectedObject> detectedObjectsRankingBetweenDates(Date startDate, Date endDate) throws Exception;

//------------------------------------------ Stats sorted by maximum values by camera ------------------------------------------

	List<DetectedObject> getPeakHoursByDaysOfTheWeekAndCamera(long cameraId) throws Exception;

	List<DetectedObject> getByHoursOfDayDetectedObjectsHistogram(int dayOfTheWeek, long camera_id) throws Exception;

	List<DetectedObject> getByDayOfTheWeekDetectedObjectsHistogram(long cameraId) throws Exception;

	List<DetectedObject> getByMonthOfTheYearDetectedObjectsHistogram(long cameraId) throws Exception;


}
