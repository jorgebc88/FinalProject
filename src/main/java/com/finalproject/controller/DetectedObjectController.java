package com.finalproject.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.sse.SseFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finalproject.model.DetectedObject;
import com.finalproject.model.DetectedObjectNorthCache;
import com.finalproject.model.DetectedObjectSouthCache;
import com.finalproject.model.UserSession;
import com.finalproject.services.DetectedObjectServices;
import com.finalproject.util.FinalProjectUtil;

@Controller
@RequestMapping("/detectedObject")
public class DetectedObjectController {
	@Autowired
	DetectedObjectServices detectedObjectServices;
	@Autowired
	UserSession userSession;
	@Autowired
	DetectedObjectSouthCache detectedObjectSouthCache;
	@Autowired
	DetectedObjectNorthCache detectedObjectNorthCache;


	static final Logger logger = Logger.getLogger(DetectedObjectController.class);

	public DetectedObjectController() {
	}

	@RequestMapping(value = "/DetectedObject", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public void newDetectedObject(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			@RequestBody String jsonEntrada) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
			// userSession);
			DetectedObject detectedObject = (DetectedObject) FinalProjectUtil.fromJson(jsonEntrada,
					DetectedObject.class);
			FinalProjectUtil.modifyDetectedObjectCache(detectedObject, detectedObjectSouthCache, detectedObjectNorthCache);
			this.detectedObjectServices.addDetectedObject(detectedObject);
			String jsonSalida = FinalProjectUtil.toJson(detectedObject);

			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			httpServletResponse.setContentType("application/json; charset=UTF-8");
			httpServletResponse.getWriter().println(jsonSalida);

		} catch (Exception ex) {
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		}
	}

	// -------------------------------------------------------------------------------------------------------------

	@RequestMapping(value = "/requestDetectedObject", method = RequestMethod.GET)
	public @ResponseBody List<DetectedObject> getDetectedObjects(HttpServletResponse httpServletResponse) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
			// userSession);
			return this.detectedObjectServices.getDetectedObjectList();
		} catch (IllegalAccessException e) {
			logger.info("No ha iniciado sesión!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Exception e) {
			logger.info("Error encontrado: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	@RequestMapping(value = "/requestDetectedObjectByDate", method = RequestMethod.GET)
	public @ResponseBody List<DetectedObject> getDetectedObjectsByDate(HttpServletResponse httpServletResponse,
			@RequestParam("date") Date date) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
			// userSession);
			return this.detectedObjectServices.findByDate(date);
		} catch (IllegalAccessException e) {
			logger.info("No ha iniciado sesión!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Exception e) {
			logger.info("Error encontrado: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	@RequestMapping(value = "/requestDetectedObjectByMonth", method = RequestMethod.GET)
	public @ResponseBody List<DetectedObject> getDetectedObjectsByMonth(HttpServletResponse httpServletResponse,
			@RequestParam("month") int month) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
			// userSession);
			return this.detectedObjectServices.findByMonth(month);
		} catch (IllegalAccessException e) {
			logger.info("No ha iniciado sesión!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Exception e) {
			logger.info("Error encontrado: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	@RequestMapping(value = "/requestDetectedObjectByYear", method = RequestMethod.GET)
	public @ResponseBody List<DetectedObject> getDetectedObjectsByYear(HttpServletResponse httpServletResponse,
			@RequestParam("year") int year) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
			// userSession);
			return this.detectedObjectServices.findByYear(year);
		} catch (IllegalAccessException e) {
			logger.info("No ha iniciado sesión!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Exception e) {
			logger.info("Error encontrado: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	@RequestMapping(value = "/requestDetectedObjectByDatesBetween", method = RequestMethod.GET)
	public @ResponseBody List<DetectedObject> getDetectedObjectsByDatesBetween(HttpServletResponse httpServletResponse,
			@RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
			// userSession);
			return this.detectedObjectServices.findByDatesBetween(startDate, endDate);
		} catch (IllegalAccessException e) {
			logger.info("No ha iniciado sesión!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Exception e) {
			logger.info("Error encontrado: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}
	
	@RequestMapping(value = "/deleteDetectedObjectBeforeDate", method = RequestMethod.GET)
	public @ResponseBody boolean deleteDetectedObjectBeforeDate(HttpServletResponse httpServletResponse,
			@RequestParam("date") Date date) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
			// userSession);
			return this.detectedObjectServices.deleteDetectedObjectBeforeDate(date);
		} catch (IllegalAccessException e) {
			logger.info("No ha iniciado sesión!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
		} catch (Exception e) {
			logger.info("Error encontrado: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return false;
	}


	@RequestMapping(value = "/serverSentEvents", method = RequestMethod.GET, produces = SseFeature.SERVER_SENT_EVENTS)
	public @ResponseBody String getServerSentEvents(HttpServletResponse httpServletResponse) {
		
		logger.info("South: " + FinalProjectUtil.toJson(detectedObjectSouthCache));
		logger.info("North: " + FinalProjectUtil.toJson(detectedObjectNorthCache));
		StringBuilder data = new StringBuilder("retry: 500\n");
		data.append("data: {\"detectedObject\":[");
		data.append(FinalProjectUtil.toJson(detectedObjectSouthCache)).append(",");
		data.append(FinalProjectUtil.toJson(detectedObjectNorthCache)).append("]}\n\n");
		logger.info("Json: " + data.toString());

		return data.toString();
	}

}
