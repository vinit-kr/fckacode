package com.ssinfo.corider.app.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ssinfo.corider.app.R;
import com.ssinfo.corider.app.adapters.ListViewAdapterHome;
import com.ssinfo.corider.app.adapters.ListViewAdapterHome.RowType;
import com.ssinfo.corider.app.handlers.MessageHandler;
import com.ssinfo.corider.app.handlers.MessageHandler.OnMessageDeliveredListener;
import com.ssinfo.corider.app.models.Header;
import com.ssinfo.corider.app.models.ListItem;
import com.ssinfo.corider.app.models.MatchedCorider;
import com.ssinfo.corider.app.models.MatchedCoriders;
import com.ssinfo.corider.app.models.MessageDTO;
import com.ssinfo.corider.app.models.RouteCoriders;
import com.ssinfo.corider.app.pojo.User;
import com.ssinfo.corider.app.util.AppUtil;

public class MatchedCoridersFragment extends Fragment implements
		OnItemClickListener, OnClickListener, OnMessageDeliveredListener {

	private View view = null;
	private static final String TAG = MatchedCoridersFragment.class.getName();

	private Context mContext;
	private ListViewAdapterHome mListAdapter = null;
	private TextView txtSearchDestination = null;
	private ListView mListView;
	private MessageHandler mMessageHandler = null;

	private String mSearchtext = null;

	private List<Item> items = new ArrayList<Item>();

	public MatchedCoridersFragment() {
	}

	public void setHeader(String searchHeaderMsg) {
		this.mSearchtext = searchHeaderMsg;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	   
		super.onCreate(savedInstanceState);
		Log.i(TAG, "Inside onCreate of MatchedCoridersFragment");
		mListAdapter = new ListViewAdapterHome(getActivity(), items);

		mMessageHandler = new MessageHandler();
		mMessageHandler.setListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        
		view = inflater
				.inflate(R.layout.fragment_search_list, null);
		mListView = (ListView) view.findViewById(R.id.coriders_list);

		mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(this);
		txtSearchDestination = (TextView) view.findViewById(R.id.search_header);
		txtSearchDestination.setText(mSearchtext);
		return view;

	}

	@Override
	public void onAttach(Activity activity) {
		mContext = activity;

		super.onAttach(activity);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onStart() {

		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	public void setCoriders(MatchedCoriders matchedCoriders) {
		if (null != items) {
			items.clear();
			RouteCoriders[] routeCoridersArr = matchedCoriders
					.getRouteCoriders();

			if (routeCoridersArr != null && routeCoridersArr.length > 0) {
				for (RouteCoriders routeCorider : routeCoridersArr) {
					if (routeCorider == null) {
						break;
					}
					items.add(new Header(routeCorider.getRouteName(),
							routeCorider.getTotalDistanceOfRoute(),
							routeCorider.getTotalDuration(), routeCorider
									.getMatchedCoriderList().length));
					for (MatchedCorider corider : routeCorider
							.getMatchedCoriderList()) {
						items.add(new ListItem(corider.getUserName(), corider
								.getUserName(), corider.getMatchedDistance(),
								corider.getMatchedRoute(), corider.getMinutes(),corider.getGcmRegistrationId()));

					}
				}

			}
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Item item = items.get(position);
		if (item.getViewType() == RowType.HEADER_ITEM.ordinal()) {
			return;
		} else {
			showMessageDialog(item);
		}

	}

	public void showMessageDialog(final Item item) {
		AlertDialog.Builder alert = new AlertDialog.Builder(mContext,
				AlertDialog.THEME_HOLO_LIGHT);
		// set the custom title bar only.
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		
		FrameLayout titlelayout = new FrameLayout(this.getActivity());
		titlelayout.layout(10, 10, 10, 10);
		View view = inflater.inflate(R.layout.layout_message_dialog_title, titlelayout);
		
		final ListItem listItem = (ListItem) item;
		String coriderName = listItem.getUserName();
		String totalDistance = String.valueOf(listItem.getDistanceInKm());
		String destination = listItem.getMatchedRoute();

		TextView toUserTextView = (TextView) view
				.findViewById(R.id.coriderName);
		TextView toKmTextview = (TextView) view.findViewById(R.id.distance);
		TextView toRouteTextView = (TextView) view
				.findViewById(R.id.destination);

		// set the value
		toUserTextView.setText("To: "+coriderName);
		toKmTextview.setText(totalDistance+" km");
		toRouteTextView.setText(destination);
		alert.setCustomTitle(view);

		 FrameLayout frameLayout = new FrameLayout(this.getActivity());
		 FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(300, 600, Gravity.CENTER_VERTICAL);
		 alert.setView(frameLayout);
		 //AlertDialog dialog = alert.create();
			View dialoglayout = inflater.inflate(R.layout.dialog_body, frameLayout);
			alert.setView(dialoglayout);
			final EditText input = (EditText) dialoglayout.findViewById(R.id.edit_msg);
			input.setText(R.string.share_a_ride_msg);
		alert.setPositiveButton(R.string.send_lbl, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
				String value = input.getText().toString();
				User fromUser = AppUtil.getUserInfo(mContext);
				User toUser = new User();
				toUser.setUserId(String.valueOf(listItem.getUserId()));
				toUser.setUserName(listItem.getUserName());
				toUser.setGcmRegistrationId(listItem.getGcmRegistrationId());
				MessageDTO messageDto = new MessageDTO();
				messageDto.setFromUser(fromUser);
				messageDto.setTouser(toUser);
				//messageDto.setTouser(fromUser); // TODO just for testing purpose
												// on single device.
				messageDto.setMsg(value);
				messageDto.setDestination(listItem.getMatchedRoute());
				messageDto.setMatchedDistance(String.valueOf(listItem
						.getMatchedDistance()));
				mMessageHandler.sendMessage(messageDto);
				dialog.dismiss();
			}
		});

		alert.setNegativeButton(R.string.cancel_lbl,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
						dialog.dismiss();
					}
				});

		
		
		alert.show();
	}

	@Override
	public void onMessageDelivered(String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onMessageDeliveryFailed(String errorMsg) {
		Toast.makeText(mContext, "Message delivery failed. Please try again",
				Toast.LENGTH_LONG).show();
	}

	public interface Item {
		public int getViewType();

		public View getView(LayoutInflater inflater, View convertView);
	}
}
