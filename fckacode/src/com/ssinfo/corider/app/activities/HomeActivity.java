package com.ssinfo.corider.app.activities;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.model.LatLng;
import com.ssinfo.corider.app.CoriderApp;
import com.ssinfo.corider.app.R;
import com.ssinfo.corider.app.adapters.NavDrawerListAdapter;
import com.ssinfo.corider.app.constants.Constants;
import com.ssinfo.corider.app.fragments.CoriderMapFragment;
import com.ssinfo.corider.app.fragments.CoriderMapFragmentView;
import com.ssinfo.corider.app.fragments.MatchedCoridersFragment;
import com.ssinfo.corider.app.fragments.OfferARideFragment;
import com.ssinfo.corider.app.fragments.RegistrationFragment;
import com.ssinfo.corider.app.fragments.SearchFragment;
import com.ssinfo.corider.app.fragments.WebViewFragment;
import com.ssinfo.corider.app.handlers.MessageHandler;
import com.ssinfo.corider.app.handlers.MessageHandler.OnMessageDeliveredListener;
import com.ssinfo.corider.app.handlers.RefreshHandler;
import com.ssinfo.corider.app.handlers.RefreshHandler.OnRefreshListener;
import com.ssinfo.corider.app.handlers.RouteHandler;
import com.ssinfo.corider.app.handlers.RouteHandler.OnRouteRequestListener;
import com.ssinfo.corider.app.handlers.SearchHandler;
import com.ssinfo.corider.app.handlers.SearchHandler.SearchCompletionListener;
import com.ssinfo.corider.app.models.AllRoutes;
import com.ssinfo.corider.app.models.Legs;
import com.ssinfo.corider.app.models.MatchedCoriders;
import com.ssinfo.corider.app.models.MessageDTO;
import com.ssinfo.corider.app.models.NavDrawerItem;
import com.ssinfo.corider.app.models.RiderLocation;
import com.ssinfo.corider.app.models.Routes;
import com.ssinfo.corider.app.models.SearchParams;
import com.ssinfo.corider.app.models.Steps;
import com.ssinfo.corider.app.pojo.User;
import com.ssinfo.corider.app.receiver.GcmBroadcastReceiver;
import com.ssinfo.corider.app.receiver.NetworkStateReceiver;
import com.ssinfo.corider.app.search.PlaceProvider;
import com.ssinfo.corider.app.tasks.GetAddressTask;
import com.ssinfo.corider.app.util.AppUtil;
import com.ssinfo.corider.app.util.LocationUtil;

/*
 * import android.support.v4.app.Fragment; import android.support.v4.app.FragmentActivity; import
 * android.support.v4.app.FragmentTransaction; import android.support.v4.view.GravityCompat; import
 * android.support.v4.widget.DrawerLayout; import
 * android.support.v4.widget.DrawerLayout.DrawerListener;
 */

