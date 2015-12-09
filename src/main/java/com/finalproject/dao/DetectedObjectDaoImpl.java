package com.finalproject.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.finalproject.controller.DetectedObjectController;
import com.finalproject.model.DetectedObject;
import com.finalproject.util.SessionHibernate;

public class DetectedObjectDaoImpl implements DetectedObjectDao {
	
	@Autowired
	SessionFactory sessionFactory;

	Transaction tx = null;

	static final Logger logger = Logger.getLogger(DetectedObjectDaoImpl.class);
	
	@Override
	public boolean addDetectedObject(DetectedObject detectedObject) throws Exception {
		logger.info("Se esta por guardar un objeto detectado!" );
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(detectedObject);
		tx.commit();
		session.close();
		logger.info("Objecto guardado!" );
		return true;
	}

	@Override
	public DetectedObject getDetectedObjectById(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public boolean deleteDetectedObject(long id) throws Exception {
		// TODO Auto-generated method stub
		return false;
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
		
		logger.info("Year: " + year + " Month: " + month + " Day: " + day + " Hour: " + hour + " Minute: " + minutes);
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
		
		logger.info("Find dates between: " + startDate + " - " + endDate);
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
		
		logger.info(startDate);
		logger.info(endDate);
		logger.info(Integer.parseInt(startHourMinutes));
		logger.info(Integer.parseInt(endHourMinutes));

		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		List<DetectedObject> detectedObjectList = session.createQuery("FROM DetectedObject WHERE (EXTRACT(HOUR_MINUTE FROM date) BETWEEN :start_hour_minute AND :end_hour_minute) AND date >= :startDate AND date <= :endDate ")
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("start_hour_minute", Integer.parseInt(startHourMinutes))
				.setParameter("end_hour_minute", Integer.parseInt(endHourMinutes))
				.list();

/*		Criteria criteria = session.createCriteria(DetectedObject.class)
				   .add(Restrictions.between("date", startDate, endDate));
		List<DetectedObject> detectedObjectList = criteria.list();
*/		
		tx.commit();
		session.close();
		
		logger.info("Number of detected objects: " + detectedObjectList.size());
		return detectedObjectList;
	}
	

}
