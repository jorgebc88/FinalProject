package com.finalproject.dao;

import com.finalproject.model.Camera;
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

@Component
public class CameraDaoImpl implements CameraDao {

	@Autowired
	SessionFactory sessionFactory;


	static final Logger LOGGER = Logger.getLogger(CameraDaoImpl.class);

	@Override
	public boolean addCamera(Camera camera) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction;

		try {
			transaction = session.beginTransaction();
			session.save(camera);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.close();
			throw new Exception();
		} finally {

		}
		LOGGER.info("Camara guardada: " + camera.toString());
		return true;
	}

	@Override
	public Camera getCameraById(long id) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction;
		Camera camera = null;

		try {
			transaction = session.beginTransaction();
			camera = (Camera) session.get(Camera.class, id);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.flush();
			session.close();
			throw new Exception();
		}
		return camera;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Camera> getCameraList() throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction;
		List<Camera> cameraList = null;

		try {
			transaction = session.beginTransaction();
			cameraList = session.createCriteria(Camera.class).list();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.close();
			throw new Exception();
		}
		return cameraList;
	}

	@Override
	public boolean deleteCamera(long id) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction;

		Camera o = (Camera) session.load(Camera.class, id);
		session.delete(o);
		session.flush();
		session.close();
		return true;
	}

	@Override
	public boolean modifyCamera(long id, boolean active) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction;

		try {
			transaction = session.beginTransaction();
			Camera camera =	(Camera) session.get(Camera.class, id);
			camera.setActive(active);
			session.update(camera);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.close();
			throw new Exception();
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Camera> getCameraRankingAllTime() throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction;
		List<Camera> allTimeRanking;
		StringBuilder hql = new StringBuilder("SELECT c.id, COUNT(*) num FROM camera c JOIN detected_object d ON c.id = d.camera_id GROUP BY c.id ORDER BY num DESC");
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
		return allTimeRanking.isEmpty() ? new ArrayList<Camera>() : allTimeRanking;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Camera> getCameraRankingByYear(Date year) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction;
		List<Camera> rankingByYear;
		StringBuilder hql = new StringBuilder("SELECT c.id, COUNT(*) num FROM camera c JOIN detected_object d ON c.id = d.camera_id WHERE EXTRACT(YEAR FROM d.date) = :year  GROUP BY c.id ORDER BY num DESC");
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

	@Override
	public List<Camera> getCameraRankingByMonth(Date month) throws Exception {
		return null;
	}

	@Override
	public List<Camera> getCameraRankingByDay(Date day) throws Exception {
		return null;
	}

	@Override
	public List<Camera> getCameraRankingBetweenDates(Date startDate, Date endDate) throws Exception {
		return null;
	}
}
