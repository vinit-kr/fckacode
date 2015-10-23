/**
 * 
 */
package com.ssinfo.corider.app.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;



/**
 * Https implementation of NetworkConnector HttpsConnection.java
 */
public class HttpsConnection implements IConnection {

	/** The Constant TIMEOUT_DURATION. */
	private static final int TIMEOUT_DURATION = 1 * 60 * 1000;

	/** The network connection. */
	ManageConnection networkConnection;

	/** The https url connection. */
	HttpsURLConnection httpsURLConnection = null;

	/** The byte array output stream. */
	ByteArrayOutputStream byteArrayOutputStream = null;

	/** The data input stream. */
	DataInputStream dataInputStream = null;

	@Override
	public void registerConnection(ManageConnection _networkConnection) {
		this.networkConnection = _networkConnection;
		httpsURLConnection = null;
		byteArrayOutputStream = null;
		dataInputStream = null;
	}

	@Override
	public void createConnection(String conType) {
		try {
			InputStream inputStream = null;
			setUpHttpsConnection();
			// set up the request header
			URL urlToConnect = new URL(networkConnection.url);
			httpsURLConnection = (HttpsURLConnection) urlToConnect.openConnection();
			setUpHttpsRequestHeader(httpsURLConnection);
			setRequestMethod(conType);
			httpsURLConnection.setConnectTimeout(TIMEOUT_DURATION);
			//httpsURLConnection.setDoInput(true);
			httpsURLConnection.setChunkedStreamingMode(0);
			// Connect to the server.
			httpsURLConnection.connect();
			// write output stream the client info.
			if (networkConnection.datatowrite != null && networkConnection.datatowrite.length > 0) {
				OutputStream outputStream = null;
				try {
					outputStream = httpsURLConnection.getOutputStream();
					outputStream.write(networkConnection.datatowrite);

				} catch (Exception e) {

				} finally {
					try {
						if (outputStream != null) {
							outputStream.close();
							outputStream = null;// release resource for GC
							// support
						}
					} catch (IOException e) {
						// if unable to close the stream
						outputStream = null;// release resource for GC
					}
				}
				networkConnection.datatowrite = null;
			}
			// get teh response code
			int responseCode = httpsURLConnection.getResponseCode();

			if (responseCode == HttpsURLConnection.HTTP_OK) {
				// read the data stream from the network.
				inputStream = httpsURLConnection.getInputStream();
				DataInputStream dataInputStream = new DataInputStream(inputStream);
				int a = -1;
				byte b[] = new byte[1024];
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				while ((a = dataInputStream.read(b)) != -1) {
					byteArrayOutputStream.write(b, 0, a);
				}
				// Set the data into the networkUpdater
				networkConnection.networkStatus.setServerResponseData(byteArrayOutputStream.toByteArray());
				networkConnection.networkStatus.setNetworkConnectionException(null);
				byteArrayOutputStream.close();
				dataInputStream.close();
				httpsURLConnection.disconnect();
			} else {
				// Something went wrong
				setErrorMessage(responseCode);
				httpsURLConnection.disconnect();
				networkConnection.networkStatus.setServerResponseData(null);
				networkConnection.networkStatus.setNetworkError(true);
				// networkConnection.networkStatus.setNetorkErrorMessage();
			}

		} catch (Exception e) {
			// Something went wrong
			// setErrorMessage(httpsURLConnection);
			networkConnection.networkStatus.setServerResponseData(null);
			networkConnection.networkStatus.setNetworkConnectionException(e);
			networkConnection.networkStatus.setNetworkError(true);
		} finally {
			if (byteArrayOutputStream != null) {
				try {
					byteArrayOutputStream.close();
				} catch (IOException e) {
				}
				byteArrayOutputStream = null;
			}
			if (dataInputStream != null) {
				try {
					dataInputStream.close();
				} catch (IOException e) {
				}
				dataInputStream = null;
			}
			if (httpsURLConnection != null) {
				httpsURLConnection.disconnect();
				httpsURLConnection = null;
			}
		}
	}

	/**
	 * Method to set error message
	 * 
	 * @param responseCode
	 */
	private void setErrorMessage(int responseCode) {
		/*
		 * InputStream errorStream = responseCode.getErrorStream();
		 * if(errorStream != null) { Scanner s = new Scanner(errorStream);
		 * s.useDelimiter("\\Z");
		 * networkConnection.networkStatus.setNetorkErrorMessage(s.next()); }
		 */
		networkConnection.networkStatus.setNetorkErrorMessage("Http Error - " + responseCode);
	}
	
	/**
	 * Method to set Request Method
	 */
	public void setRequestMethod(String reqType) {
		if(reqType != null) {
			try {
				httpsURLConnection.setRequestMethod(reqType);
				if(reqType.equals(NetworkConstants.REQUEST_METHOD_POST)){
					httpsURLConnection.setDoOutput(true);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}

	/**
	 * set up the https connectin. This also set the TrustManager and Host name
	 * Verifier
	 */
	public static void setUpHttpsConnection() {
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, new TrustManager[] { new MyTrustManager() }, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			System.setProperty(NetworkConstants.HTTP_KEEP_ALIVE, "false");
		} catch (Exception e) {
		}

	}

	/**
	 * Sets the HTTPS request header.
	 * 
	 * @param _httpsURLConnection
	 *            the _https url connection
	 * @param _networkRequestHeaderProperty
	 *            the _network request header property
	 */
	private void setUpHttpsRequestHeader(HttpsURLConnection _httpsURLConnection) {
		//_httpsURLConnection.setRequestMethod(NetworkConstants.REQUEST_METHOD_POST);
		/*_httpsURLConnection.setRequestProperty(NetworkConstants.HEADER_KEY, NetworkConstants.KEY);
		_httpsURLConnection.setRequestProperty(Constants.ACCEPT,
				NetworkConstants.APPLICATION_JSON_CHARSET_UTF_8);*/
		//_httpsURLConnection.setRequestProperty(NetworkConstants.CONTENT_TYPE,NetworkConstants.FORM_TYPE);
	}

	public static class MyTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
}
