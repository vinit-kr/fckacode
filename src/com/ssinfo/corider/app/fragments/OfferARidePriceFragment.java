package com.ssinfo.corider.app.fragments;

import com.ssinfo.corider.app.R;
import com.ssinfo.corider.app.models.OfferRideInfo;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OfferARidePriceFragment extends Fragment implements OnClickListener {
  private View mView;
  private TextView txtViewPricePerSeat = null;
  private TextView txtViewAvailableSeat = null;
  private static final String OFFER_RIDE_INFO = "offer_ride_info";
  private TextView tvTermsOfUse = null;
  private CheckBox checkbox = null;
  private static final String TERMS_CONDITIONS = "<a href='#'> Terms and Conditions</a>";
  private static OfferARidePriceFragment offerARideFragmentInstance;
  private LinearLayout mTermsAndConditionsSection = null;

  public static OfferARidePriceFragment newInstance(OfferRideInfo info) {
    Bundle bundle = new Bundle();
    bundle.putSerializable(OFFER_RIDE_INFO, info);
    if (offerARideFragmentInstance == null) {
      offerARideFragmentInstance = new OfferARidePriceFragment();
    }
    offerARideFragmentInstance.setArguments(bundle);
    return offerARideFragmentInstance;
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    if (mView == null) {
      mView = inflater.inflate(R.layout.offer_ride_price_layout, null);
    }
    tvTermsOfUse = (TextView) mView.findViewById(R.id.terms_of_use);
    tvTermsOfUse.setText(Html.fromHtml(TERMS_CONDITIONS));
    tvTermsOfUse.setOnClickListener(this);
    checkbox = (CheckBox) mView.findViewById(R.id.accept_terms);
    mTermsAndConditionsSection = (LinearLayout) mView.findViewById(R.id.terms_condition_section);
    return mView;
  }

  @Override
  public void onClick(View v) {
   

  }

}
