package com.ssinfo.corider.app.pojo;

import java.io.Serializable;

import com.ssinfo.corider.app.models.AllRoutes;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1514415004861662387L;

	private String userId;
	private String userName;
	private String emailId;
	private String gcmRegistrationId;
	private DeviceInfo device;
	private String status;

	private AllRoutes allRoutes;

	public String getGcmRegistrationId() {
		return gcmRegistrationId;
	}

	public void setGcmRegistrationId(String gcmRegistrationId) {
		this.gcmRegistrationId = gcmRegistrationId;
	}

	public AllRoutes getAllRoutes() {
		return allRoutes;
	}

	public void setAllRoutes(AllRoutes allRoutes) {
		this.allRoutes = allRoutes;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public DeviceInfo getDevice() {
		return device;
	}

	public void setDevice(DeviceInfo device) {
		this.device = device;
	}

	/*
	 * public long getUserId() { return userId; } public void setUserId(long
	 * userId) { this.userId = userId; }
	 */
	public String getUserName() {
		return userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
