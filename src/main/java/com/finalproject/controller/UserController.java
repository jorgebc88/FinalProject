package com.finalproject.controller;

import com.finalproject.model.User;
import com.finalproject.model.UserSession;
import com.finalproject.services.UserServices;
import com.finalproject.util.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * This controller provides all the services available for the Users
 */
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserServices userServices;
	@Autowired
	UserSession userSession;

	/**
	 * Creation of the logger
	 */
	static final Logger LOGGER = Logger.getLogger(UserController.class);

	/**
	 * Provides the service required for the creation of a new user for the system
	 *
	 * @param httpServletRequest The httpServletRequest
	 * @param httpServletResponse The httpServletResponse
	 * @param jsonInput	The user object converted to json
	 */
	@RequestMapping(value = "/newUser", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public void newUser(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
						@RequestBody String jsonInput) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			// Utils.userVerification(httpServletResponse,
			// userSession);
			User user = (User) Utils.fromJson(jsonInput, User.class);
			this.userServices.addUser(user);
			LOGGER.info("New user created: " + user.toString());

			String jsonOutput = Utils.toJson(user);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			httpServletResponse.setContentType("application/json; charset=UTF-8");
			httpServletResponse.getWriter().println(jsonOutput);
			LOGGER.info(httpServletResponse);
		} catch (IllegalAccessException ex) {
			LOGGER.error("Trying to create user without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception ex) {
			LOGGER.error("Error found: " + ex.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Provides the service required for the user to login
	 *
	 * @param httpServletRequest The httpServletRequest
	 * @param httpServletResponse The httpServletResponse
	 * @param jsonInput The user object converted to json
	 * @return the user who was logged in
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public
	@ResponseBody
	User login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			   @RequestBody String jsonInput) {
		Utils.addCorsHeader(httpServletResponse);
		User user = (User) Utils.fromJson(jsonInput, User.class);
		if (userSession.getUser() != null) {
			LOGGER.info("User: " + userSession.getUser().getUserName() + " is already logged!");
			return user;
		}
		try {
			user = this.userServices.login(user.getUserName(), user.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return user;
		}
		if (user == null) {
			LOGGER.info("User or password wrong.");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
		} else {
			LOGGER.info("User '" + user.getUserName() + "' logged successfully!");
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			userSession.setUser(user);
		}
		return user;
	}

	/**
	 * Provides the service required for the user to logout
	 *
	 * @param httpServletResponse The httpServletResponse
	 * @return the user who was logged out
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public
	@ResponseBody
	User logout(HttpServletResponse httpServletResponse) {
		Utils.addCorsHeader(httpServletResponse);
		User user = null;
		if (userSession.getUser() != null) {
			LOGGER.info("User " + userSession.getUser().getUserName() + " logout successfully!");
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		} else {
			LOGGER.info("There was no user logged!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
		userSession.setUser(user);
		return userSession.getUser();
	}

	/**
	 * Provides the service required to retrieve one user from the dataBase according to an ID
	 * @param httpServletResponse The httpServletResponse
	 * @param id The id of the camera to be retrieved
	 * @return The User with the id sent
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public
	@ResponseBody
	User getUser(HttpServletResponse httpServletResponse, @PathVariable("id") long id) {
		Utils.addCorsHeader(httpServletResponse);
		User user = null;
		try {
			// Utils.userVerification(httpServletResponse,
			// userSession);
			user = userServices.getUserById(id);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		} catch (IllegalAccessException ex) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return user;
	}

	/**
	 * Provides the service required to retrieve all users from the dataBase
	 * @param httpServletResponse The httpServletResponse
	 * @return The list of all Users
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public
	@ResponseBody
	List<User> getUsers(HttpServletResponse httpServletResponse) {
		Utils.addCorsHeader(httpServletResponse);
		List<User> userList = null;
		try {
			// Utils.userVerification(httpServletResponse,
			// userSession);
			userList = userServices.getUserList();
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		} catch (IllegalAccessException ex) {
			LOGGER.info("Trying to request info without logging!");
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception e) {
			LOGGER.info("Error found: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return userList;
	}

	/**
	 * Provides the service required to delete one user from the dataBase according to an ID
	 * @param httpServletResponse The httpServletResponse
	 * @param id The id of the camera to be deleted
	 * @return true if everything went well, false if it didn't go well
	 */
	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public
	@ResponseBody
	boolean deleteUser(HttpServletResponse httpServletResponse, @PathVariable("id") long id) {
		Utils.addCorsHeader(httpServletResponse);
		try {
			// Utils.userVerification(httpServletResponse,
			// userSession);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return userServices.deleteUser(id);
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