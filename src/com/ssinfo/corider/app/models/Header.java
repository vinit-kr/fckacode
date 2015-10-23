package com.ssinfo.corider.app.models;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ssinfo.corider.app.R;
import com.ssinfo.corider.app.adapters.ListViewAdapterHome.RowType;
import com.ssinfo.corider.app.fragments.MatchedCoridersFragment.Item;

public class Header implements Item{
	private String routeName;
	private String totalDistanceOfRoute;
	private String totalDuration;
	private int totalMatchedUser;
	private ViewHolder mViewHolder;
	
	public Header(String routeName, String totalDistance,String totalDuration,int totalMatchedUser){
		this.routeName = routeName;
		this.totalDistanceOfRoute = convertInKm(totalDistance);
		this.totalDuration = convertInMin(totalDuration);
		this.totalMatchedUser = totalMatchedUser;
	}
	
	public String convertInKm(String distance){
		Double totalDistance = Double.valueOf(distance);
		double distanceInKm = totalDistance  * 0.001;
		return String.format("%.2f", distanceInKm);
	}
	
	public String convertInMin(String totalSeconds){
		int seconds = Integer.parseInt(totalSeconds);
	     int minutes = seconds/60;	  
		return String.valueOf(minutes);
	}
	
	@Override
	public int getViewType() {
		return RowType.HEADER_ITEM.ordinal();
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		if(convertView == null){
			convertView = inflater.inflate(R.layout.list_header, null);
			mViewHolder = new ViewHolder();
			mViewHolder.routeSummary = (TextView) convertView.findViewById(R.id.routeSummary);
			mViewHolder.duration = (TextView) convertView.findViewById(R.id.duration);
			mViewHolder.totalDistance = (TextView) convertView.findViewById(R.id.distance);
			mViewHolder.totalMatchedUser = (TextView) convertView.findViewById(R.id.routeCoriders);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		mViewHolder.routeSummary.setText(this.routeName);
		mViewHolder.totalDistance.setText(this.totalDistanceOfRoute+" km");
		mViewHolder.duration.setText(this.totalDuration+" min");
		mViewHolder.totalMatchedUser.setText(this.totalMatchedUser+" coriders");
		return convertView;
	}
	private static class ViewHolder {
		TextView routeSummary;
		TextView totalDistance;
	    TextView duration;
	    TextView totalMatchedUser;
	}
	

}
