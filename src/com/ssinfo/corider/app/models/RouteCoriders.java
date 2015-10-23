package com.ssinfo.corider.app.models;

import java.util.List;

public class RouteCoriders {

	public RouteCoriders() {
		// TODO Auto-generated constructor stub
	}
	private String routeName;
	private String totalDistanceOfRoute;
	private String totalDuration;
	private int status;
	private MatchedCorider[] matchedCoriderList;

	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}

	public MatchedCorider[] getMatchedCoriderList() {
		return matchedCoriderList;
	}

	public void setMatchedCoriderList(List<MatchedCorider> matchedCoriderList) {
		this.matchedCoriderList = matchedCoriderList.toArray(new MatchedCorider[matchedCoriderList.size()]);
	}
	
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public String getTotalDistanceOfRoute() {
		return totalDistanceOfRoute;
	}
	public void setTotalDistanceOfRoute(String totalDistanceOfRoute) {
		this.totalDistanceOfRoute = totalDistanceOfRoute;
	}
	public String getTotalDuration() {
		return totalDuration;
	}
	public void setTotalDuration(String totalDuration) {
		this.totalDuration = totalDuration;
	}

}
