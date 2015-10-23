package com.ssinfo.corider.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Validator {
	public static boolean isValidEmail(String emailAddress) {
		if (emailAddress == null) {
          return false;
		}
		return android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress)
				.matches();
	}

	public static boolean isValidUserName(String userName) {
		userName.matches("[A-Za-z0-9._-]{15,20}");
		return true;
	}

	public static boolean isValidName(String name) {
		return name.matches("[A-Za-z0-9._-]{10,15}");

	}
	
  public static boolean isValidDate(String date) {
    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    try {
      formatter.parse(date);
      return true;
    } catch (ParseException e) {
      return false;
    }

  }
  
  public static boolean isValidTime(String time){
    SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
    try {
      formatter.parse(time);
      return true;
    } catch (ParseException e) {
      return false;
    }
  }


}
