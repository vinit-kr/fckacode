package com.ssinfo.corider.app.models;

public class MyLocation {

	public double latitude;
	public double longitude;
	public String lastKnownAddress;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getLastKnownAddress() {
		return lastKnownAddress;
	}

	public void setLastKnownAddress(String lastKnownAddress) {
		this.lastKnownAddress = lastKnownAddress;
	}

}
