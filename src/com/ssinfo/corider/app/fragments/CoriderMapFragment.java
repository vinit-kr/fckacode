package com.ssinfo.corider.app.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.ssinfo.corider.app.CoriderApp;
import com.ssinfo.corider.app.R;
import com.ssinfo.corider.app.adapters.CoriderInfoWindowAdapter;
import com.ssinfo.corider.app.constants.Constants;
import com.ssinfo.corider.app.handlers.RefreshHandler.OnRefreshListener;
import com.ssinfo.corider.app.models.RiderLocation;
import com.ssinfo.corider.app.tasks.GetAddressTask;
import com.ssinfo.corider.app.tasks.GetAddressTask.AddressListener;
import com.ssinfo.corider.app.util.LocationUtil;

public class CoriderMapFragment extends MapFragment implements ConnectionCallbacks,
    OnConnectionFailedListener, LocationListener, OnMyLocationButtonClickListener, AddressListener,
    GoogleMap.OnMapLongClickListener, GoogleMap.OnMyLocationChangeListener,
    GoogleMap.OnMarkerDragListener, OnRefreshListener {
  private static final String TAG = CoriderMapFragment.class.getName();
  private Activity activity = null;
  private GoogleMap mMap = null;
  private RiderLocation mCurrentLocation = null;
  private RiderLocation mDestinationLocation = null;

  private RiderLocation mPreviousLocation = null;
  private String mCurrentAddress = null;
  private LocationClient mLocationClient = null;
  private GetAddressTask mAddressTaskHandler = null;
  private LinearLayout mProgress = null;
  private View locationButton = null;
  private View mapView = null;
  private Handler mCurrentLocationUpdateHandler = null;
  private boolean isSearchResultAvail = false;
  private Tracker analyticsTracker;

  /**
   * Constructor for creating Corider map fragment.
   * 
   * @param activity
   */
  public CoriderMapFragment(Activity activity) {
    this.activity = getActivity();
    mProgress =
        (LinearLayout) ((FragmentActivity) getActivity()).findViewById(R.id.progressIndicator);

  }
  
  public CoriderMapFragment(){
    this.activity = getActivity();
        
  }
  

  private void setUpMapIfNeeded() {
    // Do a null check to confirm that we have not already instantiated the
    // map.
    if (mMap == null) {
      // Try to obtain the map from the SupportMapFragment.
      mMap = this.getMap();
      // Check if we were successful in obtaining the map.
      if (mMap != null) {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setPadding(15, 15, 15, 20);
        mMap.setInfoWindowAdapter(new CoriderInfoWindowAdapter(this.getActivity()));
        mMap.getUiSettings().setZoomControlsEnabled(false);

      }
    }
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setUpMapIfNeeded();
    // get the user current location and set marker on map.
    setCurrentLocationOnMap();
    // get the current location on every 3 minutes and updates the location
    // on map.
    mCurrentLocationUpdateHandler = new Handler();
    mCurrentLocationUpdateHandler.postDelayed(new Runnable() {

      @Override
      public void run() {
        // setCurrentLocationOnMap(); //TODO need to discuss that why
        // need to track the current location of user while not updating
        // on map or sending to server.
      }
    }, CoriderApp.getTimeInterval());
  }

  public void setCurrentLocationOnMap() {
    if (mCurrentLocation == null) {
      mCurrentLocation = LocationUtil.getCurrentLocation(activity);
    }
    if (!LocationUtil.isPrevLocationSame(mCurrentLocation, mPreviousLocation)) {
      // Need to fetch the new address.
      getAddress(mCurrentLocation);
    }
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {

    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    setRetainInstance(true);
    super.onCreate(savedInstanceState);
    analyticsTracker = ((CoriderApp)getActivity().getApplication()).getAnalyticsTracker();
    analyticsTracker.setScreenName(TAG);
    analyticsTracker.send(new HitBuilders.ScreenViewBuilder().build());
    if (mProgress != null) {
      mProgress.setVisibility(View.VISIBLE);
    }

  }

  /**
   * Function for getting the location address
   * 
   * @param RiderLocation ie should contain latitude and longitude.
   * @param location
   */
  public void getAddress(RiderLocation location) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Geocoder.isPresent()) {
      if (mCurrentAddress == null) {
        mAddressTaskHandler = new GetAddressTask(this.activity);
        mAddressTaskHandler.setListener(this);
        mAddressTaskHandler.execute(location);
      }

    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    if (mapView != null) {
      ViewGroup parent = (ViewGroup) mapView.getParent();
      if (parent != null) {
        parent.removeView(mapView);
      }
    }
    if (mapView == null) {
      mapView = super.onCreateView(inflater, container, savedInstanceState);
      // Get the location button view
      locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);

      // and next place it, for exemple, on bottom right (as Google Maps
      // app)
      RelativeLayout.LayoutParams rlp =
          (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
      // position on right bottom
      rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
      rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
      rlp.setMargins(0, 0, 0, 0);
      locationButton.setLayoutParams(rlp);
    }

    return mapView;

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }

  @Override
  public void onPause() {
    if (mProgress != null) {
      mProgress.setVisibility(View.GONE);
    }
    super.onPause();
  }

  @Override
  public void onResume() {
    super.onResume();
    if (mCurrentLocation == null) {
      mCurrentLocation = LocationUtil.getCurrentLocation(this.activity);
    }

  }

  @Override
  public void onSuccess(com.ssinfo.corider.app.models.RiderLocation currentLocation) {

    Log.i(TAG, "Got the address.." + currentLocation.getAddress());

    if (mProgress != null) {
      mProgress.setVisibility(View.GONE);
    }
    // mCurrentAddress = address;
    Bundle bundle = LocationUtil.getMyLocation(this.activity);
    bundle.putString(Constants.CURRENT_ADDRESS, currentLocation.getAddress());
    this.mCurrentLocation = currentLocation;
    LocationUtil.updateGoogleMarker(this.getMap(), mCurrentLocation, this.activity);
  }

  @Override
  public void onFail(String errorMsg) {

    if (mProgress != null) {
      mProgress.setVisibility(View.GONE);
    }
    // show a dialog when app is not able to get current address due to network or timeout issue.
    showFailedDialog();

  }

  public void showFailedDialog() {
    AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
    alert.setTitle("ERROR");
    alert.setMessage(R.string.get_current_location_address);


    alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        getMap().clear();
        setCurrentLocationOnMap();
      }
    });
    alert.show();

  }

  @Override
  public boolean onMyLocationButtonClick() {

    AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
    alert.setTitle(R.string.refresh_title);
    if (isSearchResultAvail) {
      alert.setMessage(R.string.onCurrent_location_btn_click_msg);
    } else {
      alert.setMessage(R.string.refresh_current_location_on_map);
    }

    alert.setPositiveButton(R.string.refresh_btn, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        getMap().clear();
        setCurrentLocationOnMap();
      }
    });

    alert.setNegativeButton(R.string.cancel_lbl, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    alert.show();

    return true;
  }

  public void onLocationChanged(Location location) {
    // this.mCurrentLocation = new
    // RiderLocation(location.getLatitude(),location.getLongitude());
    // get the location Address
    // getAddress(mCurrentLocation);

  }

  @Override
  public void onConnectionFailed(ConnectionResult arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onConnected(Bundle arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onDisconnected() {
    // TODO Auto-generated method stub

  }

  public void showMyLocation() {
    if (mLocationClient != null && mLocationClient.isConnected()) {
      // mCurrentLocation = mLocationClient.getLastLocation();
      String msg = "Location = " + mLocationClient.getLastLocation();
      Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
  }

  public GoogleMap getCoriderMap() {
    return mMap;
  }

  public RiderLocation getCurrentLocation() {
    return mCurrentLocation;
  }

  @Override
  public void onMapLongClick(LatLng point) {
    RiderLocation location = new RiderLocation(point.latitude, point.longitude);
    // get the location address and set the marker for current location on
    // map.
    getAddress(location);
  }

  @Override
  public void onMyLocationChange(Location location) {
    Log.i(TAG, "current location latitude" + location.getLatitude());
    Log.i(TAG, "current location longitude" + location.getLongitude());

  }

  @Override
  public void onMarkerDrag(Marker arg0) {

  }

  @Override
  public void onMarkerDragEnd(Marker marker) {
    LatLng latLng = marker.getPosition();
    mCurrentLocation = new RiderLocation(latLng.latitude, latLng.longitude);
    getAddress(mCurrentLocation);

  }

  @Override
  public void onMarkerDragStart(Marker currentLocationMarker) {
    if (!currentLocationMarker.getTitle().equals(mCurrentLocation.getAddress())) {
      currentLocationMarker.setDraggable(false);
    }

  }

  public RiderLocation getmDestinationLocation() {
    return mDestinationLocation;
  }

  public void setmDestinationLocation(RiderLocation mDestinationLocation) {
    this.mDestinationLocation = mDestinationLocation;
  }

  @Override
  public void onRefreshRoute(String message) {
    // TODO Auto-generated method stub
    setCurrentLocationOnMap();
  }

  @Override
  public void onRefreshFailed(String errorMsg) {
    // TODO Auto-generated method stub
    Log.e(TAG, "Error :" + errorMsg);
    Toast.makeText(activity, "Couldn't refresh previous search. Please try again.",
        Toast.LENGTH_LONG).show();
  }

  public boolean isSearchResultAvail() {
    return isSearchResultAvail;
  }

  public void setSearchResultAvail(boolean isSearchResultAvail) {
    this.isSearchResultAvail = isSearchResultAvail;
  }

}
