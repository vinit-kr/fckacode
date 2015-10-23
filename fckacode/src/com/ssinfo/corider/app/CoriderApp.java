package com.ssinfo.corider.app;

import java.util.List;
import java.util.Properties;

import android.app.Application;
import android.content.res.Configuration;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.ssinfo.corider.app.handlers.ConfigHandler;
import com.ssinfo.corider.app.handlers.ConfigHandler.OnConfigDataListener;
import com.ssinfo.corider.app.models.ConfigInfo;
import com.ssinfo.corider.app.models.ConfigInfo.Info;
import com.ssinfo.corider.app.network.request.Constants;


public class CoriderApp extends Application implements OnConfigDataListener {

	private ConfigInfo config = null;
	private static int timeInterval = 180000; // in millisecond (every three minutes).
	private static int IDLE_TIME_INTERVAL_AFTER_MATCHED_CORIDER_FOUND= 120000; // after two minutes (it will be configurable from server).
	private ConfigHandler mConfigHandler = null;
	private static Properties configProperties;
	private Tracker appTracker = null;
	private String PROPERTY_ID = "UA-60966335-1";
	public static int getTimeInterval() {
		return timeInterval;
	}

	public static int getIdleTimeInterval(){
		return IDLE_TIME_INTERVAL_AFTER_MATCHED_CORIDER_FOUND;
	}
	public void setTimeInterval(int timeInterval) {
		this.timeInterval = timeInterval;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mConfigHandler = new ConfigHandler();
		mConfigHandler.setOnConfigDataListener(this);
		mConfigHandler.getAppConfigInfo();
		 GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
		 appTracker =  analytics.newTracker(PROPERTY_ID);
		 appTracker.enableAdvertisingIdCollection(true);
	}
	
	public Tracker getAnalyticsTracker(){
	  return appTracker;
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public ConfigInfo getConfig() {
		return config;
	}

	public void setConfig(ConfigInfo config) {
		this.config = config;
	}

	@Override
	public void onConfigReturn(ConfigInfo config) {
		initConfigInfo(config);
	}

	@Override
	public void onConfigFail(String message) {
		this.config = null;

	}

	public void initConfigInfo(ConfigInfo configInfo){
		configProperties = new Properties();
		List<ConfigInfo.Info> infoList = configInfo.getConfigList();
		for(Info info:infoList){
			configProperties.put(info.getKey(), info.getValue());
		}
	}
	
}
