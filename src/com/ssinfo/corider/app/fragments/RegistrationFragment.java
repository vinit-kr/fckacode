package com.ssinfo.corider.app.fragments;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ssinfo.corider.app.R;
import com.ssinfo.corider.app.activities.HomeActivity;
import com.ssinfo.corider.app.activities.RegistrationActivity;
import com.ssinfo.corider.app.constants.Constants;
import com.ssinfo.corider.app.exceptions.SignUpException;
import com.ssinfo.corider.app.handlers.RegistrationHandler;
import com.ssinfo.corider.app.handlers.RegistrationHandler.OnRegistrationCompletionListener;
import com.ssinfo.corider.app.pojo.DeviceInfo;
import com.ssinfo.corider.app.pojo.User;
import com.ssinfo.corider.app.util.AppUtil;
import com.ssinfo.corider.app.util.StringUtil;

/**
 * Fragment for registration.
 * 
 * @author VINIT
 * 
 */
public class RegistrationFragment extends Fragment implements OnClickListener,
    OnRegistrationCompletionListener {

  private static final String TAG = RegistrationFragment.class.getName();

  private User userInfo = null;

  private EditText etFullName = null;
  private EditText etEmailAddress = null;
  private ProgressDialog mSignupProgressDialog = null;
  // private Context mContext = null;
  private Button btnRegister;
  private TextView tvErrorMsg = null;
  private TextView tvTermsOfUse = null;
  private CheckBox checkbox = null;
  private static final String TERMS_CONDITIONS = "<a href='#'> Terms and Conditions</a>";
  private LinearLayout mTermsAndConditionsSection = null;
  private RegistrationHandler regHandler = null;
  private Context context;
  // project number is sender id.
  String SENDER_ID = "1040649371936";
  // Gcm used for generating the gcm registration id of device.
  private GoogleCloudMessaging gcm;
  AtomicInteger msgId = new AtomicInteger();
  String regid;

  private boolean isUpdateProfile = false;

  public RegistrationFragment(Context context) {
    this.context = context;
  }

  public RegistrationFragment() {

  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_registration, container, false);
    etFullName = (EditText) view.findViewById(R.id.name);
    etEmailAddress = (EditText) view.findViewById(R.id.register_email_address);
    btnRegister = (Button) view.findViewById(R.id.btn_register);
    tvErrorMsg = (TextView) view.findViewById(R.id.register_error_msg);
    tvTermsOfUse = (TextView) view.findViewById(R.id.terms_of_use);
    tvTermsOfUse.setText(Html.fromHtml(TERMS_CONDITIONS));
    tvTermsOfUse.setOnClickListener(this);
    checkbox = (CheckBox) view.findViewById(R.id.accept_terms);
    mTermsAndConditionsSection = (LinearLayout) view.findViewById(R.id.terms_condition_section);
    tvErrorMsg.setVisibility(View.INVISIBLE);
    btnRegister.setOnClickListener(this);
    mSignupProgressDialog = new ProgressDialog(this.context);
    mSignupProgressDialog.setMessage(getResources().getString(R.string.signup));
    mSignupProgressDialog.setCanceledOnTouchOutside(false);
    regHandler = new RegistrationHandler();
    regHandler.setListener(this);
    if (getUserInfo() != null) {
      populateUIForProfileEdit(getUserInfo());
    }
    return view;
  }

  public void populateUIForProfileEdit(User user) {
    etFullName.setText(user.getUserName());
    etEmailAddress.setText(user.getEmailId());
    mTermsAndConditionsSection.setVisibility(View.GONE);
    btnRegister.setText(R.string.Update);

  }

  /**
   * Registers the application with GCM servers asynchronously.
   * <p>
   * Stores the registration ID and the app versionCode in the application's shared preferences.
   */
  private void registerInBackground() {
    new AsyncTask<Void, Void, String>() {
      @Override
      protected String doInBackground(Void... params) {
        String msg = "";
        try {
          if (gcm == null) {
            gcm = GoogleCloudMessaging.getInstance(context);
          }
          regid = gcm.register(SENDER_ID);
          msg = "Device registered, registration ID=" + regid;

          // Persist the regID - no need to register again.
          AppUtil.storeRegistrationId(context, regid);
        } catch (IOException ex) {
          msg = "Error :" + ex.getMessage();
          // If there is an error, don't just keep trying to register.
          // Require the user to click a button again, or perform
          // exponential back-off.
        }
        return msg;
      }

      @Override
      protected void onPostExecute(String msg) {
        Log.e(TAG, msg);
        // Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
      }
    }.execute(null, null, null);
  }

  /**
   * Check the device to make sure it has the Google Play Services APK. If it doesn't, display a
   * dialog that allows users to download the APK from the Google Play Store or enable it in the
   * device's system settings.
   */
  private boolean checkPlayServices() {
    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
    if (resultCode != ConnectionResult.SUCCESS) {
      if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
        GooglePlayServicesUtil.getErrorDialog(resultCode, (RegistrationActivity) context,
            Constants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
      } else {
        Log.i(TAG, "This device is not supported.");
        ((Activity) context).finish();
      }
      return false;
    }
    return true;
  }



  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onAttach(Activity activity) {
    // TODO Auto-generated method stub
    super.onAttach(activity);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);

    // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
    if (checkPlayServices() && getUserInfo() == null) { // same fragment we are using for fresh
                                                        // registration and editing profile.
      gcm = GoogleCloudMessaging.getInstance(context);
      regid = AppUtil.getRegistrationId(context);
      Log.i(TAG, "gcm registration id is " + regid);
      if (regid.isEmpty()) {
        registerInBackground();
      }
    } else {
      Log.e(TAG, "No valid Google Play Services APK found.");
    }
  }


  @Override
  public void onClick(View view) {

    if (view.getId() == tvTermsOfUse.getId()) {
      ((RegistrationActivity) this.context).setTermsAndConditionFragment();
      return;
    }

    if (!checkbox.isChecked() && !isUpdateProfile) {
      tvErrorMsg.setText(R.string.error_msg_terms_condition);
      tvErrorMsg.setVisibility(View.VISIBLE);
      return;
    } else {
      tvErrorMsg.setVisibility(View.GONE);
    }
    User mUser = new User();
    String usrFullName = etFullName.getEditableText().toString();
    String emailAddress = etEmailAddress.getEditableText().toString();

    try {

      if (StringUtil.isBlankOrNull(emailAddress)
          || !com.ssinfo.corider.app.util.Validator.isValidEmail(emailAddress)) {
        throw new SignUpException("Invalid email address");
      } else {
        mUser.setEmailId(emailAddress);
      }

      if (StringUtil.isBlankOrNull(usrFullName)) {
        throw new SignUpException("Full name is required for registration");
      } else {
        mUser.setUserName(usrFullName);
      }
      DeviceInfo deviceInfo = AppUtil.getDeviceInfo(this.context);
      mUser.setDevice(deviceInfo);
      // set the gcmRegistration Id.
      mUser.setGcmRegistrationId(regid);
      //TODO: Bypass for testing
      startActivity(new Intent(context, HomeActivity.class));
     // regHandler.registerUser(mUser);

      if (!mSignupProgressDialog.isShowing()) {
        mSignupProgressDialog.show();
      }

    } catch (SignUpException e) {
      tvErrorMsg.setText(e.getMessage());
      tvErrorMsg.setVisibility(View.VISIBLE);
      Toast.makeText(this.context, e.getMessage(), Toast.LENGTH_LONG).show();
    }


  }

  @Override
  public void onRegistrationCompletion(User userInfo) {
    Log.i(TAG, userInfo.toString());
    AppUtil.saveUserInfoInSharedPref(this.context, userInfo);
    if (mSignupProgressDialog.isShowing()) {
      mSignupProgressDialog.dismiss();
    }
    tvErrorMsg.setVisibility(View.INVISIBLE);
    etFullName.setText(null);
    etEmailAddress.setText(null);
    showMessageDialog(this.context);
  }

  public void showSuccessMessage() {

  }


  @Override
  public void onRegistrationFailed(String errorMsg) {
    if (mSignupProgressDialog.isShowing()) {
      mSignupProgressDialog.dismiss();
    }
    tvErrorMsg.setText(errorMsg);
    tvErrorMsg.setVisibility(View.VISIBLE);
  }

  public void showMessageDialog(final Context context) {
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
    if (isUpdateProfile) {
      alertDialog.setTitle(R.string.profile_update_success_msg_title);
      alertDialog.setMessage(R.string.profile_updated_success_msg);
    } else {
      alertDialog.setTitle(R.string.reg_dialog_completion_title);
      alertDialog.setMessage(R.string.email_activation_msg);
    }


    alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        ((Activity) context).finish();
        startActivity(new Intent(context, HomeActivity.class));
      }
    });

    alertDialog.show();
  }

  public User getUserInfo() {
    return userInfo;
  }

  public void setUserInfo(User userInfo) {
    this.userInfo = userInfo;
    isUpdateProfile = true;
  }
}
