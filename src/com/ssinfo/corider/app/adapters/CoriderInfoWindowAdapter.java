package com.ssinfo.corider.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.ssinfo.corider.app.R;
import com.ssinfo.corider.app.constants.Constants;
import com.ssinfo.corider.app.util.AppUtil;

public class CoriderInfoWindowAdapter implements InfoWindowAdapter {
	private Context mContext;
	
	public CoriderInfoWindowAdapter(Context context) {
		this.mContext = context;
	}

	@Override
	public View getInfoContents(Marker marker) {
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		View view = inflater.inflate(R.layout.user_info_window, null);
		TextView titleView = (TextView) view.findViewById(R.id.infoTitle);
		TextView addressView = (TextView) view.findViewById(R.id.infoAddress);

		String title = marker.getTitle();
		String data[] = title.split(",");
		
		if (Constants.CURRENT_LOC.equals(data[0])
				|| Constants.DESTINATION.equals(data[0])) {
			titleView.setText(data[0]);

			StringBuilder bldr = new StringBuilder();
			for (int i = 1; i < data.length; i++) {
				System.out.println("Inside address population..");
				bldr.append(data[i]);
				bldr.append(",");
			}
			addressView.setText(bldr.toString());

		} else {
			// view = inflater.inflate(R.layout.infowindow_layout, null);
			/*TextView coriderName = (TextView) view
					.findViewById(R.id.coriderName);
			TextView matchedKm = (TextView) view.findViewById(R.id.distance);
			TextView routeName = (TextView) view.findViewById(R.id.destination);*/
			StringBuilder coriderTitleBldr = new StringBuilder();
			if (data.length > 1) {
				coriderTitleBldr.append(data[1]);
				coriderTitleBldr.append(" | ");
				if (data.length > 2) {
					coriderTitleBldr
							.append(AppUtil.convertInKm(data[2]) + " km");
					if (data.length > 3) {
						addressView.setText(data[3]);
					}
				}
				titleView.setText(coriderTitleBldr.toString());
			}
		}

		return view;

	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
}

	

}
