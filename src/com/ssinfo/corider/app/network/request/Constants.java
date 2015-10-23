package com.ssinfo.corider.app.network.request;


public interface Constants {
	//Need to change this base url
	//public static final String CORIDER_BASE_LOCAL_URL = "http://54.169.71.224:8080/corider"; // production server - AWS
	public static final String CORIDER_BASE_LOCAL_URL = "http://192.168.0.51:8089/corider"; //development server
	public static final String SAVE_USER = CORIDER_BASE_LOCAL_URL + "/user/register";
	public static final String FIND_MATCHED_CORIDERS = CORIDER_BASE_LOCAL_URL + "/user/matchedCoriders";
	public static final String FIND_AVAILABLE_CORIDERS = CORIDER_BASE_LOCAL_URL+"/user/availableCoriders";
	public static final String REFRESH_ROUTES = CORIDER_BASE_LOCAL_URL + "/user/refresh?";
	public static final String SEND_MESSAGE = CORIDER_BASE_LOCAL_URL + "/user/sendMessage";
	public static final String IS_USER_ACTIVE = CORIDER_BASE_LOCAL_URL+"/user/isActive?";
	public static final String APP_CONFIG_URL = CORIDER_BASE_LOCAL_URL+"/user/configInfo/android";
	
	
	public static final String JSON = ".json";
	public static final String ADD_QUERY = "?";
	public static final String NETWORK_ERR = "Network Error";
	public static final String NO_RESPONSE = "No Response";
	public static final String ERROR = "Error";
	
	//Direction api
	public static final String DIRECTION_URL = "https://maps.googleapis.com/maps/api/directions/json?";
	public String API_KEY = "AIzaSyBK4yT7O97nyY43fLft8FKaWXXEH_PLiE0";
	
	
	//config keys
	public String SHARE_MSG = "android_share_msg";
	public String FEEDBACK_SUJECT = "android_feedback_subject";
	public String FINDING_CURRENT_LOC_MSG = "android_finding_current_loc_msg";
	public String CURRENT_LOC_FETCH_INTERVAL = "android_current_location_fetch_interval";
	public String IDLE_MSG_SHOW_INTERVAL = "idle_msg_show_interval";
	public String FEEDBACK_EMAIL_ID = "feedback_email_id";
	public String  MAX_IDLE_TIME_AFTER_MATCHED_RESPONSE= "max_idle_time_after_matched_response ";
   }
