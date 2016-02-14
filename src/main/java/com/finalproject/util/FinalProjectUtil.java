package com.finalproject.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.model.DetectedObject;
import com.finalproject.model.DetectedObjectNorthCache;
import com.finalproject.model.DetectedObjectSouthCache;
import com.finalproject.model.UserSession;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FinalProjectUtil {

	public static void addCorsHeader(HttpServletResponse response) {
		// TODO: externalize the Allow-Origin
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
		response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
		response.addHeader("Access-Control-Max-Age", "1728000");
	}

	public static void userVerification(HttpServletResponse httpServletResponse, UserSession userSession)
			throws IllegalAccessException {
		if (userSession.getUser() == null) {
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

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static Object fromJson(String json, Class clazz) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(json, clazz);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static void modifyDetectedObjectCache(DetectedObject detectedObject, DetectedObjectSouthCache detectedObjectSouthCache, DetectedObjectNorthCache detectedObjectNorthCache) {
		String type = detectedObject.getObjectType();
		String direction = detectedObject.getDirection();
		if (direction.equals("South")) {
			detectedObjectSouthCache.incrementValue(type);
		} else {
			detectedObjectNorthCache.incrementValue(type);
		}
	}


}
