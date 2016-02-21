package com.finalproject.model;

/**
 */
public enum PointingAt {
	NORTH("North"),
	WEST("West"),
	SOUTH("South"),
	EAST("East");

	private final String pointingAt;

	PointingAt(String pointingAt){
		this.pointingAt = pointingAt;
	}

	public String getPointingAt() {
		return pointingAt;
	}
	public static String getPointingAt(String pointingAt) {
		switch (pointingAt.toLowerCase().trim()){
			case "north": return PointingAt.NORTH.getPointingAt();
			case "west": return PointingAt.WEST.getPointingAt();
			case "south": return PointingAt.SOUTH.getPointingAt();
			case "east": return PointingAt.EAST.getPointingAt();
			default: return PointingAt.SOUTH.getPointingAt();
		}
	}
}
