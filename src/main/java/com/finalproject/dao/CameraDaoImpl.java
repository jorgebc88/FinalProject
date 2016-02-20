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

	Transaction transaction = null;

	static final Logger LOGGER = Logger.getLogger(CameraDaoImpl.class);

	@Override
	public boolean addCamera(Camera camera) throws Exception {
		Session session = sessionFactory.openSession();

		try {
			this.transaction = session.beginTransaction();
			session.save(camera);
			this.transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		LOGGER.info("Camara guardada: " + camera.toString());
		return true;
	}

	@Override
	public Camera getCameraById(long id) throws Exception {
		Session session = sessionFactory.openSession();
		Camera camera = null;

		try {
			this.transaction = session.beginTransaction();
			camera = (Camera) session.get(Camera.class, id);
			this.transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return camera;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Camera> getCameraList() throws Exception {
		Session session = sessionFactory.openSession();
		List<Camera> cameraList = null;

		try {
			this.transaction = session.beginTransaction();
			cameraList = session.createCriteria(Camera.class).list();
			this.transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return cameraList;
	}

	@Override
	public boolean deleteCamera(long id) throws Exception {
		Session session = sessionFactory.openSession();

		Camera o = (Camera) session.load(Camera.class, id);
		session.delete(o);
		session.flush();
		session.close();
		return true;
	}

	@Override
	public boolean modifyCamera(long id, boolean active) throws Exception {
		Session session = sessionFactory.openSession();

		try {
			this.transaction = session.beginTransaction();
			Camera camera =	(Camera) session.get(Camera.class, id);
			camera.setActive(active);
			session.update(camera);
			this.transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Camera> getCameraRankingAllTime() throws Exception {
		Session session = sessionFactory.openSession();
		List<Camera> allTimeRanking;
		StringBuilder hql = new StringBuilder("SELECT c.id, COUNT(*) num FROM camera c JOIN detected_object d ON c.id = d.camera_id GROUP BY c.id ORDER BY num DESC");
		allTimeRanking = session.createQuery(hql.toString()).list();

		try {
			this.transaction = session.beginTransaction();
			this.transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		Utils.listSizeVerifier(allTimeRanking);
		return allTimeRanking.isEmpty() ? new ArrayList<Camera>() : allTimeRanking;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Camera> getCameraRankingByYear(Date year) throws Exception {
		Session session = sessionFactory.openSession();
		List<Camera> rankingByYear;
		StringBuilder hql = new StringBuilder("SELECT c.id, COUNT(*) num FROM camera c JOIN detected_object d ON c.id = d.camera_id WHERE EXTRACT(YEAR FROM d.date) = :year  GROUP BY c.id ORDER BY num DESC");
		rankingByYear = session.createQuery(hql.toString())
				.setParameter("year",year)
				.list();

		try {
			this.transaction = session.beginTransaction();
			this.transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
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
