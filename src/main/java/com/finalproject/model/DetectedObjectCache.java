package com.finalproject.model;

public class DetectedObjectCache {
	private int car = 0;
	private int bus = 0;
	private int bike = 0;

	public int getCar() {
		return car;
	}

	public void setCar(int car) {
		this.car = car;
	}

	public int getBus() {
		return bus;
	}

	public void setBus(int bus) {
		this.bus = bus;
	}

	public int getBike() {
		return bike;
	}

	public void setBike(int bike) {
		this.bike = bike;
	}

	public void incrementValue(String type) {
		if (type.equals("Bus")) {
			bus++;
		} else if (type.equals("Car")) {
			car++;
		} else if (type.equals("Bike")) {
			bike++;
		}
	}

	@Override
	public String toString() {
		return "DetectedObjectCache{" +
				"car=" + car +
				", bus=" + bus +
				", bike=" + bike +
				'}';
	}

	public void resetCache() {
		bus = 0;
		car = 0;
		bike = 0;
	}

}
