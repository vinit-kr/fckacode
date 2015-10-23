/**
 * 
 */
package com.ssinfo.corider.app.network;



/**
 * This class is responsible for all type of network connection. It Supports
 * http only. NetworkConnection.java
 */

public class ManageConnection {
	
	/**
	 * Ref ver of NetworkUpdater Asynchronous class for network response.
	 */
	protected OnCreateConnection networkUpdater;

	/**
	 * Url to connect.
	 */
	protected String url;

	/**
	 * Data to write on n/w stream
	 */
	protected byte[] datatowrite;

	/**
	 * Ref Ver for network info, It will contains the info regarding network.
	 * 
	 */
	NetworkInfo networkInfo;

    NetworkStatus networkStatus;

	private String reqType;

	/**
	 * 
	 * @param url
	 *            URL for Connection.
	 * @param networkUpdater
	 *            Network updater listener for all type of connection response.
	 * @param data
	 *            data to sent
	 * @param currentActivity
	 *            activity class which is making use of this network connection.
	 */
	public ManageConnection(String _url, OnCreateConnection _networkUpdater, byte[] _data,String reqType) {
		this.url = _url;
		this.networkUpdater = _networkUpdater;
		this.datatowrite = _data;
		networkInfo = new NetworkInfo();
		networkStatus = new NetworkStatus();
		networkStatus.setNetworkInfo(networkInfo);
		this.reqType = reqType;
	}

	/**
	 * Get the connection based on the connection protocol
	 * 
	 * @param protocolType
	 * @return
	 */
	public IConnection getConnection(String protocolType) {
		if (protocolType.equals(NetworkConstants.PROTOCOL_HTTP)) {
			//return new HttpConnection();
		} else if (protocolType.equals(NetworkConstants.PROTOCOL_HTTPS)) {
			return new HttpsConnection();
		}else if(protocolType.equals(NetworkConstants.JERSEY_CLIENT)){
			//use jersey client for sending object to server.
		}

		return null;
	}

	/**
	 * Run method of the Thread.
	 */
	public void startConnecting() {
		// Check for the transport type of the connection.
		String _url = url;
		_url = url.substring(0, url.indexOf(":"));
		IConnection connection = getConnection(_url);
		if (connection != null) {
			connection.registerConnection(this);
			connection.createConnection(reqType);
		}
	}
}
