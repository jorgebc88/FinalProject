package com.finalproject.model;

public class DetectedObjectCacheContainer {

	DetectedObjectSouthCache detectedObjectSouthCache = new DetectedObjectSouthCache();

	DetectedObjectNorthCache detectedObjectNorthCache = new DetectedObjectNorthCache();


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
