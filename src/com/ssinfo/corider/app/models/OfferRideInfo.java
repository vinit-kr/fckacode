package com.ssinfo.corider.app.models;

import java.io.Serializable;
import java.util.Calendar;

import com.ssinfo.corider.app.pojo.LocationDetails;

public class OfferRideInfo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private LocationDetails sourceLoc;
  private LocationDetails destinationLoc;
  private String departureDate;
  private String departueTime;
  private String returnDate;
  private String returnTime;
  private String price;
  private String availableSeats;
  private boolean isRoundTrip;
  private String riderComments;
  private String maxLuggageSize;
  private String leaveOnTime;
  private String maxDetour;

  public LocationDetails getSourceLoc() {
    return sourceLoc;
  }

  public void setSourceLoc(LocationDetails sourceLoc) {
    this.sourceLoc = sourceLoc;
  }

  public LocationDetails getDestinationLoc() {
    return destinationLoc;
  }

  public void setDestinationLoc(LocationDetails destinationLoc) {
    this.destinationLoc = destinationLoc;
  }

  public String getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(String departureDate) {
    this.departureDate = departureDate;
  }

  public String getReturnDate() {
    return returnDate;
  }

  public void setReturnDate(String returnDate) {
    this.returnDate = returnDate;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getAvailableSeats() {
    return availableSeats;
  }

  public void setAvailableSeats(String availableSeats) {
    this.availableSeats = availableSeats;
  }

  public boolean isRoundTrip() {
    return isRoundTrip;
  }

  public void setRoundTrip(boolean isRoundTrip) {
    this.isRoundTrip = isRoundTrip;
  }

  public String getRiderComments() {
    return riderComments;
  }

  public void setRiderComments(String riderComments) {
    this.riderComments = riderComments;
  }

  public String getMaxLuggageSize() {
    return maxLuggageSize;
  }

  public void setMaxLuggageSize(String maxLuggageSize) {
    this.maxLuggageSize = maxLuggageSize;
  }

  public String getLeaveOnTime() {
    return leaveOnTime;
  }

  public void setLeaveOnTime(String leaveOnTime) {
    this.leaveOnTime = leaveOnTime;
  }

  public String getMaxDetour() {
    return maxDetour;
  }

  public void setMaxDetour(String maxDetour) {
    this.maxDetour = maxDetour;
  }

  public String getDepartueTime() {
    return departueTime;
  }

  public void setDepartueTime(String departueTime) {
    this.departueTime = departueTime;
  }

  public String getReturnTime() {
    return returnTime;
  }

  public void setReturnTime(String returnTime) {
    this.returnTime = returnTime;
  }



}
