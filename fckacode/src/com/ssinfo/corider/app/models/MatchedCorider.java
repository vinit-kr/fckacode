package com.ssinfo.corider.app.models;

import java.io.Serializable;

public class MatchedCorider implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MatchedCorider() {

	}

	private long userId;
	private String userName;
	private String emailId;
	private long matchedDistance;
	private int hour;
	private int minutes;
	private String matchedRoute;
	private RiderLocation currentLocation;
	private RiderLocation destination;
	private String gcmRegistrationId;

	public String getGcmRegistrationId() {
		return gcmRegistrationId;
	}

	public void setGcmRegistrationId(String gcmRegistrationId) {
		this.gcmRegistrationId = gcmRegistrationId;
	}

	public RiderLocation getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(RiderLocation currentLocation) {
		this.currentLocation = currentLocation;
	}

	public RiderLocation getDestination() {
		return destination;
	}

	public void setDestination(RiderLocation destination) {
		this.destination = destination;
	}

	public String getMatchedRoute() {
		return matchedRoute;
	}

	public void setMatchedRoute(String matchedRoute) {
		this.matchedRoute = matchedRoute;
	}

	public long getMatchedDistance() {
		return matchedDistance;
	}

	public void setMatchedDistance(long matchedDistance) {
		this.matchedDistance = matchedDistance;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MatchedCorider) {
			if (this.userId == ((MatchedCorider) obj).userId) {
				return true;
			}
		}
		return false;

	}

}
