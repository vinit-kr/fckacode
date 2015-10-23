/**
 * 
 */
package com.ssinfo.corider.app.network;


/**
 * This interface is used by the connection type implementaion
 * 
 * @author VijayK Nov 7, 2011 NetworkConnector.java
 */
public interface IConnection {

	/**
	 * Registor the connection with the n/w layer
	 * 
	 * @param networkConnection
	 */
	void registerConnection(ManageConnection networkConnection);

	/**
	 * Create the connction
	 */
	void createConnection(String connectionType);
	
}
