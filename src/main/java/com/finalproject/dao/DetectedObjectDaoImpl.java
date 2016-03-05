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

import java.math.BigInteger;
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
		List<DetectedObject> detectedObjectList = session.createCriteria(DetectedObject.class)
				.list();
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
		session.close();
		Utils.listSizeVerifier(detectedObjectList);
		return detectedObjectList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DetectedObject> findByMonthAndCameraId(int month, long cameraId) throws Exception {
		Session session = sessionFactory.openSession();
		List<DetectedObject> detectedObjectList = session.createQuery("FROM DetectedObject WHERE camera_id = :camera_id AND :month = EXTRACT(MONTH FROM Date) ")
				.setParameter("camera_id", cameraId)
				.setParameter("month", month)
				.list();
		session.close();
		Utils.listSizeVerifier(detectedObjectList);
		return detectedObjectList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DetectedObject> findByYearAndCameraId(int year, long cameraId) throws Exception {
		Session session = sessionFactory.openSession();
		List<DetectedObject> detectedObjectList = session.createQuery("FROM DetectedObject WHERE camera_id = :camera_id AND :year = EXTRACT(YEAR FROM Date) ")
				.setParameter("camera_id", cameraId)
				.setParameter("year", year)
				.list();
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

		List<DetectedObject> detectedObjectList = session.createQuery("FROM DetectedObject WHERE camera_id = :camera_id AND (EXTRACT(HOUR_MINUTE FROM date) BETWEEN :start_hour_minute AND :end_hour_minute) AND date >= :startDate AND date <= :endDate ")
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("start_hour_minute", Integer.parseInt(startHourMinutes))
				.setParameter("end_hour_minute", Integer.parseInt(endHourMinutes))
				.setParameter("camera_id", cameraId)
				.list();
		session.close();

		LOGGER.info("Number of detected objects: " + detectedObjectList.size());
		Utils.listSizeVerifier(detectedObjectList);
		return detectedObjectList;
	}


	//----------------------------------------------------------------RANKINKGS --------------------------------------------------------------------------
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
		session.close();
		Utils.listSizeVerifier(allTimeRanking);
		return allTimeRanking.isEmpty() ? new ArrayList<DetectedObject>() : allTimeRanking;
	}

	@SuppressWarnings({"unchecked", "deprecation"})
	@Override
	public List<DetectedObject> detectedObjectsRankingByYear(int year) throws Exception {
		Session session = sessionFactory.openSession();
		List<DetectedObject> rankingByYear;

		StringBuilder hql = new StringBuilder("SELECT camera_id, COUNT(*) AS num FROM DetectedObject")
				.append(" WHERE EXTRACT(YEAR FROM date) = :year")
				.append(" GROUP BY camera_id")
				.append(" ORDER BY num DESC");

		rankingByYear = session.createQuery(hql.toString())
				.setParameter("year",year)
				.list();

		session.close();
		Utils.listSizeVerifier(rankingByYear);
		return rankingByYear;
	}

	@SuppressWarnings({"unchecked", "deprecation"})
	@Override
	public List<DetectedObject> detectedObjectsRankingByYearAndMonth(int year, int month) throws Exception {
		Session session = sessionFactory.openSession();
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

		session.close();
		Utils.listSizeVerifier(rankingByYearAndMonth);
		return rankingByYearAndMonth;
	}

	@SuppressWarnings({"unchecked", "deprecation"})
	@Override
	public List<DetectedObject> detectedObjectsRankingBetweenDates(Date startDate, Date endDate) throws Exception {
		Session session = sessionFactory.openSession();
		List<DetectedObject> rankingBetweenDates;

		startDate.setHours(0);
		startDate.setMinutes(0);
		endDate.setHours(23);
		endDate.setMinutes(59);

		StringBuilder hql = new StringBuilder("SELECT camera_id, COUNT(*) AS num FROM DetectedObject");
		hql.append(" WHERE date >= :startDate")
				.append(" AND date <= :endDate")
				.append(" GROUP BY camera_id")
				.append(" ORDER BY num DESC");

		rankingBetweenDates = session.createQuery(hql.toString())
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.list();

		session.close();
		Utils.listSizeVerifier(rankingBetweenDates);
		return rankingBetweenDates;
	}

