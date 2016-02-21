package com.finalproject.dao;

import com.finalproject.model.DetectedObject;
import com.finalproject.util.Utils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is the DAO that manages the transaction to the database for the Detected Objects
 */
@Component
public class DetectedObjectDaoImpl implements DetectedObjectDao {

	@Autowired
	SessionFactory sessionFactory;

	static final Logger LOGGER = Logger.getLogger(DetectedObjectDaoImpl.class);

	@Override
	public boolean addDetectedObject(DetectedObject detectedObject) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(detectedObject);
		tx.commit();
		session.close();
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DetectedObject> getDetectedObjectList() throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<DetectedObject> detectedObjectList = session.createCriteria(DetectedObject.class)
				.list();
		tx.commit();
		session.close();
		Utils.listSizeVerifier(detectedObjectList);

		return detectedObjectList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DetectedObject> getDetectedObjectListByCameraId(long cameraId) {
		Session session = sessionFactory.openSession();

		List<DetectedObject> detectedObjectList = session.createQuery("FROM DetectedObject WHERE camera_id = :camera_id")
				.setParameter("camera_id", cameraId)
				.list();
		session.flush();
		session.close();
		Utils.listSizeVerifier(detectedObjectList);
		return detectedObjectList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean deleteDetectedObjectBeforeDateByCameraId(Date date, long cameraId) throws Exception {
		Session session = sessionFactory.openSession();

		List<DetectedObject> detectedObjectList = session.createQuery("FROM DetectedObject WHERE date <= :date AND camera_id = :camera_id")
				.setParameter("date", date)
				.setParameter("camera_id", cameraId)
				.list();
		session.flush();
		session.close();
		LOGGER.info("The detected object created before: " + date.toString() + " for the camera: " + cameraId + " were deleted Successfully!");
		Utils.listSizeVerifier(detectedObjectList);
		return true;
	}

	@SuppressWarnings({"unchecked"})
	@Override
	public List<DetectedObject> findByDateAndCameraId(Date date, long cameraId) throws Exception {
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
		Utils.listSizeVerifier(detectedObjectList);
		return detectedObjectList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DetectedObject> findByMonthAndCameraId(int month, long cameraId) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<DetectedObject> detectedObjectList = session.createQuery("FROM DetectedObject WHERE camera_id = :camera_id AND :month = EXTRACT(MONTH FROM Date) ")
				.setParameter("camera_id", cameraId)
				.setParameter("month", month)
				.list();
		tx.commit();
		session.close();
		Utils.listSizeVerifier(detectedObjectList);
		return detectedObjectList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DetectedObject> findByYearAndCameraId(int year, long cameraId) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<DetectedObject> detectedObjectList = session.createQuery("FROM DetectedObject WHERE camera_id = :camera_id AND :year = EXTRACT(YEAR FROM Date) ")
				.setParameter("camera_id", cameraId)
				.setParameter("year", year)
				.list();
		tx.commit();
		session.close();
		Utils.listSizeVerifier(detectedObjectList);
		return detectedObjectList;
	}

	@SuppressWarnings({"unchecked", "deprecation"})
	@Override
	public List<DetectedObject> findByDatesBetweenAndCameraId(Date startDate, Date endDate, long cameraId) throws Exception {

		LOGGER.info("Find dates between: " + startDate + " - " + endDate);
		int startHour, startMinutes, endHour, endMinutes;
		startHour = startDate.getHours();
		startMinutes = startDate.getMinutes();
		endHour = endDate.getHours();
		endMinutes = endDate.getMinutes();

		String startHourMinutes, endHourMinutes;

		startHourMinutes = String.format("%02d", startHour) + String.format("%02d", startMinutes);
		endHourMinutes = String.format("%02d", endHour) + String.format("%02d", endMinutes);

		startDate.setHours(0);
		startDate.setMinutes(0);
		endDate.setHours(23);
		endDate.setMinutes(59);

		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		List<DetectedObject> detectedObjectList = session.createQuery("FROM DetectedObject WHERE camera_id = :camera_id AND (EXTRACT(HOUR_MINUTE FROM date) BETWEEN :start_hour_minute AND :end_hour_minute) AND date >= :startDate AND date <= :endDate ")
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("start_hour_minute", Integer.parseInt(startHourMinutes))
				.setParameter("end_hour_minute", Integer.parseInt(endHourMinutes))
				.setParameter("camera_id", cameraId)
				.list();
		tx.commit();
		session.close();

		LOGGER.info("Number of detected objects: " + detectedObjectList.size());
		Utils.listSizeVerifier(detectedObjectList);
		return detectedObjectList;
	}

	@SuppressWarnings({"unchecked", "deprecation"})
	@Override
	public List<DetectedObject> allTimeDetectedObjectsRanking() throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction;
		List<DetectedObject> allTimeRanking;
		StringBuilder hql = new StringBuilder("SELECT camera_id, COUNT(*) AS num FROM DetectedObject")
				.append(" GROUP BY camera_id")
				.append(" ORDER BY num DESC");
		allTimeRanking = session.createQuery(hql.toString()).list();

		try {
			transaction = session.beginTransaction();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.close();
			throw new Exception();
		}
		Utils.listSizeVerifier(allTimeRanking);
		return allTimeRanking.isEmpty() ? new ArrayList<DetectedObject>() : allTimeRanking;
	}

