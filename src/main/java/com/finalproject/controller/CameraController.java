package com.finalproject.controller;

import com.finalproject.model.Camera;
import com.finalproject.model.UserSession;
import com.finalproject.services.CameraServices;
import com.finalproject.util.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/camera")
public class CameraController {
	@Autowired
	CameraServices cameraServices;

	@Autowired
	UserSession userSession;

	static final Logger LOGGER = Logger.getLogger(CameraController.class);

	/**
	 * Creates a new user for the system
	 *
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/newCamera", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public void newCamera(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
						  @RequestBody String jsonInput) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			// Utils.userVerification(httpServletResponse,
			// userSession);
			Camera camera = (Camera) Utils.fromJson(jsonInput, Camera.class);
			if (this.cameraServices.addCamera(camera)) {
				httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			} else {
				httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			String jsonOutput = Utils.toJson(camera);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			httpServletResponse.setContentType("application/json; charset=UTF-8");
			httpServletResponse.getWriter().println(jsonOutput);
		} catch (IllegalAccessException ex) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception ex) {
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Provides the service required for the user to login
	 *
	 * @return
	 */

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public
	@ResponseBody
	Camera getCamera(HttpServletResponse httpServletResponse, @PathVariable("id") long id) {
		Utils.addCorsHeader(httpServletResponse);
		Camera camera = null;
		try {
			// Utils.userVerification(httpServletResponse,
			// userSession);
			camera = cameraServices.getCameraById(id);
			System.out.println(camera.toString());
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		} catch (IllegalAccessException ex) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return camera;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public
	@ResponseBody
	List<Camera> getCameraList(HttpServletResponse httpServletResponse) {
		Utils.addCorsHeader(httpServletResponse);
		List<Camera> cameraList = null;
		try {
			// Utils.userVerification(httpServletResponse,
			// userSession);
			cameraList = cameraServices.getCameraList();
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		} catch (IllegalAccessException ex) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return cameraList;
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public
	@ResponseBody
	boolean deleteCamera(HttpServletResponse httpServletResponse, @PathVariable("id") long id) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			// Utils.userVerification(httpServletResponse,
			// userSession);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return cameraServices.deleteCamera(id);
		} catch (IllegalAccessException ex) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return false;
	}

	@RequestMapping(value = "modifyCamera", method = RequestMethod.GET)
	public
	@ResponseBody
	boolean modifyCamera(HttpServletResponse httpServletResponse,
						 @RequestParam("id") long id,
						 @RequestParam("active") boolean active) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			// Utils.userVerification(httpServletResponse,
			// userSession);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return cameraServices.modifyCamera(id, active);
		} catch (IllegalAccessException ex) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return false;
	}


}
