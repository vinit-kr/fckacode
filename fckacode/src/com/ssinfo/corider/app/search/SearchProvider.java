package com.ssinfo.corider.app.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import android.util.Log;

public class SearchProvider {
	
	private static String  mKey = "key=AIzaSyDo-sQTAyovvaFzFLjznY2Xt0i4wyaqMj8";
	
	/** A method to download json data from url */
	public String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();
			
			System.out.println("data is "+data);

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	public String getPlaceDetailsUrl(String ref) {

		// reference of place
		String reference = "reference=" + ref;

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = reference + "&" + sensor + "&" + mKey;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/place/details/"
				+ output + "?" + parameters;
		
		System.out.println("check_url_point_in_android"+url);

		return url;
	}

	public String getPlacesUrl(String qry) {

		try {
			qry = "input=" + URLEncoder.encode(qry, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		// Sensor enabled
		String sensor = "sensor=false";

		// place type to be searched
		String types = "types=geocode";
		
		
		

		// Building the parameters to the web service
		String parameters = qry + "&" + types + "&" + sensor + "&" + mKey;
		

		// Output format
		String output = "json";
		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
				+ output + "?" + parameters;
		
		System.out.println("the_url_of_this_image"+url);
		return url;
		
		
	}

	public String getPlaces(String param) {
		// For storing data from web service
		String data = "";
		String url = getPlacesUrl(param);
		try {
			// Fetching the data from web service in background
			data = downloadUrl(url);
		} catch (Exception e) {
			Log.d("Background Task", e.toString());
		}
		return data;
	}

	private String getPlaceDetails(String reference) {
		String data = "";
		String url = getPlaceDetailsUrl(reference);
		try {
			data = downloadUrl(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

}
