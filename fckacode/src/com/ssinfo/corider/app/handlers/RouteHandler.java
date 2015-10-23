package com.ssinfo.corider.app.handlers;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.ssinfo.corider.app.models.AllRoutes;
import com.ssinfo.corider.app.models.Distance;
import com.ssinfo.corider.app.models.Duration;
import com.ssinfo.corider.app.models.Legs;
import com.ssinfo.corider.app.models.RiderLocation;
import com.ssinfo.corider.app.models.Routes;
import com.ssinfo.corider.app.network.CreateConnection;
import com.ssinfo.corider.app.network.NetworkConstants;
import com.ssinfo.corider.app.network.NetworkStatus;
import com.ssinfo.corider.app.network.OnCreateConnection;
import com.ssinfo.corider.app.network.request.Constants;
import com.ssinfo.corider.app.network.request.ReqParam;

public class RouteHandler implements OnCreateConnection {

	private OnRouteRequestListener mOnRouteRequestListener;
	private Context mContext;
	private String mResponse;
	private String mUserId;
	
	
	public void getAllRouteDetails(RiderLocation sourceLocation,RiderLocation destination) {
		String url = makeUrl(sourceLocation,destination);
		new CreateConnection(url,"".getBytes(), this,NetworkConstants.REQUEST_METHOD_POST);

	}
	
	/**
	 * Method to return url
	 * 
	 * @param modelData
	 * 
	 * @return
	 */
	private String makeUrl(RiderLocation sourceLocation, RiderLocation destination) {
		return Constants.DIRECTION_URL + getBody(sourceLocation,destination);
	}
	private String getBody(RiderLocation sourceLocation, RiderLocation destination) {
		
		StringBuilder bldr = new StringBuilder();

		bldr.append(ReqParam.ORIGIN);
		bldr.append("=");
		bldr.append(sourceLocation.getLatitude());
		bldr.append(",");
		bldr.append(sourceLocation.getLongitude());
			bldr.append("&");
			bldr.append(ReqParam.DESTINATION);
			bldr.append("=");
			bldr.append(destination.getLatitude());
			bldr.append(",");
			bldr.append(destination.getLongitude());
		
			bldr.append("&");
			bldr.append(ReqParam.ALTERNATIVE);
			bldr.append("=");
			bldr.append("true");
			
			bldr.append("&");
			bldr.append(ReqParam.DEPARTURE_TIME);
			bldr.append("=");
			long departureTime = System.currentTimeMillis() / 1000;
			bldr.append(departureTime);
			
		return bldr.toString();
	}

	
	@Override
	public void onNetworkStatus(NetworkStatus _networkStatus) {

		if (_networkStatus.getIsNetworkError()) {
			if (mOnRouteRequestListener != null)
				mOnRouteRequestListener.onRouteFailed("Error");
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
							final AllRoutes responseParsedData = new Gson().fromJson(mResponse,
									AllRoutes.class);
							mHandler.post(postOperationResult(responseParsedData));
						} catch (Exception e) {
							mHandler.post(failOperationResult());
						}
					}
				}.start();			} else {
				if (mOnRouteRequestListener != null)
					mOnRouteRequestListener.onRouteFailed("No Response from server");
			}
		}
	
		
	}
	
	public void analyzeBestRoute(AllRoutes routes){
	  
		if(routes !=null){
			Routes[] routesArr = routes.getRoutes();
			int routeCount =0;
			
			for(Routes route:routesArr){
			     Legs[] legsArr = route.getLegs();
			     int legsCount = 0;
				  for(Legs legs:legsArr){
					  legsCount++;
					  System.out.println("Legs "+legsCount);
					  Duration duration = legs.getDuration();
					  Distance distance = legs.getDistance();
					  Duration durationInTraffic = legs.getDuration_in_traffic();
					  System.out.println("duration is "+duration.getValue());
					  System.out.println("distance is "+distance.getValue());
					  if(durationInTraffic != null){
						  System.out.println("duration in traffice "+durationInTraffic.getValue());  
					  }
					  
					  System.out.println("===================================");
					  
				  }
				
				
			}
		}
		
	}
	
	/**
	 * method to send post operation result.
	 * 
	 * @param data
	 * @return
	 */
	protected Runnable postOperationResult(final AllRoutes routesInfo) {

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (mOnRouteRequestListener != null)
					mOnRouteRequestListener.onRouteFound(routesInfo);
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
				if (mOnRouteRequestListener != null)
					mOnRouteRequestListener.onRouteFailed("Server error");
			}
		};
		return runnable;
	}
	
	
	
	public interface OnRouteRequestListener {
		public void onRouteFound(AllRoutes routesInfo);
		public void onRouteFailed(String errorMsg);
	}
	
	public void setListener(OnRouteRequestListener listener){
		this.mOnRouteRequestListener = listener;
	}
}
