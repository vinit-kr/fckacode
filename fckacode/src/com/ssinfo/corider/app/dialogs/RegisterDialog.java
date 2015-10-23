package com.ssinfo.corider.app.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ssinfo.corider.app.R;
import com.ssinfo.corider.app.activities.HomeActivity;
import com.ssinfo.corider.app.exceptions.SignUpException;
import com.ssinfo.corider.app.pojo.User;
import com.ssinfo.corider.app.util.AppUtil;
import com.ssinfo.corider.app.util.StringUtil;

public class RegisterDialog extends Dialog implements
		android.view.View.OnClickListener, 
		OnFocusChangeListener {

	
	private EditText etFullName = null;
	private EditText etEmailAddress = null;
	private ProgressDialog mSignupProgressDialog = null;
	private Context mContext = null;
	private Button btnRegister;
	//private Drawable delet_icon;
	private TextView tvErrorMsg = null;

	public RegisterDialog(Context context) {
		super(context);
		mContext = context;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_registration);
	/*	delet_icon = mContext.getResources()
				.getDrawable(R.drawable.delete_icon);*/
		etFullName = (EditText) findViewById(R.id.name);
		etEmailAddress = (EditText) findViewById(R.id.register_email_address);
		btnRegister = (Button) findViewById(R.id.btn_register);
		tvErrorMsg = (TextView) findViewById(R.id.register_error_msg);
		tvErrorMsg.setVisibility(View.INVISIBLE);
		btnRegister.setOnClickListener(this);
		mSignupProgressDialog = new ProgressDialog(mContext);
		mSignupProgressDialog.setCanceledOnTouchOutside(false);
		etEmailAddress.setOnFocusChangeListener((OnFocusChangeListener) this);
		etFullName.setOnFocusChangeListener((OnFocusChangeListener) this);

	}
	
	

	@Override
	protected void onStart() {
		super.onStart();
		// Google analytics
		
		// Flurry analytics
	}

	@Override
	protected void onStop() {
		super.onStop();
		// Google analytics

		// Flurry analytics
	}

	@Override
	public void onClick(View arg0) {
		User mUser = new User();
		String usrFullName = etFullName.getEditableText().toString();
		String emailAddress = etEmailAddress.getEditableText().toString();
		

		try {

			if (StringUtil.isBlankOrNull(emailAddress)
					|| !com.ssinfo.corider.app.util.Validator.isValidEmail(emailAddress)) {
				throw new SignUpException("Invalid email address");
			} else {
				mUser.setEmailId(emailAddress);
			}

			if (StringUtil.isBlankOrNull(usrFullName)) {
				throw new SignUpException(
						"Full name is required for registration");
			} else {
				mUser.setUserName(usrFullName);
			}

			
		//	SaveUserHandler sHandler = new SaveUserHandler(mContext);
			//sHandler.setListener(this);
			

			
			//sHandler.saveUser(mUser);

			if (!mSignupProgressDialog.isShowing()) {
				mSignupProgressDialog.show();
			}

		} catch (SignUpException e) {
			tvErrorMsg.setText(e.getMessage());
			tvErrorMsg.setVisibility(View.VISIBLE);
			 Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
		} /*catch (RegisterException se) {
			tvErrorMsg.setText(se.getMessage());
			tvErrorMsg.setVisibility(View.VISIBLE);
			 Toast.makeText(mContext, se.getMessage(),
			 Toast.LENGTH_LONG).show();
		}*/
	}
	
	public Dialog showMessageDialog(String message, String title) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				mContext);
		if (title != null) {
			alertDialogBuilder.setTitle(title);
		} else {
			alertDialogBuilder.setTitle("INFO:");
		}

		if (message != null) {
			alertDialogBuilder.setMessage(message);
		} else {
			alertDialogBuilder.setMessage("Internal issue");
		}

		alertDialogBuilder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		return alertDialogBuilder.create();

	}

	//@Override
	public void saveUsrFailed(String message) {
		if (mSignupProgressDialog.isShowing()) {
			mSignupProgressDialog.dismiss();
		}
	tvErrorMsg.setText(message);
		tvErrorMsg.setVisibility(View.VISIBLE);
	}

	//@Override
	public void saveUsrSuccess(User data) {
		//LoginUtil.saveSuccessLoginResponse(data, mContext);
		AppUtil.saveUserInfoInSharedPref(mContext,data);
	    if (mSignupProgressDialog.isShowing()) {
			mSignupProgressDialog.dismiss();
		}
		this.dismiss();
		Intent homeIntent = new Intent(mContext,HomeActivity.class);
		homeIntent.putExtra("user", data);
		mContext.startActivity(homeIntent);
       ((Activity)mContext).finish();
	}

	@Override
	public void onFocusChange(View view, boolean arg1) {
		/*
		((EditText) view).setCompoundDrawablesWithIntrinsicBounds(null, null,
				delet_icon, null);*/
	}

}
