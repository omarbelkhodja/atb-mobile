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

package com.androidinahurry.atb.service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.htmlcleaner.XPatherException;

import android.content.Context;

import com.androidinahurry.atb.model.Transaction;
import com.androidinahurry.network.utils.Response;
import com.androidinahurry.network.utils.WebServiceLoader;

public class HistoryLoader  extends WebServiceLoader<Response<List<Transaction>, ErrorCode>> {

	private String mUser;
	private String mPassword;
	private Date mStartDate;
	private Date mStopDate;

	public HistoryLoader(Context context, String user, String password, Date start, Date stop) {
		super(context);
		
		mUser = user;
		mPassword = password;
		mStartDate = start;
		mStopDate = stop;
	}

	@Override
	protected void releaseResources(Response<List<Transaction>, ErrorCode> data) {
	}

	@Override
	public Response<List<Transaction>, ErrorCode> loadInBackground() {
		Response<List<Transaction>, ErrorCode> response = new Response<List<Transaction>, ErrorCode>();
		
		try {
			response.data = AtbService.getHistory(mUser, mPassword, mStartDate, mStopDate);
			response.errorCode = ErrorCode.NO_ERROR;
		} catch (KeyManagementException e) {
			response.errorCode = ErrorCode.SSL_FAILED;
		} catch (UnrecoverableKeyException e) {
			response.errorCode = ErrorCode.SSL_FAILED;
		} catch (ClientProtocolException e) {
			response.errorCode = ErrorCode.SSL_FAILED;
		} catch (NoSuchAlgorithmException e) {
			response.errorCode = ErrorCode.SSL_FAILED;
		} catch (KeyStoreException e) {
			response.errorCode = ErrorCode.SSL_FAILED;
		} catch (IOException e) {
			response.errorCode = ErrorCode.NETWORK_UNREACHABLE;
		} catch (XPatherException e) {
			response.errorCode = ErrorCode.UNKNOWN;
		} catch (IllegalStateException e) {
			response.errorCode = ErrorCode.UNKNOWN;
		} catch (ParseException e) {
			response.errorCode = ErrorCode.UNKNOWN;
		} catch (AuthenticationFailureException e) {
			response.errorCode = ErrorCode.AUTHENTICATION_FAILED;
		}
		storeLoadedData(response);

		return response;
	}

}
