package com.ssinfo.corider.app.models;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ssinfo.corider.app.R;
import com.ssinfo.corider.app.adapters.ListViewAdapterHome.RowType;
import com.ssinfo.corider.app.fragments.MatchedCoridersFragment.Item;

public class ListItem implements Item{
	
	private long userId;
	private String userName;
	private String emailId;
	private long matchedDistance;
	private String distanceInKm = "0";
	private String gcmRegistrationId;
	//private int hour;
	private int minutes;
	private String matchedRoute;
	private ViewHolder mHolder;
	
	@Override
	public int getViewType() {
		return RowType.LIST_ITEM.ordinal();
	}
   
	public ListItem(String userName,String emailId, long matchedDistance,String matchedRoute,int minute,String gcmRegistrationId){
		this.userName = userName;
		this.emailId = emailId;
		this.matchedDistance = matchedDistance;
		this.distanceInKm = convertInKm(String.valueOf(matchedDistance));
		this.matchedRoute = matchedRoute;
		this.minutes = Math.round(minute/60);
		this.gcmRegistrationId = gcmRegistrationId;
		
	}
	
	public String getGcmRegistrationId() {
		return gcmRegistrationId;
	}

	public void setGcmRegistrationId(String gcmRegistrationId) {
		this.gcmRegistrationId = gcmRegistrationId;
	}

	public String convertInKm(String distance){
		Double totalDistance = Double.valueOf(distance);
		double distanceInKm = totalDistance  * 0.001;
		return String.format("%.2f", distanceInKm);
	}
	
	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.coriders_item, null);
			mHolder = new ViewHolder();
			mHolder.coriderName = (TextView) convertView.findViewById(R.id.coriderName);
			mHolder.matchedKm = (TextView) convertView.findViewById(R.id.distance);
			mHolder.destination = (TextView) convertView.findViewById(R.id.destination);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		
         mHolder.coriderName.setText(this.userName);
         mHolder.matchedKm.setText(this.distanceInKm +" km");
         mHolder.destination.setText(this.matchedRoute);		
		
		return convertView;
	  }

	public long getMatchedDistance() {
		return matchedDistance;
	}

	public void setMatchedDistance(long matchedDistance) {
		this.matchedDistance = matchedDistance;
	}

	public String getDistanceInKm() {
		return distanceInKm;
	}

	public void setDistanceInKm(String distanceInKm) {
		this.distanceInKm = distanceInKm;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public String getMatchedRoute() {
		return matchedRoute;
	}

	public void setMatchedRoute(String matchedRoute) {
		this.matchedRoute = matchedRoute;
	}

	public ViewHolder getmHolder() {
		return mHolder;
	}

	public void setmHolder(ViewHolder mHolder) {
		this.mHolder = mHolder;
	}

	private static class ViewHolder {
		TextView coriderName;
		TextView matchedKm;
	    TextView destination;	
	}
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
}
