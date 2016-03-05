package com.finalproject.dao;

import com.finalproject.model.DetectedObject;

import java.util.Date;
import java.util.List;

public interface DetectedObjectDao {
	boolean addDetectedObject(DetectedObject detectedObject) throws Exception;

	List<DetectedObject> getDetectedObjectList() throws Exception;

	List<DetectedObject> getDetectedObjectListByCameraId(long cameraId);

	boolean deleteDetectedObjectBeforeDateByCameraId(Date date, long cameraId) throws Exception;

	List<DetectedObject> findByDateAndCameraId(Date date, long cameraId) throws Exception;

	List<DetectedObject> findByMonthAndCameraId(int month, long cameraId) throws Exception;

	List<DetectedObject> findByYearAndCameraId(int Year, long cameraId) throws Exception;

	List<DetectedObject> findByDatesBetweenAndCameraId(Date startDate, Date endDate, long cameraId) throws Exception;

//------------------------------------------   RANKINGS   ------------------------------------------

	List<DetectedObject> allTimeDetectedObjectsRanking() throws Exception;

	List<DetectedObject> detectedObjectsRankingByYear(int year) throws Exception;

	List<DetectedObject> detectedObjectsRankingByYearAndMonth(int year, int month) throws Exception;

	List<DetectedObject> detectedObjectsRankingBetweenDates(Date startDate, Date endDate) throws Exception;

//------------------------------------------ Stats sorted by maximum values by camera ------------------------------------------

	List<Object[]> getPeakHoursByDaysOfTheWeekAndCamera(long cameraId) throws Exception;

	List<Object[]> getByHoursOfDayDetectedObjectsHistogram(int dayOfTheWeek, long camera_id) throws Exception;

	List<Object[]> getByDayOfTheWeekDetectedObjectsHistogram(long cameraId) throws Exception;

	List<Object[]> getByMonthOfTheYearDetectedObjectsHistogram(long cameraId) throws Exception;

	List<Object[]> getByHoursOfDayDetectedObjectsAverageHistogram(int dayOfTheWeek, long camera_id) throws Exception;

	List<Object[]> getByDayOfTheWeekDetectedObjectsAverageHistogram(long cameraId) throws Exception;

	List<Object[]> getByMonthOfTheYearDetectedObjectsAverageHistogram(long cameraId) throws Exception;

}
