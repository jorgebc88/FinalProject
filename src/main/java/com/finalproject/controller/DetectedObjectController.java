package com.finalproject.controller;

import com.finalproject.model.DetectedObject;
import com.finalproject.model.DetectedObjectCache;
import com.finalproject.model.UserSession;
import com.finalproject.services.DetectedObjectServices;
import com.finalproject.util.Utils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.sse.SseFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This controller provides all the services available for the detected objects
 */
@Controller
@RequestMapping("/detectedObject")
public class DetectedObjectController {
	@Autowired
	DetectedObjectServices detectedObjectServices;
	@Autowired
	UserSession userSession;

	/**
	 * This map will allow to have a different cache for the cameras
	 */
	private Map<Long, DetectedObjectCache> detectedObjectCacheMap = new HashMap<>();

	/**
	 * Creation of the logger
	 */
	static final Logger LOGGER = Logger.getLogger(DetectedObjectController.class);

	/**
	 * Creates a new detectedObject for the system
	 *
	 * @param httpServletRequest The httpServletRequest
	 * @param httpServletResponse The httpServletResponse
	 * @param jsonInput The detected object converted to json
	 */
	@RequestMapping(value = "/DetectedObject", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public void newDetectedObject(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
								  @RequestBody String jsonInput) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			// Utils.userVerification(httpServletResponse,
			// userSession);
			DetectedObject detectedObject = (DetectedObject) Utils.fromJson(jsonInput,
					DetectedObject.class);
			if (!detectedObjectCacheMap.containsKey(detectedObject.getCamera_id())) {

				detectedObjectCacheMap.put(detectedObject.getCamera_id(), new DetectedObjectCache());
			}
			Utils.modifyDetectedObjectCache(detectedObject, detectedObjectCacheMap.get(detectedObject.getCamera_id()));

			this.detectedObjectServices.addDetectedObject(detectedObject);
			String jsonOutput = Utils.toJson(detectedObject);

			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			httpServletResponse.setContentType("application/json; charset=UTF-8");
			httpServletResponse.getWriter().println(jsonOutput);
		} catch (Exception e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}


