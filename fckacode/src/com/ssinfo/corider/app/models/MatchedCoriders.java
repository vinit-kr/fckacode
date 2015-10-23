package com.ssinfo.corider.app.models;

import java.io.Serializable;
import java.util.List;

public class MatchedCoriders implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5617751747871377132L;
	private int status;
	private int totalMatchedUser;

	private RouteCoriders[] routeCoriders;
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public RouteCoriders[] getRouteCoriders() {
		return routeCoriders;
	}

	public void setRouteCoriders(RouteCoriders[] routeCoriders) {
		this.routeCoriders = routeCoriders;
	}

	public MatchedCoriders() {
		// TODO Auto-generated constructor stub
	}
	 public int getTotalMatchedUser() {
			return totalMatchedUser;
		}

		public void setTotalMatchedUser(int totalMatchedUser) {
			this.totalMatchedUser = totalMatchedUser;
		}
	

}
