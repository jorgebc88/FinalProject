package com.finalproject.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.model.DetectedObject;
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

    static final Logger logger = Logger.getLogger(DetectedObjectController.class);
    
    public DetectedObjectController(){}

    
    @RequestMapping(value = "/DetectedObject", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public void insert(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestBody String jsonEntrada) {
        try {
			//FinalProjectUtil.userVerification(httpServletResponse, userSession);
        	DetectedObject detectedObject = (DetectedObject) FinalProjectUtil.fromJson(jsonEntrada, DetectedObject.class);
    		this.detectedObjectServices.addDetectedObject(detectedObject);
            String jsonSalida = FinalProjectUtil.toJson(detectedObject);
             
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            httpServletResponse.setContentType("application/json; charset=UTF-8");
            httpServletResponse.getWriter().println(jsonSalida);
          
        } catch (Exception ex) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
             
        }
    }
    
        //-------------------------------------------------------------------------------------------------------------
    
    @RequestMapping(value = "/requestDetectedObject", method = RequestMethod.GET)
    public @ResponseBody List<DetectedObject> getDetectedObjects(HttpServletResponse httpServletResponse){
    	httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
    	try {
			//FinalProjectUtil.userVerification(httpServletResponse, userSession);
			return this.detectedObjectServices.getDetectedObjectList();
		} catch (IllegalAccessException e){
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
    public @ResponseBody List<DetectedObject> getDetectedObjectsByDate(HttpServletResponse httpServletResponse, @RequestParam ("date") Date date){
    	httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
		try {
			//FinalProjectUtil.userVerification(httpServletResponse, userSession);
			return this.detectedObjectServices.findByDate(date);
    	} catch (IllegalAccessException e){
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
    public @ResponseBody List<DetectedObject> getDetectedObjectsByMonth(HttpServletResponse httpServletResponse, @RequestParam("month") int month){
    	httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
    	try {
			//FinalProjectUtil.userVerification(httpServletResponse, userSession);
			return this.detectedObjectServices.findByMonth(month);
    	} catch (IllegalAccessException e){
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
    public @ResponseBody List<DetectedObject> getDetectedObjectsByYear(HttpServletResponse httpServletResponse, @RequestParam("year") int year){
    	httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
    	try {
			//FinalProjectUtil.userVerification(httpServletResponse, userSession);
			return this.detectedObjectServices.findByYear(year);
    	} catch (IllegalAccessException e){
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
    public @ResponseBody List<DetectedObject> getDetectedObjectsByDatesBetween(HttpServletResponse httpServletResponse, @RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate){
    	httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
    	try {
			//FinalProjectUtil.userVerification(httpServletResponse, userSession);
			return this.detectedObjectServices.findByDatesBetween(startDate, endDate);
    	} catch (IllegalAccessException e){
			logger.info("No ha iniciado sesión!");
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Exception e) {
			logger.info("Error encontrado: " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
    }
    
}
