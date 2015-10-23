package com.ssinfo.corider.app.dialogs;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

public class DatePickerDialogFragment extends DialogFragment implements
    DatePickerDialog.OnDateSetListener {

  private View parentView;

  @Override
  public void onDetach() {
    // TODO Auto-generated method stub
    super.onDetach();
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Calendar calendarInstance = Calendar.getInstance();
    int year = calendarInstance.get(Calendar.YEAR);
    int month = calendarInstance.get(Calendar.MONTH);
    int day = calendarInstance.get(Calendar.DAY_OF_MONTH);
    return new DatePickerDialog(getActivity(), this, year, month, day);

  }

  @Override
  public void onDestroyView() {
    // TODO Auto-generated method stub
    super.onDestroyView();
  }

  @Override
  public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    StringBuilder strBlr = new StringBuilder();
    if(monthOfYear+1 <10 ){
      strBlr.append("0");
      strBlr.append(monthOfYear+1);
      
    }else {
      strBlr.append(monthOfYear+1);
    }
    strBlr.append("/");
    if(dayOfMonth <10){
      strBlr.append("0");
      strBlr.append(dayOfMonth);
    }else {
      strBlr.append(dayOfMonth);
    }
    strBlr.append("/");
    strBlr.append(year);
    if (parentView instanceof TextView) {
      
      ((TextView) parentView).setText(strBlr.toString());
    }
  }


  public void setView(View view) {
    this.parentView = view;
  }



}
