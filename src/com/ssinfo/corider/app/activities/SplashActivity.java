package com.ssinfo.corider.app.activities;

//import io.fabric.sdk.android.Fabric;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

//import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ssinfo.corider.app.CoriderApp;
import com.ssinfo.corider.app.R;
import com.ssinfo.corider.app.constants.Constants;
import com.ssinfo.corider.app.handlers.UserActivationHandler;
import com.ssinfo.corider.app.handlers.UserActivationHandler.OnUserActivationVerification;
import com.ssinfo.corider.app.pojo.User;
import com.ssinfo.corider.app.util.AppUtil;
import com.ssinfo.corider.app.util.LocationUtil;
import com.ssinfo.corider.app.util.NetworkUtility;

public class SplashActivity extends Activity implements OnUserActivationVerification {

  private static final String TAG = SplashActivity.class.getName();
  private UserActivationHandler userAcitvationHandler = null;
 // private boolean isTimerUp = false;
 // private boolean isUserActive = false;
  private int splashTime = 2000; // 2seconds.
  private Tracker analyticsTracker;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
   // Fabric.with(this, new Crashlytics());

    analyticsTracker = ((CoriderApp) getApplication()).getAnalyticsTracker();
    analyticsTracker.setScreenName(TAG);
    analyticsTracker.send(new HitBuilders.ScreenViewBuilder().build());


    setContentView(R.layout.splash_layout);
   // isTimerUp = false;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onRestart() {
    super.onRestart();
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
  }

  @Override
  protected void onResume() {
    super.onResume();

    Log.i(TAG, "Inside onResume..");
    if (NetworkUtility.isNetworkConnected(this)) {

      if (LocationUtil.isLocationServiceEnable(this)) {

        // check is userRegistered
        if (!AppUtil.isUserRegistered(this)) {
          // show registered activity.
          startActivity(new Intent(SplashActivity.this, RegistrationActivity.class));
          this.finish();
        } else {
          if (!AppUtil.isUserActive(this)) {
          //  isUserActive = false;
            userAcitvationHandler = new UserActivationHandler();
            userAcitvationHandler.setListener(this);
            userAcitvationHandler.isUserAccountActive(AppUtil.getUserInfo(this).getUserId());
          } else {
            new CountDownTimer(splashTime, 100) {
              public void onTick(long millisUntilFinished) {

              }

              public void onFinish() {
                SplashActivity.this.finish();
                // go to home screen
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
              }
            }.start();
          }

        }

      }

    } else {
      // show the network error dialog.
      showErrorDialog();

    }

  }

  public void showEmailVerificationMsg(final Context context) {
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
    alertDialog.setTitle(R.string.email_verification_pending_dialog_title);
    alertDialog.setMessage(R.string.email_activation_msg);
    alertDialog.setPositiveButton(R.string.ok, new OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        SplashActivity.this.finish();
        // go to home screen
        startActivity(new Intent(context, HomeActivity.class));
      }
    });
    alertDialog.show();
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  public void showErrorDialog() {
    AlertDialog.Builder errorDialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);

    errorDialog.setTitle(R.string.network_error_dialog_title);
    errorDialog.setMessage(R.string.network_error_msg);
    errorDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        SplashActivity.this.finish();

      }
    });

    errorDialog.setPositiveButton(R.string.btn_enable, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        Log.i(TAG, "btn enable get called...");
        dialog.dismiss();
        // Move to settings activity.
        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);

      }
    });

    errorDialog.show();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.i(TAG, "onActivityResult called...");

    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void onSuccess(User user) {
    if (Constants.ACTIVE_USER.equals(user.getStatus())) {
      //isUserActive = true;
      // user.setStatus(user.getStatus());
      AppUtil.saveUserInfoInSharedPref(this, user);
      SplashActivity.this.finish();
      // go to home screen
      startActivity(new Intent(SplashActivity.this, HomeActivity.class));

    } else {
      //isUserActive = false;
      showEmailVerificationMsg(this);
    }

  }

  @Override
  public void onFailed(String errorMsg) {
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
    alertDialog.setTitle(R.string.title_error);
    alertDialog.setMessage(R.string.server_connection_fail_msg);
    alertDialog.setPositiveButton(R.string.ok, new OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        SplashActivity.this.finish();
      }
    });

    alertDialog.show();
  }

}
