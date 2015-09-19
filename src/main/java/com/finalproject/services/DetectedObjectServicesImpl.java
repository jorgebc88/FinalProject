package com.finalproject.services;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.finalproject.dao.DetectedObjectDao;
import com.finalproject.model.DetectedObject;

public class DetectedObjectServicesImpl implements DetectedObjectServices {
	@Autowired
	DetectedObjectDao detectedObjectDao;

	@Override
	public boolean addDetectedObject(DetectedObject detectedObject)
			throws Exception {
		return this.detectedObjectDao.addDetectedObject(detectedObject);
	}

	@Override
	public DetectedObject getDetectedObjectById(long id) throws Exception {
		return this.detectedObjectDao.getDetectedObjectById(id);
	}

	@Override
	public List<DetectedObject> getDetectedObjectList() throws Exception {
		return this.detectedObjectDao.getDetectedObjectList();
	}

	@Override
	public boolean deleteDetectedObject(long id) throws Exception {
		return this.detectedObjectDao.deleteDetectedObject(id);
	}

	@Override
	public List<DetectedObject> findByDate(Date date) throws Exception {
		return this.detectedObjectDao.findByDate(date);
	}

	@Override
	public List<DetectedObject> findByMonth(int Month) throws Exception {
		return this.detectedObjectDao.findByMonth(Month);
	}

	@Override
	public List<DetectedObject> findByYear(int Year) throws Exception {
		return this.detectedObjectDao.findByYear(Year);
	}

	@Override
	public List<DetectedObject> findByDatesBetween(Date startDate, Date endDate)
			throws Exception {
		return this.detectedObjectDao.findByDatesBetween(startDate, endDate);
	}
}
