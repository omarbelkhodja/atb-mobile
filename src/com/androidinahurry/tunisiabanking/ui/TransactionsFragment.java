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

package com.androidinahurry.tunisiabanking.ui;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.androidinahurry.atb.R;
import com.androidinahurry.network.utils.Response;
import com.androidinahurry.tunisiabanking.bank.atb.AtbService;
import com.androidinahurry.tunisiabanking.model.Transaction;
import com.androidinahurry.tunisiabanking.service.ErrorCode;
import com.androidinahurry.tunisiabanking.service.HistoryLoader;
import com.androidinahurry.utils.LoadingSupportFragment;
import com.androidinahurry.utils.LoadingViewListener;

public class TransactionsFragment extends LoadingSupportFragment<Response<List<Transaction>, ErrorCode>> {
	
	private static final String ARG_USER = "user";
	private static final String ARG_PASSWORD = "password";
	private static final String ARG_START_DATE = "start";
	private static final String ARG_STOP_DATE = "stop";
	private ListView transactionListView;
	private TransactionAdapter transactionAdapter;
	private LoadingViewListener mListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_transactions, container, false);

		transactionAdapter = new TransactionAdapter(getActivity());
		transactionListView = (ListView) rootView.findViewById(R.id.transactionListView);
		transactionListView.setAdapter(transactionAdapter);

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

	public static TransactionsFragment newInstance(String user, String password, Date startDate, Date stopDate) {
		TransactionsFragment fragment = new TransactionsFragment();

	    Bundle args = new Bundle();
	    args.putString(ARG_USER, user);
	    args.putString(ARG_PASSWORD, password);
	    args.putLong(ARG_START_DATE, startDate.getTime());
	    args.putLong(ARG_STOP_DATE, stopDate.getTime());

	    fragment.setArguments(args);

		return fragment;
	}

	@Override
	public Loader<Response<List<Transaction>, ErrorCode>> onCreateLoader(int arg0, Bundle arg1) {
		return new HistoryLoader(getActivity(), new AtbService(), getUser(), getPassword(), getStartDate(), getStopDate());
	}

	@Override
	public void onLoadFinished(Loader<Response<List<Transaction>, ErrorCode>> loader, Response<List<Transaction>, ErrorCode> response) {
		switch(response.errorCode) {
		case NO_ERROR:
			transactionAdapter.updateTransactions(response.data);
			transactionAdapter.notifyDataSetChanged();
			break;
		default:
				//TODO
		}
		mListener.onLoadFinished(getLoaderId());
	}

	@Override
	public void onLoaderReset(Loader<Response<List<Transaction>, ErrorCode>> loader) {
		// TODO Auto-generated method stub
		
	}
	
	private Date getStartDate() {
		long time = getArguments().getLong(ARG_START_DATE);
		return new Date(time);
	}

	private Date getStopDate() {
		long time = getArguments().getLong(ARG_STOP_DATE);
		return new Date(time);
	}

	private String getPassword() {
		return getArguments().getString(ARG_PASSWORD);
	}

	private String getUser() {
		return getArguments().getString(ARG_USER);
	}

	@Override
	public int getLoaderId() {
		return LoaderId.HISTORY;
	}

}
