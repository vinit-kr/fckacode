package com.ssinfo.corider.app.handlers;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.ssinfo.corider.app.models.ConfigInfo;
import com.ssinfo.corider.app.network.ServerConnection;
import com.ssinfo.corider.app.network.ServerConnection.OnServerRequestCompletionListener;
import com.ssinfo.corider.app.network.request.Constants;

public class ConfigHandler implements OnServerRequestCompletionListener{

	private OnConfigDataListener onConfigDataListener;
	
	public void getAppConfigInfo(){
		String url = makeUrl();
		new ServerConnection(this).sendRequest(url);
	}
	
	private String makeUrl() {
		return Constants.APP_CONFIG_URL;
	}
	@Override
	public void onServerResponse(final HttpResponse response) {

		if (response == null ) {
			if (onConfigDataListener != null){
				onConfigDataListener.onConfigFail("Server internal error");
			}
		} else {
			
			if(response.getEntity() ==null){
				onConfigDataListener.onConfigFail("No Response from Server");
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
					String mResponse =null;
						try {
					      mResponse = EntityUtils.toString(response.getEntity());
						} catch (ParseException e) {
							onConfigDataListener.onConfigFail("Unexpected Response from Server");
						} catch (IOException e) {
							onConfigDataListener.onConfigFail("Unexpected Response from Server");
						}
						
						if(mResponse !=null){
							mHandler.post(postOperationResult(mResponse));	
						}else {
							if (onConfigDataListener != null)
								onConfigDataListener.onConfigFail("No Response from server");
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
	protected Runnable postOperationResult(final String configInfo) {

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (onConfigDataListener != null){
					final ConfigInfo appConfig = new Gson().fromJson(configInfo, ConfigInfo.class);
					onConfigDataListener.onConfigReturn(appConfig);
				}
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
				if (onConfigDataListener != null)
					onConfigDataListener.onConfigFail("Server error");
			}
		};
		return runnable;
	}
	

	public void setOnConfigDataListener(OnConfigDataListener onConfigDataListener) {
		this.onConfigDataListener = onConfigDataListener;
	}
	
	public interface OnConfigDataListener{
		public void onConfigReturn(ConfigInfo configProperties);
		public void onConfigFail(String message);	
		
	}

}
