package com.ssinfo.corider.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.ssinfo.corider.app.R;
import com.ssinfo.corider.app.activities.HomeActivity;
import com.ssinfo.corider.app.dialogs.DatePickerDialogFragment;
import com.ssinfo.corider.app.dialogs.TimePickerDialogFragment;
import com.ssinfo.corider.app.models.OfferRideInfo;
import com.ssinfo.corider.app.util.Validator;

public class OfferARideFragment extends BaseFragment implements OnClickListener {

  private View mView = null;
  private TextView txtViewDepartureDate;
  private TextView txtViewDepartTime;

  private TextView txtViewReturnDate;
  private TextView txtViewReturnTime;


  private DatePickerDialogFragment datePickerDialogFragment;
  private TimePickerDialogFragment timePickerDialogFragment;
  private static final String DEPART_TIME_PICKER = "departTimePicker";
  private static final String DEPART_DATE_PICKER = "departDatePicker";
  private static final String RETURN_DATE_PICKER = "returnDatePicker";
  private static final String RETURN_TIME_PICKER = "returnTimePicker";
  private CheckBox chkRoundTrip = null;

  private static OfferARideFragment offerARiderFragmentInstance = null;

  public static OfferARideFragment newInstance(Bundle bundle) {
    if (offerARiderFragmentInstance == null) {
      offerARiderFragmentInstance = new OfferARideFragment();
      offerARiderFragmentInstance.setArguments(bundle);
    }
    return offerARiderFragmentInstance;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);


  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    if (mView == null) {
      mView = inflater.inflate(R.layout.offer_ride_fragment_layout, null);
    }

    txtViewDepartureDate = (TextView) mView.findViewById(R.id.departure_date);
    txtViewDepartureDate.setOnClickListener(this);
    txtViewDepartTime = (TextView) mView.findViewById(R.id.departure_time);
    txtViewDepartTime.setOnClickListener(this);
    chkRoundTrip = (CheckBox) mView.findViewById(R.id.round_trip_check_btn);
    txtViewReturnDate = (TextView) mView.findViewById(R.id.return_date);
    txtViewReturnDate.setOnClickListener(this);
    txtViewReturnTime = (TextView) mView.findViewById(R.id.return_time);
    txtViewReturnTime.setOnClickListener(this);
    Button btnNext = (Button) mView.findViewById(R.id.btn_offer_ride_next);
    btnNext.setOnClickListener(this);


    return mView;
  }
  

  @Override
  public void onClick(View v) {

    switch (v.getId()) {
      case R.id.return_date:
      case R.id.departure_date:
        datePickerDialogFragment = new DatePickerDialogFragment();
        datePickerDialogFragment.setView(v);
        datePickerDialogFragment.show(getFragmentManager(), DEPART_DATE_PICKER);
        break;
      case R.id.return_time:
      case R.id.departure_time:
        timePickerDialogFragment = new TimePickerDialogFragment();
        timePickerDialogFragment.setView(v);
        timePickerDialogFragment.show(getFragmentManager(), DEPART_TIME_PICKER);
        break;

      case R.id.btn_offer_ride_next:
        OfferRideInfo offerRideInfo = new OfferRideInfo();
        if (com.ssinfo.corider.app.util.Validator.isValidDate(txtViewDepartureDate.getText()
            .toString())) {
          offerRideInfo.setDepartureDate(txtViewDepartureDate.getText().toString());
        } else {
          Toast.makeText(getActivity(), "Please choose a valid departure date", Toast.LENGTH_LONG)
              .show();
          return;
        }

        if (Validator.isValidTime(txtViewDepartTime.getText().toString())) {
          offerRideInfo.setDepartueTime(txtViewDepartTime.getText().toString());
        } else {
          Toast.makeText(getActivity(), "Please select a valid departure time", Toast.LENGTH_LONG)
              .show();
          return;
        }

        if (chkRoundTrip.isChecked()) {
          if (com.ssinfo.corider.app.util.Validator.isValidDate(txtViewReturnDate.getText()
              .toString())) {
            offerRideInfo.setReturnDate(txtViewReturnDate.getText().toString());
          } else {
            Toast
                .makeText(getActivity(), "Please choose a valid departure date", Toast.LENGTH_LONG)
                .show();
            return;
          }

          if (Validator.isValidTime(txtViewReturnTime.getText().toString())) {
            offerRideInfo.setDepartueTime(txtViewReturnTime.getText().toString());
          } else {
            Toast.makeText(getActivity(), "Please select a valid return time", Toast.LENGTH_LONG)
                .show();
            return;
          }

          // move to next screen
          ((HomeActivity) getActivity()).setFragment(
              OfferARidePriceFragment.newInstance(offerRideInfo), true);
        }



    }



  }



}
