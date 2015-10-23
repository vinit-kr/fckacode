package com.ssinfo.corider.app.tasks;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.ssinfo.corider.app.models.RiderLocation;

public class GetAddressTask extends AsyncTask<RiderLocation, Void, RiderLocation> {

	// Store the context passed to the AsyncTask when the system instantiates
	// it.
	private Context localContext;

	// Constructor called by the system to instantiate the task
	public GetAddressTask(Context context) {

		// Required by the semantics of AsyncTask
		super();

		// Set a Context for the background task
		this.localContext = context;
	}

	/**
	 * Get a geocoding service instance, pass latitude and longitude to it,
	 * format the returned address, and return the address to the UI thread.
	 */
	@Override
	protected RiderLocation doInBackground(RiderLocation... params) {
		/*
		 * Get a new geocoding service instance, set for localized addresses.
		 * This example uses android.location.Geocoder, but other geocoders that
		 * conform to address standards can also be used.
		 */
		Geocoder geocoder = new Geocoder(this.localContext, Locale.getDefault());

		// Get the current location from the input parameter list
		RiderLocation location = params[0];

		// Create a list to contain the result address
		List<Address> addresses = null;

		// Try to get an address for the current location. Catch IO or network
		// problems.
		try {

			/*
			 * Call the synchronous getFromLocation() method with the latitude
			 * and longitude of the current location. Return at most 1 address.
			 */
			addresses = geocoder.getFromLocation(location.getLatitude(),
					location.getLongitude(), 1);

			// Catch network or other I/O problems.
		} catch (IOException exception1) {

			// Log an error and return an error message
			Log.e("GetAddressTask ", exception1.getMessage());

			// print the stack trace
			exception1.printStackTrace();

			return null;

			// Catch incorrect latitude or longitude values
		} catch (IllegalArgumentException exception2) {

			exception2.printStackTrace();

			//
			return null;
		}
		// If the reverse geocode returned an address if (addresses != null &&
		// addresses.size() > 0) {

		// Get the first address
		Address address = addresses.get(0);
       // com.ssinfo.corider.app.models.RiderLocation locationAddress = new com.ssinfo.corider.app.models.RiderLocation();
       // locationAddress.setLatitude(location.getLatitude());
        //locationAddress.setLongitude(location.getLongitude());
		location.setAddress(address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0):"");
		location.setCity(address.getSubAdminArea());
		location.setCountry(address.getCountryName());
		
     return location;
	}

	/**
	 * A method that's called once doInBackground() completes. Set the text of
	 * the UI element that displays the address. This method runs on the UI
	 * thread.
	 */
	@Override
	protected void onPostExecute(com.ssinfo.corider.app.models.RiderLocation address) {
		if (address != null) {
			mListener.onSuccess(address);
		} else {
			mListener.onFail("No address found");
		}

	}

	public interface AddressListener {
		public void onSuccess(com.ssinfo.corider.app.models.RiderLocation address);

		public void onFail(String errorMsg);

	}

	private AddressListener mListener = null;

	public void setListener(AddressListener listener) {
		this.mListener = listener;
	}

}
