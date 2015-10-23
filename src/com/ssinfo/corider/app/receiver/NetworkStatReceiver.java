package com.ssinfo.corider.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkStatReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context arg0, Intent intent) {
		if(intent.getExtras()!=null) {
	        NetworkInfo ni=(NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
	        if(ni!=null && ni.getState()==NetworkInfo.State.CONNECTED) {
	           //Need to show a dialog.
	        	Log.i("app","Network "+ni.getTypeName()+" connected");
	        }
	     }
	     if(intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE)) {
	            Log.d("app","There's no network connectivity");
	     }
		
	}
	
	

}
