package com.ssinfo.corider.app.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssinfo.corider.app.R;
import com.ssinfo.corider.app.models.NavDrawerItem;

public class NavDrawerListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private TypedArray navMenuIcons;

	public NavDrawerListAdapter(Context context,
			ArrayList<NavDrawerItem> navDrawerItems, TypedArray navMenuIcons) {
		this.context = context;
		this.navDrawerItems = navDrawerItems;
		this.navMenuIcons = navMenuIcons;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.nav_item, null);
			ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
			TextView label = (TextView) convertView.findViewById(R.id.label);
			imgIcon.setImageResource((navDrawerItems.get(position)).getIcon());
			label.setText((navDrawerItems.get(position)).getTitle());

		}

		return convertView;
	}

}