package com.finalproject.dao;

import com.finalproject.model.Camera;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CameraDaoImpl implements CameraDao {

	@Autowired
	SessionFactory sessionFactory;

	Transaction tx = null;

	static final Logger LOGGER = Logger.getLogger(CameraDaoImpl.class);

	@Override
	public boolean addCamera(Camera camera) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(camera);
		tx.commit();
		session.close();
		LOGGER.info("Camara guardada: " + camera.toString());
		return true;
	}

	@Override
	public Camera getCameraById(long id) throws Exception {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Camera camera = (Camera) session.get(Camera.class, id);
		session.flush();
		session.close();
		return camera;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Camera> getCameraList() throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<Camera> cameraList = session.createCriteria(Camera.class).list();
		tx.commit();
		session.close();
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
		Transaction tx = null;
		tx = session.beginTransaction();
		Camera camera =
				(Camera) session.get(Camera.class, id);
		camera.setActive(active);
		session.update(camera);
		tx.commit();
		session.close();
		return true;
	}
}