	/**
	 * Provides the service required to retrieve all the detectedObjects from the dataBase
	 *
	 * @param httpServletResponse The httpServletResponse
	 * @return The detectedObject list for all cameras
	 */
	@RequestMapping(value = "/requestDetectedObject", method = RequestMethod.GET)
	public
	@ResponseBody
	List<DetectedObject> getDetectedObjects(HttpServletResponse httpServletResponse) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			// Utils.userVerification(httpServletResponse,
			// userSession);
			List<DetectedObject> detectedObjectList = this.detectedObjectServices.getDetectedObjectList();
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return detectedObjectList;
		} catch (IllegalAccessException e) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Exception e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	/**
	 * Provides the service required to retrieve all the detectedObjects for a specific camera from the dataBase according to the camera ID
	 *
	 * @param httpServletResponse The httpServletResponse
	 * @param cameraId The id of the camera we want
	 * @return The detectedObject list for this camera
	 */
	@RequestMapping(value = "/requestDetectedObjectByCameraId", method = RequestMethod.GET)
	public
	@ResponseBody
	List<DetectedObject> getDetectedObjectByCameraId(HttpServletResponse httpServletResponse,
													 @RequestParam("cameraId") long cameraId) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			// Utils.userVerification(httpServletResponse,
			// userSession);
			List<DetectedObject> detectedObjectList = this.detectedObjectServices.getDetectedObjectListByCameraId(cameraId);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return detectedObjectList;
		} catch (IllegalAccessException e) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (RuntimeException e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
			return null;
		} catch (Exception e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	/**
	 * Provides the service required to retrieve the detectedObjects for an specific date and a specific camera from the dataBase according to the camera ID and the date
	 * @param httpServletResponse The httpServletResponse
	 * @param date The specific date from we want to receive the detectedObject
	 * @param cameraId The id of the camera we want
	 * @return The detectedObjects for this camera created on the specific date
	 */
	@RequestMapping(value = "/requestDetectedObjectByDateAndCameraId", method = RequestMethod.GET)
	public
	@ResponseBody
	List<DetectedObject> getDetectedObjectsByDateAndCameraId(HttpServletResponse httpServletResponse,
															 @RequestParam("date") Date date,
															 @RequestParam("cameraId") long cameraId) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			// Utils.userVerification(httpServletResponse,
			// userSession);
			List<DetectedObject> detectedObjectList = this.detectedObjectServices.findByDateAndCameraId(date, cameraId);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return detectedObjectList;
		} catch (IllegalAccessException e) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (RuntimeException e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
			return null;
		} catch (Exception e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	/**
	 * Provides the service required to retrieve the detectedObjects for an specific month and a specific camera from the dataBase according to the camera ID and the month
	 * @param httpServletResponse The httpServletResponse
	 * @param month The specific month from we want to receive the detectedObject
	 * @param cameraId The id of the camera we want
	 * @return The detectedObjects for this camera created on the specific month
	 */
	@RequestMapping(value = "/requestDetectedObjectByMonthAndCameraId", method = RequestMethod.GET)
	public
	@ResponseBody
	List<DetectedObject> getDetectedObjectsByMonthAndCameraId(HttpServletResponse httpServletResponse,
															  @RequestParam("month") int month,
															  @RequestParam("cameraId") long cameraId) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			// Utils.userVerification(httpServletResponse,
			// userSession);
			List<DetectedObject> detectedObjectList = this.detectedObjectServices.findByMonthAndCameraId(month, cameraId);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return detectedObjectList;
		} catch (IllegalAccessException e) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Exception e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	/**
	 * Provides the service required to retrieve the detectedObjects for an specific year and a specific camera from the dataBase according to the camera ID and the year
	 * @param httpServletResponse The httpServletResponse
	 * @param year The specific year from we want to receive the detectedObject
	 * @param cameraId The id of the camera we want
	 * @return The detectedObjects for this camera created on the specific year
	 */
	@RequestMapping(value = "/requestDetectedObjectByYearAndCameraId", method = RequestMethod.GET)
	public
	@ResponseBody
	List<DetectedObject> getDetectedObjectsByYearAndCameraId(HttpServletResponse httpServletResponse,
															 @RequestParam("year") int year,
															 @RequestParam("cameraId") long cameraId) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			// Utils.userVerification(httpServletResponse,
			// userSession);
			List<DetectedObject> detectedObjectList = this.detectedObjectServices.findByYearAndCameraId(year, cameraId);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return detectedObjectList;
		} catch (IllegalAccessException e) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Exception e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	/**
	 * Provides the service required to retrieve the detectedObjects for a range of dates and a specific camera from the dataBase according to the camera ID and a date
	 * @param httpServletResponse The httpServletResponse
	 * @param startDate The start date
	 * @param endDate The end date
	 * @param cameraId The id of the camera we want
	 * @return The detectedObjects for this camera created on the specific range of dates
	 */
	@RequestMapping(value = "/requestDetectedObjectByDatesBetweenAndCameraId", method = RequestMethod.GET)
	public
	@ResponseBody
	List<DetectedObject> getDetectedObjectsByDatesBetweenAndCameraId(HttpServletResponse httpServletResponse,
																	 @RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate,
																	 @RequestParam("cameraId") long cameraId) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			// Utils.userVerification(httpServletResponse,
			// userSession);
			List<DetectedObject> detectedObjectList = this.detectedObjectServices.findByDatesBetweenAndCameraId(startDate, endDate, cameraId);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return detectedObjectList;
		} catch (IllegalAccessException e) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Exception e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	/**
	 * Provides the service required to delete the detectedObjects created before an specific date for a specific camera from the dataBase according to the camera ID and the date
	 * @param httpServletResponse The httpServletResponse
	 * @param date All the detectedObjects before this date will be deleted
	 * @param cameraId The detectedObjects detected for this camera will be deleted
	 * @return True is everything went well, false if not
	 */
	@RequestMapping(value = "/deleteDetectedObjectBeforeDateByCameraId", method = RequestMethod.GET)
	public
	@ResponseBody
	boolean deleteDetectedObjectBeforeDateByCameraId(HttpServletResponse httpServletResponse,
													 @RequestParam("date") Date date,
													 @RequestParam("cameraId") long cameraId) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			// Utils.userVerification(httpServletResponse,
			// userSession);
			boolean deleteSuccessful = this.detectedObjectServices.deleteDetectedObjectBeforeDateByCameraId(date, cameraId);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return deleteSuccessful;
		} catch (IllegalAccessException e) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
		} catch (Exception e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return false;
	}

	/**
	 * Provides the service required to retrieve the cache of detected objects
	 * @param httpServletResponse The httpServletResponse
	 * @param cameraId It is the id of the camera to which we want to receive the cache
	 * @return The cache is sent every 500 ms
	 */
	@RequestMapping(value = "/serverSentEvents", method = RequestMethod.GET, produces = SseFeature.SERVER_SENT_EVENTS)
	public
	@ResponseBody
	String getServerSentEvents(HttpServletResponse httpServletResponse,
							   @RequestParam("cameraId") long cameraId){
		LOGGER.info(cameraId);
		LOGGER.info("SSE: " + detectedObjectCacheMap.size());
		DetectedObjectCache cache = detectedObjectCacheMap.get(cameraId);
		if (cache == null) {
			httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		StringBuilder data = new StringBuilder("retry: 500\n");
		data.append("data: {\"detectedObject\":[");
		data.append(Utils.toJson(cache)).append("]}\n\n");
		LOGGER.info("Json: " + data.toString());

		return data.toString();
	}

	/**
	 * Provides the service required to reset the cache
	 * @param httpServletResponse The httpServletResponse
	 * @param cameraId It is the id of the camera to which we want to restart the cache
	 */
	@RequestMapping(value = "/resetCache", method = RequestMethod.GET)
	public
	@ResponseBody
	void resetCache(HttpServletResponse httpServletResponse,
					   @RequestParam("cameraId") long cameraId) {
		DetectedObjectCache cache = detectedObjectCacheMap.get(cameraId);
		if (cache == null) {
			httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		cache.resetCache();
	}

	@RequestMapping(value = "/allTimeRanking", method = RequestMethod.GET)
	public
	@ResponseBody
	List<DetectedObject> getAllTimeDetectedObjectsRanking(HttpServletResponse httpServletResponse) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			List<DetectedObject> allTimeRanking = this.detectedObjectServices.allTimeDetectedObjectsRanking();
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return allTimeRanking;
		} catch (IllegalAccessException e) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Exception e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	@RequestMapping(value = "/rankingByYear", method = RequestMethod.GET)
	public
	@ResponseBody
	List<DetectedObject> getDetectedObjectsRankingByYear(HttpServletResponse httpServletResponse,
	                                                     @RequestParam("year") int year) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			List<DetectedObject> rankingByYear = this.detectedObjectServices.detectedObjectsRankingByYear(year);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return rankingByYear;
		} catch (IllegalAccessException e) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (RuntimeException e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
			return null;
		} catch (Exception e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	@RequestMapping(value = "/rankingByYearAndMonth", method = RequestMethod.GET)
	public
	@ResponseBody
	List<DetectedObject> getDetectedObjectsRankingByYearAndMonth(HttpServletResponse httpServletResponse,
	                                                             @RequestParam("year") int year,
	                                                             @RequestParam("month") int month) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			List<DetectedObject> rankingByYearAndMonth = this.detectedObjectServices.detectedObjectsRankingByYearAndMonth(year, month);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return rankingByYearAndMonth;
		} catch (IllegalAccessException e) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Exception e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	@RequestMapping(value = "/rankingBetweenDates", method = RequestMethod.GET)
	public
	@ResponseBody
	List<DetectedObject> getDetectedObjectsRankingBetweenDates(HttpServletResponse httpServletResponse,
	                                                           @RequestParam("startDate") Date startDate,
	                                                           @RequestParam("endDate") Date endDate) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			List<DetectedObject> rankingByYearAndMonth = this.detectedObjectServices.detectedObjectsRankingBetweenDates(startDate, endDate);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return rankingByYearAndMonth;
		} catch (IllegalAccessException e) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Exception e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	@RequestMapping(value = "/detectedObjectsHistogram", method = RequestMethod.GET)
	public
	@ResponseBody
	List<DetectedObject> getByHoursOfDayDetectedObjectsHistogram(HttpServletResponse httpServletResponse,
	                                                     @RequestParam("dayOfTheWeek") int dayOfTheWeek) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			List<DetectedObject> byHoursOfDayDetectedObjectsHistogram = this.detectedObjectServices.getByHoursOfDayDetectedObjectsHistogram(dayOfTheWeek);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return byHoursOfDayDetectedObjectsHistogram;
		} catch (IllegalAccessException e) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (RuntimeException e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
			return null;
		} catch (Exception e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	@RequestMapping(value = "/peakHoursByDaysOfTheWeekAndCamera", method = RequestMethod.GET)
	public
	@ResponseBody
	List<DetectedObject> getPeakHoursByDaysOfTheWeekAndCamera(HttpServletResponse httpServletResponse,
	                                                             @RequestParam("cameraId") int cameraId) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			List<DetectedObject> byHoursOfDayDetectedObjectsHistogram = this.detectedObjectServices.getPeakHoursByDaysOfTheWeekAndCamera(cameraId);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return byHoursOfDayDetectedObjectsHistogram;
		} catch (IllegalAccessException e) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (RuntimeException e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
			return null;
		} catch (Exception e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}


}
