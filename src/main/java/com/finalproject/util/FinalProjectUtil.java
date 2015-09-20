package com.finalproject.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.model.UserSession;

public class FinalProjectUtil {
	
	public static void addCorsHeader(HttpServletResponse response){
        //TODO: externalize the Allow-Origin
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Max-Age", "1728000");
    }

	public static void userVerification(HttpServletResponse httpServletResponse, UserSession userSession) throws IllegalAccessException{ 
	    if(userSession.getUser() == null){
			throw new IllegalAccessException();
		}
    }

	public static String toJson(Object data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
 
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
	
	public static Object fromJson(String json, Class clazz) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, clazz);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
	
}
