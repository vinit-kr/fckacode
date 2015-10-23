/**
 * 
 */
package com.ssinfo.corider.app.network;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Class to setup connection in background using AsyncTack
 * CreateConnectionInBackground.java
 */
public class CreateConnection extends AsyncTask<Object, String, Object> {

	/**
	 * Constructor to perform n/w operation
	 * 
	 * @param url
	 *            URL to connect
	 * @param req
	 *            data to server
	 * @param networkUpdater
	 *            N/w updater to notify
	 * @param activity
	 *            Current activity which is using the n/w operation
	 */
	public CreateConnection(String url, byte[] req, OnCreateConnection networkUpdater,String reqType) {
		execute(url, req, networkUpdater,reqType);
	}

	public CreateConnection(String url, byte[] req, OnCreateConnection networkUpdater,String reqType,Object obj) {
		execute(url, req, networkUpdater,reqType,obj);
	}

	
	/**
	 * Excecute after all network operation
	 */
	@Override
	synchronized protected void onPostExecute(Object result) {
		try {
			NetworkStatus networkStatus = (NetworkStatus) result;
			this.networkUpdater.onNetworkStatus(networkStatus);
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	OnCreateConnection networkUpdater;

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	/**
	 * Create n/w operation in background
	 */
	@Override
	protected Object doInBackground(Object... params) {
		String URL = (String) params[0];
		byte[] req = (byte[]) params[1];
		this.networkUpdater = (OnCreateConnection) params[2];
		String reqType = (String) params[3];
		
		ManageConnection networkConnection = new ManageConnection(URL, networkUpdater, req,reqType);
		networkConnection.startConnecting();
		return networkConnection.networkStatus;
	}

}
