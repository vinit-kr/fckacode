/**
 * 
 */
package com.ssinfo.corider.app.network;


/**
 * This class contains the Network status of the result of the network
 * connection.
 * 
 *
 */
public class NetworkStatus {

	/**
	 * Ref Ver for the NetworkStatus class, This is singleton class.
	 */
	public NetworkInfo networkInfo;

	/**
	 * Contains the info if n/w error occuers or not
	 */
	public boolean isNetworkError = false;

	/**
	 * Contains the Resposnse code for n/w connection
	 */
	public int networkResponseCode = -1;

	/**
	 * Contains the Response of the server.
	 */
	public String networkResponse = null;

	/**
	 * Contains teh Network Error if occures.
	 */
	public String netorkErrorMessage = null;

	/**
	 * Network connection exception
	 */
	public Exception networkConnectionException;

	/**
	 * Server response data
	 */
	public byte[] serverResponseData;

	/**
	 * Constructor
	 */
	public NetworkStatus() {
	}

	/**
	 * get teh N/W Error
	 * 
	 * @return n/w Error
	 */
	public String getNetorkErrorMessage() {
		return netorkErrorMessage;
	}

	/**
	 * Set teh n/e Error.
	 * 
	 * @param netorkErrorMessage
	 */
	public void setNetorkErrorMessage(String netorkErrorMessage) {
		this.netorkErrorMessage = netorkErrorMessage;
	}

	/**
	 * get the N/w Response
	 * 
	 * @return
	 */
	public String getNetworkResponse() {
		return networkResponse;
	}

	/**
	 * Get the Response code
	 * 
	 * @return Response code
	 */
	public int getNetworkResponseCode() {
		return networkResponseCode;
	}

	/**
	 * Return true if n/e error
	 * 
	 * @return
	 */
	public boolean getIsNetworkError() {
		return isNetworkError;
	}

	/**
	 * Set the N/w Error
	 * 
	 * @param isNetworkError
	 */
	public void setNetworkError(boolean isNetworkError) {
		this.isNetworkError = isNetworkError;
	}

	/**
	 * get the n/w Response
	 * 
	 * @param networkResponse
	 */
	public void setNetworkResponse(String networkResponse) {
		this.networkResponse = networkResponse;
	}

	/**
	 * Set teh n/w response code
	 * 
	 * @param networkResponseCode
	 */
	public void setNetworkResponseCode(int networkResponseCode) {
		this.networkResponseCode = networkResponseCode;
	}

	/**
	 * Get the n/w connection exception
	 * 
	 * @return
	 */
	public Exception getNetworkConnectionException() {
		return networkConnectionException;
	}

	/**
	 * Set the n/w connection exception
	 * 
	 * @param networkConnectionException
	 */
	public void setNetworkConnectionException(Exception networkConnectionException) {
		this.networkConnectionException = networkConnectionException;
	}

	/**
	 * Get the server response data
	 * 
	 * @return
	 */
	public byte[] getServerResponseData() {
		return serverResponseData;
	}

	/**
	 * Set the server respose
	 * 
	 * @param serverResponseData
	 */
	public void setServerResponseData(byte[] serverResponseData) {
		this.serverResponseData = serverResponseData;
	}

	/**
	 * Get the n/w info
	 * 
	 * @return
	 */
	public NetworkInfo getNetworkInfo() {
		return networkInfo;
	}

	/**
	 * Set the n/w info
	 * 
	 * @param networkInfo
	 */
	public void setNetworkInfo(NetworkInfo networkInfo) {
		this.networkInfo = networkInfo;
	}
}
