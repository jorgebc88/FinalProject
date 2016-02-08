package com.finalproject.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.finalproject.model.DetectedObject;

@Component
public class DetectedObjectDaoImpl implements DetectedObjectDao {
	
	@Autowired
	SessionFactory sessionFactory;

	Transaction tx = null;

	static final Logger LOGGER = Logger.getLogger(DetectedObjectDaoImpl.class);
	
	@Override
	public boolean addDetectedObject(DetectedObject detectedObject) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(detectedObject);
		tx.commit();
		session.close();
		LOGGER.info("Objecto guardado: " + detectedObject.toString() );
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DetectedObject> getDetectedObjectList() throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<DetectedObject> detectedObject = session.createCriteria(DetectedObject.class)
				.list();
		tx.commit();
		session.close();
		return detectedObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean deleteDetectedObjectBeforeDate(Date date) throws Exception {
		Session session = sessionFactory.openSession();
		
		List<DetectedObject> detectedObjectList = session.createQuery("FROM DetectedObject WHERE date <= :date ")
				.setParameter("date", date)
				.list();
		session.delete(detectedObjectList);
		session.flush();
		session.close();
		return true;
	}

	@SuppressWarnings({"unchecked" })
	@Override
	public List<DetectedObject> findByDate(Date date) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		int year = date.getYear() + 1900;
		int month = date.getMonth() + 1;
		int day = date.getDate();
		int hour = date.getHours();
		int minutes = date.getMinutes();
		
		LOGGER.info("Year: " + year + " Month: " + month + " Day: " + day + " Hour: " + hour + " Minute: " + minutes);
		List<DetectedObject> detectedObjectList = session.createQuery("FROM DetectedObject WHERE :year = EXTRACT(YEAR FROM Date) AND :month = EXTRACT(MONTH FROM Date) AND :day = EXTRACT(DAY FROM Date) "
				+ "AND :hour = EXTRACT(HOUR FROM Date) AND :minute = EXTRACT(MINUTE FROM Date)")
				.setParameter("year", year)
				.setParameter("month", month)
				.setParameter("day", day)
				.setParameter("hour", hour)
				.setParameter("minute", minutes)
				.list();
		tx.commit();
		session.close();
		return detectedObjectList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DetectedObject> findByMonth(int Month) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<DetectedObject> detectedObjectList = session.createQuery("FROM DetectedObject WHERE :month = EXTRACT(MONTH FROM Date) ")
				.setParameter("month", Month)
				.list();
		tx.commit();
		session.close();
		return detectedObjectList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DetectedObject> findByYear(int year) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<DetectedObject> detectedObjectList = session.createQuery("FROM DetectedObject WHERE :year = EXTRACT(YEAR FROM Date) ")
				.setParameter("year", year)
				.list();
		tx.commit();
		session.close();
		return detectedObjectList;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<DetectedObject> findByDatesBetween(Date startDate, Date endDate) throws Exception {
		
		LOGGER.info("Find dates between: " + startDate + " - " + endDate);
		int startHour, startMinutes, endHour, endMinutes;
		startHour = startDate.getHours();
		startMinutes = startDate.getMinutes();
		endHour = endDate.getHours();
		endMinutes = endDate.getMinutes();

		String startHourMinutes, endHourMinutes;
		
		startHourMinutes = String.format("%02d", startHour) + String.format("%02d", startMinutes);
		endHourMinutes = String.format("%02d", endHour) + String.format("%02d", endMinutes);;
		
		startDate.setHours(0);
		startDate.setMinutes(0);
		endDate.setHours(23);
		endDate.setMinutes(59);

		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		List<DetectedObject> detectedObjectList = session.createQuery("FROM DetectedObject WHERE (EXTRACT(HOUR_MINUTE FROM date) BETWEEN :start_hour_minute AND :end_hour_minute) AND date >= :startDate AND date <= :endDate ")
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("start_hour_minute", Integer.parseInt(startHourMinutes))
				.setParameter("end_hour_minute", Integer.parseInt(endHourMinutes))
				.list();
		tx.commit();
		session.close();
		
		LOGGER.info("Number of detected objects: " + detectedObjectList.size());
		return detectedObjectList;
	}
	

}
