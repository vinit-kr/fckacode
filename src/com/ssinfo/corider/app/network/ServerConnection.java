package com.ssinfo.corider.app.network;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

public class ServerConnection {
    
	private OnServerRequestCompletionListener onServerRequestCompletionListener;
	private String mUrl = null;
    private Object mParameter = null;
    private RequestSender requestSender=null;
    private HttpPost httpPost = null;
	public ServerConnection(
			OnServerRequestCompletionListener onServerRequestCompletionListener) {
		this.onServerRequestCompletionListener = onServerRequestCompletionListener;
	}

	public void sendRequest(String url, Object parameters) {
		this.mUrl = url;
		this.mParameter = parameters;;
            requestSender = new RequestSender();
		requestSender.execute();

	}
	/**
	 * Function for sending the get request.
	 * @param url
	 */
	public void sendRequest(String url){
		
	}
	public void cancelRequest(){
		if(requestSender !=null){
			//requestSender.cancel(true);
		   httpPost.abort();
			
			
		}
	}

	public interface OnServerRequestCompletionListener {
		public void onServerResponse(HttpResponse response);

	}
	
	private class RequestSender extends AsyncTask<Void, Void, HttpResponse> {

		@Override
		protected HttpResponse doInBackground(Void... param) {
			HttpResponse response =null;
			try {
			   HttpParams params = new BasicHttpParams();
			   HttpConnectionParams.setConnectionTimeout(params, 100000);
			   HttpConnectionParams.setSoTimeout(params, 100000);
			   
			   HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			   HttpProtocolParams.setHttpElementCharset(params, HTTP.UTF_8);
			   DefaultHttpClient client = new DefaultHttpClient(params);
			    if(mParameter !=null){
		            httpPost = new HttpPost(mUrl);	
		            Log.i("ServerConnection Request sender", "sent url is "+mUrl);
		            Gson gson = new Gson();
		            httpPost.setEntity(new StringEntity(gson.toJson(mParameter),HTTP.UTF_8));	            	
		            httpPost.setHeader("Accept", "application/json");
		            httpPost.setHeader("Content-type", "application/json");
					response = client.execute(httpPost);
			    }else {
			    	HttpGet httpGetRequest = new HttpGet(mUrl);
			    	response = client.execute(httpGetRequest);
			    }
			  
				// String output = response.getEntity(String.class);

			} catch (Exception e) {
	             e.printStackTrace();
				//onServerRequestCompletionListener.onServerResponse(response);
			}
			return response;
			
		}

		@Override
		protected void onPostExecute(HttpResponse result) {
			onServerRequestCompletionListener.onServerResponse(result);
			super.onPostExecute(result);
		}
		
		
		
	}

}
