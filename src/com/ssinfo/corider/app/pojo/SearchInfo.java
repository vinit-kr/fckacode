package com.ssinfo.corider.app.pojo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class SearchInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6331866836723832577L;
	private int searchId;
	private String searchType;
	private Date searchDate;
	private Time searchTime;
	private List<User> availableCoriderList;
	private List<User> matchedCoriderList;
	private List<User> contactedMatchedCoriderList;
	private int availableCoridersCount;
	private int matchedCoridersCount;

	public int getSearchId() {
		return searchId;
	}

	public void setSearchId(int searchId) {
		this.searchId = searchId;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public Date getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}

	public Time getSearchTime() {
		return searchTime;
	}

	public void setSearchTime(Time searchTime) {
		this.searchTime = searchTime;
	}

	public List<User> getAvailableCoriderList() {
		return availableCoriderList;
	}

	public void setAvailableCoriderList(List<User> availableCoriderList) {
		this.availableCoriderList = availableCoriderList;
	}

	public List<User> getMatchedCoriderList() {
		return matchedCoriderList;
	}

	public void setMatchedCoriderList(List<User> matchedCoriderList) {
		this.matchedCoriderList = matchedCoriderList;
	}

	public List<User> getContactedMatchedCoriderList() {
		return contactedMatchedCoriderList;
	}

	public void setContactedMatchedCoriderList(
			List<User> contactedMatchedCoriderList) {
		this.contactedMatchedCoriderList = contactedMatchedCoriderList;
	}

	public int getAvailableCoridersCount() {
		return availableCoridersCount;
	}

	public void setAvailableCoridersCount(int availableCoridersCount) {
		this.availableCoridersCount = availableCoridersCount;
	}

	public int getMatchedCoridersCount() {
		return matchedCoridersCount;
	}

	public void setMatchedCoridersCount(int matchedCoridersCount) {
		this.matchedCoridersCount = matchedCoridersCount;
	}

}
