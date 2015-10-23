package com.ssinfo.corider.app.handlers;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.ssinfo.corider.app.network.ServerConnection;
import com.ssinfo.corider.app.network.ServerConnection.OnServerRequestCompletionListener;
import com.ssinfo.corider.app.network.request.Constants;
import com.ssinfo.corider.app.pojo.User;

public class UserActivationHandler implements OnServerRequestCompletionListener {

	private OnUserActivationVerification mOnUserAccountActivationListener;
	private String mResponse;
	
	public void isUserAccountActive(String userId) {
		String url = makeUrl(userId);
		new ServerConnection(this).sendRequest(url, null);
		
		//new CreateConnection(url,"".getBytes(), this,NetworkConstants.REQUEST_METHOD_GET);

	}
	
	/**
	 * Method to return url
	 * 
	 * @param modelData
	 * 
	 * @return
	 */
	private String makeUrl(String userId) {
		return Constants.IS_USER_ACTIVE + getBody(userId);
	}
	private String getBody(String userId) {
		
		StringBuilder bldr = new StringBuilder();

		bldr.append("userId");
		bldr.append("=");
		bldr.append(userId);
		return bldr.toString();
	}

	

	/*public void onNetworkStatus(NetworkStatus _networkStatus) {

		if (_networkStatus.getIsNetworkError() || _networkStatus.getServerResponseData() == null) {
			if (mOnUserAccountActivationListener != null)
				mOnUserAccountActivationListener.onFailed("Error");
		} else {
			mResponse = new String(_networkStatus.getServerResponseData());
			//AppUtil.writeToSdcard((Context)mOnRouteRequestListener, mResponse);
			final Handler mHandler = new Handler(Looper.getMainLooper());
			if (mResponse != null) {
				
				//Log.i("", mResponse);
				
				new Thread() {
					 
					@Override
					public void run() {
						try {
							final User responseParsedData = new Gson().fromJson(mResponse,
									User.class);
							
							mHandler.post(postOperationResult(responseParsedData));
						} catch (Exception e) {
							mHandler.post(failOperationResult());
						}
					}
				}.start();			} else {
				if (mOnUserAccountActivationListener != null)
					mOnUserAccountActivationListener.onFailed("No Response from server");
			}
		}
	
		
	}*/
	
	/**
	 * method to send post operation result.
	 * 
	 * @param data
	 * @return
	 */
	protected Runnable postOperationResult(final User user) {

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (mOnUserAccountActivationListener != null)
					mOnUserAccountActivationListener.onSuccess(user);
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
				if (mOnUserAccountActivationListener != null)
					mOnUserAccountActivationListener.onFailed("Server error");
			}
		};
		return runnable;
	}
	
	
	
	public interface OnUserActivationVerification {
		public void onSuccess(User user);
		public void onFailed(String errorMsg);
	}
	
	public void setListener(OnUserActivationVerification listener){
		this.mOnUserAccountActivationListener = listener;
	}

	@Override
	public void onServerResponse(final HttpResponse response) {

		if (response == null ) {
			if (mOnUserAccountActivationListener != null){
				mOnUserAccountActivationListener.onFailed("Server internal error");
			}
		} else {
			
			if(response.getEntity() ==null){
				mOnUserAccountActivationListener.onFailed("No Response from Server");
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
							mOnUserAccountActivationListener.onFailed("Unexpected Response from Server");
						} catch (IOException e) {
							mOnUserAccountActivationListener.onFailed("Unexpected Response from Server");
						}
						final User responseParsedData = new Gson().fromJson(mResponse,
								User.class);
						if(mResponse !=null){
							mHandler.post(postOperationResult(responseParsedData));	
						}else {
							if (mOnUserAccountActivationListener != null)
								mOnUserAccountActivationListener.onFailed("No Response from server");
						}
						
					
				}
			}.start();
		/*} else {
			if (mRegistrationCompletionListener != null)
				mRegistrationCompletionListener.onRegistrationFailed("No Response from server");
		}*/
		}
}

}
