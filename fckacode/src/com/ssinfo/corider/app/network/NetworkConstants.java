/**
 * 
 */
package com.ssinfo.corider.app.network;

/**
 * Contain Constants related to the Network.
 * 
 */
public interface NetworkConstants {
     /**
      * Variable for http connection
      */
	 String PROTOCOL_HTTP = "http";
	 /**
	  * Variable for indication https connection
	  */
	 String PROTOCOL_HTTPS = "https";
	 
	 String JERSEY_CLIENT = "jerseyClient";
	/**
	 * variable for the Request method POST.
	 */
	String REQUEST_METHOD_POST = "POST";
	/**
	 * 
	 */
	String HEADER_KEY = "X-Mashape-Authorization";
	/**
	 * 
	 */
	String KEY = "FC0eLBDIfHBEBgxFTPetKpIuRJjxYOaQ";
	/**
	 * variable for the Request method GET
	 */
	String REQUEST_METHOD_GET = "GET";

	/**
	 * variable for the Content type
	 */
	String CONTENT_TYPE = "content-type";

	/**
	 * variable for the User agent.
	 */
	String USER_AGENT = "user-agent";

	/**
	 * Content type is plain
	 */
	String CONTENT_TYPE_TEXT_PLAIN = "text/plain";

	/**
	 * Header propery to keep teh connection Alive.
	 */
	String HTTP_KEEP_ALIVE = "http.keepAlive";

	/** APPLICATION JSON CHARSET UTF_8 */
	String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=UTF-8";
	
	String FORM_TYPE = "application/x-www-form-urlencoded";
}
