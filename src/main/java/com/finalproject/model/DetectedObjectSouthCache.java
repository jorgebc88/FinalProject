package com.finalproject.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "singleton")
public class DetectedObjectSouthCache extends DetectedObjectCache {
	private String direction = "South";
	
}
