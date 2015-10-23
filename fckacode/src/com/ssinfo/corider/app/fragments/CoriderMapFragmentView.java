package com.ssinfo.corider.app.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssinfo.corider.app.R;

public class CoriderMapFragmentView extends Fragment {

  private View mCoriderHomeView = null;
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    
    if(mCoriderHomeView == null){
      mCoriderHomeView = inflater.inflate(R.layout.fragment_home, container , false);
    }
    
    return mCoriderHomeView;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {

    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void onStart() {
    
    super.onStart();
  }

  @Override
  public void onResume() {
    // TODO Auto-generated method stub
    super.onResume();
  }

  @Override
  public void onPause() {
    // TODO Auto-generated method stub
    super.onPause();
  }

  @Override
  public void onStop() {
    // TODO Auto-generated method stub
    super.onStop();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }


}
