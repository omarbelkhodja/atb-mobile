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

package com.androidinahurry.tunisiabanking.service;

import android.content.Context;

import com.androidinahurry.network.utils.Response;
import com.androidinahurry.network.utils.WebServiceLoader;

public class LoginLoader extends WebServiceLoader<Response<Boolean, ErrorCode>> {

	private String mUser;
	private String mPassword;
	private BankService mBankService;

	public LoginLoader(Context context, BankService bankService, String user, String password) {
		super(context);

		mUser = user;
		mPassword = password;
		mBankService = bankService;
	}

	@Override
	protected void releaseResources(Response<Boolean, ErrorCode> data) {
	}

	@Override
	public Response<Boolean, ErrorCode> loadInBackground() {
		Response<Boolean, ErrorCode> response = new Response<Boolean, ErrorCode>();

		try {
			response.data = Boolean.valueOf(mBankService.checkLogin(mUser, mPassword));
			response.errorCode = ErrorCode.NO_ERROR;
		} catch (BankServiceException e) {
			response.errorCode = e.getErrorCode();
		}

		storeLoadedData(response);

		return response;
	}

}
