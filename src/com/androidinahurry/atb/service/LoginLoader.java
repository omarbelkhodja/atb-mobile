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

import org.apache.http.client.ClientProtocolException;

import android.content.Context;

import com.androidinahurry.network.utils.Response;
import com.androidinahurry.network.utils.WebServiceLoader;

public class LoginLoader extends WebServiceLoader<Response<Boolean, ErrorCode>> {
	
	public static int ERROR_CODE_NO_ERROR;
	public static int ERROR_CODE_AUTHENTICATION;
	public static int ERROR_CODE_NETWORK;
	public static int ERROR_CODE_SSL;
	public static int ERROR_CODE_UNKNOWN;
	
	private String mUser;
	private String mPassword;

	public LoginLoader(Context context, String user, String password) {
		super(context);
		
		mUser = user;
		mPassword = password;
	}

	@Override
	protected void releaseResources(Response<Boolean, ErrorCode> data) {
	}

	@Override
	public Response<Boolean,ErrorCode> loadInBackground() {
		Response<Boolean, ErrorCode> response = new Response<Boolean, ErrorCode>();
		
		try {
			response.data = Boolean.valueOf(AtbService.login(mUser, mPassword));
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
		}
		storeLoadedData(response);
		
		return response;
	}

}
