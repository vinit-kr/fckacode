package com.ssinfo.corider.app.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ssinfo.corider.app.R;
import com.ssinfo.corider.app.pojo.Place;

public class SearchListAdapter extends BaseAdapter {
	private List<Place> mPlaceList = new ArrayList<Place>();
    private Context context;
    private TextView placeDescTextView = null;
	public SearchListAdapter(Context context) {
		super();
		this.context = context;
	}

	@Override
	public int getCount() {
		return mPlaceList.size();
	}

	@Override
	public Place getItem(int position) {
		return mPlaceList.get(position);

	}

	/*@Override
	public int getPosition(Place item) {
		return super.getPosition(item);
	}

	@Override
	public long getItemId(int position) {
		return super.getItemId(position);
	}*/

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.place_item, parent, false);
			
		}
		placeDescTextView = (TextView) convertView.findViewById(R.id.placeDesc);
		placeDescTextView.setText(mPlaceList.get(position).getAddress());
		
		return convertView;
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

}
