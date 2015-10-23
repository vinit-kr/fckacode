package com.ssinfo.corider.analytics;

import android.app.Application;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Logger.LogLevel;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

public class AnalyticsAPI {

  // Placeholder property ID.
  private static final String GA_PROPERTY_ID = "UA-34061070-10";

  public static final String CAMPAIGN_SOURCE_PARAM = "utm_source";

  // Prevent hits from being sent to reports, i.e. during testing.
  private static final boolean GA_IS_DRY_RUN = false;

  // GA Logger verbosity.
  private static final LogLevel GA_LOG_VERBOSITY = LogLevel.VERBOSE;

  private static Tracker mTracker;
  private static GoogleAnalytics mGa;


  /*
   * Method to handle basic Google Analytics initialization. This call will not block as all Google
   * Analytics work occurs off the main thread.
   */

  public static void initializeGoogleAnalytics(Application context) {
    mGa = GoogleAnalytics.getInstance(context);
    mTracker = mGa.getTracker(GA_PROPERTY_ID);
    // mEasyTracker = EasyTracker.getInstance(appObj);
    // Set dispatch period.
    // GAServiceManager.getInstance().setLocalDispatchPeriod(Constants.GA_DISPATCH_PERIOD);

    // Set dryRun flag.
    mGa.setDryRun(GA_IS_DRY_RUN);

    // Set Logger verbosity.
    mGa.getLogger().setLogLevel(GA_LOG_VERBOSITY);

    // Set the opt out flag when user updates a tracking preference.
  }


  /*
   * Returns the Google Analytics tracker.
   */
  public static Tracker getGaTracker() {
    return mTracker;
  }

  /*
   * public static EasyTracker getEasyTracker() { return mEasyTracker; }
   */

  /*
   * Returns the Google Analytics instance.
   */
  public static GoogleAnalytics getGaInstance() {
    return mGa;
  }

  /*
   * capture Event for google analytics
   */
  public static void sendGaEvent(String category, String action, String label, int value) {

    // MapBuilder.createEvent().build() returns a Map of event fields and values
    // that are set and sent with the hit.
    mTracker.send(MapBuilder.createEvent(category, action, label, (long) value).build());
  }

  public static void sendGaScreen(String screenName) {
    getGaTracker().set(Fields.SCREEN_NAME, screenName);
    if (screenName != null) {
      getGaTracker().send(MapBuilder.createAppView().build());
    }
  }

  public static void clearGaScreen() {
    getGaTracker().set(Fields.SCREEN_NAME, null);
  }
}
