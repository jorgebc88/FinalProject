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

//------------------------------------------   RANKINGS   ------------------------------------------

	@Override
	public List<DetectedObject> allTimeDetectedObjectsRanking() throws Exception {
		return this.detectedObjectDao.allTimeDetectedObjectsRanking();
	}

	@Override
	public List<DetectedObject> detectedObjectsRankingByYear(int year) throws Exception {
		return this.detectedObjectDao.detectedObjectsRankingByYear(year);
	}

	@Override
	public List<DetectedObject> detectedObjectsRankingByYearAndMonth(int year, int month) throws Exception {
		return this.detectedObjectDao.detectedObjectsRankingByYearAndMonth(year, month);
	}

	@Override
	public List<DetectedObject> detectedObjectsRankingBetweenDates(Date startDate, Date endDate) throws Exception {
		return this.detectedObjectDao.detectedObjectsRankingBetweenDates(startDate, endDate);
	}

//------------------------------------------ Stats sorted by maximum values by camera ------------------------------------------

	@Override
	public List<DetectedObject> getPeakHoursByDaysOfTheWeekAndCamera(long cameraId) throws Exception {
		return this.detectedObjectDao.getPeakHoursByDaysOfTheWeekAndCamera(cameraId);
	}

	@Override
	public List<DetectedObject> getByHoursOfDayDetectedObjectsHistogram(int dayOfTheWeek, long camera_id) throws Exception {
		return this.detectedObjectDao.getByHoursOfDayDetectedObjectsHistogram(dayOfTheWeek, camera_id);
	}

	@Override
	public List<DetectedObject> getByDayOfTheWeekDetectedObjectsHistogram(long camera_id) throws Exception {
		return this.detectedObjectDao.getByDayOfTheWeekDetectedObjectsHistogram(camera_id);
	}

	@Override
	public List<DetectedObject> getByMonthOfTheYearDetectedObjectsHistogram(long cameraId) throws Exception {
		return this.detectedObjectDao.getByMonthOfTheYearDetectedObjectsHistogram(cameraId);
	}
}
