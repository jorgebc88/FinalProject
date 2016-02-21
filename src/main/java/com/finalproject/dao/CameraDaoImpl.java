package com.finalproject.dao;

import com.finalproject.model.Camera;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This is the DAO that manages the transaction to the database for the Cameras
 */
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
		}
		LOGGER.info("The camera: " + camera.toString() + " was created successfully!");
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

		Camera camera = (Camera) session.load(Camera.class, id);
		session.delete(camera);
		session.flush();
		session.close();
		LOGGER.info("The camera: " + camera.toString() + " was deleted successfully!");
		return true;
	}

	@Override
	public boolean modifyCamera(long id, boolean active) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction;
		Camera camera;
		try {
			transaction = session.beginTransaction();
			camera = (Camera) session.get(Camera.class, id);
			camera.setActive(active);
			session.update(camera);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.close();
			throw new Exception();
		}
		if (active) {
			LOGGER.info("The camera: " + camera.toString() + " was activated successfully!");
		} else {
			LOGGER.info("The camera: " + camera.toString() + " was deactivated successfully!");
		}
		return true;
	}
}
