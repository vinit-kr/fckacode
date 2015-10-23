package com.ssinfo.corider.app.models;

public class RiderLocation {
	private String description;
	private String lng;
	private String city;
	private String country;
	private String locationAddress;
	private double latitude;
	private double longitude;
	
	public RiderLocation(){
		
	}
	public RiderLocation(double latitude, double longitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAddress() {
		return locationAddress;
	}

	public void setAddress(String address) {
		this.locationAddress = address;
	}



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

	private String lat;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

}
