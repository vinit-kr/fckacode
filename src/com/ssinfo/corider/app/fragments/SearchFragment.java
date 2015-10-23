package com.ssinfo.corider.app.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;

import com.ssinfo.corider.app.R;
import com.ssinfo.corider.app.adapters.SearchListAdapter;
import com.ssinfo.corider.app.search.SearchProvider;

public class SearchFragment extends Fragment implements
		OnItemClickListener, OnClickListener, SearchView.OnQueryTextListener {

	private Context context;
	private SearchProvider mSearchProvider = null;
	private ListView mListView;
	private SearchView mSearchView;
	private SearchListAdapter mAdapter;
//	private ListView placesListView = null;

	public SearchFragment(Context context) {
		this.context = context;
		mSearchProvider = new SearchProvider();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
			View view = inflater.inflate(R.layout.layout_search_places, container,
					false);
			mSearchView = (SearchView) view.findViewById(R.id.search_view);
			mListView = (ListView) view.findViewById(R.id.place_list);
			mListView.setOnItemClickListener(this);
			mListView.setAdapter(mAdapter);
			mSearchView.setOnQueryTextListener(this);
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new SearchListAdapter(context);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onQueryTextChange(String query) {
        String searchResponse = mSearchProvider.getPlaces(query);
        System.out.println(searchResponse);
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {

		return false;
	}

}
