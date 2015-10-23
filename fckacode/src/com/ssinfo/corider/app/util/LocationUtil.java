package com.ssinfo.corider.app.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ssinfo.corider.app.R;
import com.ssinfo.corider.app.constants.Constants;
import com.ssinfo.corider.app.models.MatchedCorider;
import com.ssinfo.corider.app.models.MyLocation;
import com.ssinfo.corider.app.models.RiderLocation;
import com.ssinfo.corider.app.models.RouteCoriders;

public class LocationUtil {

	private static boolean gpsEnabled, networkEnabled;
	private static Context mContext = null;
	private static GpsStatus.Listener mGpsStatusListener;
	//private static final String PROVIDER = "CORIDER_PROVIDER";
	private static List<Marker> coriderMarkerList = new ArrayList<Marker>();
	private static Polyline polyline = null;
	public static Marker currentLocationMarker = null;
	public static Marker destinationMarker = null;
	private Handler mGpsStatusHandler = null;

	public LocationUtil(){
		
	}
	public LocationUtil(Context context){
		context = mContext;
	}
	public static boolean isLocationServiceEnable(Context context) {
		 mContext = context;
		LocationManager lm = null;

		if (lm == null)
			lm = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
		try {
			gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			networkEnabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		if (!gpsEnabled && !networkEnabled) {
			showAlertDialog(context);
			return false;
		}else {
			return true;
		}

	}
/**
 * Function for adding Gps status listener for listen the Gps status.
 */
	public  void addGpsStatusListener() {
		mGpsStatusListener = new GpsStatus.Listener() {

			@Override
			public void onGpsStatusChanged(int event) {
				switch (event) {
				case GpsStatus.GPS_EVENT_STARTED:
					break;
				case GpsStatus.GPS_EVENT_STOPPED:
					mGpsStatusHandler = new Handler();
					mGpsStatusHandler.post(new Runnable() {
						
						@Override
						public void run() {
							showAlertDialog(mContext);		// TODO Auto-generated method stub
							
						}
					});
					

				}

			}
		};
	}

	public void removeGpsStatusListener() {
		mContext = null;
		mGpsStatusListener = null;
	}

	public static void showAlertDialog(Context context) {
		if (context == null) {
			return;
		}
		mContext = context;
		AlertDialog.Builder dialog = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT);
		dialog.setTitle(R.string.gps_network_not_enabled_title);
		dialog.setPositiveButton(R.string.btn_enable, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
				mContext.startActivity(new Intent(
						android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			

			}
		});
		dialog.setNegativeButton(R.string.lbl_cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				((Activity)mContext).finish();

			}
		});
		dialog.show();
	}
	
	public static RiderLocation getCurrentLocation(Context context) {
		Log.i("Locationutil", "getCurrentLocation..");
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = locationManager.getProviders(true);

		Location location = null;
		for (int i = 0; i < providers.size(); i++) {
			location = locationManager.getLastKnownLocation(providers.get(i));
			if (location != null)
				break;
		}
     RiderLocation currentLocation = new RiderLocation();
     currentLocation.setLongitude(location.getLongitude());
     currentLocation.setLatitude(location.getLatitude());
	 return currentLocation;
	}
	
	public static Bundle getMyLocation(Context context) {
		RiderLocation location = getCurrentLocation(context);
		Bundle bundle = new Bundle();
		bundle.putDouble(Constants.CURRENT_LATITUDE, location.getLatitude());
		bundle.putDouble(Constants.CURRENT_LONGITUDE, location.getLongitude());
		return bundle;
	}
	
	public static boolean isPrevLocationSame(RiderLocation currentLocatio, RiderLocation previousLocation){
		
		if(currentLocatio != null && previousLocation != null){
			 if((currentLocatio.getLatitude() == previousLocation.getLatitude()) && (currentLocatio.getLongitude() == previousLocation.getLongitude())){
				 return true;
			 }
		}
		return false;
	}
	
	public static void updateGoogleMarker(GoogleMap map, RiderLocation location,Context context) {
		Log.i("", "Inside the updateGoogleMarker");
		map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		LatLng latLng = null;
		if (map != null) {
			
            if(location !=null){
            	latLng = new LatLng(location.getLatitude(), location.getLongitude());
            }
            MyLocation currentLocation = new MyLocation();
            currentLocation.setLatitude(location.getLatitude());
            currentLocation.setLongitude(location.getLongitude());
            currentLocation.setLastKnownAddress(location.getAddress());
			String address = location.getAddress();
			address += ","+location.getCity();
			if(currentLocationMarker ==null){
				currentLocationMarker = map.addMarker(new MarkerOptions().position(latLng)
						.title(Constants.CURRENT_LOC+","+address));
			}else {
				currentLocationMarker.remove();
				currentLocationMarker = map.addMarker(new MarkerOptions().position(latLng)
						.title(Constants.CURRENT_LOC+","+address));	
			}
			map.setPadding(15, 15, 15, 20);
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
			currentLocationMarker.setVisible(true);
			
			//save user current location in sharedPref.
			saveLastLocation(currentLocation,context);
		}
	}

	public static void setMarker(Context context, final GoogleMap map,RiderLocation sourceLocation, RiderLocation destLocation,String markerType){
		
	 if(map !=null && sourceLocation !=null && destLocation !=null){
		 double destLat = destLocation.getLatitude();
		 double destLnt = destLocation.getLongitude();
		
		 LatLng latLngDest = new LatLng(destLat, destLnt);
		 
		 LatLng latLngSource = new LatLng(sourceLocation.getLatitude(), sourceLocation.getLongitude());
		// map.clear();
		 LatLngBounds.Builder bld = new LatLngBounds.Builder();
		 bld.include(latLngSource);
		 bld.include(latLngDest);
		String address = Constants.CURRENT_LOC+","+sourceLocation.getAddress()+","+sourceLocation.getCity();
		String destinationAddress = Constants.DESTINATION+","+destLocation.getAddress()+","+destLocation.getCity();
		
		if(currentLocationMarker ==null){
			currentLocationMarker = setMarkerOption(map, latLngSource,address ,BitmapDescriptorFactory.HUE_RED,null);	
		}else {
			currentLocationMarker.remove();
			currentLocationMarker = setMarkerOption(map, latLngSource,address ,BitmapDescriptorFactory.HUE_RED,null);
		}
		 if(destinationMarker == null){
			 destinationMarker= setMarkerOption(map, latLngDest, destinationAddress,BitmapDescriptorFactory.HUE_GREEN,null);	 
		 }else {
			 destinationMarker.remove();
			 destinationMarker = setMarkerOption(map, latLngDest, destinationAddress,BitmapDescriptorFactory.HUE_GREEN,null);
		 }
		 map.moveCamera(CameraUpdateFactory.newLatLngBounds(bld.build(), 20));
	 }
	}
	
	public static Marker setMarkerOption(GoogleMap map,LatLng latLng,String address,float markerColor,BitmapDescriptor bitMapDescriptor){
		 MarkerOptions markerOptions = new MarkerOptions();
		 markerOptions.position(latLng);
		 if(bitMapDescriptor !=null){
			 markerOptions.icon(bitMapDescriptor);
		 }else {
			 markerOptions.icon(BitmapDescriptorFactory.defaultMarker(markerColor));	 
		 }
		 
		markerOptions.title(address);
		Marker marker = map.addMarker(markerOptions);
		marker.setVisible(true);
		return marker;
	}
	
	public static Marker moveMarkerToNewLocation(RiderLocation currentLocation){
		if(currentLocation.getLatitude() == 0 || currentLocation.getLongitude() !=0){
			return currentLocationMarker;
		}
		LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
		if(currentLocationMarker !=null){
		currentLocationMarker.setPosition(latLng);
		currentLocationMarker.setTitle(Constants.CURRENT_LOC+","+currentLocation.getAddress());
		}
		return currentLocationMarker;
	}
	public static void setMarkerForCoriders(RouteCoriders[] routeCoriders,GoogleMap map){
		removePrevousCoriderMarkers();
		Set<MatchedCorider> matchedUsers = new HashSet<MatchedCorider>(0);
		
		for(RouteCoriders routeCorider:routeCoriders){
			if(routeCorider == null){
				break;
			}
			MatchedCorider[] coriderList = routeCorider.getMatchedCoriderList();
			for(MatchedCorider matchCorider:coriderList){
				matchedUsers.add(matchCorider);
			}
		}
		Iterator<MatchedCorider> coriderItr = matchedUsers.iterator();
		  while(coriderItr.hasNext()){
			  MatchedCorider corider = coriderItr.next();
			RiderLocation currentLocation = corider.getCurrentLocation();
			LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
			BitmapDescriptor bitmapDesc = BitmapDescriptorFactory.fromResource(R.drawable.marker_flag_red);
			String markerTitle = "corider,"+corider.getUserName()+","+corider.getMatchedDistance()+","+corider.getMatchedRoute();
			Marker coriderMarker = setMarkerOption(map, latLng, markerTitle, BitmapDescriptorFactory.HUE_YELLOW,bitmapDesc);
			coriderMarker.setDraggable(false);
			coriderMarkerList.add(coriderMarker);
		}
	}
	
	public static void removePrevousCoriderMarkers(){
		if(coriderMarkerList !=null && coriderMarkerList.size() > 0){
			for(Marker marker:coriderMarkerList){
				marker.remove();
			}
		}
	}
	
	
	public static void drawPolylines(List<List<LatLng>> routesLatLngList,GoogleMap map){
		
		if(routesLatLngList.size()> 0 ){
			// Instantiates a new Polyline object and adds points to define a rectangle
			int count = 0;
			for(List<LatLng> latLngList:routesLatLngList){
				PolylineOptions polyOptions = new PolylineOptions();
				
				for(LatLng latLng:latLngList){
					polyOptions.add(latLng);
				}
				
				if(count <= 0){
					polyOptions.color(Color.GREEN);
					polyOptions.width(8);
				}else {
					polyOptions.color(Color.YELLOW);
					polyOptions.width(5);
				}
				count++;
				polyOptions.geodesic(true);
				// Get back the mutable Polyline
				polyline = map.addPolyline(polyOptions);
				
			}
		}
		
	}
	
	public static void saveLastLocation(MyLocation location,Context context){
		SharedPreferences pref = context.getSharedPreferences(Constants.CORIDER_PREF, 0);
		Editor edit = pref.edit();
		edit.putString(Constants.CURRENT_LATITUDE, String.valueOf(location.getLatitude()));
		edit.putString(Constants.CURRENT_LONGITUDE, String.valueOf(location.getLongitude()));
		edit.putString(Constants.CURRENT_ADDRESS, location.getLastKnownAddress());
		edit.commit();
		
	}
	
	public static void saveLastLocation(Bundle location,Context context){
		SharedPreferences pref = context.getSharedPreferences(Constants.CORIDER_PREF, 0);
		Editor edit = pref.edit();
		
		edit.putString(Constants.CURRENT_LATITUDE, String.valueOf(location.getDouble(Constants.CURRENT_LATITUDE)));
		edit.putString(Constants.CURRENT_LONGITUDE, String.valueOf(location.getDouble(Constants.CURRENT_LONGITUDE)));
		edit.putString(Constants.CURRENT_ADDRESS, location.getString(Constants.CURRENT_ADDRESS));
		edit.commit();
		
	}
	
	public MyLocation getLastSavedLocation(Context context){
		SharedPreferences pref = context.getSharedPreferences(Constants.CORIDER_PREF, 0);
		if((pref.getString(Constants.CURRENT_LATITUDE, null) != null) && (pref.getString(Constants.CURRENT_LONGITUDE, null) !=null)){
			MyLocation lastLocation = new MyLocation();
			lastLocation.setLatitude(Double.valueOf(pref.getString(Constants.CURRENT_LATITUDE, "")));
			lastLocation.setLongitude(Double.valueOf(pref.getString(Constants.CURRENT_LONGITUDE, "")));
			lastLocation.setLastKnownAddress(pref.getString(Constants.CURRENT_ADDRESS, null));
			return lastLocation;
		}
		
		return null;
	}
	
	

}