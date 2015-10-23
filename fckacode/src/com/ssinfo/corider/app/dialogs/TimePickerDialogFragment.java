package com.ssinfo.corider.app.dialogs;

import java.util.Calendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimePickerDialogFragment extends DialogFragment implements
    TimePickerDialog.OnTimeSetListener {

  private View timeView;


  @Override
  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
   if(timeView instanceof TextView){
     
     ((TextView)timeView).setText(""+hourOfDay+":"+minute);
     
   }

  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Calendar calInstance = Calendar.getInstance();
    int hour = calInstance.get(Calendar.HOUR_OF_DAY);
    int min = calInstance.get(Calendar.MINUTE);
    
    return new TimePickerDialog(getActivity(), this, hour, min, false);
    
  }

  @Override
  public void onDestroyView() {
    // TODO Auto-generated method stub
    super.onDestroyView();
  }

  @Override
  public void onDetach() {
    // TODO Auto-generated method stub
    super.onDetach();
  }
  
  public void setView(View view){
    timeView = view;
  }

}
