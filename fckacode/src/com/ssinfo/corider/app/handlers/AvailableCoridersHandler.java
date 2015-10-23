package com.ssinfo.corider.app.handlers;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.ssinfo.corider.app.models.MatchedCorider;
import com.ssinfo.corider.app.models.MatchedCoriders;
import com.ssinfo.corider.app.models.SearchParams;
import com.ssinfo.corider.app.network.ServerConnection;
import com.ssinfo.corider.app.network.ServerConnection.OnServerRequestCompletionListener;
import com.ssinfo.corider.app.network.request.Constants;

public class AvailableCoridersHandler implements OnServerRequestCompletionListener {

	private OnFindAvailCoridersCompletionListener onFindAvailCoridersCompletionListener;

	private String mResponse;
	private String mUserId;
	private HttpEntity mResponseEntity = null;

	public void searchCoriders(SearchParams searchInfo) {
		String url = makeUrl();
		Gson gson = new Gson();
		String requestedParam = gson.toJson(searchInfo);
		System.out.println(requestedParam);
		ServerConnection serverConnection = new ServerConnection(this);
		serverConnection.sendRequest(url, searchInfo);
		
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
			if (onFindAvailCoridersCompletionListener != null) {
				onFindAvailCoridersCompletionListener.onFindAvailCoridersFailed("Server internal error");
			}
		} else {
              mResponseEntity = response.getEntity();
			if (mResponseEntity == null) {
				onFindAvailCoridersCompletionListener.onFindAvailCoridersFailed("No response from server");
				return;
			} 

			final Handler mHandler = new Handler(Looper.getMainLooper());
			if (mResponseEntity!= null) {
				new Thread() {
					@Override
					public void run() {
					
							try {
								mResponse = EntityUtils.toString(mResponseEntity);
								final MatchedCoriders responseParsedData = new Gson()
								.fromJson(mResponse, MatchedCoriders.class);
						mHandler.post(postOperationResult(responseParsedData));
							} catch (ParseException e) {
								/*searchCompletionListener
										.searchFailed("Unexpected Response from Server");*/
								mHandler.post(failOperationResult());
							} catch (IOException e) {
								mHandler.post(failOperationResult());
								/*searchCompletionListener
										.searchFailed("Unexpected Response from Server");*/
							}
							
						
					}
				}.start();
			} else {
				if (onFindAvailCoridersCompletionListener != null)
					onFindAvailCoridersCompletionListener
							.onFindAvailCoridersFailed("No Response from server");
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
				//if (onFindAvailCoridersCompletionListener != null)
					//onFindAvailCoridersCompletionListener.onFindAvailCoriders(coriders.getT);
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
				if (onFindAvailCoridersCompletionListener != null)
					onFindAvailCoridersCompletionListener.onFindAvailCoridersFailed("Server error");
			}
		};
		return runnable;
	}

	public interface OnFindAvailCoridersCompletionListener {
		public void onFindAvailCoriders(MatchedCorider[] availableUsers);

		public void onFindAvailCoridersFailed(String errorMsg);
	}

	public void setSearchCompletionListener(OnFindAvailCoridersCompletionListener onFindAvailCompletionListener) {
		this.onFindAvailCoridersCompletionListener = onFindAvailCompletionListener;
	}

}
