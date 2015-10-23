package com.ssinfo.corider.app.models;

import java.io.Serializable;

import com.ssinfo.corider.app.pojo.User;

public class SearchParams implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4308538222359944111L;
	private User user;
	private RiderLocation currentLocation;
	private RiderLocation destination;
	private AllRoutes allRoutes;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public AllRoutes getAllRoutes() {
		return allRoutes;
	}

	public void setAllRoutes(AllRoutes allRoutes) {
		this.allRoutes = allRoutes;
	}

}
