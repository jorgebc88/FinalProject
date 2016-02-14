package com.finalproject.model;

import org.springframework.beans.factory.annotation.Autowired;


public class DetectedObjectCacheContainer {

	@Autowired
	DetectedObjectSouthCache detectedObjectSouthCache;

	@Autowired
	DetectedObjectNorthCache detectedObjectNorthCache;


	public DetectedObjectNorthCache getDetectedObjectNorthCache() {
		return detectedObjectNorthCache;
	}

	public void setDetectedObjectNorthCache(DetectedObjectNorthCache detectedObjectNorthCache) {
		this.detectedObjectNorthCache = detectedObjectNorthCache;
	}

	public DetectedObjectSouthCache getDetectedObjectSouthCache() {
		return detectedObjectSouthCache;
	}

	public void setDetectedObjectSouthCache(DetectedObjectSouthCache detectedObjectSouthCache) {
		this.detectedObjectSouthCache = detectedObjectSouthCache;
	}


}
