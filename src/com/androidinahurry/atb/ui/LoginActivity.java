/*
 * Copyright (C) 2013 Omar BELKHODJA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.androidinahurry.atb.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidinahurry.atb.R;
import com.androidinahurry.atb.service.ErrorCode;
import com.androidinahurry.atb.service.LoginLoader;
import com.androidinahurry.network.utils.Response;
import com.androidinahurry.utils.DialogUtils;
import com.androidinahurry.utils.DialogUtils.LoginFailureDialogFragment;

public class LoginActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Response<Boolean, ErrorCode>>, OnClickListener {
	
	final static String LOADER_ARG_USER = "user";
	final static String LOADER_ARG_PASSWORD = "password";
	
	final static String PREFS_NAME = "creadentials";
	final static String PREFS_ARG_USERNAME = "user";
	final static String PREFS_ARG_PASSWORD = "password";
	
	private ProgressDialog progressDialog;
	private Button loginButton;
	private EditText userEditText;
	private EditText passwordEditText;
	private LoginFailureDialogFragment loginFailureDialog;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		userEditText = (EditText) findViewById(R.id.userEditText);
		passwordEditText = (EditText) findViewById(R.id.passwordEditText);
		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(this);
		
		// Restore preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String username = settings.getString(PREFS_ARG_USERNAME, "");
		String password = settings.getString(PREFS_ARG_PASSWORD, "");
		userEditText.setText(username);
		passwordEditText.setText(password);
		
		// Init the loader
		if(getSupportLoaderManager().getLoader(LoaderId.LOGIN) != null) {
			showProgressDialog();
			getSupportLoaderManager().initLoader(LoaderId.LOGIN, null, this);
		}
	}

	void showProgressDialog() {
		String loggingMessage = getResources().getString(R.string.logging);
		progressDialog = ProgressDialog.show(this, "", loggingMessage);
	}
	
	void hideProgressDialog() {
		if(progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	@Override
	public void onClick(View loginButton) {
		showProgressDialog();
		
		Bundle bundle = new Bundle();
		bundle.putString(LOADER_ARG_USER, userEditText.getText().toString());
		bundle.putString(LOADER_ARG_PASSWORD, passwordEditText.getText().toString());
		
		getSupportLoaderManager().restartLoader(LoaderId.LOGIN, bundle , this);
	}

	@Override
	public Loader<Response<Boolean, ErrorCode>> onCreateLoader(int id, Bundle bundle) {
		return new LoginLoader(this, bundle.getString(LOADER_ARG_USER), bundle.getString(LOADER_ARG_PASSWORD));
	}

	@Override
	public void onLoadFinished(Loader<Response<Boolean, ErrorCode>> loader, Response<Boolean, ErrorCode> response) {
		hideProgressDialog();
		
		if(response.errorCode == ErrorCode.NO_ERROR) {
			if(response.data.booleanValue()) {
				// Login success
				
				// Save credentials
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			    SharedPreferences.Editor editor = settings.edit();
			    editor.putString(PREFS_ARG_USERNAME, userEditText.getText().toString());
			    editor.putString(PREFS_ARG_PASSWORD, passwordEditText.getText().toString());
			    editor.commit();
				
				// Start Account activity
				Intent intent = new Intent(this, AccountActivity.class);
				intent.putExtra(AccountActivity.ARG_USER, userEditText.getText().toString());
				intent.putExtra(AccountActivity.ARG_PASSWORD, passwordEditText.getText().toString());
				
				startActivity(intent);
				finish();
			}
			else {
				// Show error message
				Toast.makeText(this,R.string.logging_failure, Toast.LENGTH_SHORT).show();
				
				// Save empty credentials
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			    SharedPreferences.Editor editor = settings.edit();
			    editor.putString(PREFS_ARG_USERNAME, "");
			    editor.putString(PREFS_ARG_PASSWORD, "");
			    editor.commit();
				
				//  Set focus and show erroneous password
				String passwordErrorMsg = getResources().getString(R.string.password_error);
				
				passwordEditText.setText("");
				passwordEditText.requestFocus();
				passwordEditText.setError(passwordErrorMsg);
			}
		}
		else {
			// TODO
		}
		getSupportLoaderManager().destroyLoader(LoaderId.LOGIN);
		

	}

	@Override
	public void onLoaderReset(Loader<Response<Boolean, ErrorCode>> loader) {
	}
	
	@Override
	protected void onStop() {
		hideProgressDialog();
		
		super.onStop();
	}
	

}
