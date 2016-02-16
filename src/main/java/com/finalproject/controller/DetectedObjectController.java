package com.finalproject.controller;

import com.finalproject.model.DetectedObject;
import com.finalproject.model.DetectedObjectCacheContainer;
import com.finalproject.model.UserSession;
import com.finalproject.services.DetectedObjectServices;
import com.finalproject.util.FinalProjectUtil;
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

@Controller
@RequestMapping("/detectedObject")
public class DetectedObjectController {
	@Autowired
	DetectedObjectServices detectedObjectServices;
	@Autowired
	UserSession userSession;

	private Map<Long, DetectedObjectCacheContainer> detectedObjectCacheContainerMap = new HashMap<>();

	static final Logger LOGGER = Logger.getLogger(DetectedObjectController.class);

	@RequestMapping(value = "/DetectedObject", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public void newDetectedObject(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
								  @RequestBody String jsonInput) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
			// userSession);
			DetectedObject detectedObject = (DetectedObject) FinalProjectUtil.fromJson(jsonInput,
					DetectedObject.class);
			DetectedObjectCacheContainer detectedObjectCacheContainer;
			if (!detectedObjectCacheContainerMap.containsKey(detectedObject.getCamera_id())) {
				detectedObjectCacheContainer = new DetectedObjectCacheContainer();
				detectedObjectCacheContainerMap.put(detectedObject.getCamera_id(), detectedObjectCacheContainer);
			} else {
				detectedObjectCacheContainer = detectedObjectCacheContainerMap.get(detectedObject.getCamera_id());
			}

			FinalProjectUtil.modifyDetectedObjectCache(detectedObject, detectedObjectCacheContainer.getDetectedObjectSouthCache(), detectedObjectCacheContainer.getDetectedObjectNorthCache());
			this.detectedObjectServices.addDetectedObject(detectedObject);
			String jsonOutput = FinalProjectUtil.toJson(detectedObject);

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
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
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
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
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
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
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
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
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
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
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
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
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
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
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
							   @RequestParam("cameraId") int cameraId) {
		DetectedObjectCacheContainer detectedObjectCacheContainer = detectedObjectCacheContainerMap.get(cameraId);
		if (detectedObjectCacheContainer == null) {
			throw new UnsupportedOperationException("Un-existing camera id");
		}
		LOGGER.info("South: " + FinalProjectUtil.toJson(detectedObjectCacheContainer.getDetectedObjectSouthCache()));
		LOGGER.info("North: " + FinalProjectUtil.toJson(detectedObjectCacheContainer.getDetectedObjectNorthCache()));
		StringBuilder data = new StringBuilder("retry: 500\n");
		data.append("data: {\"detectedObject\":[");
		data.append(FinalProjectUtil.toJson(detectedObjectCacheContainer.getDetectedObjectSouthCache())).append(",");
		data.append(FinalProjectUtil.toJson(detectedObjectCacheContainer.getDetectedObjectNorthCache())).append("]}\n\n");
		LOGGER.info("Json: " + data.toString());

		return data.toString();
	}

}
