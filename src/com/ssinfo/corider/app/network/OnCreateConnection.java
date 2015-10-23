/**
 * 
 */
package com.ssinfo.corider.app.network;


/**
 * Interface used by the network connection for providing the network status
 * 
 * @author VijayK NetworkUpdater.java
 */

public interface OnCreateConnection {

	/**
	 * Invoked after the n/w layer completes its task and provide the response
	 * to the caller in the form of the Network status
	 * 
	 * @param _networkStatus
	 */
	public void onNetworkStatus(NetworkStatus _networkStatus);

}
