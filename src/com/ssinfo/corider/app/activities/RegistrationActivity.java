package com.ssinfo.corider.app.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.ssinfo.corider.app.R;
import com.ssinfo.corider.app.fragments.RegistrationFragment;
import com.ssinfo.corider.app.fragments.WebViewFragment;

public class RegistrationActivity extends FragmentActivity {
  private static final String TAG = RegistrationActivity.class.getName();
  private RegistrationFragment mRegistrationFragment;
  private WebViewFragment mWebViewFragment;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.registration_layout);
    mRegistrationFragment = new RegistrationFragment(this);
    setFragment(mRegistrationFragment);


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
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
  }


  @Override
  public void onBackPressed() {
    if (getFragmentManager().findFragmentById(R.id.regContainer) instanceof RegistrationFragment) {
      this.finish();
    } else {
      getSupportFragmentManager().popBackStack();
    }

  }

  public void setFragment(Fragment fragment) {
    Fragment currentFragment = fragment;
    FragmentTransaction transaction = null;
    if (currentFragment == null) {
      currentFragment = new RegistrationFragment(this);
    }

    transaction = getFragmentManager().beginTransaction();
    if (getFragmentManager().findFragmentById(R.id.regContainer) instanceof RegistrationFragment) {
      transaction.replace(R.id.regContainer, currentFragment);
      transaction.addToBackStack("registerFragment");
    } else {

      transaction.add(R.id.regContainer, currentFragment);
    }
    transaction.commit();
  }

  public void setTermsAndConditionFragment() {
    mWebViewFragment = new WebViewFragment();
    mWebViewFragment.setLoadUrl("http://google.com");
    setFragment(mWebViewFragment);

  }

}
