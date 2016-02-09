package com.finalproject.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "singleton")
public class DetectedObjectNorthCache extends DetectedObjectCache {
	private String direction = "North";
}