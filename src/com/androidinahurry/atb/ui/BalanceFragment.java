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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidinahurry.atb.R;
import com.androidinahurry.atb.model.Account;
import com.androidinahurry.atb.service.AccountLoader;
import com.androidinahurry.atb.service.ErrorCode;
import com.androidinahurry.network.utils.Response;
import com.androidinahurry.utils.EasyFormatter;
import com.androidinahurry.utils.LoadingSupportFragment;
import com.androidinahurry.utils.LoadingViewListener;

public class BalanceFragment extends LoadingSupportFragment<Response<Account, ErrorCode>> {
	
	private static final String ARG_USER = "user";
	private static final String ARG_PASSWORD = "password";
	
	private TextView textViewBalance;
	private LoadingViewListener mListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_balance, container, false);
		
		// Get local references on views
		textViewBalance = (TextView) rootView.findViewById(R.id.textViewBalance);
		
		// Init the loader
		getActivity().getSupportLoaderManager().initLoader(getLoaderId(), null, this);
		
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
	        this.mListener = (LoadingViewListener)activity;
	    }
	    catch (final ClassCastException e) {
	        throw new ClassCastException(activity.toString() + " must implement LoadingViewListener");
	    }
	}
	
	private String getUser() {
		return getArguments().getString(ARG_USER);
	}
	
	private String getPassword() {
		return getArguments().getString(ARG_PASSWORD);
	}
	
	public static BalanceFragment newInstance(String user, String password) {
		BalanceFragment fragment = new BalanceFragment();

	    Bundle args = new Bundle();
	    args.putString(ARG_USER, user);
	    args.putString(ARG_PASSWORD, password);

	    fragment.setArguments(args);

	    return fragment;
	}

	@Override
	public Loader<Response<Account, ErrorCode>> onCreateLoader(int id, Bundle bundle) {
		return new AccountLoader(getActivity(), getUser(), getPassword());
	}

	@Override
	public void onLoadFinished(Loader<Response<Account, ErrorCode>> loader, Response<Account, ErrorCode> response) {
		switch(response.errorCode) {
		case NO_ERROR:
			String balance = EasyFormatter.formatCurrencyWihtoutSign(response.data.balance, response.data.currency); 
			textViewBalance.setText(balance);
			break;
		default:
			// TODO : add a toast or a messagebox
		}
		
		mListener.onLoadFinished(getLoaderId());
	}

	@Override
	public void onLoaderReset(Loader<Response<Account, ErrorCode>> loader) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getLoaderId() {
		return LoaderId.ACCOUNT;
	}
}
