package com.ssinfo.corider.app.constants;

public interface Constants {

	public String USER_NAME = "userName";
	public String USER_ID = "userId";
	public String UNKNOWN = "Unknown";
	public String USER_STATUS = "userStatus";
	public String GCM_ID = "gcm_id";
	public String BLANK = "";
	public String SPACE = " ";

	public String CURRENT_LATITUDE = "current_latitude";
	public String CURRENT_LONGITUDE = "current_longitude";
	public String CURRENT_ADDRESS = "currentAddress";
	public String CURRENT_CITY = "city";
	public String CURRENT_COUNTRY = "country";
	public String SOURCE_ADDRESS = "sourceAddress";

	public String DESTINATION_ADDRESS = "destination_address";
	public String SOURCE_LATITUDE = "source_latitude";
	public String SOURCE_LONGITUDE = "source_longitude";
	public String DESTINATION_LONGITUDE = "destination_longitude";
	public String DESTINATION_LATITUDE = "destination_latitude";

	public String CORIDER_PREF = "coriderPref";

	// email id for giving feedback
	public String FEEDBACK_EMAIL = "vinit.javaengineer@gmail.com";
	public String FEEDBACK_SUBJECT = "feedback on corider";

	public String ABOUT_US = "http://www.snagfilms.com/mobile/about";
	public String TERMS_OF_SERVICE = "http://www.snagfilms.com/tos";

	public String EMAIL_ID = "emailId";

	public String USER_PREF = "userInfo";

	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	public static final String PROPERTY_APP_VERSION = "appVersion";
	public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
	//User status //TODO need to use enum for status.
	 //user status
	  public static final String ACTIVE_USER = "1";
	  public static final String VERIFICATION_PENDING = "2";
	  public static final String INACTIVE_USER = "3";
	  public static final String INVALID_VERIFICATION_CODE = "Invalid Verification Code";
	  public static final String USER_ACCOUNT_STATUS = "accountStatus";
	  	  
	  //for gcm message key
	  public static final String MESSAGE_TYPE = "messageType";
	  public static final String GCM_USER = "toUserId";
	  public static final String TO_USER_NAME = "toUserName";
	  public static final String MESSAGE = "message";
	  public static final String MESSAGE_AVAIL_USER = "availableCoriders";
	  
	  public static final String GCM_MESSAGE_ACTION = "gcm_messages";
	  
	  public static final String FROM_USER_ID = "fromUserId";
	  public static final String FROM_USER_NAME = "fromUserName";
	  public  static final String TO_USER_ID = "toUserId";
	  public static final String HEADER= "headerTitle";
	  public static final String MATCHED_DISTANCE = "matchedDistance";
	  public static final String DEST_ADDRESS = "destination";
	  
	  public static final String MAP_FRAGMENT = "mapFragment";
	  
	  public static final String CURRENT_LOC = "CurrentLocation";
	  public static final String DESTINATION = "Destination";
	  
	  //Configuration key
	  public static final String ANDROID_SHARE_MSG = "android_share_msg";
	  public static final String ANDROID_FEEDBACK_SUBJECT = "android_feedback_subject";
	  public static final String  ANDROID_FINDING_CURRENT_LOC_MSG ="android_finding_current_loc_msg";
	  public static final String ANDROID_CURRENT_LOCATION_FETCH_INTERVAL = "android_current_location_fetch_interval";
}
