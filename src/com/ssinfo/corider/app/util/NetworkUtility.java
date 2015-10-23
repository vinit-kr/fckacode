package com.ssinfo.corider.app.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;





public class NetworkUtility {

	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if ((networkInfo != null) && networkInfo.isConnectedOrConnecting()) {
			// There are no active networks.
			return true;
		} else
			return false;
	}
	
	public static Client getJerseyClient(){
		 ClientConfig clientConfig = new DefaultClientConfig();
 		 clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		    Client client = Client.create(clientConfig);
		    return client;
	}
}