public class HomeActivity extends FragmentActivity implements DrawerListener,
    ListView.OnItemClickListener, OnRouteRequestListener, OnClickListener,
    android.app.LoaderManager.LoaderCallbacks<Cursor>, SearchCompletionListener, OnRefreshListener,
    OnMessageDeliveredListener {
  private static final String TAG = HomeActivity.class.getName();

  private ImageButton mBtnNavigation = null;
  private Button mBtnRideShare = null;
  private Button mBtnNeedRide = null;
  private Button mBtnOfferRide = null;

  private SearchView mSearchView = null;
  private RiderLocation mDestinationLocation = null;
  private GetAddressTask mAddressTaskHandler = null;


  // navigation drawer
  private DrawerLayout mDrawerLayout;
  private LinearLayout mBottomBtnSection = null;
  private TypedArray navMenuIcons;
  private String[] mNavMenuTitles;
  private ListView mDrawerList;
  private NavDrawerListAdapter mNavDrawerListAdapter;
  private ArrayList<NavDrawerItem> mNavDrawerItemList;
  // search bar
  private LinearLayout mSearchLayout = null;
  // Network state receiver
  private NetworkStateReceiver mNetworkStateReceiver = null;
  private LocationUtil mLocationUtil = null;
  // For getting the all route details.
  private RouteHandler mRouteHandler = null;

  private List<List<LatLng>> routeLatLngList = null;
  private CoriderMapFragmentView mMapFragment = null;
  private WebViewFragment mWebViewFragment = null;
  private RegistrationFragment mRegistrationFragment = null;

  private SearchParams searchParams = null;
  private LinearLayout mCoriderFindingMsgLayout = null;
  private TextView mCoriderFindingMsg = null;
  private ProgressBar mMsgProgressBar = null;
  private MatchedCoriders mMatchedCoriders = null;
  // private String searchAddress = null;
  private ImageView msgIcon = null;

  private Handler mGcmMessageHandler = null;
  private TextView mAvailCoriderstxt = null;
  // Gcm message receiver that is responsible for getting the message from user and getting the
  // available coriders information from server.
  private GCMmessageReceiver messageReceiver = null;
  // GCM Configuration
  public static final String EXTRA_MESSAGE = "message";
  public static final String PROPERTY_REG_ID = "registration_id";
  private MessageHandler mMessageHandler = null;
  // Want to change the searchView with edit view and SearchFragment //TODO pending task.
  private TextView searchTextView = null;
  private SearchFragment mSearchFragment;
  // handler responsible for sending search request to server and return response.
  private SearchHandler searchHandler = null;
  // the flag is used for keep the consistent behavior of showing available coriders and matched
  // corider
  // app gets available coriders by gcm so some time app gets the matched coriders before
  // available coriders.
  private boolean isRequestCancelled = false;
  private boolean isSearchResponseGot = false;
  // handler for deleting the all previous routes of user.
  private RefreshHandler refreshHandler;
  // For calculating the idleness of user after getting the matched corider.
  // if user keep idle for five minutes, app will show a alert dialog.
  private CountDownTimer timer = null;
  // private Timer mCurrentLocationUpdateTimer = null;
  // private TimerTask mCurrentLocationUpdateTask = null;

  private Context mContext = null;
  private Tracker analyticsTracker;

  /*
   * //project number is sender id. String SENDER_ID = "1040649371936";
   * 
   * GoogleCloudMessaging gcm; AtomicInteger msgId = new AtomicInteger(); Context context;
   * 
   * String regid;
   */



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.home_layout);
    mGcmMessageHandler = new Handler();
    mContext = this;
    // set the default map fragment.
    mMapFragment = new CoriderMapFragmentView();
    setFragment(mMapFragment, false);
    mNetworkStateReceiver = new NetworkStateReceiver(this);
    mLocationUtil = new LocationUtil(this);
    mMessageHandler = new MessageHandler();
    mMessageHandler.setListener(this);
    addNavigationDrawer();
    refreshHandler = new RefreshHandler();
    refreshHandler.setListener(this);
    // searchbar
   // mSearchLayout = (LinearLayout) findViewById(R.id.search_bar);
  //  mBottomBtnSection = (LinearLayout) findViewById(R.id.bottom_btn_section);
   // mBtnRideShare = (Button) findViewById(R.id.btn_want_to_share_ride);

  //  mBtnRideShare.setOnClickListener(this);
  //  mBtnNeedRide = (Button) findViewById(R.id.btn_need_ride);
   // mBtnNeedRide.setOnClickListener(this);
  //  mBtnOfferRide = (Button) findViewById(R.id.btn_offer_ride);
  //  mBtnOfferRide.setOnClickListener(this);
    // adding search feature.
   // addSearch();

  //  mRouteHandler = new RouteHandler();
  //  mRouteHandler.setListener(this);
  //  mCoriderFindingMsgLayout = (LinearLayout) findViewById(R.id.coriderFinding);
  //  mCoriderFindingMsgLayout.setOnClickListener(this);
  //  mCoriderFindingMsg = (TextView) findViewById(R.id.coriderFinding_msg);
  //  mCoriderFindingMsg.setOnClickListener(this);
   // mAvailCoriderstxt = (TextView) findViewById(R.id.availCoriders_msg);
  //  mAvailCoriderstxt.setVisibility(View.GONE);
   // mMsgProgressBar = (ProgressBar) findViewById(R.id.coriders_progress);
  //  msgIcon = (ImageView) findViewById(R.id.msg_icon);
    // searchTextView = (TextView) findViewById(R.id.search);
    // searchTextView.setOnClickListener(this);
 //   messageReceiver = new GCMmessageReceiver();


    //handleIntent(getIntent());
  }

  public void showFindingMsg(String destination) {
    mCoriderFindingMsgLayout.setVisibility(View.VISIBLE);
    StringBuilder msgBuilder = new StringBuilder();
    msgBuilder.append("Finding coriders going in the direction of ");
    msgBuilder.append(destination);
    mCoriderFindingMsg.setText(msgBuilder);
    msgIcon.setVisibility(View.GONE);
    mMsgProgressBar.setVisibility(View.VISIBLE);
  }

  /**
   * Function for hiding the app messages shown on top.
   */
  public void hideCoriderFindingMsg() {
    mCoriderFindingMsg.setText("");
    mCoriderFindingMsgLayout.setVisibility(View.INVISIBLE);
    mAvailCoriderstxt.setText("");
    mAvailCoriderstxt.setVisibility(View.INVISIBLE);

  }

  /**
   * Function for adding the search ui and business logic to app.
   */
  public void addSearch() {/*

    mSearchView = (SearchView) findViewById(R.id.search_view);
    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    mSearchView.setHorizontalScrollBarEnabled(true);

    mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View view, boolean queryTextFocused) {
        if (!queryTextFocused) {

          mSearchView.setIconified(true);

          if (mBottomBtnSection != null) {
            mBottomBtnSection.setVisibility(View.VISIBLE);
            mSearchLayout.setVisibility(View.INVISIBLE);
          }

        }
      }
    });

  */}

  @SuppressLint("Recycle")
  private void addNavigationDrawer() {

    navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
    mNavMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);
    mNavDrawerItemList = new ArrayList<NavDrawerItem>();
    for (int index = 0; index < mNavMenuTitles.length; index++) {
      mNavDrawerItemList.add(new NavDrawerItem(mNavMenuTitles[index], navMenuIcons.getResourceId(
          index, mNavMenuTitles.length)));
    }

    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    mNavDrawerListAdapter = new NavDrawerListAdapter(this, mNavDrawerItemList, navMenuIcons);
    mDrawerList.setOnItemClickListener(this);

    mDrawerList.setAdapter(mNavDrawerListAdapter);
    mBtnNavigation = (ImageButton) findViewById(R.id.btn_nav);
    mBtnNavigation.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View view) {
        Log.i(TAG, "navigation button clicked..");
        if (mDrawerLayout != null) {
          mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
          mDrawerLayout.openDrawer(mDrawerList);
        }

      }
    });

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  @Override
  protected void onNewIntent(Intent intent) {
    handleIntent(intent);

    super.onNewIntent(intent);
  }

  public void handleIntent(Intent intent) {
    if (Intent.ACTION_VIEW.equals(intent.getAction())) {
      getPlace(intent.getStringExtra(SearchManager.EXTRA_DATA_KEY));
    }

  }

  @Override
  protected void onPause() {
    this.unregisterReceiver(mNetworkStateReceiver);
    this.unregisterReceiver(messageReceiver);
    GcmBroadcastReceiver.listener = null;
    mLocationUtil.removeGpsStatusListener();

    // set listener null while activity is in pause mode.
    if (refreshHandler != null) {
      refreshHandler.setListener(null);
    }
    if (mRouteHandler != null) {
      mRouteHandler.setListener(null);
    }
    if (searchHandler != null) {
      searchHandler.setSearchCompletionListener(null);
    }
    if (mMessageHandler != null) {
      mMessageHandler.setListener(null);
    }

    super.onPause();


  }

  @Override
  protected void onRestart() {
    super.onRestart();
  }

  @Override
  protected void onResume() {
    super.onResume();
    // initialize the listener again
    if (refreshHandler != null) {
      refreshHandler.setListener(this);
    }

    if (mRouteHandler != null) {
      mRouteHandler.setListener(this);
    }

    if (searchHandler != null) {
      searchHandler.setSearchCompletionListener(this);
    }
    if (mMessageHandler != null) {
      mMessageHandler.setListener(this);
    }

    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
    intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    intentFilter.addAction(WifiManager.EXTRA_WIFI_STATE);
    this.registerReceiver(mNetworkStateReceiver, intentFilter);

    // Register gcm message receiver
    this.registerReceiver(messageReceiver, new IntentFilter(Constants.GCM_MESSAGE_ACTION));
    GcmBroadcastReceiver.listener = this;
    mLocationUtil.addGpsStatusListener();

  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onStop() {

    super.onStop();
  }


  /**
   * Button to get current Location. This demonstrates how to get the current Location as required
   * without needing to register a LocationListener.
   */

  public void getAddress(Location location) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Geocoder.isPresent()) {
      mAddressTaskHandler = new GetAddressTask(this);
      mAddressTaskHandler
          .execute(new RiderLocation(location.getLatitude(), location.getLongitude()));
    }
  }

  @Override
  public void onDrawerClosed(View arg0) {
    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
  }

  @Override
  public void onDrawerOpened(View arg0) {

  }

  @Override
  public void onDrawerSlide(View arg0, float arg1) {

  }

  @Override
  public void onDrawerStateChanged(int arg0) {

  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    final AdapterView<?> parentLocal = parent;
    final View viewLocal = view;
    final int positionLocal = position;
    final long idLocal = id;
    mDrawerLayout.closeDrawer(mDrawerList);
    view.getRootView().post(new Runnable() {

      @Override
      public void run() {
        onItemClickWorker(parentLocal, viewLocal, positionLocal, idLocal);
      }
    });

  }

  private void onItemClickWorker(AdapterView<?> parent, View view, int position, long id) {
    String menuTitle = ((TextView) (view.findViewById(R.id.label))).getText().toString();
    if (menuTitle.equals(mNavMenuTitles[0])) {

      // It will delete all the route details saved in db and clear the map.

      refreshHandler.refreshRoutes(AppUtil.getUserInfo(this));
      // clear the map
     // mMapFragment.getMap().clear();
      // set the current location on map.
   //   mMapFragment.setCurrentLocationOnMap();
    }

    else if (menuTitle.equals(mNavMenuTitles[1])) {
      // show the profile page.
      mRegistrationFragment = new RegistrationFragment(this);
      User user = AppUtil.getUserInfo(this);
      mRegistrationFragment.setUserInfo(user);
      setFragment(mRegistrationFragment, true);

    } else if (menuTitle.equals(mNavMenuTitles[2])) {

      // share with friends dialog.
      Intent sharingIntent = new Intent(Intent.ACTION_SEND);
      sharingIntent.setType("image/*");
      sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
          Html.fromHtml("<p>I am using corider app for sharing cab.</p>")); // TODO message should
                                                                            // come from database.
      startActivityForResult(Intent.createChooser(sharingIntent, "Share using"), 0);

    } else if (menuTitle.equals(mNavMenuTitles[3])) {
      // Rate us
      Intent i = new Intent(Intent.ACTION_VIEW);
      i.setData(Uri.parse("market://details?id=" + getPackageName()));
      startActivityForResult(i, 0);
    } else if (menuTitle.equals(mNavMenuTitles[4])) {
      // Feedback
      Intent sendEmail = new Intent(Intent.ACTION_SEND);
      sendEmail.setType("message/rfc822");
      sendEmail.putExtra(Intent.EXTRA_EMAIL, new String[] {Constants.FEEDBACK_EMAIL});

      sendEmail.putExtra(Intent.EXTRA_SUBJECT, Constants.FEEDBACK_SUBJECT);
      startActivityForResult(sendEmail, 0);

    } else if (menuTitle.equals(mNavMenuTitles[5])) {
      // Terms of use
      if (mWebViewFragment != null) {
        mWebViewFragment.setLoadUrl(Constants.TERMS_OF_SERVICE);
      } else {
        mWebViewFragment = new WebViewFragment();
        mWebViewFragment.setLoadUrl(Constants.TERMS_OF_SERVICE);
      }
      setFragment(mWebViewFragment, true);
    } else if (menuTitle.equals(mNavMenuTitles[6])) {
      // About us
      if (mWebViewFragment != null) {
        mWebViewFragment.setLoadUrl(Constants.ABOUT_US);
      } else {
        mWebViewFragment = new WebViewFragment();
        mWebViewFragment.setLoadUrl(Constants.ABOUT_US);
      }

      setFragment(mWebViewFragment, true);

    } else if (menuTitle.equals(mNavMenuTitles[7])) {
      // Exit
      this.finish();
    }
  }


  @Override
  public void onBackPressed() {
    Log.i(TAG, "onBackPressed clicked...");
    mAvailCoriderstxt.setVisibility(View.GONE);
    if (mCoriderFindingMsgLayout != null
        && mCoriderFindingMsgLayout.getVisibility() == View.VISIBLE) {
      // show the alert dialog with message
      showMessageDialog(this, null);
      return;
    }
    if (getFragmentManager().findFragmentById(R.id.fragment_container) instanceof CoriderMapFragment) {
      if (!mSearchView.isIconified()) {
        mSearchView.setVisibility(View.INVISIBLE);
        mSearchView.setVisibility(View.VISIBLE);

      } else if (mSearchLayout.getVisibility() == View.VISIBLE) {
        mBottomBtnSection.setVisibility(View.VISIBLE);
        mSearchLayout.setVisibility(View.INVISIBLE);
      } else {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(i);
      }
    } else {

      getSupportFragmentManager().popBackStack();
      if (mBottomBtnSection != null) {
        mBottomBtnSection.setVisibility(View.VISIBLE);
        mSearchLayout.setVisibility(View.INVISIBLE);
        hideCoriderFindingMsg();
      }

    }

  }

  @Override
  public void onRouteFound(AllRoutes routes) {
    searchParams.setAllRoutes(routes);
    routeLatLngList = new ArrayList<List<LatLng>>();
    if (routes != null) {
      Routes[] routeArr = routes.getRoutes();
      for (Routes route : routeArr) {
        // get all the routes latlng for drawing polyline on map.
        List<LatLng> latLngList = getRouteLatLng(route);
        routeLatLngList.add(latLngList);
      }
    } else {
      Log.e(TAG, "Route response is null from server");
    }
    // draw polylines on map.
   // LocationUtil.drawPolylines(routeLatLngList, mMapFragment.getCoriderMap());

    // save searchLocations with all routes.
    searchHandler = new SearchHandler();
    searchHandler.setSearchCompletionListener(this);
    User user = AppUtil.getUserInfo(this);
    searchParams.setUser(user);
    // initialize the variable for new search.
    isRequestCancelled = false;
    isSearchResponseGot = false;
    searchHandler.searchCoriders(searchParams);
    showFindingMsg(searchParams.getDestination().getAddress());
    // hide the bottom buttons.
    mBottomBtnSection.setVisibility(View.INVISIBLE);
  }

  public List<LatLng> getRouteLatLng(Routes route) {
    List<LatLng> latLngList = new ArrayList<LatLng>();
    Legs[] legArr = route.getLegs();
    for (Legs legs : legArr) {
      Steps[] stepsArr = legs.getSteps();
      for (Steps steps : stepsArr) {
        com.ssinfo.corider.app.models.RiderLocation startLocation = steps.getStart_location();
        LatLng slLatLng =
            new LatLng(Double.valueOf(startLocation.getLat()), Double.valueOf(startLocation
                .getLng()));
        latLngList.add(slLatLng);

        com.ssinfo.corider.app.models.RiderLocation endLocation = steps.getEnd_location();
        LatLng elLatLng =
            new LatLng(Double.valueOf(endLocation.getLat()), Double.valueOf(endLocation.getLng()));
        latLngList.add(elLatLng);

      }
    }
    return latLngList;
  }

  @Override
  public void onRouteFailed(String errorMsg) {
    Log.i(TAG, errorMsg);
    Toast.makeText(this, "Could not find the route. Please try again.", Toast.LENGTH_LONG).show();
    mAvailCoriderstxt.setVisibility(View.GONE);

  }

  @Override
  public void onClick(View view) {
    if (timer != null) {
      timer.cancel();
    }
  //  mMapFragment.setSearchResultAvail(false);
    switch (view.getId()) {

      case R.id.btn_want_to_share_ride:
        // show the search bar
        mBottomBtnSection.setVisibility(View.INVISIBLE);
        mSearchLayout.setVisibility(View.VISIBLE);

        break;
      case R.id.btn_need_ride:

        break;
      /*
       * case R.id.search: if(mSearchFragment ==null){ mSearchFragment = new SearchFragment(this); }
       * setFragment(mSearchFragment, true); break;
       */
      case R.id.btn_offer_ride:

        setFragment(OfferARideFragment.newInstance(new Bundle()), true);

        break;
      case R.id.coriderFinding_msg:
      case R.id.coriderFinding:
        if (mMatchedCoriders != null && mMatchedCoriders.getRouteCoriders().length > 0) {
          mCoriderFindingMsgLayout.setVisibility(View.GONE);
          MatchedCoridersFragment matchCoriderFragment = new MatchedCoridersFragment();
          matchCoriderFragment.setCoriders(mMatchedCoriders);
          matchCoriderFragment.setHeader(mCoriderFindingMsg.getText().toString());
          setFragment(matchCoriderFragment, true);

        } else {
          mCoriderFindingMsgLayout.setVisibility(View.GONE);
          mBottomBtnSection.setVisibility(View.VISIBLE);
        }
        break;
    }
  }

  @Override
  public android.content.Loader<Cursor> onCreateLoader(int id, Bundle query) {
    CursorLoader cLoader = null;
    if (id == 0)
      cLoader =
          new CursorLoader(getBaseContext(), PlaceProvider.SEARCH_URI, null, null,
              new String[] {query.getString("query")}, null);
    else if (id == 1)
      cLoader =
          new CursorLoader(getBaseContext(), PlaceProvider.DETAILS_URI, null, null,
              new String[] {query.getString("query")}, null);
    return cLoader;

  }

  @Override
  public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {/*
    if (mSearchLayout != null) {
      mSearchLayout.setVisibility(View.INVISIBLE);
    }
    mDestinationLocation = showLocations(data);
    // clear all the markers and polyline from map.
    mMapFragment.getMap().clear();
    searchParams = new SearchParams();
    RiderLocation currentLocation = mMapFragment.getCurrentLocation();
    searchParams.setCurrentLocation(currentLocation);
    searchParams.setDestination(mDestinationLocation);
    LocationUtil.setMarker(this, mMapFragment.getMap(), mMapFragment.getCurrentLocation(),
        mDestinationLocation, null);
    if (mDestinationLocation != null) {
      mRouteHandler.getAllRouteDetails(mMapFragment.getCurrentLocation(), mDestinationLocation);
    }
  */}

  @Override
  public void onLoaderReset(android.content.Loader<Cursor> loader) {

  }

  private void getPlace(String query) {
    Bundle data = new Bundle();
    data.putString("query", query);
    getLoaderManager().restartLoader(1, data, this);
  }

  private RiderLocation showLocations(Cursor c) {

    RiderLocation dest = null;
    while (c.moveToNext()) {
      dest =
          new RiderLocation(Double.parseDouble(c.getString(1)), Double.parseDouble(c.getString(2)));
      // dest.setAddress(c.getString(0));
      String[] address = c.getString(0).split(",");
      dest.setAddress(address[0] + address[1]);
      dest.setCity(address[2]);
      dest.setCountry(address[address.length - 1]);
    }
    return dest;
  }

  // false for add and true for replace
  public void setFragment(Fragment fragment, boolean addReplace) {
    Log.i(TAG, "Inside the setFragment method replace " + addReplace);
    Fragment currentFragment = fragment;
    FragmentTransaction transaction = null;
    if (currentFragment == null) {
      currentFragment = mMapFragment = new CoriderMapFragmentView();
    }

    transaction = getFragmentManager().beginTransaction();
    if (addReplace) {
      transaction.replace(R.id.fragment_container, currentFragment);
      transaction.addToBackStack(null);
    } else {
      transaction.add(R.id.fragment_container, currentFragment);

    }
    if (currentFragment instanceof CoriderMapFragment) {
      transaction.addToBackStack(Constants.MAP_FRAGMENT);
      if (mBottomBtnSection != null) {
        mBottomBtnSection.setVisibility(View.VISIBLE);
        mSearchLayout.setVisibility(View.INVISIBLE);
      }

    } else {
      if (mBottomBtnSection != null) {
        mBottomBtnSection.setVisibility(View.INVISIBLE);
      }
      if (mSearchLayout != null) {
        mSearchLayout.setVisibility(View.INVISIBLE);
      }
    }
    transaction.commit();

  }

  @Override
  public void searchSuccess(MatchedCoriders matchedCoriders) {
    isSearchResponseGot = true;
    mAvailCoriderstxt.setVisibility(View.GONE);
    if (matchedCoriders.getTotalMatchedUser() > 0) {
      StringBuilder msgBuilder = new StringBuilder();
      msgBuilder.append(matchedCoriders.getTotalMatchedUser());
      msgBuilder.append(" people matched for ride sharing towards ");
      msgBuilder.append(mDestinationLocation.getAddress());
      mCoriderFindingMsg.setText(msgBuilder.toString());
      mMsgProgressBar.setVisibility(View.GONE);
      msgIcon.setVisibility(View.VISIBLE);
      mMatchedCoriders = matchedCoriders;
    } else {
      mCoriderFindingMsg.setText("No any matched corider found for "
          + mDestinationLocation.getAddress());
      mMsgProgressBar.setVisibility(View.GONE);
      msgIcon.setVisibility(View.VISIBLE);
    }
   // mMapFragment.setSearchResultAvail(true);
   // LocationUtil.setMarkerForCoriders(matchedCoriders.getRouteCoriders(), mMapFragment.getMap());
    timer = new CountDownTimer(CoriderApp.getIdleTimeInterval(), 100) {
      public void onTick(long millisUntilFinished) {

      }

      public void onFinish() {
        showIdleMessage();
      }
    }.start();


  }

  public void showIdleMessage() {
    AlertDialog.Builder alert = new AlertDialog.Builder(this);
    final Context context = this;
    alert.setTitle(R.string.idle_title);
    alert.setMessage(R.string.idle_msg);
    alert.setPositiveButton(R.string.refresh_btn, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        refreshHandler.refreshRoutes(AppUtil.getUserInfo(context));
        dialog.dismiss();
      }
    });

    alert.setNegativeButton(R.string.cancel_lbl, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();

      }
    });
    alert.show();
  }

  @Override
  public void searchFailed(String errorMsg) {
    Log.e(TAG, "Error are " + errorMsg);
    Toast.makeText(this, "Couldn't find coriders. Please try again", Toast.LENGTH_LONG).show();
    mCoriderFindingMsgLayout.setVisibility(View.GONE);
    mAvailCoriderstxt.setVisibility(View.GONE);
    if (mBottomBtnSection != null) {
      mBottomBtnSection.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onRefreshRoute(String message) {

    if (mCoriderFindingMsgLayout != null) {
      mCoriderFindingMsgLayout.setVisibility(View.GONE);
    }
    if (mAvailCoriderstxt != null) {
      mAvailCoriderstxt.setVisibility(View.GONE);
    }
    if (mBottomBtnSection != null) {
      mBottomBtnSection.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onRefreshFailed(String errorMsg) {
    Toast.makeText(this, "Operation failed. Please try again", Toast.LENGTH_LONG).show();
  }

  public String getCurrentLocAddress() {
    //RiderLocation currentLocation = mMapFragment.getCurrentLocation();
    //String address = currentLocation.getAddress();
  //  return address;
    return "";
  }



  private class GCMmessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      Bundle bundle = intent.getExtras();
      Log.i(TAG, "received data is " + bundle.toString());
      if (bundle != null) {
        if (bundle.getString(Constants.MESSAGE_TYPE) != null
            && bundle.getString(Constants.MESSAGE_TYPE).equals(Constants.MESSAGE_AVAIL_USER)) {
          // If request is cancelled or matched coriders response got, then no need to show the
          // available coriders.
          if (isRequestCancelled || isSearchResponseGot) {
            mAvailCoriderstxt.setText("");
            mAvailCoriderstxt.setVisibility(View.GONE);
            mBottomBtnSection.setVisibility(View.VISIBLE);
            return;
          }
          final String totalAvailCorider = bundle.getString(Constants.MESSAGE);
          if (AppUtil.isNumber(totalAvailCorider)) {
            final StringBuilder msgBuilder = new StringBuilder();
            msgBuilder.append("Found ");
            msgBuilder.append(totalAvailCorider);
            msgBuilder.append(R.string.people_waiting_msg);
            msgBuilder.append(getCurrentLocAddress());
            mGcmMessageHandler.post(new Runnable() {

              @Override
              public void run() {
                Log.i(TAG, "available coriders total count" + totalAvailCorider);
                mAvailCoriderstxt.setText(msgBuilder.toString());
                mAvailCoriderstxt.setVisibility(View.VISIBLE);

              }
            });
          } else {

          }
        } else {
          final String userId = bundle.getString(Constants.FROM_USER_ID);
          final String fromUserName = bundle.getString(Constants.FROM_USER_NAME);
          final String messageReceiverUserId = bundle.getString(Constants.TO_USER_ID);
          final String messageReceiverUserName = bundle.getString(Constants.TO_USER_NAME);
          final String message = bundle.getString(Constants.MESSAGE);
          final String matchedDistance = bundle.getString(Constants.MATCHED_DISTANCE);
          final String destination = bundle.getString(Constants.DEST_ADDRESS);
          mGcmMessageHandler.post(new Runnable() {

            @Override
            public void run() {
              // show the message in alert dialog with reply and cancel button.
              Log.i(TAG, "message from other corider");
              Log.i(TAG, "userId" + userId);
              Log.i(TAG, "messageReceiverUserId " + messageReceiverUserId);
              Log.i(TAG, "fromUserName " + fromUserName);
              Log.i(TAG, "messageReceiverUserName " + messageReceiverUserName);
              // (final String fromUserName,final String distanceInKm, final String
              // matchedRouteSummary, final String toUser,final String toUserId,final String
              // message){
              showMessageDialog(fromUserName, matchedDistance, destination,
                  messageReceiverUserName, messageReceiverUserId, message);

            }
          });


        }

      }

    }

  }


  public void sendReply(final String fromUserName, final String distanceInKm,
      final String matchedRouteSummary, final String toUser, final String toUserId, String message) {

    AlertDialog.Builder alert = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
    // set the custom title bar only.
    LayoutInflater inflater = getLayoutInflater();

    // set the custom title bar
    FrameLayout titlelayout = new FrameLayout(this);
    titlelayout.layout(10, 10, 10, 10);
    View view = inflater.inflate(R.layout.layout_message_dialog_title, titlelayout);
    alert.setCustomTitle(view);
    // set the body of dialog.
    FrameLayout frameLayout = new FrameLayout(this);
    // FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(300, 600,
    // Gravity.CENTER_VERTICAL); //Remove this after verifying.
    alert.setView(frameLayout);

    View dialoglayout = inflater.inflate(R.layout.dialog_body, frameLayout);
    alert.setView(dialoglayout);
    final EditText input = (EditText) dialoglayout.findViewById(R.id.edit_msg);
    // ------------------->

    TextView toUserTextView = (TextView) view.findViewById(R.id.coriderName);
    TextView toKmTextview = (TextView) view.findViewById(R.id.distance);
    TextView toRouteTextView = (TextView) view.findViewById(R.id.destination);

    // set the value
    toUserTextView.setText(fromUserName);
    toKmTextview.setText(distanceInKm);
    toRouteTextView.setText(matchedRouteSummary);
    alert.setCustomTitle(view);


    // Set an EditText view to get user input
    alert.setPositiveButton(R.string.send_lbl, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        String value = input.getText().toString();
        User fromUser = AppUtil.getUserInfo(HomeActivity.this);
        User toUser = new User();
        toUser.setUserId(toUserId);
        toUser.setUserName(fromUserName);
        MessageDTO messageDto = new MessageDTO();
        messageDto.setFromUser(fromUser);
        // messageDto.setTouser(fromUser); // TODO just for testing purpose on single device.
        messageDto.setTouser(toUser);
        messageDto.setMsg(value);
        messageDto.setDestination(matchedRouteSummary);
        messageDto.setMatchedDistance(distanceInKm);
        mMessageHandler.sendMessage(messageDto);
        dialog.dismiss();
      }
    });


    alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        // Canceled.
        dialog.dismiss();
      }
    });

    alert.show();
  }

  public void showMessageDialog(final String fromUserName, final String distanceInMeter,
      final String matchedRouteSummary, final String toUser, final String toUserId,
      final String message) {
    AlertDialog.Builder alert =
        new AlertDialog.Builder(HomeActivity.this, AlertDialog.THEME_HOLO_LIGHT);
    // set the custom title bar only.
    // set the custom title bar
    FrameLayout titlelayout = new FrameLayout(this);
    titlelayout.layout(10, 10, 10, 10);
    LayoutInflater inflater = getLayoutInflater();
    View view = inflater.inflate(R.layout.layout_message_dialog_title, titlelayout);
    alert.setCustomTitle(view);

    TextView toUserTextView = (TextView) view.findViewById(R.id.coriderName);
    TextView toKmTextview = (TextView) view.findViewById(R.id.distance);
    TextView toRouteTextView = (TextView) view.findViewById(R.id.destination);

    // set the value
    toUserTextView.setText("From: " + fromUserName);
    toKmTextview.setText(AppUtil.convertInKm(distanceInMeter) + " km");
    toRouteTextView.setText(matchedRouteSummary);
    alert.setCustomTitle(view);
    alert.setMessage(message);
    alert.setPositiveButton(R.string.reply_lbl, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {

        sendReply(fromUserName, distanceInMeter, matchedRouteSummary, toUser, toUserId, message);
        dialog.dismiss();
      }
    });


    alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        // Canceled.
        dialog.dismiss();
      }
    });

    alert.show();
  }

  @Override
  public void onMessageDelivered(String message) {
    Toast.makeText(this, "Reply sent", Toast.LENGTH_LONG).show();

  }

  @Override
  public void onMessageDeliveryFailed(String errorMsg) {
    Toast.makeText(this, "Reply sending failed", Toast.LENGTH_LONG).show();

  }


  public void showMessageDialog(final Context context, String message) {
    AlertDialog.Builder alertDialog =
        new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
    if (message != null) {
      alertDialog.setMessage(message);
    } else {
      alertDialog.setMessage(R.string.want_different_search_msg);
    }

    alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        // cancel the currenRequest
        searchHandler.cancelRequest();
        isRequestCancelled = true;
        // hide the header.
        hideCoriderFindingMsg();
        mBottomBtnSection.setVisibility(View.VISIBLE);
        dialog.dismiss();

      }
    });

    alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();

      }
    });

    alertDialog.show();
  }

}
