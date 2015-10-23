package com.ssinfo.corider.app.receiver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

import com.ssinfo.corider.app.R;

public class NetworkStateReceiver extends BroadcastReceiver{
	
	private Handler mNetworkDialogHandler = null;
	private Context mContext = null;
	
	public NetworkStateReceiver(Context context){
	 this.mContext = context;
	 mNetworkDialogHandler = new Handler();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getExtras()!=null) {
	        NetworkInfo ni=(NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
	        if(ni!=null && ni.getState()==NetworkInfo.State.CONNECTED) {
	           //Need to show a dialog.
	        	Log.i("app","Network "+ni.getTypeName()+" connected");
	        }else if(ni !=null && ni.getState() == NetworkInfo.State.DISCONNECTED || intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE)){
	        	//Toast.makeText(context, "Network disconnected.", Toast.LENGTH_LONG).show();
	        	mNetworkDialogHandler.post(new Runnable() {
					
					@Override
					public void run() {
						AlertDialog.Builder errorDialog = new AlertDialog.Builder(mContext,AlertDialog.THEME_HOLO_LIGHT);

						errorDialog.setTitle(R.string.network_error_dialog_title);
						errorDialog.setMessage(R.string.network_error_msg);
						errorDialog.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
										((Activity)mContext).finish();

									}
								});

						errorDialog.setPositiveButton(R.string.btn_enable,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
										// Move to settings activity.
										((Activity)mContext).startActivityForResult(new Intent(
												android.provider.Settings.ACTION_SETTINGS), 0);

									}
								});

						errorDialog.show();
						
					}
				});
	        }
	     }
	    /* if(intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE)) {
	            Toast.makeText(context, "There is no network connection", Toast.LENGTH_LONG).show();
	     }
		*/
	}
	
	

}
