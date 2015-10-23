package com.ssinfo.corider.app.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.ssinfo.corider.app.activities.HomeActivity;
import com.ssinfo.corider.app.constants.Constants;
import com.ssinfo.corider.app.pojo.DeviceInfo;
import com.ssinfo.corider.app.pojo.User;

@SuppressLint("NewApi")
public class AppUtil {
	private static final String TAG = AppUtil.class.getName(); 
	
	public static boolean isUserRegistered(Context context){
		SharedPreferences sharedPref = context.getSharedPreferences(Constants.USER_PREF, 0);
		String userId = sharedPref.getString(Constants.USER_ID, null);
		if(userId != null){
			return true;
		}
		return false;
	}
	
	public static boolean isUserActive(Context context){
		SharedPreferences sharedPref = context.getSharedPreferences(Constants.USER_PREF, 0);
		String userStatus = sharedPref.getString(Constants.USER_STATUS, null);
		if(userStatus !=null && Constants.ACTIVE_USER.equals(userStatus)){
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean isNumber(String val){
		try{
		int number = Integer.parseInt(val);
		}catch(NumberFormatException nfe){
			Log.i(TAG, val+" is not a number");
			return false;
		}
		return true;
	}
	
	public static String convertInKm(String distance){
		Double totalDistance = Double.valueOf(distance);
		double distanceInKm = totalDistance  * 0.001;
		return String.format("%.2f", distanceInKm);
	}
	
	public static String convertInMin(String totalSeconds){
		int seconds = Integer.parseInt(totalSeconds);
	     int minutes = seconds/60;	  
		return String.valueOf(minutes);
	}
	
	public static void saveUserInfoInSharedPref(Context context,User user){
		SharedPreferences sharedPref = context.getSharedPreferences(Constants.USER_PREF, 0);
		Editor editor = sharedPref.edit();
		editor.putString(Constants.USER_ID,   user.getUserId());
		editor.putString(Constants.USER_NAME, user.getUserName());
		editor.putString(Constants.EMAIL_ID,  user.getEmailId());
		editor.putString(Constants.USER_STATUS, user.getStatus());
		editor.putString(Constants.GCM_ID, user.getGcmRegistrationId());
		//editor.putString(Constants.USER_ID, String.valueOf(user.getUserId()));
		editor.commit();
	}
	
	public static User getUserInfo(Context context){
		User userInfo = null;
		SharedPreferences sharedPref = context.getSharedPreferences(Constants.USER_PREF, 0);
		String userId = sharedPref.getString(Constants.USER_ID, null);
		String userName = sharedPref.getString(Constants.USER_NAME, null);
		String emailId = sharedPref.getString(Constants.EMAIL_ID, null);
		String status = sharedPref.getString(Constants.USER_STATUS, "2");
		String gcmId = sharedPref.getString(Constants.GCM_ID, null);
		if(userId != null){
			userInfo = new User();
			userInfo.setUserId(userId);
			userInfo.setUserName(userName);
			userInfo.setEmailId(emailId);
			userInfo.setStatus(status);
			userInfo.setGcmRegistrationId(gcmId);
			
		}
		return userInfo;
	
	}
	
	  /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    public static void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.PROPERTY_REG_ID, regId);
        editor.putInt(Constants.PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    public static String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(Constants.PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(Constants.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private static SharedPreferences getGcmPreferences(Context context) {
        return context.getSharedPreferences(HomeActivity.class.getName(), Context.MODE_PRIVATE);
    }
    
    
	public static DeviceInfo getDeviceInfo(Context context){
		
		DeviceInfo deviceInfo = new DeviceInfo();
		String s="Debug-infos:";
		s += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
		s += "\n OS API Level: " + android.os.Build.VERSION.SDK;
		s += "\n Device: " + android.os.Build.DEVICE;
		s += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";
		System.out.println(s);
		
		deviceInfo.setModelNo(android.os.Build.MODEL);
		deviceInfo.setOsType("android");
		deviceInfo.setOsVersion(System.getProperty("os.version"));
		deviceInfo.setAppVersion("1.0");
		deviceInfo.setScreenResolution(getScrenResolution(context));
		deviceInfo.setScreenSize(String.valueOf(getScreenSizeInInch(context)));
		return deviceInfo;
	}
	public static double getScreenSizeInInch(Context context){
		DisplayMetrics dm = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		double x = Math.pow(dm.widthPixels/dm.xdpi,2);
		double y = Math.pow(dm.heightPixels/dm.ydpi,2);
		double screenInches = Math.sqrt(x+y);
		return screenInches;
	}
	
	 public static String getScrenResolution(Context context){
		String resoultion = Constants.UNKNOWN;
		WindowManager wManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wManager.getDefaultDisplay();
		if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB){
			resoultion = display.getHeight() +"x" + display.getHeight();
			return resoultion;
		}else {
			Point screenPoints = new Point();
			 display.getSize(screenPoints);
			 resoultion = screenPoints.x +"x"+screenPoints.y;
		}
		return resoultion;
		
	}
	
	public static void writeToSdcard(Context context,String response){
		try{
		 File myFile = new File("/sdcard/myTest.txt");
         myFile.createNewFile();
         FileOutputStream fOut = new FileOutputStream(myFile);
         OutputStreamWriter myOutWriter = 
                                 new OutputStreamWriter(fOut);
         myOutWriter.append(response);
         myOutWriter.close();
         fOut.close();
         Toast.makeText(context, "Response successfully write to sdcard",
                 Toast.LENGTH_SHORT).show();
     } catch (Exception e) {
         Toast.makeText(context, e.getMessage(),
                 Toast.LENGTH_SHORT).show();
     }
	}
	

}