//------------------------------------------ Stats sorted by maximum values by camera ------------------------------------------

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getPeakHoursByDaysOfTheWeekAndCamera(long cameraId) throws Exception {
		Session session = sessionFactory.openSession();
		List<Object[]> peakHours;

		StringBuilder hql = new StringBuilder("SELECT DISTINCT days, hours, MAX(cant) FROM")
				.append(" (SELECT d.camera_id, dayofweek(d.date) AS days, HOUR(d.date) AS hours ,COUNT(*)")
				.append(" AS CANT FROM detected_object AS d  where camera_id = :camera_id group by days,hours order by days,CANT DESC)")
				.append(" AS RESULT GROUP BY days");

		peakHours = session.createSQLQuery(hql.toString())
				.setParameter("camera_id", cameraId)
				.list();

		session.close();
		Utils.listSizeVerifier(peakHours);
		return peakHours;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getByHoursOfDayDetectedObjectsHistogram(int dayOfTheWeek, long camera_id) throws Exception {
		Session session = sessionFactory.openSession();
		List<Object[]> byHoursHistogram;

		StringBuilder hql = new StringBuilder("SELECT EXTRACT(HOUR FROM date), COUNT(*) AS num FROM DetectedObject")
				.append(" WHERE DAYOFWEEK(date) = :dayOfTheWeek")
				.append(" AND camera_id = :camera_id")
				.append(" GROUP BY ( EXTRACT(HOUR FROM date) )");

		byHoursHistogram = session.createQuery(hql.toString())
				.setParameter("dayOfTheWeek", dayOfTheWeek)
				.setParameter("camera_id", camera_id)
				.list();

		session.close();
		Utils.listSizeVerifier(byHoursHistogram);
		return byHoursHistogram;
	}

	@Override
	public List<Object[]> getByDayOfTheWeekDetectedObjectsHistogram(long cameraId) throws Exception {
		Session session = sessionFactory.openSession();
		List<Object[]> ByDayOfTheWeekDetectedObjectsHistogram;

		StringBuilder hql = new StringBuilder("SELECT dayofweek(d.date)	AS days ,COUNT(*) AS CANT FROM detected_object AS d WHERE camera_id = :camera_id group by days" );

		ByDayOfTheWeekDetectedObjectsHistogram = session.createSQLQuery(hql.toString())
				.setParameter("camera_id", cameraId)
				.list();

		session.close();
		Utils.listSizeVerifier(ByDayOfTheWeekDetectedObjectsHistogram);
		return ByDayOfTheWeekDetectedObjectsHistogram;
	}

	@Override
	public List<Object[]> getByMonthOfTheYearDetectedObjectsHistogram(long cameraId) throws Exception {
		Session session = sessionFactory.openSession();
		List<Object[]> ByMonthOfTheYearDetectedObjectsHistogram;

		StringBuilder hql = new StringBuilder("SELECT MONTH(d.date) AS months ,COUNT(*) AS CANT FROM detected_object AS d WHERE camera_id = :camera_id group by months");

		ByMonthOfTheYearDetectedObjectsHistogram = session.createSQLQuery(hql.toString())
				.setParameter("camera_id", cameraId)
				.list();

		session.close();
		Utils.listSizeVerifier(ByMonthOfTheYearDetectedObjectsHistogram);
		return ByMonthOfTheYearDetectedObjectsHistogram;
	}

	//------------------------------------------ Stats sorted by average values by camera ------------------------------------------
	@Override
	public List<Object[]> getByHoursOfDayDetectedObjectsAverageHistogram(int dayOfTheWeek, long camera_id) throws Exception {
		Session session = sessionFactory.openSession();
		List<Object[]> byHoursOfDayDetectedObjectsAverageHistogram = this.getByHoursOfDayDetectedObjectsHistogram(dayOfTheWeek, camera_id);
		List<Object[]> historicalHoursOfDayQuantity;
		StringBuilder hql = new StringBuilder("SELECT days, count(*) as Cant FROM ")
				.append("(SELECT dayofweek(d.date) AS days ,COUNT(*) AS CANT FROM detected_object AS d WHERE camera_id = :camera_id group by year(d.date), month(d.date),day(d.date)) as subSet ")
				.append(" WHERE subSet.days = :dayOfTheWeek  GROUP BY days");

		historicalHoursOfDayQuantity = session.createSQLQuery(hql.toString())
				.setParameter("dayOfTheWeek", dayOfTheWeek)
				.setParameter("camera_id", camera_id)
				.list();

		session.close();
		List<Object[]> ByHoursOfDayDetectedObjectsAverageHistogram = Utils.calculateAverage(byHoursOfDayDetectedObjectsAverageHistogram, historicalHoursOfDayQuantity.get(0));

		return ByHoursOfDayDetectedObjectsAverageHistogram;
	}

	@Override
	public List<Object[]> getByDayOfTheWeekDetectedObjectsAverageHistogram(long cameraId) throws Exception {
		Session session = sessionFactory.openSession();
		List<Object[]> byDayOfTheWeekDetectedObjectsHistogram = this.getByDayOfTheWeekDetectedObjectsHistogram(cameraId);
		List<Object[]> historicalDayOfTheWeekQuantity;
		StringBuilder hql = new StringBuilder("SELECT days, count(*) as Cant from (SELECT dayofweek(d.date) AS days ,COUNT(*) AS CANT FROM detected_object AS d WHERE camera_id = :camera_id group by year(d.date), month(d.date),day(d.date)) as subSet group by days");

		historicalDayOfTheWeekQuantity = session.createSQLQuery(hql.toString())
				.setParameter("camera_id", cameraId)
				.list();

		session.close();
		List<Object[]> ByDayOfTheWeekDetectedObjectsAverageHistogram = Utils.calculateAverage(byDayOfTheWeekDetectedObjectsHistogram, historicalDayOfTheWeekQuantity);

		return ByDayOfTheWeekDetectedObjectsAverageHistogram;
	}

	@Override
	public List<Object[]> getByMonthOfTheYearDetectedObjectsAverageHistogram(long cameraId) throws Exception {
		Session session = sessionFactory.openSession();
		List<Object[]> ByMonthOfTheYearDetectedObjectsHistogram = this.getByMonthOfTheYearDetectedObjectsHistogram(cameraId);
		List<Object[]> historicalMonthQuantity;
		StringBuilder hql = new StringBuilder("SELECT months, count(*) as Cant from (SELECT MONTH(d.date) AS months, year(d.date) AS years FROM detected_object AS d WHERE camera_id = :camera_id group by years,months) as subSet group by months ");

		historicalMonthQuantity = session.createSQLQuery(hql.toString())
				.setParameter("camera_id", cameraId)
				.list();

		session.close();
		List<Object[]> ByMonthOfTheYearDetectedObjectsAverageHistogram = Utils.calculateAverage(ByMonthOfTheYearDetectedObjectsHistogram, historicalMonthQuantity);

		return ByMonthOfTheYearDetectedObjectsAverageHistogram;
	}

}
