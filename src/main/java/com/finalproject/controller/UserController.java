package com.finalproject.controller;

import com.finalproject.model.User;
import com.finalproject.model.UserSession;
import com.finalproject.services.UserServices;
import com.finalproject.util.FinalProjectUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserServices userServices;
	@Autowired
	UserSession userSession;

	static final Logger LOGGER = Logger.getLogger(UserController.class);

	/**
	 * Creates a new user for the system
	 *
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param jsonInput
	 * @return
	 */
	@RequestMapping(value = "/newUser", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public void newUser(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
						@RequestBody String jsonInput) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
			// userSession);
			User user = (User) FinalProjectUtil.fromJson(jsonInput, User.class);
			this.userServices.addUser(user);
			LOGGER.info("New user created: " + user.toString());

			String jsonOutput = FinalProjectUtil.toJson(user);
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
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param jsonEntrada
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public
	@ResponseBody
	User login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			   @RequestBody String jsonEntrada) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		User user = (User) FinalProjectUtil.fromJson(jsonEntrada, User.class);
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
	 * @param httpServletResponse
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public
	@ResponseBody
	User logout(HttpServletResponse httpServletResponse) throws Exception {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
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

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public
	@ResponseBody
	User getUser(HttpServletResponse httpServletResponse, @PathVariable("id") long id) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		User user = null;
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
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

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public
	@ResponseBody
	List<User> getUsers(HttpServletResponse httpServletResponse) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		List<User> userList = null;
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
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

	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public
	@ResponseBody
	boolean deleteUser(HttpServletResponse httpServletResponse, @PathVariable("id") long id) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
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