package com.ssinfo.corider.app.handlers;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Looper;

import com.ssinfo.corider.app.network.ServerConnection;
import com.ssinfo.corider.app.network.ServerConnection.OnServerRequestCompletionListener;
import com.ssinfo.corider.app.network.request.Constants;
import com.ssinfo.corider.app.pojo.User;

public class RefreshHandler implements OnServerRequestCompletionListener{

	private OnRefreshListener mOnRefreshListener;
	
	public void refreshRoutes(User user) {
		String url = makeUrl(user.getUserId());
		new ServerConnection(this).sendRequest(url, null);
		//new CreateConnection(url,"".getBytes(), this,NetworkConstants.REQUEST_METHOD_GET);

	}
	private String makeUrl(String userId) {
		return Constants.REFRESH_ROUTES + getBody(userId);
	}
	/**
	 * Method to return url
	 * 
	 * @param modelData
	 * 
	 * @return
	 */
private String getBody(String userId) {
		
		StringBuilder bldr = new StringBuilder();

		bldr.append("userId");
		bldr.append("=");
		bldr.append(userId);
		return bldr.toString();
	}
	
	/*@Override
	public void onNetworkStatus(NetworkStatus _networkStatus) {

		if (_networkStatus.getIsNetworkError()) {
			if (mOnRefreshListener != null)
				mOnRefreshListener.onRefreshFailed("Error");
		} else {
			mResponse = new String(_networkStatus.getServerResponseData());
			final Handler mHandler = new Handler(Looper.getMainLooper());
			if (mResponse != null) {
				
				new Thread() {
					 
					@Override
					public void run() {
						try {
							
							
							mHandler.post(postOperationResult(mResponse));
						} catch (Exception e) {
							mHandler.post(failOperationResult());
						}
					}
				}.start();			} else {
				if (mOnRefreshListener != null)
					mOnRefreshListener.onRefreshFailed("No Response from server");
			}
		}
	
		
	}*/
	
	
	
	/**
	 * method to send post operation result.
	 * 
	 * @param data
	 * @return
	 */
	protected Runnable postOperationResult(final String status) {

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (mOnRefreshListener != null)
					mOnRefreshListener.onRefreshRoute(status);
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
				if (mOnRefreshListener != null)
					mOnRefreshListener.onRefreshFailed("Server error");
			}
		};
		return runnable;
	}
	
	
	
	public interface OnRefreshListener {
		public void onRefreshRoute(String message);
		public void onRefreshFailed(String errorMsg);
	}
	
	public void setListener(OnRefreshListener listener){
		this.mOnRefreshListener = listener;
	}

	@Override
	public void onServerResponse(final HttpResponse response) {

		if (response == null ) {
			if (mOnRefreshListener != null){
				mOnRefreshListener.onRefreshFailed("Server internal error");
			}
		} else {
			
			if(response.getEntity() ==null){
				mOnRefreshListener.onRefreshFailed("No Response from Server");
				return;
			}
			
		final Handler mHandler = new Handler(Looper.getMainLooper());
			new Thread() {
				
				@Override
				public void run() {
					String mResponse =null;
						try {
					      mResponse = EntityUtils.toString(response.getEntity());
						} catch (ParseException e) {
							mOnRefreshListener.onRefreshFailed("Unexpected Response from Server");
						} catch (IOException e) {
							mOnRefreshListener.onRefreshFailed("Unexpected Response from Server");
						}
						
						if(mResponse !=null){
							mHandler.post(postOperationResult(mResponse));
							//mOnRefreshListener.onRefreshRoute(mResponse);	
						}else {
							if (mOnRefreshListener != null)
								mOnRefreshListener.onRefreshFailed("No Response from server");
						}
						
					
				}
			}.start();
		
		}
}
}
