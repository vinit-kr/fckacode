package com.ssinfo.corider.app.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ssinfo.corider.app.R;
import com.ssinfo.corider.app.constants.Constants;

public class WebViewFragment extends Fragment{


	private Context mContext;
	private WebView webview;
	private String loadUrl=null;
	private ProgressBar mLoadingProgress;

	
	/**
	 * @return the loadUrl
	 */
	public String getLoadUrl() {
		if(this.loadUrl == null){
			return Constants.ABOUT_US;
		}
		return loadUrl;
	}

	
	public void setLoadUrl(String loadUrl) {
		this.loadUrl = loadUrl;
	}

	Bundle webViewBundle;
	View view;

	public WebViewFragment() 
	{

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		webview.restoreState(savedInstanceState);
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setRetainInstance(true);
		
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

			View view = inflater.inflate(R.layout.webview_layout, container,
					false);
			

			mLoadingProgress = (ProgressBar) view.findViewById(R.id.aboutprogressBar);
			webview = (WebView) view.findViewById(R.id.webView);
			webview.getSettings().setJavaScriptEnabled(true);
			webview.loadUrl(getLoadUrl());
			webview.setWebViewClient(new WebViewClient() {
				
				@Override
				public void onPageStarted(WebView view, String url,
						Bitmap favicon) {
					mLoadingProgress.setVisibility(View.VISIBLE);
					super.onPageStarted(view, url, favicon);
				}

				@Override
				public void onPageFinished(WebView view, String url) {
					mLoadingProgress.setVisibility(View.INVISIBLE);
					super.onPageFinished(view, url);
				}

				@Override
				public boolean shouldOverrideUrlLoading(WebView view,
						String url) {
					
					return super.shouldOverrideUrlLoading(view, url);

				}
			});
			
		return view;

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext = activity;
	}

	
}
