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

import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;

import com.androidinahurry.atb.R;
import com.androidinahurry.utils.DialogUtils;
import com.androidinahurry.utils.LoadingViewListener;

public class AccountActivity extends FragmentActivity implements LoadingViewListener {
	
	public static String ARG_USER = "user";
	public static String ARG_PASSWORD = "password";

	private View sumUpLoaderLayout;
	private View sumUpLayout;
	SparseBooleanArray mFinishedLoaders = new SparseBooleanArray();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_account);
		
		sumUpLoaderLayout = (View) findViewById(R.id.sumUpLoaderLayout);
		sumUpLayout = (View) findViewById(R.id.sumUpLayout);
		
		mFinishedLoaders.put(LoaderId.ACCOUNT, false);
		mFinishedLoaders.put(LoaderId.HISTORY,false);
		showLoadingView();
	
		// Initial setup
		if (savedInstanceState == null) {
			// Extract data from from extras 
			Bundle extras = getIntent().getExtras();
		    String user = extras.getString(ARG_USER);
		    String password = extras.getString(ARG_PASSWORD);
		    
			// TODO : implement infinite list management
			Calendar cal = Calendar.getInstance();
			Date today = cal.getTime();
			cal.add( Calendar.MONTH ,  -1 );
			Date monthAgo = cal.getTime();
			
			// Create fragments
			BalanceFragment balanceFragment = BalanceFragment.newInstance(user, password);
			TransactionsFragment transactionsFragment = TransactionsFragment.newInstance(user, password, monthAgo, today);
			
			// Add fragments to the layout
			getSupportFragmentManager().beginTransaction()
				.add(R.id.balanceLayout, balanceFragment)
				.add(R.id.transactionsLayout, transactionsFragment)
				.commit();
		}
	}
	
	@Override
	public void onLoadFinished(int id) {
		// Will be notified by both account and history loaders
		mFinishedLoaders.put(id, true);

		if(mFinishedLoaders.get(LoaderId.ACCOUNT) && mFinishedLoaders.get(LoaderId.HISTORY)) {
			hideLoadingView();
		}
	}
	
	private void hideLoadingView() {
		sumUpLayout.setVisibility(View.VISIBLE);
		sumUpLoaderLayout.setVisibility(View.GONE);
	}

	private void showLoadingView() {
		sumUpLayout.setVisibility(View.GONE);
		sumUpLoaderLayout.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_about:
			DialogUtils.showAboutDialog(this);
			return true;
		case R.id.action_logout:
			Intent intent = new Intent(this, LoginActivity.class);
			
			startActivity(intent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);			
		}
	}


}
