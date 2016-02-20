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
import java.util.*;

@Controller
@RequestMapping("/detectedObject")
public class DetectedObjectController {
	@Autowired
	DetectedObjectServices detectedObjectServices;
	@Autowired
	UserSession userSession;

	private Map<Long, DetectedObjectCache> detectedObjectCacheMap = new HashMap<>();


	static final Logger LOGGER = Logger.getLogger(DetectedObjectController.class);

	@RequestMapping(value = "/DetectedObject", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public void newDetectedObject(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
								  @RequestBody String jsonInput) {
		Utils.addCorsHeader(httpServletResponse);
		Long newCacheValue;
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

	// -------------------------------------------------------------------------------------------------------------

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
	// -------------------------------------------------------------------------------------------------------------

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

}
