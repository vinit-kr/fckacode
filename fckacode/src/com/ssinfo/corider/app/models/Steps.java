package com.ssinfo.corider.app.models;

public class Steps {

	private String html_instructions;

	private Duration duration;

	private Distance distance;

	private RiderLocation end_location;

	private Polyline polyline;

	private RiderLocation start_location;

	private String travel_mode;

	public String getHtml_instructions() {
		return html_instructions;
	}

	public void setHtml_instructions(String html_instructions) {
		this.html_instructions = html_instructions;
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

	public Polyline getPolyline() {
		return polyline;
	}

	public void setPolyline(Polyline polyline) {
		this.polyline = polyline;
	}

	public RiderLocation getStart_location() {
		return start_location;
	}

	public void setStart_location(RiderLocation start_location) {
		this.start_location = start_location;
	}

	public String getTravel_mode() {
		return travel_mode;
	}

	public void setTravel_mode(String travel_mode) {
		this.travel_mode = travel_mode;
	}
}
