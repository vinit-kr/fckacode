package com.ssinfo.corider.app.handlers;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.ssinfo.corider.app.models.MatchedCoriders;
import com.ssinfo.corider.app.models.SearchParams;
import com.ssinfo.corider.app.network.ServerConnection;
import com.ssinfo.corider.app.network.ServerConnection.OnServerRequestCompletionListener;
import com.ssinfo.corider.app.network.request.Constants;

public class SearchHandler implements OnServerRequestCompletionListener {

	private SearchCompletionListener searchCompletionListener;

	private String mResponse;
	private String mUserId;
	private HttpEntity mResponseEntity = null;
   private ServerConnection serverConnection = null;
	public void searchCoriders(SearchParams searchInfo) {
		String url = makeUrl();
		//Gson gson = new Gson();
		//String requestedParam = gson.toJson(searchInfo);
		serverConnection = new ServerConnection(this);
		serverConnection.sendRequest(url, searchInfo);
		
	}
	
	public void cancelRequest(){
		if(serverConnection !=null){
			serverConnection.cancelRequest();
		}
	}

	/**
	 * Method to return url
	 * 
	 * @param modelData
	 * 
	 * @return
	 */
	private String makeUrl() {
		return Constants.FIND_MATCHED_CORIDERS;
	}

	@Override
	public void onServerResponse(HttpResponse response) {

		if (response == null) {
			if (searchCompletionListener != null) {
				searchCompletionListener.searchFailed("Server internal error");
			}
		} else {
              mResponseEntity = response.getEntity();
			if (mResponseEntity == null) {
				searchCompletionListener.searchFailed("No response from server");
				return;
			} 

			final Handler mHandler = new Handler(Looper.getMainLooper());
			if (mResponseEntity!= null) {
				new Thread() {
					@Override
					public void run() {
					
							try {
								mResponse = EntityUtils.toString(mResponseEntity);
								Log.i("SearchHandler", "Matched corider response is "+mResponse);
								final MatchedCoriders responseParsedData = new Gson()
								.fromJson(mResponse, MatchedCoriders.class);
						mHandler.post(postOperationResult(responseParsedData));
							} catch (ParseException e) {
								mHandler.post(failOperationResult());
							} catch (IOException e) {
								mHandler.post(failOperationResult());
							}
							
						
					}
				}.start();
			} else {
				if (searchCompletionListener != null)
					searchCompletionListener
							.searchFailed("No Response from server");
			}
		}
	}

	/**
	 * method to send post operation result.
	 * 
	 * @param data
	 * @return
	 */
	protected Runnable postOperationResult(final MatchedCoriders coriders) {

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (searchCompletionListener != null)
					searchCompletionListener.searchSuccess(coriders);
			}
		};
		return runnable;
	}

	/**
	 * method to send post operation result.
	 * 
	 * @param data
	 * @return
	 */
	protected Runnable failOperationResult() {

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (searchCompletionListener != null)
					searchCompletionListener.searchFailed("Server error");
			}
		};
		return runnable;
	}

	public interface SearchCompletionListener {
		public void searchSuccess(MatchedCoriders matchedCoriders);

		public void searchFailed(String errorMsg);
	}

	public void setSearchCompletionListener(
			SearchCompletionListener searchCompletionListener) {
		this.searchCompletionListener = searchCompletionListener;
	}
}
