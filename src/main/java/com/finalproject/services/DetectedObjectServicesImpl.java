package com.finalproject.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.finalproject.dao.DetectedObjectDao;
import com.finalproject.model.DetectedObject;

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
	public boolean deleteDetectedObjectBeforeDate(Date date) throws Exception{
		return this.detectedObjectDao.deleteDetectedObjectBeforeDate(date);
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
