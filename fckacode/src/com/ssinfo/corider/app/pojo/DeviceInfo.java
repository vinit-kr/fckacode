package com.ssinfo.corider.app.pojo;

import java.io.Serializable;

public class DeviceInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -920623887777256879L;
	private int deviceInfoId;
	private int userId;
	private String osType;
	private String osVersion;
	private String appVersion;
	private String modelNo;
	private String screenSize;
	private String screenResolution;

	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getDeviceInfoId() {
		return deviceInfoId;
	}

	public void setDeviceInfoId(int deviceInfoId) {
		this.deviceInfoId = deviceInfoId;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getModelNo() {
		return modelNo;
	}

	public void setModelNo(String modelNo) {
		this.modelNo = modelNo;
	}

	public String getScreenSize() {
		return screenSize;
	}

	public void setScreenSize(String screenSize) {
		this.screenSize = screenSize;
	}

	public String getScreenResolution() {
		return screenResolution;
	}

	public void setScreenResolution(String screenResolution) {
		this.screenResolution = screenResolution;
	}

}
