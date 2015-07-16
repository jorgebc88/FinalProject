package com.finalproject.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finalproject.model.User;
import com.finalproject.model.UserSession;
import com.finalproject.services.UserServices;

@Controller
@RequestMapping("/user" )
public class UserController {
    @Autowired
    UserServices userServices;
    @Autowired
    UserSession userSession;

    static final Logger logger = Logger.getLogger(UserController.class);
    
    public UserController() {}
    
	/**
     * Creates a new user for the system
     * @param userName The new user name
     * @param password The password for the new user
     * @return
	 * @throws Exception 
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public @ResponseBody User addUser(HttpServletResponse httpServletResponse, @RequestParam ("userName") String userName, @RequestParam ("password") String password) throws Exception{
		httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
    	if(userSession.getUser() == null){
    		httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    		return null;
        }
    	User user = new User(userName, password);
    	if(this.userServices.addUser(user)){
    		httpServletResponse.setStatus(HttpServletResponse.SC_OK);
    		return user;
    	}else{
    		httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    		return null;
    	}
    }

    /**
     * Provides the service required for the user to login
     * @param userName The name of the user who wants to login.
     * @param password The user's password.
     * @return
    */
    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody User login (HttpServletResponse httpServletResponse, @RequestParam("userName") String userName, @RequestParam("password") String password){
    	User user = null;
    	this.addCorsHeader(httpServletResponse);
    	if(userSession.getUser() != null){
    		logger.info("Usuario: "+userSession.getUser().getUserName()+" esta logueado!");
    		return user;
    	}
    	try{
    		user = this.userServices.login(userName, password);	
    	} catch (Exception e) {
    		e.printStackTrace();
    		httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    		return user;
    	}
    	if(user == null){
    		logger.info("Usuario y/o contraseña incorrecta.");
    		httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
    		return user;
    	}else{
    		logger.info("Usuario '" + user.getUserName() + "' logueado correctamente!");
	    	httpServletResponse.setStatus(HttpServletResponse.SC_OK);
	    	userSession.setUser(user);
	    	return user;
    	}
    }
    
    /**
     * Provides the service required for the user to logout
     * @return
     * @throws Exception 
    */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public @ResponseBody User logout (HttpServletResponse httpServletResponse) throws Exception{
    	httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
    	User user = null;
    	if(userSession.getUser() != null){
    		logger.info("Usuario " + userSession.getUser().getUserName() + " deslogueado correctamente!");
    	}else{
    		logger.info("No existia Usuario Logueado!");
    	}
       	userSession.setUser(user);
        return userSession.getUser();
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	User getUser(HttpServletResponse httpServletResponse, @PathVariable("id") long id) {
    	httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
    	User user = null;
		try {
			user = userServices.getUserById(id);
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			e.printStackTrace();
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return user;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody
	List<User> getUser(HttpServletResponse httpServletResponse) {
    	httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
		List<User> userList = null;
		try {
			userList = userServices.getUserList();
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			e.printStackTrace();
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return userList;
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public @ResponseBody
	boolean deleteUser(HttpServletResponse httpServletResponse, @PathVariable("id") long id) {
    	httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
		try {
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			return userServices.deleteUser(id);			
		} catch (Exception e) {
			e.printStackTrace();
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return false;
	}
	
	private void addCorsHeader(HttpServletResponse response){
        //TODO: externalize the Allow-Origin
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Max-Age", "1728000");
    }


}