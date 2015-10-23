package com.ssinfo.corider.app.handlers;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.ssinfo.corider.app.models.MessageDTO;
import com.ssinfo.corider.app.network.ServerConnection;
import com.ssinfo.corider.app.network.ServerConnection.OnServerRequestCompletionListener;
import com.ssinfo.corider.app.network.request.Constants;
import com.ssinfo.corider.app.pojo.User;


public class MessageHandler implements OnServerRequestCompletionListener{

	private OnMessageDeliveredListener mOnMessageDeliveredListener;
	private Context mContext;
	private String mResponse;
	
	
	public void sendMessage(MessageDTO messagedto){
		String url = makeUrl();
		//Gson gson = new Gson();
		//String requestedParam = gson.toJson(messagedto);
		//System.out.println(requestedParam);
		ServerConnection serverConnection = new ServerConnection(this);
		serverConnection.sendRequest(url, messagedto);
	}
	
	/**
	 * Method to return url
	 * 
	 * @param modelData
	 * 
	 * @return
	 */
	private String makeUrl() {
		return Constants.SEND_MESSAGE;
	}
	
		@Override
		public void onServerResponse(final HttpResponse response) {

			if (response == null ) {
				if (mOnMessageDeliveredListener != null){
					mOnMessageDeliveredListener.onMessageDeliveryFailed("Message couldn't be sent.Please try again.");
				}
			} else {
				
				if(response.getEntity() ==null){
					mOnMessageDeliveredListener.onMessageDeliveryFailed("Message couldn't be sent.Please try again.");
					return;
				}
				
			final Handler mHandler = new Handler(Looper.getMainLooper());
			//if (mResponse != null) {
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
								mOnMessageDeliveredListener.onMessageDeliveryFailed("Message couldn't be sent.Please try again.");
							} catch (IOException e) {
								mOnMessageDeliveredListener.onMessageDeliveryFailed("Message couldn't be sent.Please try again.");
							}
							/*final User responseParsedData = new Gson().fromJson(mResponse,
									User.class);*/
							if(mResponse !=null){
								mHandler.post(postOperationResult(mResponse));	
							}else {
								if (mOnMessageDeliveredListener != null)
									mOnMessageDeliveredListener.onMessageDeliveryFailed("Message couldn't be sent.Please try again.");
							}
							
						
					}
				}.start();
			/*} else {
				if (mRegistrationCompletionListener != null)
					mRegistrationCompletionListener.onRegistrationFailed("No Response from server");
			}*/
			}
	}
	
	/**
	 * method to send post operation result.
	 * 
	 * @param data
	 * @return
	 */
	protected Runnable postOperationResult(final String message) {

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (mOnMessageDeliveredListener != null)
					mOnMessageDeliveredListener.onMessageDeliveryFailed("Message couldn't be sent.Please try again.");
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
				if (mOnMessageDeliveredListener != null)
					mOnMessageDeliveredListener.onMessageDeliveryFailed("Message couldn't be sent.Please try again.");
			}
		};
		return runnable;
	}
	
	
	
	public interface OnMessageDeliveredListener {
		public void onMessageDelivered(String message);
		public void onMessageDeliveryFailed(String errorMsg);
	}
	
	public void setListener(OnMessageDeliveredListener listener){
		this.mOnMessageDeliveredListener = listener;
	}

	

	

}
