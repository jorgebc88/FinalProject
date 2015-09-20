package com.finalproject.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import com.finalproject.model.DetectedObject;

public class DetectedObjectDaoImpl implements DetectedObjectDao {
	
	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction tx = null;

	@Override
	public boolean addDetectedObject(DetectedObject detectedObject) throws Exception {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		session.save(detectedObject);
		tx.commit();
		session.close();
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
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
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
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		List<DetectedObject> detectedObjectList = session.createQuery("FROM DetectedObject WHERE date = :date")
				.setParameter("date", date)
				.list();
		tx.commit();
		session.close();
		return detectedObjectList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DetectedObject> findByMonth(int Month) throws Exception {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
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
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		List<DetectedObject> detectedObjectList = session.createQuery("FROM DetectedObject WHERE :year = EXTRACT(YEAR FROM Date) ")
				.setParameter("year", year)
				.list();
		tx.commit();
		session.close();
		return detectedObjectList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DetectedObject> findByDatesBetween(Date startDate, Date endDate) throws Exception {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		List<DetectedObject> detectedObjectList = session.createQuery("FROM DetectedObject WHERE date BETWEEN :startDate AND :endDate ")
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.list();
		tx.commit();
		session.close();
		return detectedObjectList;
	}

}
