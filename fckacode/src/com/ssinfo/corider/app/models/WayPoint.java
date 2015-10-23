package com.ssinfo.corider.app.models;

public class WayPoint {
	private String step_interpolation;

	private String step_index;

	private RiderLocation location;

	public String getStep_interpolation() {
		return step_interpolation;
	}

	public void setStep_interpolation(String step_interpolation) {
		this.step_interpolation = step_interpolation;
	}

	public String getStep_index() {
		return step_index;
	}

	public void setStep_index(String step_index) {
		this.step_index = step_index;
	}

	public RiderLocation getLocation() {
		return location;
	}

	public void setLocation(RiderLocation location) {
		this.location = location;
	}
}