	@SuppressWarnings({"unchecked", "deprecation"})
	@Override
	public List<DetectedObject> detectedObjectsRankingByYear(int year) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction;
		List<DetectedObject> rankingByYear;

		StringBuilder hql = new StringBuilder("SELECT camera_id, COUNT(*) AS num FROM DetectedObject")
				.append(" WHERE EXTRACT(YEAR FROM date) = :year")
				.append(" GROUP BY camera_id")
				.append(" ORDER BY num DESC");

		rankingByYear = session.createQuery(hql.toString())
				.setParameter("year",year)
				.list();

		try {
			transaction = session.beginTransaction();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.close();
			throw new Exception();
		}
		Utils.listSizeVerifier(rankingByYear);
		return rankingByYear;
	}

	@SuppressWarnings({"unchecked", "deprecation"})
	@Override
	public List<DetectedObject> detectedObjectsRankingByYearAndMonth(int year, int month) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction;
		List<DetectedObject> rankingByYearAndMonth;

		StringBuilder hql = new StringBuilder("SELECT camera_id, COUNT(*) AS num FROM DetectedObject");
		hql.append(" WHERE EXTRACT(YEAR FROM date) = :year")
				.append(" AND EXTRACT(MONTH FROM date) = :month")
				.append(" GROUP BY camera_id")
				.append(" ORDER BY num DESC");

		rankingByYearAndMonth = session.createQuery(hql.toString())
				.setParameter("year", year)
				.setParameter("month", month)
				.list();

		try {
			transaction = session.beginTransaction();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.close();
			throw new Exception();
		}
		Utils.listSizeVerifier(rankingByYearAndMonth);
		return rankingByYearAndMonth;
	}

	@SuppressWarnings({"unchecked", "deprecation"})
	@Override
	public List<DetectedObject> detectedObjectsRankingBetweenDates(Date startDate, Date endDate) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction;
		List<DetectedObject> rankingBetweenDates;

		int startHour, startMinutes, endHour, endMinutes;
		startHour = startDate.getHours();
		startMinutes = startDate.getMinutes();
		endHour = endDate.getHours();
		endMinutes = endDate.getMinutes();

		String startHourMinutes, endHourMinutes;

		startHourMinutes = String.format("%02d", startHour) + String.format("%02d", startMinutes);
		endHourMinutes = String.format("%02d", endHour) + String.format("%02d", endMinutes);

		startDate.setHours(0);
		startDate.setMinutes(0);
		endDate.setHours(23);
		endDate.setMinutes(59);

		StringBuilder hql = new StringBuilder("SELECT camera_id, COUNT(*) AS num FROM DetectedObject");
		hql.append(" WHERE (EXTRACT(HOUR_MINUTE FROM date)) BETWEEN :start_hour_minute AND :end_hour_minute)")
				.append(" AND date >= :startDate")
				.append(" AND date <= :endDate")
				.append(" GROUP BY camera_id")
				.append(" ORDER BY num DESC");

		rankingBetweenDates = session.createQuery(hql.toString())
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("start_hour_minute", Integer.parseInt(startHourMinutes))
				.setParameter("end_hour_minute", Integer.parseInt(endHourMinutes))
				.list();
		try {
			transaction = session.beginTransaction();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.close();
			throw new Exception();
		}
		return rankingBetweenDates;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DetectedObject> getByHoursOfDayDetectedObjectsHistogram(int dayOfTheWeek) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction;
		List<DetectedObject> byHoursHistogram;

		StringBuilder hql = new StringBuilder("SELECT EXTRACT(HOUR FROM date), COUNT(*) AS num FROM DetectedObject")
				.append(" WHERE DAYOFWEEK(date) = :dayOfTheWeek")
				.append(" GROUP BY ( EXTRACT(HOUR FROM date) )")
				.append(" ORDER BY num DESC");

		byHoursHistogram = session.createQuery(hql.toString())
				.setParameter("dayOfTheWeek",dayOfTheWeek)
				.list();

		try {
			transaction = session.beginTransaction();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.close();
			throw new Exception();
		}
		Utils.listSizeVerifier(byHoursHistogram);
		return byHoursHistogram;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<DetectedObject> getPeakHoursByDaysOfTheWeekAndCamera(long cameraId) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction;
		List<DetectedObject> peakHours;

		StringBuilder hql = new StringBuilder("SELECT DISTINCT camera_id, days, hours, MAX(CANT) AS PeakHour FROM")
				.append(" (SELECT d.camera_id, dayofweek(d.date) AS days, HOUR(d.date) AS hours ,COUNT(*) AS CANT FROM detected_object AS d group by hours order by CANT DESC) AS RESULT")
				.append(" WHERE camera_id = :camera_id")
				.append(" GROUP BY days")
				.append(" ORDER BY PeakHour DESC");

		LOGGER.info(hql.toString());

		peakHours = session.createSQLQuery(hql.toString())
				.setParameter("camera_id", cameraId)
				.list();

		try {
			transaction = session.beginTransaction();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.close();
			throw new Exception();
		}
		Utils.listSizeVerifier(peakHours);
		return peakHours;
	}


}
