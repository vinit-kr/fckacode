package com.ssinfo.corider.app.models;

public class Legs {
	private Duration duration;

	private Distance distance;

	private RiderLocation end_location;

	private String start_address;

	private String end_address;

	private RiderLocation start_location;

	private WayPoint[] via_waypoint;

	private Steps[] steps;

	private Duration duration_in_traffic;

	public Duration getDuration_in_traffic() {
		return duration_in_traffic;
	}

	public void setDuration_in_traffic(Duration duration_in_traffic) {
		this.duration_in_traffic = duration_in_traffic;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public Distance getDistance() {
		return distance;
	}

	public void setDistance(Distance distance) {
		this.distance = distance;
	}

	public RiderLocation getEnd_location() {
		return end_location;
	}

	public void setEnd_location(RiderLocation end_location) {
		this.end_location = end_location;
	}

	public String getStart_address() {
		return start_address;
	}

	public void setStart_address(String start_address) {
		this.start_address = start_address;
	}

	public String getEnd_address() {
		return end_address;
	}

	public void setEnd_address(String end_address) {
		this.end_address = end_address;
	}

	public RiderLocation getStart_location() {
		return start_location;
	}

	public void setStart_location(RiderLocation start_location) {
		this.start_location = start_location;
	}

	public WayPoint[] getVia_waypoint() {
		return via_waypoint;
	}

	public void setVia_waypoint(WayPoint[] via_waypoint) {
		this.via_waypoint = via_waypoint;
	}

	public Steps[] getSteps() {
		return steps;
	}

	public void setSteps(Steps[] steps) {
		this.steps = steps;
	}

}
