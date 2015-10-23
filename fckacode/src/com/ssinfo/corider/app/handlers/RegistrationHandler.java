package com.ssinfo.corider.app.handlers;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.ssinfo.corider.app.network.ServerConnection;
import com.ssinfo.corider.app.network.ServerConnection.OnServerRequestCompletionListener;
import com.ssinfo.corider.app.network.request.Constants;
import com.ssinfo.corider.app.pojo.User;

public class RegistrationHandler implements OnServerRequestCompletionListener {

	private OnRegistrationCompletionListener mRegistrationCompletionListener;
	private Context mContext;
	private String mResponse;
	private String mUserId;
	
	
	public void registerUser(User userInfo) {
		String url = makeUrl(userInfo);
		ServerConnection serverConnection = new ServerConnection(this);
		serverConnection.sendRequest(url, userInfo);
		//new ServerConnection(url,requestedParam.getBytes(), this,NetworkConstants.REQUEST_METHOD_POST);
	}
	
	/**
	 * Method to return url
	 * 
	 * @param modelData
	 * 
	 * @return
	 */
	private String makeUrl(User modelData) {
		return Constants.SAVE_USER;
	}
	
		@Override
		public void onServerResponse(final HttpResponse response) {

			if (response == null ) {
				if (mRegistrationCompletionListener != null){
					mRegistrationCompletionListener.onRegistrationFailed("Server internal error");
				}
			} else {
				
				if(response.getEntity() ==null){
					mRegistrationCompletionListener.onRegistrationFailed("No Response from Server");
					return;
				}
				
			final Handler mHandler = new Handler(Looper.getMainLooper());
				new Thread() {
					/*
					 * (non-Javadoc)
					 * 
					 * @see java.lang.Thread#run()
					 */
					@Override
					public void run() {
							
							try {
								mResponse = EntityUtils.toString(response.getEntity());
							} catch (ParseException e) {
								mRegistrationCompletionListener.onRegistrationFailed("Unexpected Response from Server");
							} catch (IOException e) {
								mRegistrationCompletionListener.onRegistrationFailed("Unexpected Response from Server");
							}
							final User responseParsedData = new Gson().fromJson(mResponse,
									User.class);
							if(mResponse !=null){
								mHandler.post(postOperationResult(responseParsedData));	
							}else {
								if (mRegistrationCompletionListener != null)
									mRegistrationCompletionListener.onRegistrationFailed("No Response from server");
							}
					}
				}.start();
			}
	}
	
	/**
	 * method to send post operation result.
	 * 
	 * @param data
	 * @return
	 */
	protected Runnable postOperationResult(final User userInfo) {

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (mRegistrationCompletionListener != null)
					mRegistrationCompletionListener.onRegistrationCompletion(userInfo);
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
				if (mRegistrationCompletionListener != null)
					mRegistrationCompletionListener.onRegistrationFailed("Server error");
			}
		};
		return runnable;
	}
	
	
	
	public interface OnRegistrationCompletionListener {
		public void onRegistrationCompletion(User userInfo);
		public void onRegistrationFailed(String errorMsg);
	}
	
	public void setListener(OnRegistrationCompletionListener listener){
		this.mRegistrationCompletionListener = listener;
	}

	

	

}
