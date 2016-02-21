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

/**
 * This controller provides all the services available for the cameras
 */
@Controller
@RequestMapping("/camera")
public class CameraController {
	@Autowired
	CameraServices cameraServices;

	@Autowired
	UserSession userSession;

	/**
	 * Creation of the logger
	 */
	static final Logger LOGGER = Logger.getLogger(CameraController.class);

	/**
	 * Creates a new camera for the system
	 *
	 * @param httpServletRequest The httpServletRequest
	 * @param httpServletResponse The httpServletResponse
	 * @param jsonInput The camera object converted to json
	 */
	@RequestMapping(value = "/newCamera", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public void newCamera(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
						  @RequestBody String jsonInput) {
		Utils.addCorsHeader(httpServletResponse);
		try {
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
	 * Provides the service required to retrieve one camera from the dataBase according to an ID
	 * @param httpServletResponse The httpServletResponse
	 * @param id The id of the camera to be retrieved
	 * @return The camera with the id sent
	 */

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public
	@ResponseBody
	Camera getCamera(HttpServletResponse httpServletResponse, @PathVariable("id") long id) {
		Utils.addCorsHeader(httpServletResponse);
		Camera camera = null;
		try {
			camera = cameraServices.getCameraById(id);
			System.out.println(camera.toString());
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		} catch (IllegalAccessException ex) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception e) {
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return camera;
	}

	/**
	 * Provides the service required to retrieve all cameras from the dataBase
	 * @param httpServletResponse The httpServletResponse
	 * @return The camera list with all cameras
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public
	@ResponseBody
	List<Camera> getCameraList(HttpServletResponse httpServletResponse) {
		Utils.addCorsHeader(httpServletResponse);
		List<Camera> cameraList = null;
		try {
			cameraList = cameraServices.getCameraList();
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		} catch (IllegalAccessException ex) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception e) {
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return cameraList;
	}

	/**
	 * Provides the service required to delete one camera from the dataBase according to an ID
	 * @param httpServletResponse The httpServletResponse
	 * @param id The id of the camera to be deleted
	 * @return true if everything went well, false if it didn't go well
	 */
	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public
	@ResponseBody
	boolean deleteCamera(HttpServletResponse httpServletResponse, @PathVariable("id") long id) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return cameraServices.deleteCamera(id);
		} catch (IllegalAccessException ex) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception e) {
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return false;
	}

	/**
	 * Provides the service required to modify the activity flag for one camera from the dataBase according to an ID
	 * @param httpServletResponse The httpServletResponse
	 * @param id The id of the camera to be modified
	 * @param active The new value of the active flag
	 * @return true if everything went well, false if it didn't go well
	 */
	@RequestMapping(value = "modifyCamera", method = RequestMethod.GET)
	public
	@ResponseBody
	boolean modifyCamera(HttpServletResponse httpServletResponse,
						 @RequestParam("id") long id,
						 @RequestParam("active") boolean active) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return cameraServices.modifyCamera(id, active);
		} catch (IllegalAccessException ex) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception e) {
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return false;
	}


}
