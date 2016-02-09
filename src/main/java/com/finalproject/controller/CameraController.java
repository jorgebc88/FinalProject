package com.finalproject.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finalproject.model.Camera;
import com.finalproject.model.UserSession;
import com.finalproject.services.CameraServices;
import com.finalproject.util.FinalProjectUtil;

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
	 * @param userName
	 *            The new user name
	 * @param password
	 *            The password for the new user
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/newCamera", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public void newCamera(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			@RequestBody String jsonEntrada) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
			// userSession);
			Camera camera = (Camera) FinalProjectUtil.fromJson(jsonEntrada, Camera.class);
			if (this.cameraServices.addCamera(camera)) {
				httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			} else {
				httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			String jsonSalida = FinalProjectUtil.toJson(camera);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			httpServletResponse.setContentType("application/json; charset=UTF-8");
			httpServletResponse.getWriter().println(jsonSalida);
		} catch (IllegalAccessException ex) {
			LOGGER.info("No ha iniciado sesi�n!");
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception ex) {
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Provides the service required for the user to login
	 * 
	 * @param userName
	 *            The name of the user who wants to login.
	 * @param password
	 *            The user's password.
	 * @return
	 */

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody Camera getCamera(HttpServletResponse httpServletResponse, @PathVariable("id") long id) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		Camera camera = null;
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
			// userSession);
			camera = cameraServices.getCameraById(id);
			System.out.println(camera.toString());
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		} catch (IllegalAccessException ex) {
			LOGGER.info("No ha iniciado sesi�n!");
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception e) {
			LOGGER.info("Error encontrado: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return camera;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody List<Camera> getCameraList(HttpServletResponse httpServletResponse) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		List<Camera> cameraList = null;
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
			// userSession);
			cameraList = cameraServices.getCameraList();
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		} catch (IllegalAccessException ex) {
			LOGGER.info("No ha iniciado sesi�n!");
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception e) {
			LOGGER.info("Error encontrado: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return cameraList;
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public @ResponseBody boolean deleteCamera(HttpServletResponse httpServletResponse, @PathVariable("id") long id) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
			// userSession);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return cameraServices.deleteCamera(id);
		} catch (IllegalAccessException ex) {
			LOGGER.info("No ha iniciado sesi�n!");
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception e) {
			LOGGER.info("Error encontrado: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return false;
	}

}
