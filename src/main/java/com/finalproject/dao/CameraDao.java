package com.finalproject.dao;

import com.finalproject.model.Camera;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface CameraDao {
	boolean addCamera(Camera camera) throws Exception;

	Camera getCameraById(long id) throws Exception;

	List<Camera> getCameraList() throws Exception;

	List<Camera> getCameraRankingAllTime() throws Exception;

	List<Camera> getCameraRankingByYear(Date year) throws Exception;

	List<Camera> getCameraRankingByMonth(Date month) throws Exception;

	List<Camera> getCameraRankingByDay(Date day) throws Exception;

	List<Camera> getCameraRankingBetweenDates(Date startDate, Date endDate) throws Exception;

	boolean deleteCamera(long id) throws Exception;

	boolean modifyCamera(long id, boolean active) throws Exception;
}
