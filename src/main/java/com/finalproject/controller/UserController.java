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

import com.finalproject.model.User;
import com.finalproject.model.UserSession;
import com.finalproject.services.UserServices;
import com.finalproject.util.FinalProjectUtil;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserServices userServices;
	@Autowired
	UserSession userSession;

	static final Logger logger = Logger.getLogger(UserController.class);

	public UserController() {
	}

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

	@RequestMapping(value = "/newUser", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public void insert(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			@RequestBody String jsonEntrada) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
			// userSession);
			User user = (User) FinalProjectUtil.fromJson(jsonEntrada, User.class);
			if (this.userServices.addUser(user)) {
				httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			} else {
				httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			String jsonSalida = FinalProjectUtil.toJson(user);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			httpServletResponse.setContentType("application/json; charset=UTF-8");
			httpServletResponse.getWriter().println(jsonSalida);
		} catch (IllegalAccessException ex) {
			logger.info("No ha iniciado sesión!");
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
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody User login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			@RequestBody String jsonEntrada) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		User user = (User) FinalProjectUtil.fromJson(jsonEntrada, User.class);
		if (userSession.getUser() != null) {
			logger.info("Usuario: " + userSession.getUser().getUserName() + " esta logueado!");
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
			logger.info("Usuario y/o contraseña incorrecta.");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
		} else {
			logger.info("Usuario '" + user.getUserName() + "' logueado correctamente!");
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			userSession.setUser(user);
		}
		return user;
	}

	/**
	 * Provides the service required for the user to logout
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public @ResponseBody User logout(HttpServletResponse httpServletResponse) throws Exception {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		User user = null;
		if (userSession.getUser() != null) {
			logger.info("Usuario " + userSession.getUser().getUserName() + " deslogueado correctamente!");
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		} else {
			logger.info("No existia Usuario Logueado!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
		userSession.setUser(user);
		return userSession.getUser();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody User getUser(HttpServletResponse httpServletResponse, @PathVariable("id") long id) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		User user = null;
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
			// userSession);
			user = userServices.getUserById(id);
			System.out.println("USUARIO: " + user.getUserName() + " " + user.getPassword());
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		} catch (IllegalAccessException ex) {
			logger.info("No ha iniciado sesión!");
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception e) {
			logger.info("Error encontrado: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return user;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody List<User> getUser(HttpServletResponse httpServletResponse) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		List<User> userList = null;
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
			// userSession);
			userList = userServices.getUserList();
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		} catch (IllegalAccessException ex) {
			logger.info("No ha iniciado sesión!");
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception e) {
			logger.info("Error encontrado: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return userList;
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public @ResponseBody boolean deleteUser(HttpServletResponse httpServletResponse, @PathVariable("id") long id) {
		FinalProjectUtil.addCorsHeader(httpServletResponse);
		try {
			// FinalProjectUtil.userVerification(httpServletResponse,
			// userSession);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return userServices.deleteUser(id);
		} catch (IllegalAccessException ex) {
			logger.info("No ha iniciado sesión!");
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception e) {
			logger.info("Error encontrado: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return false;
	}

}