/**
 * 
 */
package com.ssinfo.corider.app.network;


/**
 * This class contains the meta Info regarding n/w Connection,These info are
 * used by the n/w manager for creating the n/w connection. NetworkInfo.java
 */

public class NetworkInfo {

	/**
	 * URL to connect
	 */
	public String url;

	/**
	 * Client data which need to sent to teh server.
	 */
	public byte[] clientDataToServer;

	/**
	 * Ref ver for the Network updater.
	 */
	public OnCreateConnection networkUpdater;

	/**
	 * Get the client data which need to sent to the server.
	 * 
	 * @return Client data.
	 */
	public byte[] getClientDataToServer() {
		return clientDataToServer;
	}

	/**
	 * Set the n/w updater
	 * 
	 * @param networkUpdater
	 */
	public void setNetworkUpdater(OnCreateConnection networkUpdater) {
		this.networkUpdater = networkUpdater;
	}

	/**
	 * Get the N/W updator
	 * 
	 * @return N/w Updator
	 */
	public OnCreateConnection getNetworkUpdater() {
		return networkUpdater;
	}

	/**
	 * Get teh URL to connect
	 * 
	 * @return URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Set the URL to connect
	 * 
	 * @param url
	 *            URL
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Set the client data to the server.
	 * 
	 * @param clientDataToServer
	 *            Client data
	 */
	public void setClientDataToServer(byte[] clientDataToServer) {
		this.clientDataToServer = clientDataToServer;
	}

}
